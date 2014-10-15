package edu.utexas.shenfu.mapsapp;

import java.io.IOException;
import java.util.List;
 
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import com.google.android.gms.maps.MapFragment;


public class MainActivity extends Activity {
	
	private static GoogleMap map;
	private GoogleMap googleMap;
	private static final String GEO_CODE_SERVER = "http://maps.googleapis.com/maps/api/geocode/json?";
	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 
	
	double currentLat;
	double currentLng;
	
	double lastLat;
	double lastLng;

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
		
    	lastLat = currentLat;
    	lastLng = currentLng;
    	
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
		
        currentLat = lat;
        currentLng = lng;
		
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

        MarkerOptions marker = new MarkerOptions().position(newLocation).title("Hello Maps ");


        googleMap.addMarker(marker);
		  
	} 
    
    public void onClickCurrentLocation (View view) {

    	double UTlat = 30.288956;
    	double UTlng = -97.7356784;
    	
    	double distance = getDistanceFromLatLonInKm(UTlat, UTlng, currentLat, currentLng);
    	distance = Math.round(distance*100.0)/100.0;
    	String newDistance = String.valueOf(distance);
    	Toast.makeText(this, "Distance from UT to your searched location is: " + newDistance + " kilometers", Toast.LENGTH_LONG).show();
    	
		  
	} 
    
    public void onClickLastLocation (View view) {
    	
    	double distance = getDistanceFromLatLonInKm(lastLat, lastLng, currentLat, currentLng);
    	distance = Math.round(distance*100.0)/100.0;
    	String newDistance = String.valueOf(distance);
    	Toast.makeText(this, "Distance from your last searched location to your currently searched location is: " + newDistance + " kilometers", Toast.LENGTH_LONG).show();
    	
		  
	} 
    
    public void onClickMcDonalds (View view) {
		
		try {
	    	Geocoder gc = new Geocoder(this);
			List<Address> list = gc.getFromLocationName("McDonald's", 1, currentLat-.01, currentLng-.01, currentLat+.01, currentLng+.01);
			Address add = list.get(0);
			String address = add.getAddressLine(0);
			Toast.makeText(this, address, Toast.LENGTH_LONG).show();
		
			double lat = add.getLatitude();
			double lng = add.getLongitude();
			LatLng newLocation = new LatLng(lat, lng);
		
			GoogleMap map = ((MapFragment) getFragmentManager()
	            .findFragmentById(R.id.map)).getMap();
	
	
			map.addMarker(new MarkerOptions()
	            .title("Nearby McDonald's")
	            .snippet(address)
	            .position(newLocation));
	
			MarkerOptions marker = new MarkerOptions().position(newLocation).title("McDonald's");
	
			googleMap.addMarker(marker);
		}
		catch (IllegalArgumentException e){
			Toast.makeText(this, "No nearby McDonald's", Toast.LENGTH_LONG).show();

			
		}
		catch (IOException e) {
			Toast.makeText(this, "No nearby McDonald's", Toast.LENGTH_LONG).show();

		}
		catch (IllegalStateException e) {
			Toast.makeText(this, "No nearby McDonald's", Toast.LENGTH_LONG).show();

		}
 	} 
    
    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
  	  double R = 6371; // Radius of the earth in km
  	  double dLat = Math.toRadians(lat2-lat1);  // deg2rad below
  	  double dLon = Math.toRadians(lon2-lon1); 
  	  double a = 
  	    Math.sin(dLat/2) * Math.sin(dLat/2) +
  	    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
  	    Math.sin(dLon/2) * Math.sin(dLon/2); 
  	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  	  double d = R * c; // Distance in km
  	  return d;
  	}
    
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
	
	
}
