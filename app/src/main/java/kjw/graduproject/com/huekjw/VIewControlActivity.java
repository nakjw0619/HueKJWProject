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

        // Bridge를 탐색하는 버튼에 대한 이벤트 컨트롤 부분
        Button bChartButton = (Button)findViewById(R.id.chartButton);
        bChartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toSearch = new Intent(getApplicationContext(), StaticEnergeUsageCharActivity.class);  // 메인 액티비티(브릿지검색)으로 이동
                startActivity(toSearch);
            }
        });
    }

    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), BridgeCheckService.class));
    }
}
