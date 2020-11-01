package com.example.prototipo.ui.configuracion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.prototipo.R;
import com.example.prototipo.clsWebRequest;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class configuracionFragment extends Fragment {

    private EditText pass, pass_rep, pass_actual;
    private Button btnActualizar;
    private TextView txtViewAlerta;
    Intent intent;
    Context context;
    View view;
    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public configuracionFragment() {
        // Required empty public constructor
    }



   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        pass = (EditText) view.findViewById(R.id.etCambiar_pass);
        pass_rep = (EditText) view.findViewById(R.id.etCambiar_pass_rep);
        pass_actual = (EditText) view.findViewById(R.id.etpass_actual);
        txtViewAlerta = (TextView) view.findViewById(R.id.textViewAlerta);
        txtViewAlerta.setVisibility(View.GONE);
        btnActualizar = (Button) view.findViewById(R.id.btnActualizar);



        sharedPreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String tipoAlta = sharedPreferences.getString("tipo-alta", "");

        //Log.d("idusuario ", "usuario_id:: " + idusuario);
        Log.d("tipoAlta ", "tipo_alta::" + tipoAlta);


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences sharedPreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                String password_actual = pass_actual.getText().toString();
                final String password = pass.getText().toString();
                final String password_rep = pass_rep.getText().toString();
                //String email = sharedPreferences.getString("email", "");
                final String idusuario = sharedPreferences.getString("idUsuario", "");

                RequestParams params = new RequestParams();
                if (password.equals(password_rep)) {
                    Log.d("entro a::",tipoAlta);
                    params.add("type", "cambiar-pass-menu");
                    params.add("password", password);
                    params.add("idUsuario", idusuario);
                    params.add("pass_actual", pass_actual.getText().toString());

                    //params.add("pedir_rec","0");
                    //params.add("email", email);

                    clsWebRequest.post(context, "denuncia.php", params, new configuracionFragment.ResponseHandler());
                } else {
                    txtViewAlerta.setVisibility(View.VISIBLE);
                }


            }

        });
    }
    private class ResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            super.onSuccess(statusCode, headers, response);
            Log.d("cambiar pass:: ", "Exito " + statusCode);
            try {
                //mensaje = response.getString("message");
                if (response.getBoolean("error")) {
                    Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_nav_configuracion_to_nav_home);
                }else{
                    Snackbar.make(getView(),  "Contraseña actualizada con exito.", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_nav_configuracion_to_nav_home);
                }
                /*if (response.getBoolean("error")) {
                    // error no se cambio la contraseña
                    String prueba=response.getString("type").toString();
                    Log.v("error-cambiar pass:: ", "error.. "+ prueba );
                    if(response.getString("type").equals("cambiar-pass-menu")){
                        Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_nav_configuracion_to_nav_home);
                    }
                    if(response.getString("type").equals("generar-pass") ){
                        //vuelvo al login
                        Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                       *//* intent = new Intent(context, actLogin.class);//me tengo q ir al mainactivity o home?
                        startActivity(intent);*//*
                        fragmentManager = getActivity().getSupportFragmentManager();
                        //final Fragment fragment=fragmentManager.findFragmentById(R.id.contenedor_fragment_config);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.nav_host_fragment));
                        //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        //onFinish();
                    }
                    //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    if(response.getString("type").equals("cambiar-pass-menu")){
                        Snackbar.make(getView(),  "Contraseña actualizada con exito.", Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, "contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
                        pass_actual.setVisibility(View.GONE);
                        Navigation.findNavController(view).navigate(R.id.action_nav_configuracion_to_nav_home);
                    }else{
                        String prueba=response.getString("type");
                        Log.v("tipo_:: ", "exitooo.. "+ prueba );
                        Snackbar.make(getView(),  response.getString("message"), Snackbar.LENGTH_SHORT).show();

                        //Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue
                        SharedPreferences.Editor edt= sharedPreferences.edit();//TODO: NUEVO
                        edt.putString("tipo-alta","cambiar-pass-menu");
                        edt.apply();
                        edt.commit();
                        fragmentManager = getActivity().getSupportFragmentManager();
                        //final Fragment fragment=fragmentManager.findFragmentById(R.id.contenedor_fragment_config);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.nav_host_fragment));
                        //fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        //onFinish();
                        *//*Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        onFinish();*//*
                        //finish();

                    }


                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("cambiar-pass::", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();


        }


    }
}