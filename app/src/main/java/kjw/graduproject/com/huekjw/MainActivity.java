package kjw.graduproject.com.huekjw;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import data.AccessPointListAdapter;
import data.HueSharedPreferences;

/*
    브릿지를 찾는 액티비티입니다.
    브릿지를 찾고 나서 해당 리스트를 클릭시 해당 브릿지와 연결을 하도록 구현되어있습니다.
 */
public class MainActivity extends Activity implements OnItemClickListener {

    private PHHueSDK phHueSDK;                      // hueSDK 클래스 변수 생성
    public static final String TAG = "statisticsEnergyUsageWithHue";    // 태그에 대한 문자열 상수
    private HueSharedPreferences prefs;             // preference를 저장하여, Hue에 재접속시 연결을 위한 변수
                                                    // Hue 에 재접속시 마지막에 저장된 Bridge의 IP로 접속
    private AccessPointListAdapter adapter;         // 브릿지 정보를 저장하는 ListAdapter 변수

    private boolean lastSearchWasIPScan = false;    //

    // MainActivity에서 제일 먼저 시작하는 라이프 사이클
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the Device Name (name of your app). This will be stored in your bridge whitelist entry.

        phHueSDK = PHHueSDK.create();                                   // 필립스 휴 SDK에서 팩토리얼 패턴으로 객체 얻어 옴, 
                                                                        // 이 객체를 사용하여 브릿지와 연결

        phHueSDK.setAppName("statisticsEnergyUsageWithHueApp");           // app name
        phHueSDK.setDeviceName(android.os.Build.MODEL);                     // android 모델 정보를 브릿지에 저장

        // Register the PHSDKListener to receive callbacks from the bridge.
        phHueSDK.getNotificationManager().registerSDKListener(listener);    // 브릿지로부터 정보를 받기위해 리스너등록
                                                                            // 브릿지가 연결되면 정보를 준다.

        adapter = new AccessPointListAdapter(getApplicationContext(), phHueSDK.getAccessPointsFound()); 
        // MacAddress와 IP가 Adapter에 저장

        ListView accessPointList = (ListView) findViewById(R.id.bridge_list);   
        // XML에 있는 리스트를 accessPointList 변수로 선언
        accessPointList.setOnItemClickListener(this);                           
        // onClickListener 등록
        accessPointList.setAdapter(adapter);
        //Adapter 정보를 List에 저장


        // Try to automatically connect to the last known bridge.  For first time use this will be empty so a bridge search is automatically started.
        prefs = HueSharedPreferences.getInstance(getApplicationContext());
        String lastIpAddress   = prefs.getLastConnectedIPAddress();
        String lastUsername    = prefs.getUsername();

