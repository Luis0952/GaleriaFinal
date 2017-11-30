package com.app.jhon.galeriafinal.Views;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.Models.Foto;
import com.app.jhon.galeriafinal.Models.FotosFavoritas;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Utilities.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ActivityInfoFoto extends AppCompatActivity {
    private ImageView imgFoto;
    TextView tvRuta,tvNombre,tvDesc;
    Button btnComentario;
    EditText edComentario;
    ListView lvComentarios;
    int idFoto;

    SqliteHelper sqliteHelper;

    public static final String KEYPHOTO1  = "fotoFavorita";
    public static final String KEYPHOTO  = "foto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_foto);

        imgFoto = (ImageView) findViewById(R.id.img_foto);
        tvRuta = (TextView) findViewById(R.id.tv_ruta);
        tvNombre = (TextView)findViewById(R.id.tv_nombre_foto);
        tvDesc = (TextView)findViewById(R.id.tv_desc_foto);
        lvComentarios = (ListView) findViewById(R.id.rv_coments);

        btnComentario = (Button)findViewById(R.id.btn_comentar);
        edComentario = (EditText) findViewById(R.id.ed_comment);

        sqliteHelper = new SqliteHelper(this, "db_galeria", null, 1);
        if(getIntent()!=null){
            if(getIntent().getSerializableExtra(KEYPHOTO)!=null){
                Foto foto = (Foto) getIntent().getSerializableExtra(KEYPHOTO);
                tvNombre.setText("Nombre: "+foto.getNombre());
                tvRuta.setText("Ruta: "+foto.getRuta());
                tvDesc.setText("Descripcion: "+foto.getDescripcion());
                this.idFoto = foto.getId();//guardar el id de la foto
                Glide.with(this).load(Uri.parse(foto.getRuta())).into(imgFoto);
                setTitle(foto.getNombre());


            }else if(getIntent().getSerializableExtra(KEYPHOTO1)!=null){
                FotosFavoritas fotosFavoritas = (FotosFavoritas) getIntent().getSerializableExtra(KEYPHOTO1);
                tvNombre.setText("Nombre: "+fotosFavoritas.getNombre());
                tvRuta.setText("Ruta: "+fotosFavoritas.getRuta());
                tvDesc.setText("Descripcion: "+fotosFavoritas.getDescripcion());
                this.idFoto = fotosFavoritas.getId();//guardar el id de la foto
                Glide.with(this).load(Uri.parse(fotosFavoritas.getRuta())).into(imgFoto);
                setTitle(fotosFavoritas.getNombre());
            }

        }
        btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarComentario();
            }
        });

        cargarComentarios();

    }

    //Metodo que agrega un comentario a la tabla
    private void agregarComentario() {


        if(TextUtils.isEmpty(edComentario.getText().toString())){
            Toast.makeText(this,"No puede agregar comentarios vacios",Toast.LENGTH_LONG).show();
        }else{
            SQLiteDatabase db = sqliteHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Constants.TABLA_FIELD_IDCP, this.idFoto);
            values.put(Constants.TABLA_FIELD_COMMENT, edComentario.getText().toString());

            Long idResult = db.insert(Constants.TABLA_NAME_COMMENTS, Constants.TABLA_FIELD_IDC, values);

            if(idResult>0){//Exito
                cargarComentarios(); //Cargamos el adaptador de nuevo
            }else{//No agrego
            Toast.makeText(this,"no se pudo agregar comentario",Toast.LENGTH_LONG).show();

            }
        }
    }

    //Metodo que carga los comentario
    private void cargarComentarios() {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select comment from comments where id_photo = "+this.idFoto, null);
        ArrayList<String> commentsA = new ArrayList<>();

        while (cursor.moveToNext()){
            commentsA.add(cursor.getString(0));
        }

        cursor.close();
        Collections.reverse(commentsA);
        String [] comments = convertirArrayList(commentsA);
        if(comments.length>0){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
            lvComentarios.setAdapter(adapter);
        }




    }

    private String[] convertirArrayList(ArrayList<String> commentsA) {
        String [] temporal = new String[commentsA.size()];
        Iterator<String> ite = commentsA.iterator();
        int i = 0;
        while (ite.hasNext()){
            temporal[i]="Comentario n° "+(i+1)+": "+ite.next();
            i++;
        }
        return temporal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
