package com.example.prototipo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.prototipo.ui.configuracion.configuracionFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{//implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    private NavController navController;
    SharedPreferences.Editor sharedPrefEditor;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SharedPreferences sharedPreferences;
    private CoordinatorLayout coordinatorLayout1;
    //ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        coordinatorLayout1 = findViewById(R.id.coordinatorlaout1);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setItemIconTintList(null); //todo: propiedad para que se muestre el icono en el menu navigation

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_hacer_denuncia, R.id.nav_mis_denuncias, R.id.nav_mi_ubicacion, R.id.fragment_unaDenuncia, R.id.nav_mis_infracciones, R.id.nav_configuracion)
                .setDrawerLayout(drawer)
                .build();



        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);//se encarga de conectar los item del menu con los

        // destino de navegacion que configure en en el grafico de navegacion
        //navigationView.setNavigationItemSelectedListener(this);//establecer evento onclick al navigationView todo
        sharedPreferences = this.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String nombre = sharedPreferences.getString("nombre", "");
        Snackbar.make(coordinatorLayout1,"Bienvenido " + nombre , Snackbar.LENGTH_SHORT).show();

        dirigirse();
        permisoAcceso();

    }

    @SuppressLint("LongLogTag")
    private void dirigirse(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String tipoAlta = sharedPreferences.getString("tipo-alta", "");

        if(tipoAlta.equals("alta-primero") || tipoAlta.equals("alta-recupero-pass")){
            /*fragmentManager = MainActivity.this.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment, new configuracionFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            Intent intent =new Intent(this , act_cambiar_pass.class);
            startActivity(intent);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
        //return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawer); //todo: ver si va esto o lo de arriba
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //TODO: NUEVO
    //PARA CAMBIAR EL TITULO AL ACTIONBAR(LO USO AL REUTILIZAR FRAGMENT_UNAdENUNCIA)
    public void setActionBarTitle(String titulo){
        getSupportActionBar().setTitle(titulo);
    }


    //TODO: permisos camara, escritura, localizacion------------------------
    public void permisoAcceso(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNecessary(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new
                            String[0]),
                    1);
        }
    }

    //---------------FIN DE LOS PERMISOS-----------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Intent intent = new Intent(this, actLogin.class);
                Toast mensaje = Toast.makeText(this,"Hasta Luego...", Toast.LENGTH_LONG);
                mensaje.show();
                //startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
