package com.example.prototipo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.prototipo.ui.configuracion.configuracionFragment;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class actLogin extends AppCompatActivity {
    Context context;
    Intent intent;
    SharedPreferences sharedPreferences;
    String SHARED_PREF_NAME = "credenciales";
    SharedPreferences.Editor sharedPrefEditor;

    private NavController navController;
    private EditText etEmail, etPassword;
    //private InputType etEmail, etPassword;
    private Button btnLogin, btnExit, btnCrear;

    private clsMySQLiteOpenHelper db;
    //private clsCRUDUsuarios objCRUDUsuarios;

    private TextView txtVnuevaCuenta, txtVolvidePass;;
    private String user, pass;
    private RelativeLayout layout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_act_login);
        /*Usuario           Contraseña
          admin               admin
          contribuyente1     123456
          contribuyente2     123456
        */
        layout = findViewById(R.id.relativeLayout);
        init();
        getViews();
    }

    protected void init() {
        context = this;
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putBoolean("login", false);
        //sharedPrefEditor.putString("ipLocal", clsGlobal.getInstance().BASE_URL);
    }

    private void getViews(){
        this.etEmail = (EditText) findViewById(R.id.etEmail);
        this.etPassword = (EditText) findViewById(R.id.etPassword);

        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = etEmail.getText().toString();
                pass = etPassword.getText().toString();

                //TODO: falta agregar validacion de datos, ej q no este vacio user pass,etc..

                //TODO: cargo los datos ya validados para hacer la consulta en la api
                RequestParams params = new RequestParams();
                params.add("type", "login");
                params.add("user", user);
                params.add("password",pass);

                clsWebRequest.post(context, "denuncia.php", params, new actLogin.ResponseHandler());
            }
        });



        //TODO: falta modificar
        this.txtVnuevaCuenta = (TextView) findViewById(R.id.textViewNvaCuenta);
        SpannableString spannableString = new SpannableString("No tengo cuenta. Quiero una.");
        ForegroundColorSpan color = new ForegroundColorSpan(getResources().getColor(R.color.colorTxt));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(actLogin.this, act_crear_usuario.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan,0,28, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(color, 0, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtVnuevaCuenta.setText(spannableString);
        txtVnuevaCuenta.setMovementMethod(LinkMovementMethod.getInstance());

        this.txtVolvidePass =  (TextView) findViewById(R.id.textViewOlvidoPass);
        SpannableString spannableString2 = new SpannableString("Olvidé mi contraseña.");
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(actLogin.this, act_olvide_password.class);
                startActivity(intent);
                //finish();
            }
        };

        spannableString2.setSpan(clickableSpan2,0,21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString2.setSpan(color, 0, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtVolvidePass.setText(spannableString2);
        txtVolvidePass.setMovementMethod(LinkMovementMethod.getInstance());
    }





    //consulto en la api para el login
    private class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("Denuncia::1 ", "Exito " + statusCode);
            try {
                if (response.getBoolean("error")) {
                    // error de login
                    Snackbar.make(layout, response.getString("message"), Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    Log.d("Denuncia::2 ", "Fallo en los datos del login " + statusCode);
                } else {
                    // login exito
                    JSONObject user = response.getJSONObject("user");
                    //grabo valores para mantener en la sesion

                    sharedPrefEditor.putString("idUsuario", user.getString("id_usuario"));
                    sharedPrefEditor.putString("nombre", user.getString("nombre"));
                    sharedPrefEditor.putString("apellido", user.getString("apellido"));
                    sharedPrefEditor.putString("password", user.getString("password"));
                    sharedPrefEditor.putString("email", user.getString("email"));
                    sharedPrefEditor.putString("fecha_alta", user.getString("fecha_alta"));
                    sharedPrefEditor.putString("fecha_alta_rec", user.getString("fecha_alta_rec"));
                    sharedPrefEditor.putString("fecha_ultimo_acceso", user.getString("fecha_ultimo_acceso"));

                    //sharedPrefEditor.putString("tipo-alta", "cambiar-pass-menu");
                    //sharedPrefEditor.apply();
                    //sharedPrefEditor.commit();

                    try {
                        //det el formato de fecha para obtener un objeto de tipo Date el cual es el que se utiliza para obtener la diferencia.
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String fecha_alta1 = user.getString("fecha_alta");
                        String fecha_alta_rec1=user.getString("fecha_alta_rec");
                        String fecha_ultimo_acceso1=user.getString("fecha_ultimo_acceso");
                        Integer pedir_rec =user.getInt("pedir_rec");

                        String fecha_actual = dateFormat.format(new Date());
                        Date fechaActual = dateFormat.parse(fecha_actual);

                        Log.d("tag1:" ,  "primero");



                        if(fecha_ultimo_acceso1.equals("null")){//TODO: primera vez que ingresa
                            sharedPrefEditor.putString("tipo-alta", "alta-primero");
                            sharedPrefEditor.apply();
                            sharedPrefEditor.commit();
                            intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }else{
                            if (pedir_rec.equals(0)){
                                sharedPrefEditor.putString("tipo-alta", "cambiar-pass-menu");
                                sharedPrefEditor.apply();
                                sharedPrefEditor.commit();
                                if (sharedPreferences.getBoolean("login",false)==false){//todo: primera vez al entrar al home con la contraseña ya cambiada
                                    String nombreCompleto = user.getString("apellido") + " " + user.getString("nombre");
                                    //Toast.makeText(context, "Bienvenido " + nombreCompleto, Toast.LENGTH_SHORT).show();
                                    //Snackbar.make(layout,"Bienvenido " + nombreCompleto , Snackbar.LENGTH_SHORT).show();
                                    sharedPrefEditor.putBoolean("login", true);
                                    sharedPrefEditor.apply();
                                    sharedPrefEditor.commit();
                                }
                                intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Date fecha_alta_rec = dateFormat.parse(fecha_alta_rec1);
                                Date fecha_ultimo_acceso = dateFormat.parse(fecha_ultimo_acceso1);
                                if(fecha_ultimo_acceso.compareTo(fecha_alta_rec)>0){
                                    sharedPrefEditor.putString("tipo-alta","cambiar-pass-menu");
                                    sharedPrefEditor.apply();
                                    sharedPrefEditor.commit();
                                    intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    long difference = Math.abs(fechaActual.getTime() - fecha_alta_rec.getTime());
                                    difference= difference / (60 * 60 * 1000);
                                    Log.v("TAGentro", "aqui");
                                    if(difference < 24){
                                        sharedPrefEditor.putString("tipo-alta", "alta-recupero-pass");
                                        sharedPrefEditor.apply();
                                        sharedPrefEditor.commit();
                                        intent = new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Snackbar.make(layout,"Debe volver a solicitar clave. Pasaron más de 24 hs desde la recuperación.", Snackbar.LENGTH_SHORT).show();
                                        //Toast.makeText(context, "Debe volver a solicitar clave. Pasaron más de 24 hs desde la recuperación.", Toast.LENGTH_LONG).show();
                                    }

                                }

                            }

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sharedPrefEditor.apply();
                    sharedPrefEditor.commit();

                    //--------------------------------------

                    etEmail.setText("");
                    etPassword.setText("");

                    if (findViewById(R.id.etPassword).isFocusable()) {
                        findViewById(R.id.etEmail).requestFocus();
                    }

                    //------------------------------------


                    //TODO:vamos al home
                    /*Log.d("Denuncia::2 ", "Yendo al home");
                    if (sharedPreferences.getBoolean("login",false)==false){//todo: primera vez al entrar al home con la contraseña ya cambiada
                        String nombreCompleto = user.getString("apellido") + " " + user.getString("nombre");
                        Toast.makeText(context, "Bienvenido " + nombreCompleto, Toast.LENGTH_SHORT).show();
                        sharedPrefEditor.putBoolean("login", true);
                    }
                    intent = new Intent(context, MainActivity.class);
                    startActivity(intent);*/
                    //finish();
                    //}

                }
            } catch (JSONException e ) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("DENUNCIA::2", "Error " + statusCode);
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }



}