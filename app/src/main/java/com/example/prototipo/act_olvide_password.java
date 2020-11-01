package com.example.prototipo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import cz.msebera.android.httpclient.Header;

public class act_olvide_password extends AppCompatActivity {

    private EditText emailRecuperacion;
    private Button btnRecuperar, btnCancelarRecu;
    Context context;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_olvide_password);
        context = this;
        this.emailRecuperacion = (EditText) findViewById(R.id.etEmailRecupero);
        this.btnRecuperar = (Button) findViewById(R.id.btnRecuperarPass);
        this.btnCancelarRecu = (Button) findViewById(R.id.btnCancelarRecu);

        this.btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRecuperacion.getText().toString();
                Log.v("emailTAG", email);
                RequestParams params = new RequestParams();
                params.add("type", "recuperar-pass");
                params.add("email", email);
                clsWebRequest.post(context, "denuncia.php", params, new act_olvide_password.ResponseHandler());
            }
        });


        this.btnCancelarRecu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(context, actLogin.class);
                startActivity(intent);*/
                finish();
            }
        });

    }
    private class ResponseHandler extends JsonHttpResponseHandler {
        final ProgressDialog cargando = ProgressDialog.show(act_olvide_password.this,"Enviando...", "Espere por favor");
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("act_olvide_password", "Exito " + statusCode);
            try {
                String mensaje = "";
                if (response.getBoolean("error")) {
                    cargando.dismiss();
                    //caso en que el email no esta registrado
                    //Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("act_olvide_password--", "error1 " + statusCode);

                    mensaje = response.getString("message");
                    intent = new Intent(context, act_msj_envio_email.class);
                    intent.putExtra("mensaje",mensaje);
                    startActivity(intent);
                    finish();
                } else {
                    cargando.dismiss();
                    Log.d("act_olvide_password-", "Exito1 " + statusCode);
                    mensaje = response.getString("message");
                    intent = new Intent(context, act_msj_envio_email.class);
                    intent.putExtra("mensaje",mensaje);
                    startActivity(intent);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("act_olvide_password", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }


        @Override
        public void onFinish() {
            super.onFinish();
        }


    }


}