package com.example.rakshithr.weatherforecast;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by rakshithr on 11/23/15.
 */
public class DetailsActivity extends Activity {
    private Button nextTFHandler;
    private Button nextSDHandler;

    private LinearLayout nextTF;
    private LinearLayout nextSD;

    private String output;
    private JSONArray hourly;
    private JSONArray weekly;
    private static int hourlyContent = 0;
    private static int weeklyContent = 0;
    private String degree = null;
    private String state  = null;
    private String city   = null;

    private TextView moreDetailsSum = null;
    private HashMap<String, Integer> icon;
    private static int viewMoreBut = 0;
    private Button         summaryFooter;
    private LinearLayout containerFooter;
    private String timeZone;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        this.nextTFHandler = (Button) findViewById(R.id.nextTFHandler);
        this.nextSDHandler = (Button) findViewById(R.id.nextSDHandler);
        this.nextSD        = (LinearLayout) findViewById(R.id.nextSD);
        this.nextTF        = (LinearLayout) findViewById(R.id.nextTF);
        this.moreDetailsSum = (TextView) findViewById(R.id.moreDetailsSum);
        //get json data
        Bundle bundle = getIntent().getExtras();
        this.output = bundle.getString("output");
        this.degree = (bundle.getString("degree").equals("us")?"F":"C");
        this.state  = bundle.getString("state");
        this.city   = bundle.getString("city");
        this.timeZone = bundle.getString("timezone");
        this.moreDetailsSum.setText("More Details for " + this.city.toString() + ", " + this.state.toString());
;        this.hourlyContent = 0;
        this.weeklyContent = 0;
        //Fill icon values
        this.icon = ResultActivity.fillIcon();

        //handler for next twenty four hours
        this.nextTFHandler.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                DetailsActivity.this.nextTF.setVisibility(View.VISIBLE);
                DetailsActivity.this.nextSD.setVisibility(View.INVISIBLE);
                DetailsActivity.this.nextTFHandler.setBackgroundColor(Color.parseColor("#005ce6"));
                DetailsActivity.this.nextSDHandler.setBackgroundResource(R.color.common_signin_btn_dark_text_disabled);
                //if (DetailsActivity.this.hourlyContent != 0)
                    //return;
                renderNextTF();
            }
        });
        //handler for next seven days hours
        this.nextSDHandler.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                DetailsActivity.this.nextTF.setVisibility(View.INVISIBLE);
                DetailsActivity.this.nextSD.setVisibility(View.VISIBLE);
                DetailsActivity.this.nextSDHandler.setBackgroundColor(Color.parseColor("#005ce6"));
                DetailsActivity.this.nextTFHandler.setBackgroundResource(R.color.common_signin_btn_dark_text_disabled);