        // Automatically try to connect to the last connected IP Address.  For multiple bridge support a different implementation is required.
        if (lastIpAddress !=null && !lastIpAddress.equals("")) {
            PHAccessPoint lastAccessPoint = new PHAccessPoint();
            lastAccessPoint.setIpAddress(lastIpAddress);
            lastAccessPoint.setUsername(lastUsername);

            if (!phHueSDK.isAccessPointConnected(lastAccessPoint)) {
                PHWizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, MainActivity.this);
                phHueSDK.connect(lastAccessPoint);
            }
        }
        else {  // First time use, so perform a bridge search.
            doBridgeSearch();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w(TAG, "Inflating home menu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.find_new_bridge:
                doBridgeSearch();
                break;
        }
        return true;
/*        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listener !=null) {
            phHueSDK.getNotificationManager().unregisterSDKListener(listener);
        }
        phHueSDK.disableAllHeartbeat();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PHAccessPoint accessPoint = (PHAccessPoint) adapter.getItem(position);

        PHBridge connectedBridge = phHueSDK.getSelectedBridge();

        if (connectedBridge != null) {
            String connectedIP = connectedBridge.getResourceCache().getBridgeConfiguration().getIpAddress();
            if (connectedIP != null) {   // We are already connected here:-
                phHueSDK.disableHeartbeat(connectedBridge);
                phHueSDK.disconnect(connectedBridge);
            }
        }
        PHWizardAlertDialog.getInstance().showProgressDialog(R.string.connecting, MainActivity.this);
        phHueSDK.connect(accessPoint);
    }

    public void doBridgeSearch() {
        PHWizardAlertDialog.getInstance().showProgressDialog(R.string.search_progress, MainActivity.this);
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        // Start the UPNP Searching of local bridges.
        sm.search(true, true);
    }

    // Local SDK Listener
    private PHSDKListener listener = new PHSDKListener() {

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoint) {
            Log.w(TAG, "Access Points Found. " + accessPoint.size());

            PHWizardAlertDialog.getInstance().closeProgressDialog();
            if (accessPoint != null && accessPoint.size() > 0) {
                phHueSDK.getAccessPointsFound().clear();
                phHueSDK.getAccessPointsFound().addAll(accessPoint);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(phHueSDK.getAccessPointsFound());
                    }
                });

            }

        }

        @Override
        public void onCacheUpdated(List<Integer> arg0, PHBridge bridge) {
            Log.w(TAG, "On CacheUpdated");

        }

        @Override
        public void onBridgeConnected(PHBridge b, String username) {
            phHueSDK.setSelectedBridge(b);
            phHueSDK.enableHeartbeat(b, PHHueSDK.HB_INTERVAL);
            phHueSDK.getLastHeartbeat().put(b.getResourceCache().getBridgeConfiguration() .getIpAddress(), System.currentTimeMillis());
            prefs.setLastConnectedIPAddress(b.getResourceCache().getBridgeConfiguration().getIpAddress());
            prefs.setUsername(username);
            PHWizardAlertDialog.getInstance().closeProgressDialog();
            startMainActivity();
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
            Log.w(TAG, "Authentication Required.");
            phHueSDK.startPushlinkAuthentication(accessPoint);
            startActivity(new Intent(MainActivity.this, PHPushlinkActivity.class));

        }

        @Override
        public void onConnectionResumed(PHBridge bridge) {
            if (MainActivity.this.isFinishing())
                return;

            Log.v(TAG, "onConnectionResumed" + bridge.getResourceCache().getBridgeConfiguration().getIpAddress());
            phHueSDK.getLastHeartbeat().put(bridge.getResourceCache().getBridgeConfiguration().getIpAddress(),  System.currentTimeMillis());
            for (int i = 0; i < phHueSDK.getDisconnectedAccessPoint().size(); i++) {

                if (phHueSDK.getDisconnectedAccessPoint().get(i).getIpAddress().equals(bridge.getResourceCache().getBridgeConfiguration().getIpAddress())) {
                    phHueSDK.getDisconnectedAccessPoint().remove(i);
                }
            }

        }

        @Override
        public void onConnectionLost(PHAccessPoint accessPoint) {
            Log.v(TAG, "onConnectionLost : " + accessPoint.getIpAddress());
            if (!phHueSDK.getDisconnectedAccessPoint().contains(accessPoint)) {
                phHueSDK.getDisconnectedAccessPoint().add(accessPoint);
            }
        }

        @Override
        public void onError(int code, final String message) {
            Log.e(TAG, "on Error Called : " + code + ":" + message);

            if (code == PHHueError.NO_CONNECTION) {
                Log.w(TAG, "On No Connection");
            }
            else if (code == PHHueError.AUTHENTICATION_FAILED || code== PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
                PHWizardAlertDialog.getInstance().closeProgressDialog();
            }
            else if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
                Log.w(TAG, "Bridge Not Responding . . . ");
                PHWizardAlertDialog.getInstance().closeProgressDialog();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PHWizardAlertDialog.showErrorDialog(MainActivity.this, message, R.string.btn_ok);
                    }
                });

            }
            else if (code == PHMessageType.BRIDGE_NOT_FOUND) {

                if (!lastSearchWasIPScan) {  // Perform an IP Scan (backup mechanism) if UPNP and Portal Search fails.
                    phHueSDK = PHHueSDK.getInstance();
                    PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                    sm.search(false, false, true);
                    lastSearchWasIPScan=true;
                }
                else {
                    PHWizardAlertDialog.getInstance().closeProgressDialog();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PHWizardAlertDialog.showErrorDialog(MainActivity.this, message, R.string.btn_ok);
                        }
                    });
                }


            }
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
            for (PHHueParsingError parsingError: parsingErrorsList) {
                Log.e(TAG, "ParsingError : " + parsingError.getMessage());
            }
        }
    };

    // Starting the main activity this way, prevents the PushLink Activity being shown when pressing the back button.
    public void startMainActivity() {
//        Intent intent = new Intent(getApplicationContext(), VIewControlActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
//        startActivity(intent);
    }
}
