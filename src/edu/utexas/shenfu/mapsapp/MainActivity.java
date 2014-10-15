package edu.utexas.shenfu.mapsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
 
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.MapFragment;


public class MainActivity extends Activity {
	
	private static GoogleMap map;
	private GoogleMap googleMap;
	private static final String GEO_CODE_SERVER = "http://maps.googleapis.com/maps/api/geocode/json?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
//        LatLng sydney2 = new LatLng(-36.867, 156.206);
//        
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney2, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney2")
//                .snippet("The 2nd most populous city in Australia.")
//                .position(sydney2));

	}
	
//	public static LatLng getLatLongFromAddress(String youraddress) {
//	    String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
//	                  youraddress + "&sensor=falsekey=AIzaSyBYG_Ch_bvFTU5HkevEirWi4VVzCDiiZPE";
//	    HttpGet httpGet = new HttpGet(uri);
//	    HttpClient client = new DefaultHttpClient();
//	    HttpResponse response;
//	    StringBuilder stringBuilder = new StringBuilder();
//
//	    try {
//	        response = client.execute(httpGet);
//	        HttpEntity entity = response.getEntity();
//	        InputStream stream = entity.getContent();
//	        int b;
//	        while ((b = stream.read()) != -1) {
//	            stringBuilder.append((char) b);
//	        }
//	    } catch (ClientProtocolException e) {
//	        e.printStackTrace();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//
//	    JSONObject jsonObject = new JSONObject();
//	    try {
//	        jsonObject = new JSONObject(stringBuilder.toString());
//
//	        double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//	            .getJSONObject("geometry").getJSONObject("location")
//	            .getDouble("lng");
//
//	        double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//	            .getJSONObject("geometry").getJSONObject("location")
//	            .getDouble("lat");
//
//	        Log.d("latitude", "" + lat);
//	        Log.d("longitude", "" + lng);
//	        LatLng location = new LatLng(lat, lng);
//	        String strLat = Double.toString(lat);
//	        String strLng = Double.toString(lng);
//	        return location;
//
//
//	    } catch (JSONException e) {
//	        e.printStackTrace();
//	    }
//		return null;
//
//	}
	
	/**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    
    public void onClick (View view) {
		EditText text = (EditText) findViewById(R.id.main_input);
		Toast.makeText(this, text.getText().toString(),
		Toast.LENGTH_LONG).show();
//		findLocation(text.getText().toString());
//		LatLng location = getLatLongFromAddress(text.getText().toString());
		
//		map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(location));
        // create marker
//        MarkerOptions marker = new MarkerOptions().position(location).title("Hello Maps ");
         
        // adding marker
//        googleMap.addMarker(marker);
		
		  
	} 
    
    public static void findLocation(String string)
    {
        String code = string;

        String response = getLocation(code);

        String[] result = parseLocation(response);

        System.out.println("Latitude: " + result[0]);
        System.out.println("Longitude: " + result[1]);
     // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Integer.parseInt(result[0]), Integer.parseInt(result[1]))).title("Hello Maps ");
         
        // adding marker
        map.addMarker(marker);
    }

    private static String getLocation(String code)
    {
        String address = buildUrl(code);

        String content = null;

        try
        {
            URL url = new URL(address);

            InputStream stream = url.openStream();

            try
            {
                int available = stream.available();

                byte[] bytes = new byte[available];

                stream.read(bytes);

                content = new String(bytes);
            }
            finally
            {
                stream.close();
            }

            return (String) content.toString();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String buildUrl(String code)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(GEO_CODE_SERVER);

        builder.append("address=");
        builder.append(code.replaceAll(" ", "+"));
        builder.append("&sensor=false");

        return builder.toString();
    }

    private static String[] parseLocation(String response)
    {
        // Look for location using brute force.
        // There are much nicer ways to do this, e.g. with Google's JSON library: Gson
        //     https://sites.google.com/site/gson/gson-user-guide

        String[] lines = response.split("\n");

        String lat = null;
        String lng = null;

        for (int i = 0; i < lines.length; i++)
        {
            if ("\"location\" : {".equals(lines[i].trim()))
            {
                lat = getOrdinate(lines[i+1]);
                lng = getOrdinate(lines[i+2]);
                break;
            }
        }

        return new String[] {lat, lng};
    }

    private static String getOrdinate(String s)
    {
        String[] split = s.trim().split(" ");

        if (split.length < 1)
        {
            return null;
        }

        String ord = split[split.length - 1];

        if (ord.endsWith(","))
        {
            ord = ord.substring(0, ord.length() - 1);
        }

        // Check that the result is a valid double
        Double.parseDouble(ord);

        return ord;
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
