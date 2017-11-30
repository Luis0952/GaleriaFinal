package com.app.jhon.galeriafinal.Models;

import java.io.Serializable;

/**
 * Created by David on 28/11/2017.
 */

public class Usuario implements Serializable{//implementa serializable para poder ser enviado como un tipo de objeto en general
    private int id;
    private String nombre;
    private String telefono;
    private String correo;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    private String contraseña;


    public Usuario(int id, String nombre, String telefono, String correo, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.contraseña = contraseña;
    }
}
