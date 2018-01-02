package com.example.rakshithr.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.parameter.PlaceParameter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.xml.transform.Result;

/**
 * Created by rakshithr on 11/23/15.
 */
public class ResultActivity extends FragmentActivity {
    private JSONObject result = null;
    private ImageView currentImageSrc;
    private TextView currentSummary;
    private TextView currTemp;
    private TextView lhTemp;
    private static HashMap<String, Integer> icon;
    private String state, city, degree, summary, fabIco, temp;
    private TextView currPrecipitation;
    private TextView currChanceOfRain;
    private TextView currWindSpeed;
    private TextView currDewPoint;
    private TextView currHumidity;
    private TextView currVisibility;
    private TextView currSunrise;
    private TextView currSunset;
    private Button moreDetails;
    private ImageButton imageFBButton;
    private Button viewMap;
    private String output;
    private String timeZone;
    private long longitude;
    private long latitude;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private TextView unit;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.result);
        Bundle bundle = getIntent().getExtras();
        icon = new HashMap<String, Integer>(30);
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            output = (String) bundle.getString("jsonOut");
            result = new JSONObject(output);
            city   = bundle.getString("city");
            state  = bundle.getString("state");
            degree = bundle.getString("degree");

            JSONObject daily = result.getJSONObject("daily");
            JSONObject currently = result.getJSONObject("currently");
            this.summary = currently.getString("summary");
            this.fabIco = currently.getString("icon");
            this.temp = String.valueOf(currently.getInt("temperature"));
            JSONArray ob = daily.getJSONArray("data");
            JSONObject mtemp = ob.getJSONObject(0);
            timeZone = result.getString("timezone");
            this.latitude = result.getLong("latitude");
            this.longitude = result.getLong("longitude");
            String maxtemp = String.valueOf(mtemp.getInt("temperatureMax"));
            String mintemp = String.valueOf(mtemp.getInt("temperatureMin"));
            //Formating date and time
            long sunriseTimeStamp = mtemp.getLong("sunriseTime") * 1000;
            long sunsetTimeStamp = mtemp.getLong("sunsetTime") * 1000;

            Date sunrise = new Date(sunriseTimeStamp);
            Date sunset  = new Date(sunsetTimeStamp);
            SimpleDateFormat timeHandler = new SimpleDateFormat("hh:mm aa");
            timeHandler.setTimeZone(TimeZone.getTimeZone(this.timeZone));


            //show the details of current weather
            this.currentImageSrc    = (ImageView) findViewById(R.id.currentImageSrc);
            this.currentSummary     = (TextView) findViewById(R.id.currentSummary);
            this.lhTemp             = (TextView) findViewById(R.id.lhTemp);
            this.currTemp           = (TextView) findViewById(R.id.currTemp);
            this.currPrecipitation  = (TextView) findViewById(R.id.currPrecipitation);
            this.currChanceOfRain   = (TextView) findViewById(R.id.currChanceOfRain);
            this.currWindSpeed      = (TextView) findViewById(R.id.currWindSpeed);
            this.currDewPoint       = (TextView) findViewById(R.id.currDewPoint);
            this.currHumidity       = (TextView) findViewById(R.id.currHumidity);
            this.currVisibility     = (TextView) findViewById(R.id.currVisibility);
            this.currSunrise        = (TextView) findViewById(R.id.currSunrise);
            this.currSunset         = (TextView) findViewById(R.id.currSunset);
            this.moreDetails        = (Button)   findViewById(R.id.moreDetails);
            this.imageFBButton      = (ImageButton) findViewById(R.id.imageFBButton);
            this.viewMap            = (Button) findViewById(R.id.viewMap);
            this.unit               = (TextView) findViewById(R.id.unit);
            //call sattic method to upload urls fro icon values, only if hash map size is lesser than zero
            if(icon.size() == 0)
                icon = fillIcon();
            JSONObject current = result.getJSONObject("currently");
            this.currentImageSrc.setBackgroundResource(icon.get(current.get("icon")));
            this.currentSummary.setText(current.get("summary").toString() + " in " + city + ", " + state);
            this.lhTemp.setText(Html.fromHtml("L: "+mintemp+"&deg; | H: " + maxtemp + "&deg;"));
            this.currTemp.setText(String.valueOf(current.getInt("temperature")));
            this.unit.setText(Html.fromHtml( "<sup style='font-size:8dp;'><small>&deg; " + ((((String) bundle.getString("degree")).equals("us")) ? "F" : "C") + "</small></sup>"));
            //if precipitation intensity is si or us do the conversion accordingly
            Double preipIntensity = current.getDouble("precipIntensity");
            if(this.degree.equals("si"))
                preipIntensity = preipIntensity / 25.4;
            String preIntenVal = "";
            if(preipIntensity >= 0 && preipIntensity < 0.002)
                preIntenVal = "None";
            else if(preipIntensity >= 0.002 && preipIntensity < 0.017)
                preIntenVal = "Very Light";
            else if(preipIntensity >= 0.017 && preipIntensity < 0.1)
                preIntenVal = "Light";
            else if(preipIntensity >= 0.1 && preipIntensity < 0.4)
                preIntenVal = "Moderate";
            else if(preipIntensity >= 0.4)
                preIntenVal = "Heavy";
            this.currPrecipitation.setText(preIntenVal.toString());
            String chanceOfRain = (current.get("precipProbability").toString().isEmpty())? "NA" : String.valueOf((int)(current.getDouble("precipProbability") * 100)) + " %";
            this.currChanceOfRain.setText(chanceOfRain);
            String windSpeed = (current.get("windSpeed").toString().isEmpty())? "NA" : df.format(current.getDouble("windSpeed")) + ((this.degree.equals("us"))? " mph":" m/s");
            this.currWindSpeed.setText(windSpeed);
            String dewPoint = (current.get("dewPoint").toString().isEmpty())? "NA" : Html.fromHtml(String.valueOf(current.getInt("dewPoint"))+ " &deg;") + ((this.degree.equals("us"))? "F":"C");
            this.currDewPoint.setText(dewPoint);
            String humidity = (current.get("humidity").toString().isEmpty())? "NA" : String.valueOf((int)(current.getDouble("humidity") * 100)) + " %";
            this.currHumidity.setText(humidity);
            String visibility = (current.get("visibility").toString().isEmpty())? "NA" : df.format(current.getDouble("visibility")) + ((this.degree.equals("us"))? " mi":" km");
            this.currVisibility.setText(visibility);
            //double offset = Double.parseDouble((String) result.get("offset"));
            //Double timeStamp = sunriseTimeStamp + offset * 3600;
            String sunriseStr = timeHandler.format(sunrise);
            String sunsetStr  = timeHandler.format(sunset);
            this.currSunrise.setText(sunriseStr);
            this.currSunset.setText(sunsetStr);
            //long EventUnixTime = eventData.getEventUnixtimeTime();
            //set on click event handler for more details button
            this.moreDetails.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    showMoreDetails(ResultActivity.this.output);
                }
            });
            this.shareDialog = new ShareDialog(this);
            this.callbackManager = CallbackManager.Factory.create();

            // this part is optional
            this.shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getApplicationContext(), "Facebook Successful", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Posted Cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(), "Posted Cancelled", Toast.LENGTH_LONG).show();
                }


            });
            this.imageFBButton.setOnClickListener(new ImageButton.OnClickListener(){
                public void onClick(View v){
                    HashMap<String, String> iconUrl = new HashMap<String, String>();
                    iconUrl.put("clear-day", "http://cs-server.usc.edu:45678/hw/hw8/images/clear.png");
                    iconUrl.put("clear-night","http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png");
                    iconUrl.put("rain","http://cs-server.usc.edu:45678/hw/hw8/images/rain.png");
                    iconUrl.put("snow","http://cs-server.usc.edu:45678/hw/hw8/images/snow.png");
                    iconUrl.put("sleet","http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png");
                    iconUrl.put("wind","http://cs-server.usc.edu:45678/hw/hw8/images/wind.png");
                    iconUrl.put("fog", "http://cs-server.usc.edu:45678/hw/hw8/images/fog.png");
                    iconUrl.put("cloudy","http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png");
                    iconUrl.put("partly-cloudy-day","http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png");
                    iconUrl.put("partly-cloudy-night","http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png");
                    String desc = (Html.fromHtml(ResultActivity.this.summary + ", " + ResultActivity.this.temp + "&deg;" + ((ResultActivity.this.degree.equals("us"))?"F" : "C")).toString());
                    String img = iconUrl.get(ResultActivity.this.fabIco).toString();
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle("Current Weather in " + ResultActivity.this.city + ", " + ResultActivity.this.state)
                                .setContentDescription(
                                        desc)
                                .setContentUrl(Uri.parse("http://forecast.io/"))
                                .setImageUrl(Uri.parse(img))
                                .build();

                        shareDialog.show(linkContent, ShareDialog.Mode.FEED);
                    }
                    //MessageDialog.show(activityOrFragment, content);
                }
            });
            // Map con click listener
            this.viewMap.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    showWeatherMap();
                }
            });
        }catch (Exception e){
            Log.d("ParseException", "Error while parsing forecast IO outputs");
        }
    }
    public void showMoreDetails(String Output){
        Intent details = new Intent(this, DetailsActivity.class);
        details.putExtra("output", Output);
        details.putExtra("degree",ResultActivity.this.degree);
        details.putExtra("state",ResultActivity.this.state);
        details.putExtra("city",ResultActivity.this.city);
        details.putExtra("timezone",ResultActivity.this.timeZone);
        startActivity(details);
    }
    public void showWeatherMap(){

        Intent map = new Intent(this, MapActivity.class);
        map.putExtra("longitude",ResultActivity.this.longitude);
        map.putExtra("latitude",ResultActivity.this.latitude);
        startActivity(map);
    }

    public static HashMap<String, Integer> fillIcon(){
        icon.put("clear-day", R.drawable.clear);
        icon.put("clear-night",R.drawable.clear_night);
        icon.put("rain",R.drawable.rain);
        icon.put("snow",R.drawable.snow);
        icon.put("sleet",R.drawable.sleet);
        icon.put("wind",R.drawable.wind);
        icon.put("fog", R.drawable.fog);
        icon.put("cloudy",R.drawable.cloudy);
        icon.put("partly-cloudy-day",R.drawable.cloud_day);
        icon.put("partly-cloudy-night",R.drawable.cloud_night);
        return icon;
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
