package com.example.mahmoud.androidinstagramfilter.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahmoud.androidinstagramfilter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud on 3/09/18.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> color_list;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.color_list = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.color_section.setCardBackgroundColor(color_list.get(position));

    }

    @Override
    public int getItemCount() {
        return color_list.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        public CardView color_section;


        public ColorViewHolder(View itemView) {
            super(itemView);
            color_section=(CardView) itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(color_list.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList() {

        List<Integer> colorList=new ArrayList<>();

        colorList.add(Color.parseColor("#2e8b57"));
        colorList.add(Color.parseColor("#ee15ed"));
        colorList.add(Color.parseColor("#15ee16"));
        colorList.add(Color.parseColor("#5cf35d"));
        colorList.add(Color.parseColor("#fff700"));
        colorList.add(Color.parseColor("#d742ff"));
        colorList.add(Color.parseColor("#d1cac0"));
        colorList.add(Color.parseColor("#ff1a72"));
        colorList.add(Color.parseColor("#c92c2c"));
        colorList.add(Color.parseColor("#5a80b4"));


        return  colorList;
    }


    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
