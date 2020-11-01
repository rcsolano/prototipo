package com.example.prototipo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.prototipo.ui.misDenuncias.mis_denunciasFragment;
import com.google.android.material.textview.MaterialTextView;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_unaDenuncia extends Fragment {

    private TextView tvestado, tvfecha, tvtexto, tvPatente;

    private TextView mtvEstado;
    private ImageView ivfoto;

    private Toolbar toolbar;

    private Context context;
    private MapView map;
    private GeoPoint myPoint;
    private IMapController mapController;
    private Marker marker;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private CRUDdenuncia objCRUDdenuncia;
    private clsMySQLiteOpenHelper objHelper;




    public Fragment_unaDenuncia() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_una_denuncia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        onResumen();
        tvtexto = (TextView) view.findViewById(R.id.textView_denuncia);
        tvPatente = (TextView) view.findViewById(R.id.textView_dominio);
        tvestado = (TextView) view.findViewById(R.id.textView_estado);
        tvfecha = (TextView) view.findViewById(R.id.textView_fecha);
        ivfoto = (ImageView) view.findViewById(R.id.imageView_foto);
        mtvEstado = (TextView) view.findViewById(R.id.MaterialTextView_estado);

        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String tipo = sharedPreferences.getString("tipo", "");
        if(tipo.equals("mis_infracciones")){
            Log.v("TAG:TIPO....", tipo);
            tvestado.setVisibility(View.GONE);
            mtvEstado.setVisibility(View.GONE);
        }else{
            tvestado.setVisibility(View.VISIBLE);
            mtvEstado.setVisibility(View.VISIBLE);
        }*/

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        int id_denuncia = sharedPreferences.getInt("id",0);

        //int id_denuncia = Fragment_unaDenunciaArgs.fromBundle(getArguments()).getIddenuncia();
        //Toast.makeText(context, " id_denuncia" + id_denuncia, Toast.LENGTH_SHORT).show();

        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(id_denuncia));

        //clsWebRequest.post(context, "denuncia.php", params, new mis_denunciasFragment.ResponseHandler());


        //objCRUDdenuncia = new CRUDdenuncia(getContext());
        //objCRUDdenuncia.open();
        //objHelper = new clsMySQLiteOpenHelper(getContext());
        //Cursor myCursor = objCRUDdenuncia.getDenunciaXidDenuncia(id_denuncia);










        String latitud = sharedPreferences.getString("latitud","");
        String longitud = sharedPreferences.getString("longitud", "");
        String estado = sharedPreferences.getString("estado", "");
        String texto = sharedPreferences.getString("texto", "");
        String foto = sharedPreferences.getString("foto", "");
        String fecha = sharedPreferences.getString("fecha_hora", "");
        String patente = sharedPreferences.getString("patente", "");
        double lat = Double.valueOf(latitud);
        double longi = Double.valueOf(longitud);
        /*Bundle recuperoDatos = getArguments();
        if(recuperoDatos == null) {
            Toast.makeText(context, "no hay datos", Toast.LENGTH_SHORT).show();
            return;
        }

        String estado = recuperoDatos.getString("estado");
        String fecha = recuperoDatos.getString("fecha");
        String texto = recuperoDatos.getString("texto");
        String foto = recuperoDatos.getString("foto");
        String latitud = recuperoDatos.getString("latitud");
        String longitud = recuperoDatos.getString("longitud");
        double lat = Double.valueOf(latitud);
        double longi = Double.valueOf(longitud);*/

        //Toast.makeText(context, "estado" + estado, Toast.LENGTH_SHORT).show();
        tvtexto.setText(tvtexto.getText() + texto);
        tvestado.setText(tvestado.getText() + estado);
        tvfecha.setText(tvfecha.getText() + fecha);
        tvPatente.setText(tvPatente.getText() + patente);
//        final int i = ivfoto.getResources().getIdentifier(foto, null, "com.sistema.prototipo");//todo:ver si esta bien definido el packpage
//        ivfoto.setImageResource(i);

        //imagen guardada en el celular
        //Bitmap bm = BitmapFactory.decodeFile(Uri.decode(foto));
        //ivfoto.setImageBitmap(bm);
        //-----

        Picasso.with(context).load(clsGlobal.getInstance().BASE_URL+"imagenes/"+foto+".png").into(ivfoto);

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        this.map = (MapView) view.findViewById(R.id.mapItem);
        this.map.setTileSource(TileSourceFactory.MAPNIK);
        this.map.setBuiltInZoomControls(true);
        this.map.setMultiTouchControls(true);
        this.myPoint = new GeoPoint(lat, longi);
        this.mapController = this.map.getController();
        this.mapController.setCenter(this.myPoint);
        this.mapController.setZoom(17.0);
        marker = new Marker(map);
        marker.setPosition(this.myPoint);
        marker.setAnchor(marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        this.map.getOverlays().add(marker);
        this.map.invalidate();
        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    //CAMBIO EL NOMBRE DEL TITULO DE ACTIONBAR SEGUN SEA UNA INFRACCION O UNA DENUNCIA
    public void onResumen(){
        super.onResume();
        //TODO: NUEVO
        SharedPreferences sharedPreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue
        final String tipo = sharedPreferences.getString("tipo","");
        if(tipo.equals("misInfracciones")){
            Log.v("titulo", tipo);
            ((MainActivity) getActivity()).setActionBarTitle("Infraccion");
        }else{
            Log.v("titulo::", tipo);
            ((MainActivity) getActivity()).setActionBarTitle("Denuncia");
        }
    }
}