package com.example.prototipo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class clsMySQLiteOpenHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "denunciaDB";
    static final int DB_VERSION = 1;

    public static final String TABLE_USUARIO = "Usuarios";
    public static final String TABLE_IDUSUARIO = "id_usuario";
    public static final String FIELD_USUARIO = "usuario";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_APELLIDO = "apellido";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_TELEFONO = "telefono";
    public static final String FIELD_FCHALTA = "fecha_alta";



    private static final String CREATE_TABLE_USUARIO = "create table "
            + TABLE_USUARIO + "(" + TABLE_IDUSUARIO
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_USUARIO + " TEXT NOT NULL, "
            + FIELD_PASSWORD + " TEXT NOT NULL, "
            + FIELD_APELLIDO + " TEXT, "
            + FIELD_NOMBRE + " TEXT, "
            + FIELD_TELEFONO + " TEXT, "
            + FIELD_FCHALTA + " TEXT "
            + ");";


    public static final String TABLE_DENUNCIA = "denuncia";
    public static final String TABLE_IDDENUNCIA = "id_denuncia";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String ESTADO = "estado";
    public static final String TEXTO = "texto";
    public static final String FECHA = "fechaYhora";
    public static final String FOTO = "foto";
    public static final String ID_USUARIO = "idusuario";
    public static final String PATENTE = "patente";//TODO:agregue

    private static final String CREATE_TABLE_DENUNCIA = "create table "
            + TABLE_DENUNCIA + "("
            + TABLE_IDDENUNCIA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LATITUD + " TEXT NOT NULL, "
            + LONGITUD + " TEXT NOT NULL, "
            + ESTADO + " TEXT NOT NULL, "
            + TEXTO + " TEXT NOT NULL, "
            + FECHA + " TEXT NOT NULL, "
            + FOTO + " TEXT NOT NULL, "
            + ID_USUARIO + " TEXT NOT NULL, "
            + PATENTE + " TEXT NOT NULL "//TODO: NUEVO
            + ");";



    public static final String TABLE_ESTADO = "Estados";
    public static final String TABLE_IDESTADO = "id_estado";
    public static final String FIELD_DESCRIPCION = "descripcion_estado";

    private static final String CREATE_TABLE_ESTADO = "create table "
            + TABLE_ESTADO + "(" + TABLE_IDESTADO
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_DESCRIPCION + " TEXT NOT NULL "
            + ");";


    public static final String TABLE_DOMINIO = "Dominios";
    public static final String TABLE_IDDOMINIO = "id_dominio";
    public static final String FIELD_IDUSUARIO = "id_usuario";
    public static final String FIELD_PATENTE = "patente";

    private static final String CREATE_TABLE_DOMINIO = "create table "
            + TABLE_DOMINIO + "(" + TABLE_IDDOMINIO
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_IDUSUARIO + " TEXT NOT NULL, "
            + FIELD_PATENTE + " TEXT NOT NULL "
            + ");";


    public clsMySQLiteOpenHelper(Context context){super(context, DB_NAME, null,DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String fecha = dateFormat.format(new Date());

        {db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL("INSERT INTO Usuarios (usuario, password, apellido, nombre, telefono)" +
                "VALUES ('admin','admin','Usuario','Contribuyente', '3874428363')");
        db.execSQL("INSERT INTO Usuarios (usuario, password, apellido, nombre, telefono)" +
                    "VALUES ('contribuyente1','123456','Perez','Juan', '3872186720')");
        db.execSQL("INSERT INTO Usuarios (usuario, password, apellido, nombre)" +
                    "VALUES ('contribuyente2','123456','Lopez','Sergio')");
        }

       db.execSQL(CREATE_TABLE_DENUNCIA);
       /*     db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('0', '-24.843203','-65.395245','Iniciado','Auto estacionado sobre una esquina.', '" + fecha + "', '@drawable/foto1')");
            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('1', '-24.788553','-65.409389','Iniciado',' Auto mal estacionado sobre vereda.', '15-6-2020 12:02:30', '@drawable/foto2')");
            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('2', '-24.791540','-65.406859','Iniciado','Auto color negro y otro blanco estacionados en esquina.', '10-5-2020 22:19:22', '@drawable/foto3')");
            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('3', '-24.792945','-65.404259','Iniciado','Auto color Blanoc estacionado en esquina, obstruyendo el paso de peatón', '10-5-2020 13:34:45', '@drawable/foto4')");
            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('4', '-24.786420','-65.401817','Iniciado','Auto obstruyendo rampa.', '22-6-2020 16:08:38', '@drawable/foto5')");
            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('5', '-24.807096','-65.405210','Iniciado','Auto bloqueando la rampa.', '29-6-2020 18:17:00', '@drawable/foto6')");

            db.execSQL("INSERT INTO denuncia (id_denuncia, latitud, longitud, estado, texto, fechaYhora, foto) VALUES ('6', '-24.806491','-65.416416','iniciado','Auto bloqueando la rampa.', '29-6-2020 18:17:00', '@drawable/foto6')");
        }*/
        { db.execSQL(CREATE_TABLE_DOMINIO);//TODO: agregue
            db.execSQL("INSERT INTO Dominios (id_usuario, patente)" +
                    "VALUES ('1','AAA 000')");
            db.execSQL("INSERT INTO Dominios (id_usuario, patente)" +
                    "VALUES ('1','BBB 000')");
            db.execSQL("INSERT INTO Dominios (id_usuario, patente)" +
                    "VALUES ('2','CCC 111')");
        }


        {db.execSQL(CREATE_TABLE_ESTADO);
            db.execSQL("INSERT INTO Estados (descripcion_estado)" +
                    "VALUES ('Iniciado')");
            db.execSQL("INSERT INTO Estados (descripcion_estado)" +
                    "VALUES ('En proceso...')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DENUNCIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTADO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOMINIO);//TODO: NUEVO

        onCreate(db);
    }

}


