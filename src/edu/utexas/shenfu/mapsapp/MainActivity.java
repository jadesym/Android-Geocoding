package edu.utexas.shenfu.mapsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
 
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.io.IOUtils;


public class MainActivity extends Activity {
	
	private static GoogleMap map;
	private GoogleMap googleMap;
	private static final String GEO_CODE_SERVER = "http://maps.googleapis.com/maps/api/geocode/json?";
	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 

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
	
	public static LatLng getLatLongFromAddress(String youraddress) {
	    String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
	                  youraddress + "&key=AIzaSyBYG_Ch_bvFTU5HkevEirWi4VVzCDiiZPE";
	    HttpGet httpGet = new HttpGet(uri);
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());

	        double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	        double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");

	        Log.d("latitude", "" + lat);
	        Log.d("longitude", "" + lng);
	        LatLng location = new LatLng(lat, lng);
	        String strLat = Double.toString(lat);
	        String strLng = Double.toString(lng);
	        return location;


	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
		return null;

	}
	
	/*
	* Here the fullAddress String is in format like "address,city,state,zipcode". Here address means "street number + route" .
	*
	*/
	public String getJSONByGoogle(String fullAddress) {
		
		
		/*
		* Create an java.net.URL object by passing the request URL in constructor. 
		* Here you can see I am converting the fullAddress String in UTF-8 format. 
		* You will get Exception if you don't convert your address in UTF-8 format. Perhaps google loves UTF-8 format. :)
		* In parameter we also need to pass "sensor" parameter.
		* sensor (required parameter) — Indicates whether or not the geocoding request comes from a device with a location sensor. This value must be either true or false.
		*/
		URL url;
		try {
			url = new URL(fullAddress);
			// Open the Connection 

			URLConnection conn;
			conn = url.openConnection();
			//This is Simple a byte array output stream that we will use to keep the output data from google. 
			ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		
			// copying the output data from Google which will be either in JSON or XML depending on your request URL that in which format you have requested.
			IOUtils.copy(conn.getInputStream(), output);
		
			//close the byte array output stream now.
			output.close();
			return output.toString(); // This returned String is JSON string from which you can retrieve all key value pair and can save it in POJO.

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	
	}
	
	
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
 
    
    public void onClick (View view) throws IOException {
		EditText text = (EditText) findViewById(R.id.main_input);
		String location = text.getText().toString();
		
		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocationName(location, 1);
		Address add = list.get(0);
		String locality = add.getLocality();
		String postal_code = add.getPostalCode();
		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
		
		double lat = add.getLatitude();
		double lng = add.getLongitude();
        LatLng newLocation = new LatLng(lat, lng);
		

//		Toast.makeText(this, text.getText().toString(), Toast.LENGTH_LONG).show();
		
//		String getUrl = buildUrl(text.getText().toString());
//		try {
//			URL newUrl = new URL(getUrl);
//			
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		LatLng newLocation = getLatLongFromAddress(getUrl);
//		String jsonString = getJSONByGoogle(getUrl);
//		Toast.makeText(this, getUrl, Toast.LENGTH_LONG).show();
//		JSONObject jsonObject = new JSONObject();
//		
//	    try {
//	        jsonObject = new JSONObject(jsonString);
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
//	        GoogleMap map = ((MapFragment) getFragmentManager()
//	                .findFragmentById(R.id.map)).getMap();	        
//	        LatLng newLoc = new LatLng(lat, lng);
//
//	        map.setMyLocationEnabled(true);
//	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 13));
//
//	        map.addMarker(new MarkerOptions()
//	                .title("Input Location")
//	                .snippet("This is the location you chose.")
//	                .position(newLoc));
//
//	    } catch (JSONException e) {
//	        e.printStackTrace();
//	    }
		
		
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
//		LatLng location = new LatLng(-30.867, 160.206);
//		
		map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 13));
//
        map.addMarker(new MarkerOptions()
                .title("Locality: " + locality)
                .snippet("Postal Code: " + postal_code)
                .position(newLocation));
//        // create marker
        MarkerOptions marker = new MarkerOptions().position(newLocation).title("Hello Maps ");
//         
//        // adding marker
        googleMap.addMarker(marker);
		
		  
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
        builder.append("&key=AIzaSyBYG_Ch_bvFTU5HkevEirWi4VVzCDiiZPE");

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
