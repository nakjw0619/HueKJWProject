package kjw.graduproject.com.huekjw;



import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class VIewControlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_control);
//        setTitle("Welcome - Smart Grid With Philipse Hue And KJW");

        Button searchButton = (Button)findViewById(R.id.ButtonToSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
//                Intent toSearch = new Intent(this, MainActivity.class);
                Intent toSearch = new Intent(getApplicationContext(), MainActivity.class);
                //getApplicationContext()
//                downloadIntent.setData(Uri.parse(fileUrl));
                startActivity(toSearch);
            }

        });
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_control, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
