package ute.place;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        String url = "https://apitest.orange.pl/Localization/v1/GeoLocation?msisdn=48506436958&apikey=qr1d7R3Ag3gop06s1bzRuySh7fxukfSA";
        JSONObject[] json = getRequest(url);

        //LatLngBounds orangeLocation = getLatLngBounds(new LatLng(52.220955,21.008073));
        //builder.setLatLngBounds(orangeLocation);

        try {
            Intent intent = builder.build(getApplicationContext());
            startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
//    public static LatLngBounds getLatLngBounds(LatLng center) {
//        double radius = 100;
//
//        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
//        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
//        return new LatLngBounds(southwest, northeast);
//    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng ll = place.getLatLng();

                System.out.printf("Latitude:" + String.valueOf(ll.latitude)+ "Longitute:"+ String.valueOf(ll.longitude));
            }
        }
    }

    // this should return latitude from json
    private int getLatitude(JSONObject[] object){
        return 0;
    }
    // this should return longtitude from json
    private int getLongitude(JSONObject[] object){
        return 0;
    }
    private JSONObject[] getRequest(String urlStr){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject[] responseJson = {null};
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlStr, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseJson[0] = response;
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Didn't work", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
        return responseJson;
    }
}
