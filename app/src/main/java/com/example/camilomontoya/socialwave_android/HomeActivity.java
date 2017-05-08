package com.example.camilomontoya.socialwave_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import serial.Post;
import serial.Usuario;
import serial.Validacion;

import static android.R.attr.bitmap;


public class HomeActivity extends AppCompatActivity {

    BottomBar mBottomBar,mBottomBar2;
    Usuario currentUser;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar2 = mBottomBar;
        mBottomBar.setTabTitleTypeface("fonts/OpenSans-Light.ttf");

        Intent changed = getIntent();
        Bundle b = changed.getExtras();

        if(b!=null){
            currentUser = (Usuario) b.get("usuario");
        }

        try {
            Cliente.getInstance().enviar(new Validacion(true,"actualizar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);

        mBottomBar2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    menu.getItem(0).setVisible(false);
                    getSupportActionBar().setTitle("Home");
                    Home_Frag f = new Home_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, f).commit();
                    try {
                        Cliente.getInstance().enviar(new Validacion(true,"actualizar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (tabId == R.id.tab_post) {
                    postId = Cliente.getInstance().getMaxPostId();
                    menu.getItem(0).setVisible(true);
                    getSupportActionBar().setTitle("Post");
                    Post_Frag f = new Post_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, f).commit();
                } else if (tabId == R.id.tab_notify) {
                    menu.getItem(0).setVisible(false);
                    getSupportActionBar().setTitle("Notificaciones");
                    Notify_Frag f = new Notify_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, f).commit();
                } else if (tabId == R.id.tab_profile) {
                    menu.getItem(0).setVisible(false);
                    getSupportActionBar().setTitle("Perfil");
                    Perfil_Frag f = new Perfil_Frag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, f).commit();
                } else {
                    for (int i = 0; i < menu.size(); i++) {
                        menu.getItem(i).setVisible(false);
                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:

                EditText content = (EditText) findViewById(R.id.content_post);
                ImageView selectedImg = (ImageView) findViewById(R.id.selected_img);
                int tipo = 0;
                byte[] pack = new byte[0];

                if(selectedImg.getDrawable()!=null) {
                    Bitmap bitmap = ((BitmapDrawable) selectedImg.getDrawable()).getBitmap();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    pack = out.toByteArray();
                    tipo = 1;
                }

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String nombre = df.format(c.getTime());

                try {
                    Cliente.getInstance().enviar(new Post(Cliente.getInstance().getCurrentUser(),content.getText().toString(),nombre+postId,tipo,postId,pack));
                    Cliente.getInstance().setMaxPostId(postId++);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void aviso(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
