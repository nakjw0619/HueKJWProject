package kjw.graduproject.com.huekjw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class PostActivity extends AsyncTask<String, Void, String> {
    private PHHueSDK phHueSDK;

    private Context context;
    private int byGetOrPost = 0;
    private String isOnOrOffValue = "0";

    //flag 0 means get and 1 means post.(By default it is get.)
    public PostActivity(Context context, int flag) {
        this.context = context;
        byGetOrPost = flag;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {

        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            if( lightState.isOn() ) {
                isOnOrOffValue = "1";
            }else
            {
                isOnOrOffValue = "0";
            }

        }

        try {

            String link = "http://rainbowp45.cafe24.com/HueKJWProject/input_data.php"; // POST 주소
            String data = URLEncoder.encode("HueBulbInfo", "UTF-8") + "=" + URLEncoder.encode(isOnOrOffValue, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    @Override
    protected void onPostExecute(String result) {
        // 토스트, 인풋 서섹스 엔 인풋 내용 0 or 1
//        Toast.makeText(this, "Input success", Toast.LENGTH_SHORT).show();
    }
}