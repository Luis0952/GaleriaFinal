package com.app.jhon.galeriafinal.Views;

import android.content.Intent;
import android.database.Cursor;
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
import com.app.jhon.galeriafinal.Models.Usuario;
import com.app.jhon.galeriafinal.R;
import com.dd.CircularProgressButton;

/**
 * Created by David on 28/11/2017.
 */

public class FragmentLogin extends Fragment {

    private EditText edCorreo,edContraseña;
    SqliteHelper sqliteHelper;
    CircularProgressButton buttonProgress;
    Usuario usuario;

    private static final String KEYUSER = "usuario";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edCorreo = (EditText)  view.findViewById(R.id.ed_correo);
        edContraseña = (EditText)  view.findViewById(R.id.ed_contraseña);
        buttonProgress = (CircularProgressButton) view.findViewById(R.id.btn_progress2);

        sqliteHelper = new SqliteHelper(getActivity(), "db_galeria", null, 1);

        buttonProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonProgress.getProgress()==CircularProgressButton.SUCCESS_STATE_PROGRESS || buttonProgress.getProgress()==CircularProgressButton.ERROR_STATE_PROGRESS){
                    buttonProgress.setProgress(CircularProgressButton.IDLE_STATE_PROGRESS);//Para volver habilitar el boton en el estado normal
                }else {
                    onClickIniciarSesion();
                }
            }
        });


    }

    //Metodo que inicia sesion
    private void onClickIniciarSesion() {
        String correo = edCorreo.getText().toString();
        String contraseña = edContraseña.getText().toString();

        if(TextUtils.isEmpty(correo)) {
            Toast.makeText(getContext(), "El correo no puede ser vacio", Toast.LENGTH_LONG).show();
            buttonProgress.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
            return;
        }else if(TextUtils.isEmpty(contraseña)){
            Toast.makeText(getContext(), "la contraseña no puede ser vacio", Toast.LENGTH_LONG).show();
            buttonProgress.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
            return;
        }

        if(validateUserExits(correo,contraseña)){//si devuelve true, pasa a la siguiente actividad
            Intent intent = new Intent(getActivity(),ActivityGallery.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//se limpia la pila de actividades, apra evitar volver atras
            intent.putExtra(KEYUSER,usuario); //enviamos el objeto completo a la siguiente actividad
            startActivity(intent);
        }else{//mensaje de error
            buttonProgress.setProgress(CircularProgressButton.ERROR_STATE_PROGRESS);
            Toast.makeText(getContext(),"El usuario no existe",Toast.LENGTH_LONG).show();
        }


    }

    //metodo que busca en la base de datos el usuario, por nombre y contraseña
    private boolean validateUserExits(String correo, String contraseña) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,name,phone,email,password from users where email = '"+correo+"' and password = '"+contraseña+"'", null);

        if(cursor.moveToNext()){//si entra por el ciclo significa que si hay un usuario registrado y se puede logear
            //Se llena el objeto usuario, no por los meotodos set, si no por el constructor
            usuario = new Usuario(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            return true;//devuelve verdadero para señalar que si existe
        }


        return false;
    }


}
