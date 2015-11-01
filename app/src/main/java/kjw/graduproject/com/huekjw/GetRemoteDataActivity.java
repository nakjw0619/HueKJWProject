package kjw.graduproject.com.huekjw;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String TAG_TIME = "time";
    private static final String TAG_ISONOFF = "is_on_off";

    // data JSONArray
    JSONArray data = null;

//    String kind;

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
                        String time = c.getString(TAG_TIME);
                        String is_on_off = c.getString(TAG_ISONOFF);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_TIME, time);
                        map.put(TAG_ISONOFF, is_on_off);

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

            // dataList 가 모두 입력 되어있는 상태이다.
            // 현재 안드로이드의 날짜 기준으로 30일전까지의 내용을 보여주자. (x축의 값은 11/1 또는 10/25일등의 태그로 표시된다.)
            // 현재 안드로이드 날짜 가져오기

            // 받는 데이타 타입
            // {"id":"97","time":"2015-10-25 09:56:35","is_on_off":"1"}

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 현재 시간이 필요할 때 사용.
            // String currentDateAndTime = sdf.format(new Date());

            Date fromTenDaysAgoToToday = null;
            try {
                fromTenDaysAgoToToday = sdf.parse(sdf.format(new Date()));
            }
            catch(Exception ee) { }

            TextView testview1 = (TextView)findViewById(R.id.datashow);

            List<HashMap<Date, Integer>> chartData = new ArrayList<HashMap<Date, Integer>>();
            // creating new HashMap
            HashMap<Date, Integer> map = new HashMap<Date, Integer>();

            // data 초기화, date 및 시간
//            int k = 3;
//            while( --k >= 0 ) {
//                map.put(new Date(fromTenDaysAgoToToday.getTime() - k * 24 * 3600 * 1000), 0);
//            }
//            chartData.add(map);

            //testview1.setText(chartData.toString());

            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int dateListForTen = 10;

//            List<Integer> intArrayData = new ArrayList<Integer>();
            int[] intArrayData = new int[10];

            for(int i=0; i < 10; i++)
                intArrayData[i] = 0;

            while(--dateListForTen >= 0) {

                int on_off_flag = 2;
                Date t1 = null;

                long sum = 0;

                for(int i=0; i < dataList.size(); i++) {
                    Date date = null;
                    try {
                        date = sdf.parse(dataList.get(i).get(TAG_TIME));
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "error in date convertion", Toast.LENGTH_SHORT).show();
                    }

                    // compareTo() == 0 이면 같은 날짜이다.
                    if (new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000).compareTo(date) == 0) {
                        // is_on_off를 판단하여 데이타를 넣는다. 바로 전 값과 같으면 넣지 않고 다르면 넣는다.
                        // 맨처음 값이 1일 경우 00:00:00 부터 사용한 시간을 계산한다.
                        // 맨 마지막 값이 1일 경우 24:00:00까지 시간을 계산한다.

                        Date dateTo = null;
                        try {
                            dateTo = time.parse(dataList.get(i).get(TAG_TIME));
                        }catch(Exception e){
                            //Toast.makeText(getApplicationContext(), "error in date convertion", Toast.LENGTH_SHORT).show();
                        }

                        int onOffVal = Integer.parseInt(dataList.get(i).get(TAG_ISONOFF));

                        if(on_off_flag == 2 && onOffVal == 1) {
                            t1 = new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000);
                            on_off_flag = 1;
                        }else if(on_off_flag == 2 && onOffVal == 0){
                            t1 = null;
                            on_off_flag = 0;
                        }else if(on_off_flag == 1 && onOffVal == 1) {
                            // do noting
                        }else if(on_off_flag == 1 && onOffVal == 0) {
                            sum += dateTo.getTime() - t1.getTime();
                            on_off_flag = 0;
                        }else if(on_off_flag == 0 && onOffVal == 1) {
                            t1 = dateTo;
                            on_off_flag = 1;
                        }else if(on_off_flag == 0 && onOffVal == 0) {
                            // do nothing
                        }

                        // Toast.makeText(getApplicationContext(), "date is correct" + date , Toast.LENGTH_SHORT).show();
                    }
                }

                if(on_off_flag == 2){
                    // (new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000))
//                    chartData.get(dateListForTen).remove(new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000));
                    HashMap<Date, Integer> newmap = new HashMap<Date, Integer>();
                    newmap.put(new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000), 0);
                    chartData.add(newmap);
//                    intArrayData.add(0);
//                    intArrayData[9 - dateListForTen] = 0;
                    intArrayData[dateListForTen] = 0;
                }else if(on_off_flag == 1) {
                    sum += new Date(fromTenDaysAgoToToday.getTime() - (dateListForTen - 1) * 24 * 3600 * 1000).getTime() - t1.getTime();
                    HashMap<Date, Integer> newmap = new HashMap<Date, Integer>();
                    newmap.put(new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000), (int) sum);
                    //chartData.set(dateListForTen, newmap);
                    chartData.add(newmap);
                    //intArrayData.add((int)sum);
                    intArrayData[dateListForTen] = (int)sum;
                }else if(on_off_flag == 0) {
                    HashMap<Date, Integer> newmap = new HashMap<Date, Integer>();
                    newmap.put(new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000), (int) sum);
//                    chartData.set(dateListForTen, newmap);
                    chartData.add(newmap);
                    //intArrayData.add((int)sum);
                    intArrayData[dateListForTen] = (int)sum;
                    // data는 밀리세컨드 단위로 저장된다.
                    // 시간을 구하기 위해서는 sum / ( 60분 * 60초 * 1000 미리 )을 하면 된다.
                }
            }


            Intent intent=new Intent(getApplicationContext(), StaticEnergeUsageCharActivity.class);
//            intent.putIntegerArrayListExtra("chartData", (ArrayList<Integer>) intArrayData);
            intent.putExtra("chartData", intArrayData);
            startActivity(intent);
            finish();


//            testview1.setText(chartData.toString());

//            for(int i=0; i < dataList.size(); i++) {
//                Date date = null;
//                try {
//                    date = sdf.parse(dataList.get(i).get(TAG_TIME));
//                }catch(Exception e){
//                    Toast.makeText(getApplicationContext(), "error in date convertion", Toast.LENGTH_SHORT).show();
//                }
//
//                int dateListForTen = 3;
//
//                // 10일 전부터 현재 일의 데이타만 가지고 계산을 한다.
//                while(--dateListForTen != 0) {
//                    // compareTo() == 0 이면 같은 날짜이다.
//                    if (new Date(fromTenDaysAgoToToday.getTime() - dateListForTen * 24 * 3600 * 1000).compareTo(date) == 0) {
//                        // is_on_off를 판단하여 데이타를 넣는다. 바로 전 값과 같으면 넣지 않고 다르면 넣는다.
//                        // 맨처음 값이 1일 경우 00:00:00 부터 사용한 시간을 계산한다.
//                        // 맨 마지막 값이 1일 경우 24:00:00까지 시간을 계산한다.
//                        // Toast.makeText(getApplicationContext(), "date is correct" + date , Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }



//            TextView testview1 = (TextView)findViewById(R.id.datashow);
//            testview1.setText(dataList.toString());

            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            AllDataActivity.this, dataList,
//                            R.layout.list_item, new String[] { TAG_USERNAME,
//                            TAG_USERCONTENTS},
//                            new int[] { R.id.username, R.id.usercontents });
//                    // updating listview
//                    setListAdapter(adapter);
//
//                    Toast.makeText(getApplicationContext(), "kind is : " + kind, Toast.LENGTH_LONG).show();
//                }
//            });
//
        }

    }
}
