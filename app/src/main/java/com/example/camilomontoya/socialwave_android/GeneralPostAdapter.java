package com.example.camilomontoya.socialwave_android;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;

import serial.Wave;

public class GeneralPostAdapter extends RecyclerView.Adapter<GeneralPostAdapter.MyViewHolder> {

    private List<GeneralPost> postList;
    private boolean liked;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView autorTitle, autorSubtitle, wavesOfPost, message;
        public ImageView imagePost;
        public ImageButton comment, save;
        public ToggleButton wave;

        public MyViewHolder(View view) {
            super(view);
            autorTitle = (TextView) view.findViewById(R.id.autor_post_title);
            autorSubtitle = (TextView) view.findViewById(R.id.autor_post);
            wavesOfPost = (TextView) view.findViewById(R.id.waves_of_post);
            message = (TextView) view.findViewById(R.id.msg);

            imagePost = (ImageView) view.findViewById(R.id.image_post);

            wave = (ToggleButton) view.findViewById(R.id.wave_post);
            comment = (ImageButton) view.findViewById(R.id.comment_post);
            save = (ImageButton) view.findViewById(R.id.save_post);
        }
    }

    public GeneralPostAdapter(List<GeneralPost> postList) {
        this.postList = postList;
    }

    /**
     * Metodo para pintar en la interfaz el layout correspondiente para el post
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return view
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     * Metodo para setear elementos del ViewHolder como TextView e ImageView
     *
     * @param holder   MyViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GeneralPost posting = postList.get(position);
        holder.autorTitle.setText(posting.getAutor());
        holder.autorSubtitle.setText(posting.getAutor());

        boolean likeUser = false;
        String[] allWaves = posting.getWaves();

        for (int i = 0; i < posting.getWaves().length; i++) {
            if (Cliente.getInstance().getCurrentUser().contentEquals(allWaves[i])) {
                likeUser = true;
            }
        }

        if (likeUser) {
            holder.wavesOfPost.setText("Tu y otros " + posting.getWaves().length + " waves");
        } else {
            holder.wavesOfPost.setText(posting.getWaves().length + " waves");
        }
        holder.message.setText("- \"" + posting.getMsg() + "\"");

        holder.autorTitle.setTypeface(ControlTipografia.getInstance().getTypeTitle());
        holder.autorSubtitle.setTypeface(ControlTipografia.getInstance().getTypeSubtitle());
        holder.wavesOfPost.setTypeface(ControlTipografia.getInstance().getTypeMsg());
        holder.message.setTypeface(ControlTipografia.getInstance().getTypeMsg());

        holder.imagePost.setImageBitmap(posting.getImageBitmap());

        holder.wave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cliente.getInstance().enviar(new Wave(position, posting.getAutor()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
