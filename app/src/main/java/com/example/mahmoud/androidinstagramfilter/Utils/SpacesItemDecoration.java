package com.example.mahmoud.androidinstagramfilter.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mahmoud on 21/07/18.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration (int space){
        this.space=space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == state.getItemCount() -1){
            outRect.left=space;
            outRect.right=0;

        }else {
            outRect.left=0;
            outRect.right=space;
        }
    }
}
