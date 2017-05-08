package com.example.camilomontoya.socialwave_android;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import serial.Validacion;

import static android.app.Activity.RESULT_OK;

/**
 * Created by CamiloMontoya on 1/05/17.
 */

public class Post_Frag extends Fragment implements Observer{

    private TextView name, content,adding;
    private ImageButton add_file,add_img,add_music;
    private Typeface type,type2;

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static  final String IMAGE_TYPE = "image/*";

    private ImageView selectedImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_section, container, false);
        name = (TextView) v.findViewById(R.id.name_post);
        content = (TextView) v.findViewById(R.id.content_post);
        adding = (TextView) v.findViewById(R.id.addfile_post);

        Cliente.getInstance().setObserver(this);

        name.setText(Cliente.getInstance().getCurrentUser());
        type = Typeface.createFromAsset(((AppCompatActivity) getActivity()).getAssets(), "fonts/Montserrat-BoldItalic.ttf");
        type2 = Typeface.createFromAsset(((AppCompatActivity) getActivity()).getAssets(), "fonts/OpenSans-Regular.ttf");

        name.setTypeface(type);
        content.setTypeface(type2);
        adding.setTypeface(type);

        content.setSingleLine(false);
        content.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

        add_file = (ImageButton) v.findViewById(R.id.addfile_btn);
        add_img = (ImageButton) v.findViewById(R.id.addimg_btn);
        add_music = (ImageButton) v.findViewById(R.id.addmusic_btn);

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Seleccione una imagen"), SELECT_SINGLE_PICTURE);
            }
        });

        selectedImage = (ImageView) v.findViewById(R.id.selected_img);

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    selectedImage.setImageBitmap(new ImagenCarga(selectedImageUri, ((AppCompatActivity) getActivity()).getContentResolver()).getBitmap());
                } catch (IOException e) {
                    System.out.println("Error al cargar imagen");
                }
            }
        } else {
            Toast.makeText(((AppCompatActivity) getActivity()).getApplicationContext(), "No se cargo ninguna imagen", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Validacion){
            Validacion v = (Validacion) arg;
            if(v.isCheck() && v.getType().contains("posteado")){
                ((AppCompatActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedImage.setImageDrawable(null);
                        content.setText("");
                        Toast.makeText(((AppCompatActivity) getActivity()).getApplicationContext(), "Se realizo el post", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}