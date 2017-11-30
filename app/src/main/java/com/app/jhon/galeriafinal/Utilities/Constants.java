package com.app.jhon.galeriafinal.Utilities;

/**
 * Created by David on 28/11/2017.
 */

public class Constants  {
    public static final String TABLA_NAME_USERS = "users";
    public static final String TABLA_FIELD_ID = "id";
    public static final String TABLA_FIELD_NAME = "name";
    public static final String TABLA_FIELD_PHONE = "phone";
    public static final String TABLA_FIELD_EMAIL = "email";
    public static final String TABLA_FIELD_PASSWORD = "password";
    public static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS "+ TABLA_NAME_USERS+" ("+
                    TABLA_FIELD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TABLA_FIELD_NAME+" TEXT, "+
                    TABLA_FIELD_PHONE+" TEXT, "+
                    TABLA_FIELD_EMAIL+" TEXT, "+
                    TABLA_FIELD_PASSWORD+" TEXT)";



    public static final String TABLA_NAME_PHOTOS = "photos"; //tabla fotos
    public static final String TABLA_FIELD_IDP = "id";  //id de la foto
    public static final String TABLA_FIELD_NAMEP = "name"; //nombre de la foto
    public static final String TABLA_FIELD_DESCRIPTION = "description"; //nombre de la foto
    public static final String TABLA_FIELD_RUTE = "rute"; //la ruta de la sdcard
    public static final String TABLA_FIELD_IDUSERP = "id_user"; //id del usuario al que pertenece
    public static final String CREATE_TABLE_PHOTOS =
            "CREATE TABLE IF NOT EXISTS "+ TABLA_NAME_PHOTOS+" ("+
                    TABLA_FIELD_IDP+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TABLA_FIELD_NAMEP+" TEXT, "+
                    TABLA_FIELD_DESCRIPTION+" TEXT, "+
                    TABLA_FIELD_RUTE+" TEXT, "+
                    TABLA_FIELD_IDUSERP+" INTEGER)";

    public static final String TABLA_NAME_FAVORITE = "favorites"; //tabla favoritos
    public static final String TABLA_FIELD_IDF = "id_fav";  //id de favoritos
    public static final String TABLA_FIELD_IDFP = "id_photo"; //id fe la foto
    public static final String CREATE_TABLE_FAVORITE =
            "CREATE TABLE IF NOT EXISTS "+ TABLA_NAME_FAVORITE+" ("+
                    TABLA_FIELD_IDF+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TABLA_FIELD_IDFP+" INTEGER)";

       public static final String TABLA_NAME_COMMENTS = "comments"; //tabla comentarios
    public static final String TABLA_FIELD_IDC = "id_comment";  //id de comenario
    public static final String TABLA_FIELD_IDCP = "id_photo"; //id fe la foto
    public static final String TABLA_FIELD_COMMENT = "comment"; //comentario
    public static final String CREATE_TABLE_COMMENTS =
            "CREATE TABLE IF NOT EXISTS "+ TABLA_NAME_COMMENTS+" ("+
                    TABLA_FIELD_IDC+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TABLA_FIELD_COMMENT+" TEXT, "+
                    TABLA_FIELD_IDCP+" INTEGER)";



    /*
    public static final String TABLA_NAME_COMMENTS = "comments";
    public static final String TABLA_FIELD_IDC = "id";
    public static final String TABLA_FIELD_TITLE = "title";
    public static final String TABLA_FIELD_COMMENT = "comment";
    public static final String TABLA_FIELD_IDUSER = "id_user";
    public static final String CREATE_TABLE_COMMENTS =
            "CREATE TABLE "+ TABLA_NAME_COMMENTS+" ("+
                    TABLA_FIELD_IDC+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TABLA_FIELD_TITLE+" TEXT, "+
                    TABLA_FIELD_COMMENT+" TEXT, "+
                    TABLA_FIELD_IDUSER+" INTEGER)";*/


}
