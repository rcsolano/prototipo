package com.example.prototipo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;


import com.example.prototipo.ui.hacerDenuncia.hacer_denunciaFragment;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class act_crear_usuario extends AppCompatActivity {
    private EditText nombre, apellido, email, dni, telefono, pass;
    private Button btnCrear, btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_crear_usuario);


        this.nombre = (EditText) findViewById(R.id.etNombre);
        this.apellido = (EditText) findViewById(R.id.etApellido);
        this.email = (EditText) findViewById(R.id.etEmail);
        this.dni = (EditText) findViewById(R.id.etDni);
        this.telefono = (EditText) findViewById(R.id.etTelefono);

        //this.pass = (EditText) findViewById(R.id.etPASS);

        this.btnCrear = (Button) findViewById(R.id.btnAgregar);

        this.btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nomb =  nombre.getText().toString();
                final String ap =  apellido.getText().toString();
                final String em =  email.getText().toString();
                final String DNI =  dni.getText().toString();
                final String tel =  telefono.getText().toString();

                //final String passs =  pass.getText().toString();




               /* objCRUDUsuario = new clsCRUDUsuarios(act_crear_usuario.this);
                objCRUDUsuario.open();
                objHelper = new clsMySQLiteOpenHelper(act_crear_usuario.this);*/

                AlertDialog.Builder alerta = new AlertDialog.Builder(act_crear_usuario.this);
                alerta.setMessage("Por favor Verifique los datos cargados. " +
                        "Desea crear la cuenta?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //objCRUDUsuario.insert(em,cont,ap,nomb,tel);
                        RequestParams params = new RequestParams();
                        params.add("type", "login-registro");
                        params.add("nombre", nomb);
                        params.add("apellido", ap);
                        params.add("email", em);
                        params.add("dni", DNI);
                        params.add("telefono", tel);

                        //params.add("pass", passs);
                        clsWebRequest.post(act_crear_usuario.this, "denuncia.php", params, new act_crear_usuario.ResponseHandler());

                        //Toast info = Toast.makeText(act_crear_usuario.this,"Usuario Creado con Exito", Toast.LENGTH_LONG);
                        //info.show();

                        /*Intent intent = new Intent(act_crear_usuario.this, act_msj_envio_email.class);
                        intent.putExtra("email", em);
                        startActivity(intent);
                        finish();*/
                    }
                });

                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog confirmacion =alerta.create();
                confirmacion.setTitle("Atencion");
                confirmacion.show();
            }
        });

        this.btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act_crear_usuario.this, actLogin.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private class ResponseHandler extends JsonHttpResponseHandler {
        final ProgressDialog cargando = ProgressDialog.show(act_crear_usuario.this,"Verificando...", "Espere por favor");
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            super.onSuccess(statusCode, headers, response);
            Log.d("registro-login:: ", "Exito " + statusCode);
            try {
                //mensaje = response.getString("message");
                if (response.getBoolean("error")) {
                    cargando.dismiss();
                    String msj = response.getString("message");
                    Intent intent = new Intent(act_crear_usuario.this, act_msj_envio_email.class);
                    intent.putExtra("mensaje", msj);
                    startActivity(intent);
                    finish();
                    // error no se inserto la denuncia
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();//Todo: ver la forma de mostrar mensaje da error
                } else {
                    cargando.dismiss();
                    String em =  email.getText().toString();
                    String msj = "En los próximos minutos recibirás la clave por email del usuario: " + em;
                    Intent intent = new Intent(act_crear_usuario.this, act_msj_envio_email.class);
                    intent.putExtra("mensaje", msj);
                    startActivity(intent);
                    finish();
                    // Exito al registrar usuario
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    //returnMain();;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("REGISTRO-LOGIN::", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
