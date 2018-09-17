package com.example.mahmoud.androidinstagramfilter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoud.androidinstagramfilter.R;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by mahmoud on 3/09/18.
 */

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    Context context;
    List<String> emojList;
    EmojiAdapterListener listener;

    public EmojiAdapter(Context context, List<String> emojList, EmojiAdapterListener listener) {
        this.context = context;
        this.emojList = emojList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmojiAdapter.EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.emoji_item,parent,false);
        return new EmojiViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.EmojiViewHolder holder, int position) {
        holder.emoji_text_view.setText(emojList.get(position));

    }

    @Override
    public int getItemCount() {
            return emojList.size();
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView emoji_text_view;
        public EmojiViewHolder(View itemView) {
            super(itemView);
            emoji_text_view=(EmojiconTextView)itemView.findViewById(R.id.emoji_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEmojiItemSelected(emojList.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface EmojiAdapterListener{
        void onEmojiItemSelected(String emoji);

    }
}
