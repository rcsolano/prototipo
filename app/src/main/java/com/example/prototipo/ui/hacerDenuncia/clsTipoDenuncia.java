package com.example.prototipo.ui.hacerDenuncia;

class clsTipoDenuncia {
    private String id_tipo;
    private String descripcion;

    public clsTipoDenuncia(String id_tipo, String descripcion) {
        this.id_tipo = id_tipo;
        this.descripcion = descripcion;
    }

    public String getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(String id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
