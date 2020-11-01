package com.example.prototipo.ui.hacerDenuncia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipo.CRUDdenuncia;
import com.example.prototipo.MainActivity;
import com.example.prototipo.R;
import com.example.prototipo.actLogin;
import com.example.prototipo.clsCRUDInfracciones;
import com.example.prototipo.clsGlobal;
import com.example.prototipo.clsMySQLiteOpenHelper;
import com.example.prototipo.clsWebRequest;
import com.example.prototipo.ui.miUbicacion.UbicacionFragment;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cz.msebera.android.httpclient.Header;

public class hacer_denunciaFragment extends Fragment {

    private Spinner spinner_denuncia;
    private EditText etDenuncia, etPatente;
    private Button btnEnviar, btnCancelar;
    private String mensaje;

    //private CRUDdenuncia objCRUDdenuncia;
    //private clsMySQLiteOpenHelper objHelper;
    //private clsMySQLiteOpenHelperDenuncia objHelper;

    //private clsCRUDInfracciones objCRUDinfracciones;//TODO: PROBANDO
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;


    //para la foto
    private File photoFile;
    private Uri photoURI;
    private ImageView cam;
    private Button btnCam;
    private static final String CARPETA_PRINCIPAL = "MisFotoDenuncia";//directorio principal
    private static final String CARPETA_IMAGEN = "Imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap = null;
    String dirFoto;
    //--------------



    //--------------
    private String subir_imagen_url;
    private String id_nombre_img;
    //

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private hacer_denunciaViewModel hacerdenunciaViewModel;

    private NavController navController;


    Session session = null;
    ProgressDialog pdialog = null;

    SharedPreferences sharedPreferences;
    private String tipo1="";//obtengo el la opcion del spinner que el ususario selecciono
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hacerdenunciaViewModel =
                ViewModelProviders.of(this).get(hacer_denunciaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hacer_denuncia, container, false);


        //Permisos
        //permisoAccesoCamara();
        //permisoAccesoUbicacion();
        //-----------------------