//                if (DetailsActivity.this.weeklyContent != 0)
//                    return;
                renderNextSD();
            }
        });
        //Default set one button has selected - next 24 hours
        this.nextTFHandler.setBackgroundColor(Color.parseColor("#005ce6"));
        DetailsActivity.this.nextSDHandler.setBackgroundResource(R.color.common_signin_btn_dark_text_disabled);
        //Convert string to JSON object
        try{
            JSONObject result        = new JSONObject((String)this.output);
            JSONObject resultHourly  = result.getJSONObject("hourly");
            this.hourly              = resultHourly.getJSONArray("data");
            JSONObject resultWeekly  = result.getJSONObject("daily");
            this.weekly              = resultWeekly.getJSONArray("data");
            //Initially set next 24 screen
            renderNextTF();
        }catch(Exception e){
            Log.d("parseDetails","There was an error while parsing string in Details Activity");
        }
        //handler for next seven days

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void renderNextTF(){
        DetailsActivity.this.nextSD.removeAllViews();
        DetailsActivity.this.nextSD.removeAllViewsInLayout();
        //DetailsActivity.this.nextSD.
        //----------------------------------Header Section of the next 24 hours-------------------------------------
        LinearLayout containerHeader = new LinearLayout(this);
        containerHeader.removeAllViews();
        containerHeader.removeAllViewsInLayout();
        //containerHeader.setPadding(10,10,10,10);
        LinearLayout.LayoutParams paramsHeader = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        paramsHeader.setMargins(0,0,0,10);

        TextView          timeHeader = new TextView(this);
        TextView       summaryHeader = new TextView(this);
        TextView          tempHeader = new TextView(this);
        LinearLayout.LayoutParams generalContentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        containerHeader.setOrientation(LinearLayout.HORIZONTAL);
        timeHeader.setLayoutParams(generalContentParams);
        summaryHeader.setLayoutParams(generalContentParams);
        tempHeader.setLayoutParams(generalContentParams);
        timeHeader.setText("Time");
        summaryHeader.setText("Summary");
        summaryHeader.setGravity(Gravity.CENTER);
        tempHeader.setText(Html.fromHtml("Temp(" + "&deg;" + DetailsActivity.this.degree + ")"));
        tempHeader.setGravity(Gravity.RIGHT);
        tempHeader.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_VIEW_END);
        timeHeader.setTextColor(Color.parseColor("#000000"));
        summaryHeader.setTextColor(Color.parseColor("#000000"));
        tempHeader.setTextColor(Color.parseColor("#000000"));
        ((LinearLayout)findViewById(R.id.nextTF)).addView(containerHeader, paramsHeader);
        containerHeader.addView(timeHeader, 0);
        containerHeader.addView(summaryHeader,1);
        containerHeader.addView(tempHeader,2);
        containerHeader.setBackgroundColor(Color.parseColor("#00cccc"));
        containerHeader.setPadding(13, 10, 13, 25);

        LinearLayout.LayoutParams firstImageContentParams = new LinearLayout.LayoutParams(5, 80, 1);
        //-----------------------------------End of Header Section of next 24 hours ---------------------------------
        //-----------------------------------Start of rest of the body section of next 24----------------------------
        JSONArray content = DetailsActivity.this.hourly;
        LinearLayout[] container = new LinearLayout[50];

        TextView[] time = new TextView[59];
        ImageView[] summary = new ImageView[50];
        TextView[] temp = new TextView[50];
        for(int i = 1 ; i <= 24 ; i++){
            try {
                //if(i < 24 || DetailsActivity.this.viewMoreBut == 1) {
                JSONObject obj = content.getJSONObject(i);
                String iconStr = obj.getString("icon");
                long timeStr = obj.getLong("time") * 1000;
                Date timeLongFormat = new Date(timeStr);
                SimpleDateFormat timeHandler = new SimpleDateFormat("hh:mm aa");
                timeHandler.setTimeZone(TimeZone.getTimeZone(this.timeZone));
                String timeVal  = timeHandler.format(timeLongFormat);
                String tempStr = String.valueOf(obj.getInt("temperature"));
//                container[i].setId(i);
                container[i] = new LinearLayout(this);
                container[i].setPadding(3, 0, 3, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140, 3);
                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(5,140, 1);
                container[i].setOrientation(LinearLayout.HORIZONTAL);
                //container[i].setGravity();
                time[i] = new TextView(this);
                summary[i] = new ImageView(this);
                temp[i] = new TextView(this);

                LinearLayout.LayoutParams parcs = new LinearLayout.LayoutParams(40, 140, 1);
                //summary.getLayoutParams().width = 10;
                //summary.getLayoutParams().height = 10;
                //summary.setLayoutParams();
                //summary[i].setLayoutParams(parcs);
                time[i].setLayoutParams(contentParams);
                summary[i].setLayoutParams(contentParams);
                temp[i].setLayoutParams(contentParams);
                temp[i].setPadding(10, 13, 10, 10);
                time[i].setText(timeVal);
                time[i].setTextColor(Color.parseColor("#000000"));
                summary[i].setBackgroundResource(DetailsActivity.this.icon.get(iconStr));
                temp[i].setText(tempStr);
                temp[i].setGravity(Gravity.RIGHT);
                DetailsActivity.this.nextTF.addView(container[i], params);
                summary[i].getLayoutParams().width = 50;
                summary[i].getLayoutParams().height = 100;
                //summary[i].getLayoutParams().width = 80;
                //summary[i].getLayoutParams().height = 80;
                container[i].addView(time[i], 0);
                container[i].addView(summary[i], 1);
                container[i].addView(temp[i], 2);
                if(i % 2 != 0)
                    container[i].setBackgroundResource(R.color.common_signin_btn_light_text_disabled);
                else
                    container[i].setBackgroundResource(R.color.common_signin_btn_light_text_pressed);

                //}


                container[i].setPadding(10,10,0,0);

            }catch (Exception e){
                Log.d("next24Parse","Error while parsing next 24 data");
            }
        }
        //-----------------------------------end of rest of the body section of next 24----------------------------
        //----------------------------------Header Section of the next 24 hours-------------------------------------

        DetailsActivity.this.containerFooter = new LinearLayout(this);
        DetailsActivity.this.containerFooter.setPadding(0,10,0,10);
        LinearLayout.LayoutParams paramsFooter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        TextView          timeFooter = new TextView(this);
        Button summaryFooter = new Button(this);
        TextView          tempFooter = new TextView(this);
        LinearLayout.LayoutParams footerContentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        DetailsActivity.this.containerFooter.setOrientation(LinearLayout.HORIZONTAL);
        timeFooter.setLayoutParams(footerContentParams);
        summaryFooter.setLayoutParams(footerContentParams);
        tempFooter.setLayoutParams(footerContentParams);
        timeFooter.setText("");
        summaryFooter.setText("+");
        summaryFooter.setBackgroundColor(Color.parseColor("#0066ff"));
        summaryFooter.getLayoutParams().width = 20;
        summaryFooter.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                DetailsActivity.this.viewMoreBut = (DetailsActivity.this.viewMoreBut == 0) ? 1 : 0;
                //renderNextTF();
                viewMore();
            }
        });
        tempFooter.setText(Html.fromHtml(""));
        tempFooter.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_VIEW_END);
        ((LinearLayout)findViewById(R.id.nextTF)).addView(DetailsActivity.this.containerFooter, paramsFooter);
        DetailsActivity.this.containerFooter.addView(timeFooter,0);
        DetailsActivity.this.containerFooter.addView(summaryFooter,1);
        DetailsActivity.this.containerFooter.addView(tempFooter,2);
        DetailsActivity.this.containerFooter.setBackgroundResource(R.color.common_signin_btn_light_text_disabled);
        //-----------------------------------End of Header Section of next 24 hours ---------------------------------
        //set hourly content to 1 so that for that next time the rendering function need not be called
        DetailsActivity.this.hourlyContent = 1;
    }
    public void viewMore(){
        //hide view more button
        DetailsActivity.this.containerFooter.setVisibility(LinearLayout.GONE);
        //-----------------------------------Start of rest of the body section of next 24----------------------------
        JSONArray content = DetailsActivity.this.hourly;
        LinearLayout[] container = new LinearLayout[50];
        LinearLayout.LayoutParams firstImageContentParams = new LinearLayout.LayoutParams(5, 80, 1);
        TextView[] time = new TextView[59];
        ImageView[] summary = new ImageView[50];
        TextView[] temp = new TextView[50];
        for(int i = 25; i <= content.length() ; i++){
            try {
                JSONObject obj = content.getJSONObject(i);
                String iconStr = obj.getString("icon");
                long timeStr = obj.getLong("time") * 1000;
                Date timeLongFormat = new Date(timeStr);
                SimpleDateFormat timeHandler = new SimpleDateFormat("hh:mm aa");
                timeHandler.setTimeZone(TimeZone.getTimeZone(this.timeZone));
                String timeVal  = timeHandler.format(timeLongFormat);
                String tempStr = String.valueOf(obj.getInt("temperature"));
//                container[i].setId(i);
                container[i] = new LinearLayout(this);
                container[i].setPadding(0, 10, 0, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140, 3);
                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(5,140, 1);
                container[i].setOrientation(LinearLayout.HORIZONTAL);
                time[i] = new TextView(this);
                summary[i] = new ImageView(this);
                temp[i] = new TextView(this);
                temp[i].setGravity(Gravity.RIGHT);

                LinearLayout.LayoutParams parcs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                //summary.getLayoutParams().width = 10;
                //summary.getLayoutParams().height = 10;
                //summary.setLayoutParams();
                summary[i].setLayoutParams(parcs);
                time[i].setLayoutParams(contentParams);
                summary[i].setLayoutParams(contentParams);
                temp[i].setLayoutParams(contentParams);
                temp[i].setPadding(10, 13, 10, 10);
                time[i].setText(timeVal);
                time[i].setTextColor(Color.parseColor("#000000"));
                summary[i].setBackgroundResource(DetailsActivity.this.icon.get(iconStr));
                temp[i].setText(tempStr);
                DetailsActivity.this.nextTF.addView(container[i], params);
                summary[i].getLayoutParams().width = 50;
                summary[i].getLayoutParams().height = 100;
                container[i].addView(time[i], 0);
                container[i].addView(summary[i], 1);
                container[i].addView(temp[i], 2);
                if(i % 2 != 0)
                    container[i].setBackgroundResource(R.color.common_signin_btn_light_text_disabled);
                else
                    container[i].setBackgroundResource(R.color.common_signin_btn_light_text_pressed);
                container[i].setPadding(10,10,0,0);
            }catch (Exception e){
                Log.d("next24Parse","Error while parsing next 24 data");
            }
        }
        //-----------------------------------end of rest of the body section of next 24----------------------------
    }
    public void renderNextSD(){
        DetailsActivity.this.nextTF.removeAllViews();
        LinearLayout[] containerFirst  = new LinearLayout[8];
        LinearLayout[] containerSecond = new LinearLayout[8];
        JSONArray content = DetailsActivity.this.weekly;
        int i = 1;
        for(i = 1 ; i <= 7 ; i++){
            try{
                String jlrok = null;
                JSONObject obj = content.getJSONObject(i);
                String iconStr = obj.getString("icon");
                //String timeStr = obj.getString("time");
                String tempMin = String.valueOf(obj.getInt("temperatureMin"));
                String tempMax = String.valueOf(obj.getInt("temperatureMax"));
                long timeStr = obj.getLong("time") * 1000;

                Date timeLongFormat = new Date(timeStr);
                SimpleDateFormat timeHandler = new SimpleDateFormat("EEEE, MMM dd");
                timeHandler.setTimeZone(TimeZone.getTimeZone(this.timeZone));
                String timeVal  = timeHandler.format(timeLongFormat);
                //String tempStr = ;
                //----------------------------------First Section of the next 07
                //----------------------------------First Section of the next 07 days-------------------------------------
                containerFirst[i] = new LinearLayout(this);
                containerFirst[i].setPadding(0,10,0,10);
                LinearLayout.LayoutParams paramsFirst = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
                TextView          timeFirst = new TextView(this);
                ImageView      summaryFirst = new ImageView(this);

                //TextView          tempFirst = new TextView(this);
                LinearLayout.LayoutParams firstContentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80, 1);
                LinearLayout.LayoutParams firstImageContentParams = new LinearLayout.LayoutParams(5, 80, 1);
                containerFirst[i].setOrientation(LinearLayout.HORIZONTAL);
                timeFirst.setLayoutParams(firstContentParams);
                summaryFirst.setLayoutParams(firstImageContentParams);
                timeFirst.setPadding(4, 0, 0, 0);
                summaryFirst.setPadding(0,4,0,0);
                //tempFirst.setLayoutParams(firstContentParams);
                timeFirst.setText(timeVal);
                timeFirst.setTextSize(22);
                timeFirst.setTextColor(Color.parseColor("#000000"));
                summaryFirst.setBackgroundResource(DetailsActivity.this.icon.get(iconStr));
                //summaryFirst.getLayoutParams().width = 30;
                //summaryFirst.getLayoutParams().height = 30;
                //tempFirst.setText("Temp");
                DetailsActivity.this.nextSD.addView(containerFirst[i], paramsFirst);
                containerFirst[i].addView(timeFirst,0);
                containerFirst[i].addView(summaryFirst, 1);
                //containerFirst[i].addView(tempFirst,2);

                //-----------------------------------End of First Section of next 07 days ---------------------------------
                //----------------------------------Second Section of the next 07 days-------------------------------------
                containerSecond[i] = new LinearLayout(this);
                containerSecond[i].setPadding(0, 45, 0, 40);
                LinearLayout.LayoutParams paramsSecond = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                paramsSecond.setMargins(0,0,0,35);
                //TextView          timeSecond = new TextView(this);
                //TextView       summarySecond = new TextView(this);
                TextView          tempSecond = new TextView(this);
                LinearLayout.LayoutParams secondContentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                containerSecond[i].setOrientation(LinearLayout.HORIZONTAL);
                //timeSecond.setLayoutParams(secondContentParams);
                //summarySecond.setLayoutParams(secondContentParams);
                tempSecond.setLayoutParams(secondContentParams);
                //timeSecond.setText("Time");
                //summarySecond.setText("Summary");
                tempSecond.setText(Html.fromHtml("Min: " + tempMin + "&deg;" + DetailsActivity.this.degree + " | Max: " + tempMax + "&deg;" + DetailsActivity.this.degree));
                tempSecond.setTextSize(22);
                tempSecond.setPadding(4,0,0,0);
                DetailsActivity.this.nextSD.addView(containerSecond[i], paramsSecond);
                //containerSecond[i].addView(timeSecond,0);
                //containerSecond[i].addView(summarySecond,1);
                containerSecond[i].addView(tempSecond, 0);
                switch (i){
                    case 1 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#e6b800"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#e6b800"));
                        break;
                    case 2 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#66e0ff"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#66e0ff"));
                        break;
                    case 3 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#ffb3ff"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#ffb3ff"));
                        break;
                    case 4 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#99ff99"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#99ff99"));
                        break;
                    case 5 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#ff8080"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#ff8080"));
                        break;
                    case 6 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#ffff99"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#ffff99"));
                        break;
                    case 7 :
                        containerFirst[i].setBackgroundColor(Color.parseColor("#bb99ff"));
                        containerSecond[i].setBackgroundColor(Color.parseColor("#bb99ff"));
                        break;
                }

            //-----------------------------------End of Second Section of next 07 days ---------------------------------
            }catch (Exception e){
                Log.d("ParseNextSeven","Error while parsing for next 7 days");
            }
        }
        //set weekly content to 1 so that, next time rendering function is not called
        DetailsActivity.this.weeklyContent = 1;
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Details Page", // TODO: Define a title for the content shown.
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
                "Details Page", // TODO: Define a title for the content shown.
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
}
