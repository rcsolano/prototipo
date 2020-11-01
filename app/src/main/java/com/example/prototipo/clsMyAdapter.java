package com.example.prototipo;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class clsMyAdapter extends RecyclerView.Adapter<clsMyAdapter.MyViewHolder> implements View.OnClickListener{

    private ArrayList<clsDenuncia> lista;
    private View.OnClickListener listener;
    private Context context;


    public clsMyAdapter(Context context, ArrayList<clsDenuncia> lista) {
        this.lista = lista;
        this.context = context;
    }


    @Override
    public clsMyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_denuncia, parent, false);
        v.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String estado = lista.get(position).getEstado();
        String fecha = lista.get(position).getFecha();
        String texto = lista.get(position).getTexto();
        String foto = lista.get(position).getFoto();
        String patente = lista.get(position).getPatente();

        //final int i = holder.ImagenViewfoto.getResources().getIdentifier(foto, null, "com.sistema.prototipo");
        //holder.ImagenViewfoto.setImageResource(i);


        //imagen guardada en el celular
        //Bitmap bm = BitmapFactory.decodeFile(Uri.decode(foto));
        //holder.ImagenViewfoto.setImageBitmap(bm);
        //-----
        //imagen guardada en la web

        //SharedPreferences sharedPreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue
        //String ip = sharedPreferences.getString("ipLocal","");
        //String idUsuario = sharedPreferences.getString("idUsuario","");
        Picasso.with(context).load(clsGlobal.getInstance().BASE_URL+"imagenes/"+foto+".png").into(holder.ImagenViewfoto);

        //TODO: Cambie esto
        /*holder.txtDenuncia.setText(holder.txtDenuncia.getText().toString() + texto);
        holder.txtVFecha.setText(holder.txtVFecha.getText().toString() + fecha);
        holder.txtVEstado.setText(holder.txtVEstado.getText() + estado);
        holder.txtPatente.setText(holder.txtPatente.getText().toString() + patente);//TODO: NUEVO*/

        //TODO: por este codigo
        holder.txtDenuncia.setText(texto);
        holder.txtVFecha.setText(fecha);
        holder.txtVEstado.setText(estado);
        holder.txtPatente.setText(patente);//TODO: NUEVO
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtVEstado;
        private TextView txtVFecha;
        private TextView txtDenuncia;
        private TextView txtPatente; //TODO: NUEVO

        private ImageView ImagenViewfoto;
        private View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtVEstado = (TextView) itemView.findViewById(R.id.textViewEstado);
            txtVFecha = (TextView) itemView.findViewById(R.id.textViewFecha);
            txtDenuncia = (TextView) itemView.findViewById(R.id.textViewDenuncia);
            txtPatente = (TextView) itemView.findViewById(R.id.textViewDominio);
            ImagenViewfoto = (ImageView) itemView.findViewById(R.id.ImageViewFoto);

            //TODO: NUEVO
            /*SharedPreferences sharedPreferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE);//TODO:agregue
            final String tipo = sharedPreferences.getString("tipo","");
            if(tipo.equals("mis_infracciones")){
                Log.v("mis_infracciones", tipo);
                txtVEstado.setVisibility(View.GONE);
            }else{
                Log.v("mis_infracciones::", tipo);
                txtVEstado.setVisibility(View.VISIBLE);
            }*/
        }
    }
    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(this.listener!=null){
            this.listener.onClick(v);
        }
    }


}
