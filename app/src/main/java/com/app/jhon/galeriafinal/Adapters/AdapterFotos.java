package com.app.jhon.galeriafinal.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.Models.Foto;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Utilities.Constants;
import com.app.jhon.galeriafinal.Views.ActivityInfoFoto;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by David on 28/11/2017.
 */

public class AdapterFotos extends RecyclerView.Adapter<AdapterFotos.FotosViewHolder>{

    private List<Foto> items;
    private Context context;

    SqliteHelper sqliteHelper;

    private static final String KEYUSER = "usuario";
    public static final String KEYPHOTO  = "foto";

    public AdapterFotos(List<Foto> items, Context context) {
        this.items = items;
        this.context = context;
        this.sqliteHelper = new SqliteHelper(context, "db_galeria", null, 1);//inicializacion del sqlLite
    }

    @Override
    public FotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_view_fotos,parent,false);
        return new FotosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FotosViewHolder holder, int position) {
        final Foto foto = items.get(position);//Obtenemos la foto actual seleccionada

        Glide.with(context).load(Uri.parse(foto.getRuta())).into(holder.foto);

        final Boolean ban = verifivarFavorito(foto.getId()); //verificamos si la foto esta en favoritos

        if(ban){//si existe en favoritos, pintamos de color rojo la estrella
            holder.btnFav.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }else{
            holder.btnFav.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        holder.tvNombre.setText(foto.getNombre());

        holder.tvTexto.setText("Foto n° "+(position+1));//la posicion del adaptador +1
        //Metodos de onclick
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ban2 = verifivarFavorito(foto.getId());//Volvemosy consultamos antes de
                if(!ban2) {//Si no existe en favoritos, agregguela
                    insertarEnFavoritos(holder.btnFav, foto.getId());
                }else {//Si ya existe, al presionar eliminara
                    deleteFavoritos(holder.btnFav,foto.getId());
                }
            }
        });
        holder.btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            verInfoFoto(foto);

            }
        });


    }

    private void deleteFavoritos(ImageView btnFav, int id) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        db.execSQL("delete from favorites where id_photo = "+id);
        Toast.makeText(context,"Foto eliminada de favoritas",Toast.LENGTH_LONG).show();

        btnFav.setBackgroundColor(context.getResources().getColor(android.R.color.white));
    }

    //Metoodo que verifica si la foto esta en favoritos
    private Boolean verifivarFavorito(int id) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id_fav,id_photo from favorites where id_photo = "+id, null); //consultamos la tabala de fotos, si la foto esta en favoritos es porque ya existe en la bd

        if (cursor.moveToNext()){
            cursor.close();
            return true;
        }else {
            return false;
        }
    }

    //Metodo que envia el id del usuario y el id de la foto
    private void insertarEnFavoritos(ImageView btnFav, int codFoto) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TABLA_FIELD_IDFP, codFoto);

        Long idResult = db.insert(Constants.TABLA_NAME_FAVORITE, Constants.TABLA_FIELD_IDF, values);



        if(idResult>0){
            Toast.makeText(context,"Foto Agregada a Favoritos",Toast.LENGTH_LONG).show();

            btnFav.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }else{
            Toast.makeText(context,"Error Al Guardar la foto a favoritos",Toast.LENGTH_LONG).show();

        }
    }

    //Metodo que inicia la actividad y envia la foto completa para llenar la informacion
    private void verInfoFoto(Foto foto) {
        Intent intent = new Intent(context,ActivityInfoFoto.class);
        intent.putExtra(KEYPHOTO,foto);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class FotosViewHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        ImageView btnFav,btninfo;
        TextView tvNombre,tvTexto;
        public FotosViewHolder(View itemView) {
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.img_foto);
            btnFav = (ImageView) itemView.findViewById(R.id.btn_fav);
            btninfo = (ImageView) itemView.findViewById(R.id.btn_info);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre_foto);
            tvTexto = (TextView) itemView.findViewById(R.id.tv_texto_foto);
        }
    }
}
