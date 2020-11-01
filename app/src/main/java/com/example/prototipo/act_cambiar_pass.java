package com.example.prototipo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prototipo.ui.configuracion.configuracionFragment;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class act_cambiar_pass extends AppCompatActivity {
    private EditText pass_new, pass_new_rep;
    private Button btnActualizar, btnCancelar;
    private TextView txtViewAlerta;
    Intent intent;
    Context context;

    private SharedPreferences sharedPreferences;
    private LinearLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cambiar_pass);

        this.layout1 = findViewById(R.id.layout1);


        this.pass_new = (EditText) findViewById(R.id.editTextCambiarpass1);
        this.pass_new_rep = (EditText) findViewById(R.id.edit_Text_Cambiar_pass_rep);
        this.txtViewAlerta = (TextView) findViewById(R.id.textView_Info);
        this.txtViewAlerta.setVisibility(View.GONE);
        this.btnActualizar = (Button) findViewById(R.id.btn_Actualizar1);
        this.btnCancelar = (Button) findViewById(R.id.btn_cancelar1);

        sharedPreferences = this.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        final String idusuario = sharedPreferences.getString("idUsuario", "");

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_new.getText().toString().equals(pass_new_rep.getText().toString())) {
                    SharedPreferences sharedPreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue
                    SharedPreferences.Editor edt= sharedPreferences.edit();//TODO: NUEVO
                    edt.putString("tipo-alta","cambiar-pass-menu");
                    edt.apply();
                    edt.commit();
                    RequestParams params = new RequestParams();
                    params.add("password", pass_new.getText().toString());
                    params.add("idUsuario", idusuario);
                    params.add("type", "generar-pass");
                    params.add("pedir_rec","0");
                    clsWebRequest.post(context, "denuncia.php", params, new act_cambiar_pass.ResponseHandler());
                }else{
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
                    Log.d("cambiar pass11:: ", "Exito ");
                    // error no se cambio la contraseña

                    Log.v("error-cambiar pass:: ", "error.. "+ response.getString("type").toString() );

                    Snackbar.make(layout1,  response.getString("message"), Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        //Navigation.findNavController(view).navigate(R.id.action_nav_configuracion_to_nav_home);
                    finish();

                    //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("cambiar pass::22", "Exito ");

                        Snackbar.make(layout1,  "Contraseña actualizada con exito.", Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, "contraseña actualizada con exito", Toast.LENGTH_SHORT).show();
                        finish();
                }
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