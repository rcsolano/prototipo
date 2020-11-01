package com.example.prototipo.ui.misInfracciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prototipo.CRUDdenuncia;
import com.example.prototipo.R;
import com.example.prototipo.clsCRUDInfracciones;
import com.example.prototipo.clsDenuncia;
import com.example.prototipo.clsMyAdapter;
import com.example.prototipo.clsMySQLiteOpenHelper;
import com.example.prototipo.clsWebRequest;
import com.example.prototipo.ui.misInfracciones.mis_infraccionesFragment;
import com.example.prototipo.ui.misInfracciones.mis_infraccionesFragmentDirections;
import com.example.prototipo.ui.misInfracciones.mis_infraccionesViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class mis_infraccionesFragment extends Fragment {

    private RecyclerView recyclerViewInfracciones;
    private clsMyAdapter objAdapter;
    private RecyclerView.LayoutManager objLayoutManager;

    private mis_infraccionesViewModel misinfraccionesViewModel;

    //private clsCRUDInfracciones objCRUDinfracciones;
    //private clsMySQLiteOpenHelper objHelper;

    private Context context;//TODO: NUEVO
    private String mensaje;
    private ArrayList<clsDenuncia> listInfracciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        misinfraccionesViewModel = ViewModelProviders.of(this).get(mis_infraccionesViewModel.class);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_infracciones, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO: agregue
        final String idusuario = sharedPreferences.getString("idUsuario", "");

        SharedPreferences.Editor sharedPrefEditor;
        sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString("tipo", "misInfracciones");
        sharedPrefEditor.apply();
        sharedPrefEditor.commit();


        recyclerViewInfracciones = (RecyclerView) view.findViewById(R.id.recyclerViewInfracciones);
        objLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewInfracciones.setLayoutManager(objLayoutManager);

        RequestParams params = new RequestParams();
        params.add("type", "misInfracciones");
        params.add("idUsuario", idusuario);


        clsWebRequest.post(context, "denuncia.php", params, new mis_infraccionesFragment.ResponseHandler());

        //Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("Infracciones::3 ", "Exito " + statusCode);
            try {
                mensaje = response.getString("message");
                if (response.getBoolean("error")) {
                    // error no se encontraron denuncias
                    Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                } else {
                    // Exito al cargar las denuncias
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    final JSONArray infracciones = response.getJSONArray("infracciones");

                    listInfracciones = new ArrayList<>();
                    for (int i=0;i<infracciones.length();i++) {
                        Log.d("infracciones", "" + infracciones.getJSONObject(i).toString());
                        try {
                            JSONObject jsonObject = infracciones.getJSONObject(i);
                            String latitud = jsonObject.getString("latitud");
                            String longitud = jsonObject.getString("longitud");
                            String estado = jsonObject.getString("estado");
                            String texto = jsonObject.getString("texto");
                            String foto = jsonObject.getString("foto_web");
                            //String fotoweb = jsonObject.getString("foto_web");
                            String fecha = jsonObject.getString("fecha_hora");
                            String idusuario = jsonObject.getString("id_usuario");
                            String patente = jsonObject.getString("patente");

                            listInfracciones.add(new clsDenuncia(latitud, longitud, estado, texto, foto, patente, fecha, idusuario));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    context = getContext();

                    objAdapter = new clsMyAdapter(context, listInfracciones);
                    recyclerViewInfracciones.setAdapter(objAdapter);


                    objAdapter.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                JSONObject jsonObject = infracciones.getJSONObject(recyclerViewInfracciones.getChildAdapterPosition(v));

                                String estado = jsonObject.getString("estado");
                                String texto = jsonObject.getString("texto");
                                String foto = jsonObject.getString("foto_web");

                                String fecha = jsonObject.getString("fecha_hora");
                                String idusuario = jsonObject.getString("id_usuario");
                                String patente = jsonObject.getString("patente");

                                int id = recyclerViewInfracciones.getChildAdapterPosition(v);

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO: agregue
                                SharedPreferences.Editor sharedPrefEditor;

                                sharedPrefEditor = sharedPreferences.edit();
                                sharedPrefEditor.putInt("id", id);
                                sharedPrefEditor.putString("latitud",jsonObject.getString("latitud"));
                                sharedPrefEditor.putString("longitud", jsonObject.getString("longitud"));
                                sharedPrefEditor.putString("estado", jsonObject.getString("estado"));
                                sharedPrefEditor.putString("texto", jsonObject.getString("texto"));
                                sharedPrefEditor.putString("foto", jsonObject.getString("foto_web"));
                                sharedPrefEditor.putString("fecha_hora", jsonObject.getString("fecha_hora"));
                                sharedPrefEditor.putString("id_usuario", jsonObject.getString("id_usuario"));
                                sharedPrefEditor.putString("patente", jsonObject.getString("patente"));

                                sharedPrefEditor.apply();
                                sharedPrefEditor.commit();


                                mis_infraccionesFragmentDirections.ActionNavMisInfraccionesToFragmentUnaDenuncia action= mis_infraccionesFragmentDirections.actionNavMisInfraccionesToFragmentUnaDenuncia(-1);
                                action.setIddenuncia(id);
                                Navigation.findNavController(v).navigate(action);


                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("Infracciones::4", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }

    }

}