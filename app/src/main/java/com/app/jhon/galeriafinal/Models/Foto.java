package com.app.jhon.galeriafinal.Models;

import java.io.Serializable;

/**
 * Created by David on 28/11/2017.
 */

public class Foto implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private String ruta;
    private int id_usuario;

    public Foto(int id, String nombre,String descripcion, String ruta, int id_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ruta = ruta;
        this.id_usuario = id_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
