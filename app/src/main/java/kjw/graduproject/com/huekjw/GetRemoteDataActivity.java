package kjw.graduproject.com.huekjw;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRemoteDataActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> dataList;

    // Remote Server와 연동할 수 있는 Script 주소
    private static String url_all_data = "http://rainbowp45.cafe24.com/HueKJWProject/get_all_data.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "data";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_USERCONTENTS = "usercontents";

    // data JSONArray
    JSONArray data = null;

    String kind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_remote_data);

        // Hashmap for ListView
        dataList = new ArrayList<HashMap<String, String>>();

        // Loading data in Background Thread
        new LoadAllData().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_remote_data, menu);
        return true;
    }

    @Override
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
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetRemoteDataActivity.this);
            pDialog.setMessage("Loading data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All data from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL

            JSONObject json = jParser.makeHttpRequest(url_all_data, "POST", params);
            //JSONObject json = jParser.makeHttpRequest(url_all_data, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Data: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // data found
                    // Getting Array of Data
                    data = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_USERNAME);
                        String usercontents = c.getString(TAG_USERCONTENTS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_USERNAME, id);
                        map.put(TAG_USERCONTENTS, usercontents);

                        // adding HashList to ArrayList
                        dataList.add(map);
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

                    // jaeisk modify
                	/*
                    // no data found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all data
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            AllDataActivity.this, dataList,
//                            R.layout.list_item, new String[] { TAG_USERNAME,
//                            TAG_USERCONTENTS},
//                            new int[] { R.id.username, R.id.usercontents });
//                    // updating listview
//                    setListAdapter(adapter);

                    Toast.makeText(getApplicationContext(), "kind is : " + kind, Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}
