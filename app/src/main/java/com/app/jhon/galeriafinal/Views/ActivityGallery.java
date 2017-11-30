package com.app.jhon.galeriafinal.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.jhon.galeriafinal.Adapters.AdapterGallery;
import com.app.jhon.galeriafinal.Models.Usuario;
import com.app.jhon.galeriafinal.R;

public class ActivityGallery extends AppCompatActivity {

    private TabLayout tabs ;
    private ViewPager viewPager;
    private AdapterGallery adapterPager;

    private static final String KEYUSER = "usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);

        tabs = (TabLayout) findViewById(R.id.tabLayout_gallery );
        viewPager = (ViewPager) findViewById(R.id.viewpager_gallery);

        adapterPager = new AdapterGallery(getSupportFragmentManager());
        viewPager.setAdapter(adapterPager);

        tabs.setupWithViewPager(viewPager,true);

        tabs.getTabAt(0).setIcon(R.drawable.ic_image);
        tabs.getTabAt(1).setIcon(R.drawable.ic_favorite);

        //obtenemos el usuario
        Usuario usuario = (Usuario) getIntent().getSerializableExtra(KEYUSER);

        getSupportActionBar().setTitle("Bienvenido "+ usuario.getNombre());

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        if(requestCode==0){
            Uri uri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            Log.e("uriActivity",uri.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.cerrar_Sesion){
            goBackLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo que me devuelve al login principal
    private void goBackLogin() {
        Intent intent = new Intent(this,ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//se limpia la pila de actividades, apra evitar volver atras
        startActivity(intent);
    }
}
