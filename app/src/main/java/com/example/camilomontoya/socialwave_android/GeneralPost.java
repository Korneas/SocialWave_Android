package com.example.camilomontoya.socialwave_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.ArrayList;

public class GeneralPost {

    private String autor, msg;
    private String[] waves;
    private ArrayList<String> likes;
    private byte[] display;
    private Bitmap map;

    public GeneralPost(String autor, String msg, String[] waves, byte[] display) {
        this.autor = autor;
        this.msg = msg;
        this.waves = waves;
        this.display = display;

        likes = new ArrayList<String>();

        for (int i = 0; i < waves.length; i++) {
            likes.add(waves[i]);
        }
        if (this.display != null) {
            System.out.println("El ByteArray es de tamaño"+this.display.length);
            map = BitmapFactory.decodeByteArray(this.display, 0, this.display.length);
        }
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getWaves() {
        return waves;
    }

    public void setWaves(String[] waves) {
        this.waves = waves;
    }

    public void addWave(String wave) {
        likes.add(wave);
    }

    public Bitmap getImageBitmap() {
        return map;
    }
}
