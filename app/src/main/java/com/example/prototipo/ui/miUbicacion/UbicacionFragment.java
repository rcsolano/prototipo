package com.example.prototipo.ui.miUbicacion;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.prototipo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class UbicacionFragment extends Fragment implements LocationListener {

    private FloatingActionButton fabLocation;
    private TextView tvLocation;
    private LocationManager locationManager;

    //private Location location;

    private MapView map;

    private GeoPoint p, newPoint;
    private IMapController mapController;
    private Marker startMarker;
    private boolean restart;

    private Double latitud, longitud;
    private RotationGestureOverlay mRotationGestureOverlay;
    private CompassOverlay mCompassOverlay;
    private static final int STORAGE_REQUEST = 100;
    private static final int LOCATION_REQUEST = 101;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ubicacion_fragment, container, false);

        //Permiso para activar la ubicacion
        permisoAccesoUbicacion();
        //--------------------

        this.restart = false;
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        this.map = (MapView) view.findViewById(R.id.map);

        this.fabLocation = view.findViewById(R.id.fabLocalizar);
        //this.tvLocation = (TextView) view.findViewById(R.id.tvLocation);


        this.fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        this.map.setTileSource(TileSourceFactory.MAPNIK);
        this.map.setBuiltInZoomControls(true);
        this.map.setMultiTouchControls(true);
        this.newPoint = new GeoPoint(-24.786383, -65.412249);
        this.mapController = this.map.getController();
        this.mapController.setCenter(this.newPoint);
        this.mapController.setZoom(17.0);

        //para rotar
        mRotationGestureOverlay = new RotationGestureOverlay(getActivity(), map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(this.mRotationGestureOverlay);

        //para brujula
        mCompassOverlay = new CompassOverlay(getActivity(), new InternalCompassOrientationProvider(getActivity()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);


        this.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        permisoAccesoUbicacion();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return TODO;

        }

        //this.locationManager.requestLocationUpdates(NETWORK_PROVIDER, 5000, 10, this);
        this.locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 0, this);
       /* startMarker = new Marker(map);
        startMarker.setPosition(this.newPoint);
        startMarker.setAnchor(startMarker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.map.getOverlays().add(startMarker);*/

        //Posicion Actual
        //final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), this.map);
        //this.map.getOverlays().add(myLocationoverlay); //para marcar la posicion
        //myLocationoverlay.enableMyLocation();
        /*myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });*/

        //this.newPoint = new GeoPoint(p.getLongitude(), p.getLatitude());

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void getLocation() {
        try {
            //Toast.makeText(getActivity(), "Inicio", Toast.LENGTH_SHORT).show();
            //Log.d("GPS::2 ", "getLocation");

            //Permiso para activar la ubicacion
            permisoAccesoUbicacion();
            //--------------------


            this.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            this.locationManager.requestLocationUpdates(NETWORK_PROVIDER, 500, 5, this);




            //Posicion Actual
            //final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), this.map);
            //this.map.getOverlays().add(myLocationoverlay); //para marcar la posicion
            //myLocationoverlay.enableMyLocation();

            //final MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()),this.map);
            //mLocationOverlay.enableMyLocation();
            //map.getOverlays().add(mLocationOverlay);




            //this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            /*boolean isGPSEnabled = false;
            isGPSEnabled =locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isNetworkEnabled = false;
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);*/



            this.map.getOverlays().clear();
            this.map.invalidate();

            this.restart = true;
        }
        catch(SecurityException e) {
            Log.d("GPS::2 ", "Error getLocation");
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //this.counter = this.counter + 1;
        //this.tvLocation.setText("Lat " +location.getLatitude() + " Long " + location.getLongitude());
        this.longitud = location.getLongitude();
        this.latitud = location.getLatitude();
        Log.v("verLocacion::", latitud.toString() + longitud.toString());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt= sharedpreferences.edit();

        edt.putString("longitud",Double.toString(this.longitud));
        edt.putString("latitud",Double.toString(this.latitud));
        edt.commit();

        this.newPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        startMarker = new Marker(map);
        //startMarker.setIcon(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));
        startMarker.setTitle("Posicion Actual... ");
        this.restart = false;
        startMarker.setPosition(this.newPoint);
        startMarker.setAnchor(startMarker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.map.getOverlays().add(startMarker);
        this.mapController.setCenter(this.newPoint);
        this.mapController.setZoom(19.0);
        locationManager.removeUpdates(this); //todo : no solicitará la actualización de la ubicación


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Por favor habilite el GPS e Internet", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }*/



    private void permisoAccesoUbicacion(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            /*requestPermissionsIfNecessary(new String[]{
                    // if you need to show the current location, uncomment the line below
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    // WRITE_EXTERNAL_STORAGE is required in order to show the map
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            });*/
        }
    }


    /*private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new
                            String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
*/
}
