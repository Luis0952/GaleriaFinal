package com.app.jhon.galeriafinal.Models;

/**
 * Created by David on 29/11/2017.
 */

public class FotosFavoritas extends Foto { //La clase foto favorita extiende de foto, para poder usar sus metodos y atributos de la superClase
    private int id_fav;


    public FotosFavoritas(int id, String nombre, String descripcion, String ruta, int id_usuario,int id_fav) {
        super(id, nombre, descripcion, ruta, id_usuario);
        this.id_fav = id_fav;
    }


    public int getId_fav() {
        return id_fav;
    }

    public void setId_fav(int id_fav) {
        this.id_fav = id_fav;
    }


}
