package com.example.rakshithr.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private TextView error = null;
    private EditText street = null;
    private EditText city = null ;
    private RadioGroup deg = null ;
    private Spinner state = null;
    private Button search, clear;
    private ImageView forecast = null;
    private Button about = null;
    private JSONObject outRes = null;
    private String queryURL = null;
    private String degValue = null, stateValue = null, cityVal = null;
    private List<String> stateList = new ArrayList<String>(60);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forecast);
        stateList.add(0, "");
        stateList.add(1,"Al");
        stateList.add(2,"AK");
        stateList.add(3,"AZ");
        stateList.add(4,"AR");
        stateList.add(5,"CA");
        stateList.add(6,"CO");
        stateList.add(7,"CT");
        stateList.add(8,"DE");
        stateList.add(9,"DC");
        stateList.add(10,"FL");
        stateList.add(11,"GA");
        stateList.add(12,"HI");
        stateList.add(13,"ID");
        stateList.add(14,"IL");
        stateList.add(15,"IN");
        stateList.add(16,"IA");
        stateList.add(17,"KS");
        stateList.add(18,"KY");
        stateList.add(19,"LA");
        stateList.add(20,"ME");
        stateList.add(21,"MD");
        stateList.add(22,"MA");
        stateList.add(23,"MI");
        stateList.add(24,"MN");
        stateList.add(25,"MS");
        stateList.add(26,"MO");
        stateList.add(27,"MT");
        stateList.add(28,"NE");
        stateList.add(29,"NV");
        stateList.add(30,"NH");
        stateList.add(31,"NJ");
        stateList.add(32,"NM");
        stateList.add(33,"NY");
        stateList.add(34,"NC");
        stateList.add(35,"ND");
        stateList.add(36,"NH");
        stateList.add(37,"OK");
        stateList.add(38,"OR");
        stateList.add(39,"PA");
        stateList.add(40,"RI");
        stateList.add(41,"SC");
        stateList.add(42,"SD");
        stateList.add(43,"TN");
        stateList.add(44,"TX");
        stateList.add(45,"UT");
        stateList.add(46,"VT");
        stateList.add(47,"VA");
        stateList.add(48,"WA");
        stateList.add(49,"WV");
        stateList.add(50,"WI");
        stateList.add(51,"WY");

        this.search = (Button) findViewById(R.id.searchButton);
        this.clear = (Button) findViewById(R.id.clear);
        this.street = (EditText) findViewById(R.id.street);
        this.city   = (EditText) findViewById(R.id.city);
        this.state   = (Spinner) findViewById(R.id.state);
        this.deg  = (RadioGroup) findViewById(R.id.degree);
        this.error  = (TextView) findViewById(R.id.error);
        this.forecast = (ImageView) findViewById(R.id.logoForecast);
        this.about = (Button) findViewById(R.id.about);
        this.search.setOnClickListener(new View.OnClickListener() {
              @Override

              public void onClick(View v) {
                  //String errMsg   = street.getText().toString() + city.getText().toString();
                  error.setText("");
                  if (checkForm()) {
                      outRes = null;
                      String st = ((Spinner) findViewById(R.id.state)).getSelectedItem().toString();
                      Log.d("spinner id", st);
                      MainActivity.this.queryURL = "http://csci57173-env.elasticbeanstalk.com/index/server.php?street=" + ((EditText) findViewById(R.id.street)).getText().toString() + "" +
                              "&city="+((EditText)findViewById(R.id.city)).getText().toString()+"&states="+ MainActivity.this.stateValue +"&degree=" + MainActivity.this.degValue;
                      MainActivity.this.queryURL = MainActivity.this.queryURL.replaceAll(" ","+");
                      Log.d("Bi", MainActivity.this.queryURL);
                      getWeatherDetails();
                  }
              }
          }
        );
        MainActivity.this.degValue = (R.id.fahrenheit == this.deg.getCheckedRadioButtonId()) ? "us" : "si";
        MainActivity.this.cityVal = this.city.getText().toString();
        this.deg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MainActivity.this.degValue = (R.id.fahrenheit == checkedId) ? "us" : "si";
            }
        });
        this.state.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    MainActivity.this.stateValue = MainActivity.this.stateList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.clear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                clearForm();
            }
        });

        this.forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForeCastIO();
            }
        });

        this.about.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openAboutActivity();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //Open about me activity
    public void openAboutActivity(){
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }
    //check whether data entered is valid
    public boolean checkForm() {
        if(this.street.getText().toString().trim().isEmpty()){
            this.error.setText("Please enter a street");
            return false;
        }
        if(this.city.getText().toString().trim().isEmpty()){
            this.error.setText("Please enter a city");
            return false;
        }
        if(this.state.getSelectedItem().toString().equals("Select")){
            this.error.setText("Please select a state");
            return false;
        }

        Log.d("errpr", "hi this call was successful");
        return true;

    }

    //if form is valid then make an ajax call
    public void getWeatherDetails() {
        //URL wsCallUrl =
        String data = new RequestHandler().execute(new String[]{queryURL}).toString();
        Log.d("Data---",data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forecast, menu);
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
    //Clear form values and also error messages
    public void clearForm() {
        this.error.setText("");
        this.street.setText("");
        this.city.setText("");
        this.state.setSelection(0);
        this.deg.check(R.id.fahrenheit);
    }
    public void openForeCastIO(){
        Intent forecastIO = new Intent();
        forecastIO.setAction(Intent.ACTION_VIEW);
        forecastIO.addCategory(Intent.CATEGORY_BROWSABLE);
        forecastIO.setData(Uri.parse("http://www.forecast.io"));
        startActivity(forecastIO);
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Forecast Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rakshithr.weatherforecast/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Forecast Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rakshithr.weatherforecast/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class RequestHandler extends AsyncTask<String, Void, String> {

        protected String onPostExecute(Boolean result) {
            return null;
        }

        @Override
        protected String doInBackground(String[] urls) {
            String data = null;
            try {

                for(String url : urls){
                    HttpGet requestURL = new HttpGet(url);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResp = httpClient.execute(requestURL);
                    //get status code
                    int status = httpResp.getStatusLine().getStatusCode();
                    if(status == 200){
                        HttpEntity httpEntity = httpResp.getEntity();
                        data = EntityUtils.toString(httpEntity);
                        //return data;
                    }
                }
            }catch(Exception e){
                Log.d("RequestFailure","There was an error while making a http get request in Request Handler Class "+e);
            }
            try {
                MainActivity.this.outRes = new JSONObject(data);
                showResult(data);
            }catch (Exception e){
                Log.d("parseException","There is an error while parsing");
            }
            return data;
        }

        protected void onPreExecute() {
            super.onPreExecute();

        }

    }
    public void showResult(String data){
        Intent showResult = new Intent(this, ResultActivity.class);
        showResult.putExtra("jsonOut", data);
        showResult.putExtra("city",MainActivity.this.city.getText().toString());
        showResult.putExtra("state",this.stateValue.toString());
        showResult.putExtra("degree",this.degValue);
        startActivity(showResult);
    }
}
