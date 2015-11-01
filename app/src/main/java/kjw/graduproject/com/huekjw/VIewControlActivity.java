package kjw.graduproject.com.huekjw;

/*
copyright by KJW
 */

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;

/*
  처음 시작하는 Activity입니다.
  이 액티비티에서 3개의 버튼을 클릭시 다른 액티비티로 이동을 합니다.
 */
public class VIewControlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_control);

        // Bridge를 탐색하는 버튼에 대한 이벤트 컨트롤 부분
        Button searchButton = (Button)findViewById(R.id.ButtonToSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toSearch = new Intent(getApplicationContext(), MainActivity.class);  // 메인 액티비티(브릿지검색)으로 이동
                startActivity(toSearch);
            }
        });

        // 차트 확인하는 부분
        Button bChartButton = (Button)findViewById(R.id.chartButton);
        bChartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent toSearch = new Intent(getApplicationContext(), StaticEnergeUsageCharActivity.class);
                Intent toSearch = new Intent(getApplicationContext(), GetRemoteDataActivity.class);  // 전구 상태 가져오는 부분으로 이동
                startActivity(toSearch);
            }
        });

        Button bisOnButton = (Button)findViewById(R.id.isOnButton);
        bisOnButton .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String isOnOrOffValue = "";
                PHHueSDK phHueSDK;
                phHueSDK = PHHueSDK.create();
                PHBridge bridge = phHueSDK.getSelectedBridge();

                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                for (PHLight light : allLights) {
                    PHLightState lightState = light.getLastKnownLightState();
                    if (lightState.isOn()) {
                        isOnOrOffValue = "1";
                    } else {
                        isOnOrOffValue = "0";
                    }

                }

                Toast.makeText(getApplicationContext(), isOnOrOffValue, Toast.LENGTH_SHORT).show();

            }
        });

    }

    // 서비스를 시행하는 부분
    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), BridgeCheckService.class));
    }
}
