package com.app.jhon.galeriafinal.Views;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Utilities.Constants;
import com.dd.CircularProgressButton;

/**
 * Created by David on 28/11/2017.
 */

public class FragmentSignIn extends Fragment {

    SqliteHelper sqliteHelper;
    CircularProgressButton buttonProgress;
    private EditText edNombre,edTelefono,edCorreo,edPassword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    edNombre = (EditText)  view.findViewById(R.id.ed_nombre);
    edTelefono = (EditText)  view.findViewById(R.id.ed_telefono);
    edCorreo = (EditText)  view.findViewById(R.id.ed_correo);
    edPassword = (EditText)  view.findViewById(R.id.ed_contraseña);
    buttonProgress = (CircularProgressButton) view.findViewById(R.id.btn_progress);

    sqliteHelper = new SqliteHelper(getActivity(), "db_galeria", null, 1);

        buttonProgress.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(buttonProgress.getProgress()==CircularProgressButton.SUCCESS_STATE_PROGRESS || buttonProgress.getProgress()==CircularProgressButton.ERROR_STATE_PROGRESS){
                buttonProgress.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);//Para volver habilitar el boton en el estado normal
            }else {
                onClickRegistrarse(v);
            }
        }
    });
}


    public void onClickRegistrarse(View view) {
        buttonProgress.setProgress(CircularProgressButton.INDETERMINATE_STATE_PROGRESS);
        String nombre = edNombre.getText().toString();
        String telefono = edTelefono.getText().toString();
        String correo = edCorreo.getText().toString();
        String password = edPassword.getText().toString();

        if(validateCamposVacios(nombre,telefono,correo,password))//Se valida los campos si alguno esta vacio
            createUser(nombre,telefono,correo,password);
        else {
            Toast.makeText(getActivity(), "No puede insertar campos vacios", Toast.LENGTH_LONG).show();
            buttonProgress.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
        }

    }


    private void createUser(String nombre, String telefono, String correo, String password) {
        buttonProgress.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TABLA_FIELD_NAME, nombre);
        values.put(Constants.TABLA_FIELD_PHONE, telefono);
        values.put(Constants.TABLA_FIELD_EMAIL, correo);
        values.put(Constants.TABLA_FIELD_PASSWORD, password);


        Long idResult = db.insert(Constants.TABLA_NAME_USERS, Constants.TABLA_FIELD_ID, values);

        if(idResult>0){
            buttonProgress.setProgress(CircularProgressButton.SUCCESS_STATE_PROGRESS);
            edNombre.setText("");
            edCorreo.setText("");
            edTelefono.setText("");
            edPassword.setText("");
        }else{
            buttonProgress.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
        }

    }

    //Valida campos vacios, devuelve false si es vacio
    public boolean validateCamposVacios(String nom,String telefoni,String correo,String contraseña){
        if(TextUtils.isEmpty(nom))return false;
        if(TextUtils.isEmpty(telefoni)) return false;
        if(TextUtils.isEmpty(correo)) return false;
        if(TextUtils.isEmpty(contraseña)) return false;
        return true;
    }
}