        return root;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flUbicacion, new UbicacionFragment());
        fragmentTransaction.commit();



        spinner_denuncia= (Spinner)view.findViewById(R.id.spinner);

        cargar_Spinner();
        spinner_denuncia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String pos= Integer.toString(position);
                    Log.v("posicion::::" , pos );
                    //clsTipoDenuncia tipoDenuncia=(clsTipoDenuncia)((Spinner) view.findViewById(R.id.spinner)).getSelectedItem();
                    tipo1 = parent.getItemAtPosition(position).toString();
                   //Toast.makeText(parent.getContext(), "Seleccionado: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        etPatente = (EditText)  view.findViewById(R.id.etPatente);
        btnEnviar = (Button) view.findViewById(R.id.btnEnviar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);

        permisoAcceso();
        sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue

        //ip del server local
        subir_imagen_url = clsGlobal.getInstance().BASE_URL+"subir-imagen.php";


        id_nombre_img = sharedPreferences.getString("idUsuario","");

        cam = (ImageView) view.findViewById(R.id.imageView_foto);
        btnCam = (Button) view.findViewById(R.id.btn_cam);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto(v);
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.v("envio1:", tipo1);
                final String descDenuncia =  tipo1;
                final String descPatente =  etPatente.getText().toString();
                //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                Toast.makeText(getContext(), sharedPreferences.getString("nombre",""), Toast.LENGTH_SHORT).show();
                //id_nombre_img = sharedPreferences.getString("idUsuario","");

                //posicion
                //String latitud = "-24.831371";
                //String longitud = "-65.429342";
                final String latitud = sharedPreferences.getString("latitud","");
                final String longitud = sharedPreferences.getString("longitud","");
                final String idusuario = sharedPreferences.getString("idUsuario", "");//TODO:agregue

                //double lat = Double.valueOf(latitud);
                //double longi = Double.valueOf(longitud);
                //Toast.makeText(getActivity(), "Latitud= " + lat + "longitud = " + longi, Toast.LENGTH_SHORT).show();
                //final String foto = dirFoto;

                final String foto = photoFile.toString();

                final String fotoweb = id_nombre_img;





                //todo: ver condicion de que si o si haya tomado una foto(ver si es obligatorio escribir la denuncia y si es obligatorio la geolocalizacion)

                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                alerta.setMessage("Por favor Verifique los datos cargados. " +
                        "Enviar Denuncia?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ProgressDialog progressDialog = new ProgressDialog(getContext());
                        //progressDialog.setMessage("Espere por favor");
                        //progressDialog.setTitle("Subiendo Denuncia...");
                        //TODO: carga de datos para hacer la consulta
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Todo: Cambie la forma de escribir la fecha porq sino da error en mysql
                        String fecha = dateFormat.format(new Date());
                        RequestParams params = new RequestParams();
                        params.add("type","insertDenuncia");
                        params.add("estado","iniciado");
                        params.add("texto", descDenuncia);
                        params.add("foto", foto);
                        params.add("latitud", latitud);
                        params.add("longitud", longitud);
                        params.add("fecha", fecha);
                        params.add("idusuario", idusuario);
                        params.add("patente", descPatente);
                        params.add("fotoweb",fotoweb);



                        clsWebRequest.post(getContext(), "denuncia.php", params, new hacer_denunciaFragment.ResponseHandler());
                        subirImagen(v);//TODO:va al servidor

                        //Toast info = Toast.makeText(getContext(),mensaje, Toast.LENGTH_LONG);
                        //info.show();

                        //progressDialog.dismiss();
                        //Navigation.findNavController(v).navigate(R.id.action_nav_hacer_denuncia_to_nav_home);
                        //navController.popBackStack(R.id.nav_home, false);
                        //returnMain();
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


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                returnMain();
            }
        });

    }

    private void cargar_Spinner(){
        RequestParams params = new RequestParams();
        params.add("type","lista-spinner");
        clsWebRequest.post(getContext(), "denuncia.php", params, new hacer_denunciaFragment.ResponseHandler1());

    }

    private class ResponseHandler1 extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("Denuncia::3 ", "Exito " + statusCode);
            try {

                if (response.getBoolean("error")) {
                    // error no se inserto la denuncia
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();//Todo: ver la forma de mostrar mensaje da error
                } else {
                    clsTipoDenuncia objTipo = null;
                    //ArrayList<clsTipoDenuncia> arrayTipo;
                    ArrayList<String> arrayTipo;


                    arrayTipo = new ArrayList<>();
                    JSONArray jArray = response.optJSONArray("lista");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = null;
                        json_data = jArray.getJSONObject(i);
                        String id_tipo = json_data.optString("id_tipo");
                        String descripcion = json_data.optString("descripcion");
                        objTipo = new clsTipoDenuncia(id_tipo, descripcion);
                        arrayTipo.add(objTipo.getDescripcion());

                    }

                    ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(getContext(), R.layout.style_spinner, arrayTipo);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    spinner_denuncia.setAdapter(arrayAdapter);
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
                    // error no se inserto la denuncia
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();//Todo: ver la forma de mostrar mensaje da error
                } else {
                    // Exito al cargar la denuncia
                    //Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    //returnMain();
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

//------------------------------


    //Metodo para crear un nombre unico a cada Fotografia
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException{
        //Creacion nombre de la imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_"+ timeStamp + "_";

        id_nombre_img = id_nombre_img + timeStamp;//TODO: En id_nombre_img guardo el nombre que utilizo para subir la imagen al servidor

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Metodo para tomar foto y crear el archivo
    static final int REQUEST_TAKE_PHOTO =1;
    public void tomarFoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null){
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){
                //Error ocurrido mientras se creo el archivo
            }

            //Continua solo si el archivo es creado con exito
            if (photoFile != null){
                //String authorities = getContext().getPackageName()+".provider";
                photoURI = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    //subir imagen
    public void subirImagen(View v){
        final View vista = v;
        final ProgressDialog cargando = ProgressDialog.show(getContext(),"Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, subir_imagen_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargando.dismiss();//TODO:en caso de exito al subir la foto al servidor cierro el progressDialog y vuelvo al home
                //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                //Snack
                Snackbar.make(getView(),"Denuncia realizada con éxito.", Snackbar.LENGTH_SHORT).show();

                Navigation.findNavController(vista).navigate(R.id.action_nav_hacer_denuncia_to_nav_home);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismiss();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{

                String imagen = getStringImagen(bitmap);

                String nombre = id_nombre_img;//
                Map<String, String> params = new Hashtable<String, String>();
                params.put("foto",imagen);
                params.put("nombre", nombre);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //convierte la imagen en un string
    private String getStringImagen(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
        byte[] imagenBytes = byteArrayOutputStream.toByteArray();
        String encodedImagen = Base64.encodeToString(imagenBytes, Base64.DEFAULT);
        return encodedImagen;
    }


    //Metodo para mostrar vista previa en un imageview de la foto tomada
    static  final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                cam.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }else{
            photoFile.delete();
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    /*public void tomarFoto() {
        //String nombreImagen = "";
        //File fileImagen = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        //boolean isCreada = fileImagen.exists();

        //if(isCreada == false) {
        //    isCreada = fileImagen.mkdirs();
        //}

        //if(isCreada == true) {
        //    nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        //}

        path = Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombreImagen;
        File imagen = new File(path);

        dirFoto = imagen.getAbsolutePath();

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getContext().getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }

        startActivityForResult(intent, 0);
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {

            }
        });


        bitmap = BitmapFactory.decodeFile(path);
        //bitmap= rotacionImagen(bitmap,angulo(path));

        cam.setImageBitmap(bitmap);
        //bitmap.recycle();

    }*/


    //pendiente rotacion de imagen
    /*private final static int angulo(String path) {
        int angRot = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angRot = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angRot = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angRot = 270;
                break;
        }
        return angRot;
    }

    private Bitmap rotacionImagen(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap imgFinal = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return imgFinal;
    }*/


    private void returnMain() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }

    //TODO: permisos camara, escritura, localizacion------------------------
    public void permisoAcceso(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new
                            String[0]),
                    1);
        }
    }

    //---------------FIN DE LOS PERMISOS-----------------------------------
}
