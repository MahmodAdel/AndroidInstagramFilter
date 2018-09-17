package com.example.mahmoud.androidinstagramfilter;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mahmoud.androidinstagramfilter.Adapter.FrameAdapter;
import com.example.mahmoud.androidinstagramfilter.Interface.AddFrameListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrameFragment extends BottomSheetDialogFragment implements FrameAdapter.FrameAdapterLisnter {

    static FrameFragment instance;
    Button btn_Add_frame;
    int frame_selected=-1;
    RecyclerView recyclerView_frame;
    AddFrameListener listener;


    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }

    public static FrameFragment getInstance() {
        if(instance == null)
            instance=new FrameFragment();
        return instance;
    }

    public FrameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_frame, container, false);

        recyclerView_frame=(RecyclerView)itemview.findViewById(R.id.recycler_frame);
        btn_Add_frame=(Button)itemview.findViewById(R.id.btn_add_frame);

        recyclerView_frame.setHasFixedSize(true);
        recyclerView_frame.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView_frame.setAdapter(new FrameAdapter(getContext(),this));
        btn_Add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddFrame(frame_selected);
            }
        });
        return itemview;
    }

    @Override
    public void onFrameSelected(int frame) {
        frame_selected=frame;

    }
}
