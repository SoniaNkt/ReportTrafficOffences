package localhost.traffic_offences;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng central = new LatLng(-1.2795185, 36.8161327);
        mMap.addMarker(new MarkerOptions()
                .position(central)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Central Police Station"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(central));

        // Add a marker in Sydney and move the camera
        LatLng kilimani = new LatLng(-1.2916532, 36.7929634);
        mMap.addMarker(new MarkerOptions()
                .position(kilimani)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Kilimani Police Station"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(kilimani));

        LatLng eastlands = new LatLng(-1.2925323, 36.8587087);
        mMap.addMarker(new MarkerOptions()
                .position(eastlands)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Jogoo Rd Police Station"));

        LatLng headquarters = new LatLng(-1.256801, 36.8552187);
        mMap.addMarker(new MarkerOptions()
                .position(headquarters)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Traffic Police Headquarters"));

        LatLng ngongrd = new LatLng(-1.2986173, 36.8031713);
        mMap.addMarker(new MarkerOptions()
                .position(ngongrd)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Nairobi Area Traffic Headquarters"));

        LatLng karen = new LatLng(-1.3219421, 36.7040785);
        mMap.addMarker(new MarkerOptions()
                .position(karen)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Karen Police Station"));

        LatLng kileleshwa = new LatLng(-1.2753379, 36.7920731);
        mMap.addMarker(new MarkerOptions()
                .position(kileleshwa)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Kileleshwa Police Station"));
    }
}