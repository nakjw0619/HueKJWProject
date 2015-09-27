package kjw.graduproject.com.huekjw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Phee on 2015-09-27.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent myIntent = new Intent(context, BridgeCheckService.class);
        context.startService(myIntent);
    }

}
