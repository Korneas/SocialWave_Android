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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import serial.Post;

public class Home_Frag extends Fragment implements Observer{

    private List<GeneralPost> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GeneralPostAdapter postAdapter;

    private ArrayList<Post> inPosts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_section,container,false);

        Cliente.getInstance().setObserver(this);

        recyclerView = (RecyclerView) v.findViewById(R.id.posteados);

        postAdapter = new GeneralPostAdapter(postList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(((AppCompatActivity)getActivity()).getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(((AppCompatActivity)getActivity()).getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(postAdapter);

        //Typeface typeTitle = Typeface.createFromAsset(((AppCompatActivity)getActivity()).getAssets(),"fonts/Montserrat-BoldItalic.ttf");
        //Typeface typeSubtitle = Typeface.createFromAsset(((AppCompatActivity)getActivity()).getAssets(),"fonts/OpenSans-Bold.ttf");
        //Typeface typeMsg = Typeface.createFromAsset(((AppCompatActivity)getActivity()).getAssets(),"fonts/OpenSans-Light.ttf");
        return v;
    }

    private void addPost(final Post p){
        ((AppCompatActivity)getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final GeneralPost generalPost = new GeneralPost(p.getAutor(),p.getMsg(),new String[2],p.getFile());
                postList.add(generalPost);
                System.out.println("Cantidad de Post "+postList.size());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ArrayList){
            inPosts = (ArrayList) arg;
            postList.clear();

            Cliente.getInstance().setMaxPostId(inPosts.size()-1);

            for (int i = inPosts.size()-1; i >= 0; i--) {
                addPost(inPosts.get(i));
            }

            ((AppCompatActivity)getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    postAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
