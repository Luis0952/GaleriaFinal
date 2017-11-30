package com.app.jhon.galeriafinal.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.app.jhon.galeriafinal.Models.FotosFavoritas;
import com.app.jhon.galeriafinal.R;
import com.app.jhon.galeriafinal.Views.ActivityInfoFoto;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by David on 29/11/2017.
 */

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.FavoritosViewHolder> {
    public static final String KEYPHOTO1  = "fotoFavorita";
    private List<FotosFavoritas> items;
    private Context context;

    SqliteHelper sqliteHelper;

    public AdapterFavoritos(List<FotosFavoritas> items, Context context) {
        this.items = items;
        this.context = context;
        this.sqliteHelper = new SqliteHelper(context, "db_galeria", null, 1);//inicializacion del sqlLite
    }

    @Override
    public AdapterFavoritos.FavoritosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_view_fotos,parent,false);
        return new FavoritosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FavoritosViewHolder holder, final int position) {
        final FotosFavoritas fotofav = items.get(position);//Obtenemos la fotofav actual seleccionada
        Glide.with(context).load(Uri.parse(fotofav.getRuta())).into(holder.foto);

        //como la foto esta en favoritos asi q omitimos la logica de el color
            holder.btnFav.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));

        holder.tvNombre.setText(fotofav.getNombre());

        holder.tvTexto.setText("Favorito n° "+(position+1));//la posicion del adaptador +1
        //Metodos de onclick
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Se supone q ya esta insertado en facoritos, por lo tanto solo se elimina
                deleteFavoritos(holder.btnFav,fotofav.getId(),position);
            }
        });
        holder.btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int idUser = ((Usuario)((ActivityGallery)context).getIntent().getSerializableExtra(KEYUSER)).getId(); //Hacemos un cast de la info traida, convertida al final en objeto Usuario y especifico el id de el
                verInfoFoto(fotofav);
            }
        });

    }

    //Metodo que inicia la actividad y envia la foto completa para llenar la informacion
    private void verInfoFoto(FotosFavoritas fotofav) {
        Intent intent = new Intent(context,ActivityInfoFoto.class);
        intent.putExtra(KEYPHOTO1,fotofav);
        context.startActivity(intent);
    }

    private void deleteFavoritos(ImageView btnFav, int fotofavId, int id) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        db.execSQL("delete from favorites where id_photo = "+fotofavId);
        Toast.makeText(context,"Foto eliminada de favoritas",Toast.LENGTH_LONG).show();
        items.remove(id);
        this.notifyDataSetChanged();
    }





    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FavoritosViewHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        ImageView btnFav,btninfo;
        TextView tvNombre,tvTexto;
        public FavoritosViewHolder(View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.img_foto);
            btnFav = (ImageView) itemView.findViewById(R.id.btn_fav);
            btninfo = (ImageView) itemView.findViewById(R.id.btn_info);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre_foto);
            tvTexto = (TextView) itemView.findViewById(R.id.tv_texto_foto);

        }
    }
}
