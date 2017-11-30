package com.app.jhon.galeriafinal.Views;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.jhon.galeriafinal.Adapters.AdapterFotos;
import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.Models.Foto;
import com.app.jhon.galeriafinal.Models.Usuario;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Utilities.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 28/11/2017.
 */

public class FragmentFotos extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 222;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 333;
    private static final String KEYUSER = "usuario";//llave
    private static final String KEYIDUSER = "iduser";
    //Ruta y nuevo archivo de tipo file
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    List<Foto> items;
    SqliteHelper sqliteHelper;
    private RecyclerView rvFotos;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fbCamera;
    private SwipeRefreshLayout swipe; //Permite recargar manualmente una interfaz
    private RelativeLayout containerVacia;
    private int idUser;//id user q llega por el intent
    private File file = new File(ruta_fotos);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Pedimos permisos de escritura y de camara
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        //lectura de permisos de almacenamiento
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        //lectura de permisos de camara
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }

        return inflater.inflate(R.layout.fragment_fotos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fbCamera = (FloatingActionButton) view.findViewById(R.id.fab_tomar_foto);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh1);

        containerVacia = (RelativeLayout) view.findViewById(R.id.container_vacia);


        idUser = ((Usuario) getActivity().getIntent().getSerializableExtra(KEYUSER)).getId();//Obtencion del id del usuario
        sqliteHelper = new SqliteHelper(getActivity(), "db_galeria", null, 1);
        iniciarRecycler(view);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarAdaptador();
            }
        });

        fbCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_dialog_crear_foto frm = new Fragment_dialog_crear_foto();
                Bundle bundle = new Bundle();
                bundle.putInt(KEYIDUSER,idUser);
                frm.setArguments(bundle);
                frm.show(getActivity().getSupportFragmentManager(),"");
                //tomarFoto();
            }
        });
        file.mkdirs();//Si no esta la carpeta, se crea

    }

    //Metodo que toma la foto desde la camara
    private void tomarFoto() {
        String code = getCode();
        String file = ruta_fotos + code + ".jpg";
        File mi_foto = new File(file);
        //
        try {
            mi_foto.createNewFile();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
        //Log.e("nombreFoto: ",code+".jpg");
        String nombreFoto = code + ".jpg";
        Uri uri = Uri.fromFile(mi_foto);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Abre la camara para tomar la foto
        //Guarda imagen

        //Log.e("urlFoto",uri.toString());
        //Log.e("Foto",mi_foto.getAbsolutePath());

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        guardarImagenUsuario(uri, nombreFoto);

        //Retorna a la actividad
        startActivityForResult(cameraIntent, 0);


    }

    private void guardarImagenUsuario(Uri uri, String nombreFoto) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TABLA_FIELD_NAMEP, nombreFoto);
        values.put(Constants.TABLA_FIELD_RUTE, uri.toString());
        values.put(Constants.TABLA_FIELD_IDUSERP, idUser);


        Long idResult = db.insert(Constants.TABLA_NAME_PHOTOS, Constants.TABLA_FIELD_IDP, values);

        if (idResult > 0) {//Exito
            cargarAdaptador(); //Cargamos el adaptador de nuevo
        } else {//No agrego
            msjError("Error al Agregar la foto");
        }
    }


    //iniciar el recycler view
    private void iniciarRecycler(View view) {
        rvFotos = (RecyclerView) view.findViewById(R.id.rv_fotos);
        rvFotos.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        rvFotos.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        adapter = new AdapterFotos(items, getActivity());
        rvFotos.setAdapter(adapter);

        cargarAdaptador();
    }

    private void cargarAdaptador() {
        items.clear();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,name,description,rute,id_user from photos where id_user = " + this.idUser, null);

        while (cursor.moveToNext()) {
            Foto foto = new Foto(cursor.getInt(0), cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getInt(4));
            File file = new File(Uri.parse(foto.getRuta()).getPath());//Creamos un archivo, a partir del ficher
            //Log.e("peso",""+file.length()+"se peude leer"+file.canRead()+"total espace"+file.getTotalSpace());
            if (file.length() > 0) {//Validamos q solo ingrese el q tiene el peso mayor a 0
                items.add(foto);
            }
        }

        cursor.close();

        if (items.size() > 0) {
            Collections.reverse(items);//ordena los elementos al contrario
            containerVacia.setVisibility(View.GONE);
            swipe.setRefreshing(false);
            adapter = new AdapterFotos(items, getActivity());
            rvFotos.setAdapter(adapter);
        } else {
            containerVacia.setVisibility(View.VISIBLE);
            swipe.setRefreshing(false);
        }

    }


    //Metodo que genera un nombre para la foto, apartir de el año,mes,dias,horas,minustos, segundos
    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode = "pic_" + date;
        return photoCode;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //Si se garantiza permisos
                } else {
                    msjError("Error Permuso de Almacenamiento Externo");
                }
                return;
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    msjError("Error de Permiso de Acceso a Camara");
                }
                return;

            }

        }
    }

    //Metodo q envia msj con toast
    private void msjError(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_LONG).show();
    }

}
