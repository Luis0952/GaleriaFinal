package com.app.jhon.galeriafinal.Views;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.jhon.galeriafinal.Adapters.AdapterFavoritos;
import com.app.jhon.galeriafinal.Helpers.SqliteHelper;
import com.app.jhon.galeriafinal.Models.FotosFavoritas;
import com.app.jhon.galeriafinal.Models.Usuario;
import com.app.jhon.galeriafinal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 28/11/2017.
 */

public class FragmentFavoritos extends Fragment {

    private RecyclerView rvFavoritos;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout rlContainer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FotosFavoritas> lista;
    private int idUser;//id user q llega por el intent
    private static final String KEYUSER = "usuario";//llave
    SqliteHelper sqliteHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoritos,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh2);
        rlContainer = (RelativeLayout)view.findViewById(R.id.container_fav);

        idUser = ((Usuario)getActivity().getIntent().getSerializableExtra(KEYUSER)).getId();//Obtencion del id del usuario
        sqliteHelper = new SqliteHelper(getActivity(), "db_galeria", null, 1);
        iniciarRecycler(view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarAdaptador();
            }
        });



    }

    private void iniciarRecycler(View view) {
        rvFavoritos = (RecyclerView)view.findViewById(R.id.rv_favoritos);
        rvFavoritos.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        rvFavoritos.setLayoutManager(layoutManager);

        lista = new ArrayList<>();
        adapter = new AdapterFavoritos(lista,getActivity());
        rvFavoritos.setAdapter(adapter);

        cargarAdaptador();

    }

    private void cargarAdaptador() {
        lista.clear();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,name,description,rute,id_user,id_fav from photos as p join favorites as f on p.id=f.id_photo where p.id_user = "+this.idUser, null);

        while (cursor.moveToNext()){
            FotosFavoritas fotosFavoritas = new FotosFavoritas(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5));
                lista.add(fotosFavoritas);

        }

        cursor.close();

        if(lista.size()>0){
            Collections.reverse(lista);
            rlContainer.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            adapter = new AdapterFavoritos(lista,getActivity());
            rvFavoritos.setAdapter(adapter);
        }else{
            rvFavoritos.removeAllViews();
            rlContainer.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }



}
