package com.example.camilomontoya.socialwave_android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import serial.Confirmacion;
import serial.Validacion;

public class LogActivity extends AppCompatActivity implements Observer{

    private EditText name,pass;
    private ImageButton enter;

    private String[] info;

    private Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        info = new String[2];

        Cliente.getInstance().setObserver(this);

        type = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");

        name = (EditText) findViewById(R.id.user_in);
        pass = (EditText) findViewById(R.id.pass_in);

        enter = (ImageButton) findViewById(R.id.ingreso);

        name.setTypeface(type);
        pass.setTypeface(type);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info[0]=name.getText().toString();
                info[1]=pass.getText().toString();

                if(info[0].isEmpty() && info[1].isEmpty()){
                    aviso("Escribe los campos para entrar");
                } else {
                    try {
                        Cliente.getInstance().enviar(new Confirmacion(info[0], info[1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void aviso(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Validacion){
            Validacion v = (Validacion) arg;
            if(v.getType().contains("log")){
                if(v.isCheck()){
                    startActivity(new Intent(LogActivity.this,HomeActivity.class));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aviso("No estas registrado");
                        }
                    });
                }
            }
        }
    }
}
