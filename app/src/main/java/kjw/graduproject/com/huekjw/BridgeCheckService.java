package kjw.graduproject.com.huekjw;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BridgeCheckService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "kjw.graduproject.com.huekjw.action.FOO";
    private static final String ACTION_BAZ = "kjw.graduproject.com.huekjw.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "kjw.graduproject.com.huekjw.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "kjw.graduproject.com.huekjw.extra.PARAM2";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
//        final String action = intent.getAction();
//        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            public void run(){
                while(true) {

                    try {
                        new PostActivity(getApplicationContext(), 1).execute();
                        Thread.sleep(1000*60); // 5분에 1회로 변경
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Destroy Started", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BridgeCheckService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BridgeCheckService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public BridgeCheckService() {
        super("BridgeCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *  데이타 구조
     *  실행 날짜, 실행 월, 현재시간, IsOn Or Is Off (추가 기능 사용 색 및 색에대한 전력량)
     *  전력량 계산은 is on의 시작 시간부터 is off의 시작 시간까지
     * jsonparser makeHttpRequest in intentservice android
     * http://www.serversfree.com/
     */

    // http://rainbowp45.cafe24.com/HueKJWProject/input_data_get.php?HueBulbInfo=1
    // Get 방식으로 데이타 넣는 법
}
