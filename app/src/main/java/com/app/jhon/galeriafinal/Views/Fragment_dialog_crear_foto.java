package com.app.jhon.galeriafinal.Views;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Utilities.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 30/11/2017.
 */

public class Fragment_dialog_crear_foto extends DialogFragment {

    private static final String KEYIDUSER = "iduser";
    private AutoCompleteTextView edNombre,edDescricpion;
    private Button btnGuardar,btnCancelar;
    private int idUser;
    SqliteHelper sqliteHelper;
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    private File file = new File(ruta_fotos);
    public Fragment_dialog_crear_foto() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialog();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_dialog_crear_foto, null);

        edDescricpion = (AutoCompleteTextView) v.findViewById(R.id.ed_descripcion_foto);
        edNombre = (AutoCompleteTextView) v.findViewById(R.id.ed_nombre_foto);
        btnGuardar = (Button)v.findViewById(R.id.btn_guardar_foto);
        btnCancelar = (Button) v.findViewById(R.id.btn_cancelar_foto);

        sqliteHelper = new SqliteHelper(getActivity(), "db_galeria", null, 1);
        if (getArguments() != null) {
            //se trae el iddel usuario
            this.idUser = getArguments().getInt(KEYIDUSER, 0);
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreFoto = edNombre.getText().toString();
                String Descripcion = edDescricpion.getText().toString();
                if(TextUtils.isEmpty(nombreFoto)||TextUtils.isEmpty(Descripcion)){
                    Toast.makeText(getActivity(),"No puede haber campos vacios",Toast.LENGTH_LONG).show();
                }else {
                    tomarFoto(nombreFoto,Descripcion);
                }
            }
        });

        file.mkdirs();//Si no esta la carpeta, se crea
        builder.setView(v);
        return builder.create();
    }

    //Metodo que toma la foto desde la camara
    private void tomarFoto(String nombreFoto, String descripcion) {
        String code = getCode(nombreFoto);
        String file = ruta_fotos + code + ".jpg";
        File mi_foto = new File(file);
        //
        try {
            mi_foto.createNewFile();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
        //Log.e("nombreFoto: ",code+".jpg");
        String nombre = code + ".jpg";
        Uri uri = Uri.fromFile(mi_foto);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Abre la camara para tomar la foto
        //Guarda imagen

        //Log.e("urlFoto",uri.toString());
        //Log.e("Foto",mi_foto.getAbsolutePath());

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        guardarImagenUsuario(uri, nombre,descripcion);

        //Retorna a la actividad
        startActivityForResult(cameraIntent, 0);


    }

    private void guardarImagenUsuario(Uri uri, String nombreFoto,String desc) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TABLA_FIELD_NAMEP, nombreFoto);
        values.put(Constants.TABLA_FIELD_RUTE, uri.toString());
        values.put(Constants.TABLA_FIELD_DESCRIPTION, desc);
        values.put(Constants.TABLA_FIELD_IDUSERP, idUser);


        Long idResult = db.insert(Constants.TABLA_NAME_PHOTOS, Constants.TABLA_FIELD_IDP, values);

        if (idResult > 0) {//Exito
            Toast.makeText(getActivity(),"La foto se guardo correctamente",Toast.LENGTH_LONG).show();
            dismiss();
        } else {//No agrego

        }
    }


    //Metodo que genera un nombre para la foto, apartir de el año,mes,dias,horas,minustos, segundos y un nombre
    private String getCode(String nombreFoto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode = nombreFoto+"_" + date;
        return photoCode;
    }


}
