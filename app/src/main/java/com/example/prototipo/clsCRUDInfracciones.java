package com.example.prototipo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class clsCRUDInfracciones {
    private clsMySQLiteOpenHelper objHelper;
    private Context objContext;
    private SQLiteDatabase objDatabase;

    public clsCRUDInfracciones(Context context) {
        this.objContext = context;
    }

    public clsCRUDInfracciones open() throws SQLException {
        this.objHelper = new clsMySQLiteOpenHelper(objContext);
        this.objDatabase = objHelper.getWritableDatabase();
        return this;
    }

    //obtengo el id de usuario a partit de la pantente.
    public String devuelve_idusuario(String dominio){
        String query = "SELECT id_usuario FROM Dominios WHERE patente = '"+dominio+"'";
        String id_usuario = "null";
        Cursor myCursor = null;
        myCursor = this.objDatabase.rawQuery(query, null);
        if(myCursor != null){
            myCursor.moveToFirst();
            id_usuario = myCursor.getString(0);
        }
        return id_usuario;
    }
    //pasar a crud usuarios
    public Cursor obtengo_usuario(String id_usuario){
        Cursor myCursor = null;

        if(!id_usuario.equals("null")) {
            String query = "SELECT * FROM Usuarios WHERE id_usuario = '" + id_usuario + "'";
            myCursor = this.objDatabase.rawQuery(query,null);
            if(myCursor != null) {
                myCursor.moveToFirst();
            }
        }
        return myCursor;
    }

    //filtro infracciones de un id_usuario
    public Cursor buscar_infraccionesXidusuario(int idusuario){
        /*Cursor myCursor = this.objDatabase.rawQuery("SELECT * FROM Dominios " +
                "INNER JOIN Usuarios " +
                "ON Dominios.id_usuario = '"+idusuario+"'" +
                "INNER JOIN denuncia " +
                "ON denuncia.patente = Dominios.patente"
                , null);*/
        String query = "SELECT * FROM Dominios INNER JOIN Usuarios ON Dominios.id_usuario = Usuarios.id_usuario INNER JOIN denuncia ON denuncia.patente = Dominios.patente WHERE Usuarios.id_usuario = '"+idusuario+"'";
        Cursor myCursor = this.objDatabase.rawQuery(query, null);
        if(myCursor != null){
            myCursor.moveToFirst();
        }else{
            myCursor = null;
        }
        return myCursor;
    }
    public int getNbrOfRecords(){
        return (int) DatabaseUtils.queryNumEntries(this.objDatabase, this.objHelper.TABLE_DOMINIO);
    }
}
