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

    //ADDRESS Del servidor en el momento, se muestra en la consola del servidor para cambiarlo aqui
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

    /**
     * Instancia del cliente para realizar el patron Singleton
     * @return
     */
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
                /**
                 * Se conecta con el servidor con el ADDRESS que se tiene de este
                 */
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

    /**
     * Metodo para recibir todos los objetos que envie el servidor y notificarlos al Observer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void recibir() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        Object o = in.readObject();
        boss.update(null, o);
    }

    /**
     * Metodo para enviar objetos al servidor
     * @param o Object
     * @throws IOException
     */
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
