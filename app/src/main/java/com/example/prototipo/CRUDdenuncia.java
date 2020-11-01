package com.example.prototipo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CRUDdenuncia {

    private clsMySQLiteOpenHelper objHelper;
    private Context objContext;
    private SQLiteDatabase objDatabase;

    public CRUDdenuncia(Context context){
        this.objContext = context;
    }
    public CRUDdenuncia open() throws SQLException {
        this.objHelper = new clsMySQLiteOpenHelper(objContext);
        this.objDatabase = objHelper.getWritableDatabase();
        return this;
    }

    public void insertDenuncia(String texto, String foto, String latitud, String longitud, String idusuario, String patente){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ContentValues myRecord = new ContentValues();
        myRecord.put(this.objHelper.ESTADO, "iniciado");
        myRecord.put(this.objHelper.TEXTO, texto);
        myRecord.put(this.objHelper.FOTO, foto);
        myRecord.put(this.objHelper.LATITUD, latitud);
        myRecord.put(this.objHelper.LONGITUD, longitud);
        myRecord.put(this.objHelper.FECHA, dateFormat.format(new Date()));
        myRecord.put(this.objHelper.ID_USUARIO, idusuario);
        myRecord.put(this.objHelper.PATENTE, patente);//TODO:NUEVO AGREGUE

        this.objDatabase.insert(this.objHelper.TABLE_DENUNCIA, null, myRecord);
    }

    public void close(){
        this.objHelper.close();
    }


    public Cursor getDenuncia(){
        String query = "SELECT * FROM denuncia";

        Cursor myCursor = this.objDatabase.rawQuery(query , null);

        //el cursor va al comienzo
        if (myCursor!=null){
            myCursor.moveToFirst();
        }
        return myCursor;
    }
    public Cursor getDenunciaXid(String id_usuario){
        Cursor myCursor = this.objDatabase.rawQuery("SELECT * FROM denuncia WHERE idusuario = '"+id_usuario+"'", null);
        if(myCursor != null){
            myCursor.moveToFirst();
        }
        return myCursor;


    }

    public Cursor getDenunciaXidDenuncia(int id_denuncia){
        Cursor myCursor = this.objDatabase.rawQuery("SELECT * FROM denuncia WHERE id_denuncia = '"+id_denuncia+"'", null);
        if(myCursor != null){
            myCursor.moveToFirst();
        }
        return myCursor;
    }
    public int getNbrOfRecords(){
        return (int) DatabaseUtils.queryNumEntries(this.objDatabase, this.objHelper.TABLE_DENUNCIA);
    }

}
