package com.example.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class act_msj_envio_email extends AppCompatActivity {

    private TextView informacion;
    private Button btnContinuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_msj_envio_email);

        this.informacion = (TextView) findViewById(R.id.txtVinfo);
        Bundle bundle= getIntent().getExtras();
        String msj = bundle.getString("mensaje");
        this.informacion.setText(msj);

        this.btnContinuar = (Button) findViewById(R.id.btnContinuar);
        this.btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(act_msj_envio_email.this, actLogin.class);
                startActivity(intent);*/
                finish();
            }
        });
    }

    //TODO: HACER QUE EL BOTON BACK VUELVA AL LOGIN!!!
}