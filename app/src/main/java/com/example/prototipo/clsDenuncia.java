package com.example.prototipo;

import java.util.Date;

public class clsDenuncia {
    private String latitud, longitud, estado, texto, foto, patente, fecha, id_usuario;

    public clsDenuncia(String latitud, String longitud, String estado, String texto, String foto, String patente, String fecha, String id_usuario) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.texto = texto;
        this.foto = foto;
        this.patente = patente;
        this.fecha = fecha;
        this.id_usuario = id_usuario;
    }

    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPatente() {
        return patente;
    }
    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

}
