package com.example.camilomontoya.socialwave_android;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import serial.Post;
import serial.Wave;

public class Home_Frag extends Fragment implements Observer {

    private List<GeneralPost> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GeneralPostAdapter postAdapter;

    private ArrayList<Post> inPosts = new ArrayList<>();
    private ArrayList<Wave> inWave = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_section, container, false);

        Cliente.getInstance().setObserver(this);

        /**
         * Se instancia el RecyclerView del layout de la activity
         */
        recyclerView = (RecyclerView) v.findViewById(R.id.posteados);

        //Se crea el adaptador que hace que los objetos serializable Post se adapten a la clase GeneralPost
        postAdapter = new GeneralPostAdapter(postList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postAdapter);

        ControlTipografia.getInstance().setTypeTitle(Typeface.createFromAsset(((AppCompatActivity) getActivity()).getAssets(), "fonts/Montserrat-BoldItalic.ttf"));
        ControlTipografia.getInstance().setTypeSubtitle(Typeface.createFromAsset(((AppCompatActivity) getActivity()).getAssets(), "fonts/OpenSans-Bold.ttf"));
        ControlTipografia.getInstance().setTypeMsg(Typeface.createFromAsset(((AppCompatActivity) getActivity()).getAssets(), "fonts/OpenSans-Light.ttf"));

        return v;
    }

    /**
     * Metodo para añadir Post a la lista del RecyclerView
     *
     * @param p Post
     */
    private void addPost(final Post p) {
        ((AppCompatActivity) getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final GeneralPost generalPost = new GeneralPost(p.getAutor(), p.getMsg(), p.getLikes(), p.getFile());
                postList.add(generalPost);
                System.out.println("Cantidad de Post " + postList.size());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ArrayList) {

            if (((ArrayList) arg).get(0) != null) {
                if (((ArrayList) arg).get(0) instanceof Post) {

                    inPosts = (ArrayList) arg;
                    postList.clear();

                    Cliente.getInstance().setMaxPostId(inPosts.size() - 1);

                    for (int i = inPosts.size() - 1; i >= 0; i--) {
                        addPost(inPosts.get(i));
                    }

                    //Actualizar la interfaz para ver los cambios del RecyclerView
                    ((AppCompatActivity) getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postAdapter.notifyDataSetChanged();
                        }
                    });
                } else if (((ArrayList) arg).get(0) instanceof Wave) {

                    inWave = (ArrayList) arg;
                    for (int i = 0; i < inWave.size(); i++) {
                        
                    }
                }
            }
        }
    }
}
