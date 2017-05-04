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

import serial.Usuario;
import serial.Validacion;

public class RegisterActivity extends AppCompatActivity implements Observer {

    private EditText name, pass_1, pass_2, mail;
    private String[] info;
    private ImageButton reg;

    private static Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        info = new String[4];

        type = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");

        name = (EditText) findViewById(R.id.name_reg);
        pass_1 = (EditText) findViewById(R.id.password_1);
        pass_2 = (EditText) findViewById(R.id.password_2);
        mail = (EditText) findViewById(R.id.correo);

        name.setTypeface(type);
        pass_1.setTypeface(type);
        pass_2.setTypeface(type);
        mail.setTypeface(type);

        reg = (ImageButton) findViewById(R.id.reg_envio);

        Cliente.getInstance().setObserver(this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info[0] = name.getText().toString();
                info[1] = pass_1.getText().toString();
                info[2] = pass_2.getText().toString();
                info[3] = mail.getText().toString();

                if (info[0].isEmpty() || info[1].isEmpty() || info[2].isEmpty() || info[3].isEmpty()) {
                    aviso("Completa todos los campos para continuar");
                } else if(!info[1].contentEquals(info[2])){
                    aviso("Las contraseñas no coinciden");
                } else {
                    try {
                        Cliente.getInstance().enviar(new Usuario(info[0],info[1],info[3]));
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
            if(v.getType().contains("registro")){
                if(v.isCheck()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aviso("Registro exitoso");
                        }
                    });
                    startActivity(new Intent(RegisterActivity.this,LogActivity.class));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aviso("El nombre de usuario ya existe");
                        }
                    });
                }
            }
        }
    }
}
