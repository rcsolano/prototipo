package com.example.prototipo.ui.misDenuncias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototipo.CRUDdenuncia;
import com.example.prototipo.Fragment_unaDenuncia;
import com.example.prototipo.R;
import com.example.prototipo.clsDenuncia;
import com.example.prototipo.clsMyAdapter;
import com.example.prototipo.clsMySQLiteOpenHelper;
import com.example.prototipo.clsWebRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Spliterator;

import cz.msebera.android.httpclient.Header;

public class mis_denunciasFragment extends Fragment {

    private mis_denunciasViewModel misdenunciasViewModel;

    private RecyclerView recyclerViewDenuncias;
    //private RecyclerView.Adapter objAdapter;
    private clsMyAdapter objAdapter;
    private RecyclerView.LayoutManager objLayoutManager;

    private Context context;//TODO: NUEVO

    private String mensaje;
    private Boolean error;
    private ArrayList<clsDenuncia> listDenuncias;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        misdenunciasViewModel = ViewModelProviders.of(this).get(mis_denunciasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mis_denuncias, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO: agregue


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO: agregue
        final String idusuario = sharedPreferences.getString("idUsuario", "");
        SharedPreferences.Editor sharedPrefEditor;
        sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString("tipo", "misDenuncias");
        sharedPrefEditor.apply();
        sharedPrefEditor.commit();

        recyclerViewDenuncias = (RecyclerView) view.findViewById(R.id.recyclerView);
        objLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewDenuncias.setLayoutManager(objLayoutManager);

        RequestParams params = new RequestParams();
        params.add("type","misDenuncias");
        params.add("idUsuario",idusuario);
        context = getContext();
        clsWebRequest.post(context, "denuncia.php", params, new mis_denunciasFragment.ResponseHandler());
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
            Log.d("Denuncia::3 ", "Exito " + statusCode);
            try {
                mensaje = response.getString("message");
                if (response.getBoolean("error")) {
                    // error no se encontraron denuncias
                    Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();//
                } else {
                    // Exito al cargar las denuncias
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    final JSONArray denuncias = response.getJSONArray("denuncias");

                    listDenuncias = new ArrayList<>();
                    for (int i=0;i<denuncias.length();i++) {
                        Log.d("denuncias", "" + denuncias.getJSONObject(i).toString());
                        try {
                            JSONObject jsonObject = denuncias.getJSONObject(i);
                            String latitud = jsonObject.getString("latitud");
                            String longitud = jsonObject.getString("longitud");
                            String estado = jsonObject.getString("estado");
                            String texto = jsonObject.getString("texto");
                            String foto = jsonObject.getString("foto_web");
                            String fecha = jsonObject.getString("fecha_hora");
                            String idusuario = jsonObject.getString("id_usuario");
                            String patente = jsonObject.getString("patente");

                            listDenuncias.add(new clsDenuncia(latitud, longitud, estado, texto, foto, patente, fecha, idusuario));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    context = getContext();
                    objAdapter = new clsMyAdapter(context, listDenuncias);
                    recyclerViewDenuncias.setAdapter(objAdapter);


                    objAdapter.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                JSONObject jsonObject = denuncias.getJSONObject(recyclerViewDenuncias.getChildAdapterPosition(v));

                                String estado = jsonObject.getString("estado");
                                String texto = jsonObject.getString("texto");
                                String foto = jsonObject.getString("foto_web");
                                String fecha = jsonObject.getString("fecha_hora");
                                String idusuario = jsonObject.getString("id_usuario");
                                String patente = jsonObject.getString("patente");


                                int id = recyclerViewDenuncias.getChildAdapterPosition(v);

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

                                mis_denunciasFragmentDirections.ActionNavMisDenunciasToFragmentUnaDenuncia action = mis_denunciasFragmentDirections.actionNavMisDenunciasToFragmentUnaDenuncia(-1);
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
            Log.d("DENUNCIA::4", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }

    }



}
