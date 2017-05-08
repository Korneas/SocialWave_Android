package com.example.camilomontoya.socialwave_android;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by CamiloMontoya on 30/04/17.
 */

public class Cliente implements Runnable {

    private Socket s;
    private static Cliente ref;
    private Observer boss;
    private static final String ADDRESS = "192.168.1.60";
    //private static final String ADDRESS = "172.30.178.0";
    //private static final String ADDRESS = "172.30.180.13";
    private static final int PORT = 5000;
    private boolean life;
    private String currentUser;
    private int maxPostId;

    private Cliente() {
        s = null;
        life = true;
    }

    public static Cliente getInstance() {
        if (ref == null) {
            ref = new Cliente();
            new Thread(ref).start();
        }
        return ref;
    }

    @Override
    public void run() {
        while (life) {

            try {
                if (s == null) {
                    s = new Socket(InetAddress.getByName(ADDRESS), PORT);
                } else {
                    recibir();
                }

                Thread.sleep(500);

            } catch (IOException e) {
                System.out.println("No hay servidor conectado");
                life = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void recibir() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        Object o = in.readObject();
        boss.update(null, o);
    }

    public void enviar(Object o) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject(o);
        out.flush();
    }

    public void setObserver(Observer boss) {
        this.boss = boss;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public int getMaxPostId() {
        return maxPostId;
    }

    public void setMaxPostId(int maxPostId) {
        this.maxPostId = maxPostId;
    }
}
