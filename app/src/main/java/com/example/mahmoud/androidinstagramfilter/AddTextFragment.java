   package com.example.mahmoud.androidinstagramfilter;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mahmoud.androidinstagramfilter.Adapter.ColorAdapter;
import com.example.mahmoud.androidinstagramfilter.Adapter.FontAdapter;
import com.example.mahmoud.androidinstagramfilter.Interface.AddTextFragmentListener;


   /**
 * A simple {@link Fragment} subclass.
 */
public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterClickListener {

    int colorSelected= Color.parseColor("#000000");

    AddTextFragmentListener listener;

    EditText edit_add_text;
    RecyclerView recyclerView_color,recyclerView_font;
    Button btn_done;
    Typeface typefaceSelected = Typeface.DEFAULT;


    static AddTextFragment instance;

       public static AddTextFragment getInstance() {
           if(instance == null)
               instance=new AddTextFragment();
           return instance;
       }

       public void setListener(AddTextFragmentListener listener) {
           this.listener = listener;
       }

       public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_add_text, container, false);

        edit_add_text=(EditText)itemview.findViewById(R.id.edt_add_text);
        btn_done=(Button)itemview.findViewById(R.id.btn_done);
        recyclerView_color=(RecyclerView)itemview.findViewById(R.id.recycler_color);
        recyclerView_color.setHasFixedSize(true);
        recyclerView_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView_font=(RecyclerView)itemview.findViewById(R.id.recycler_font);
        recyclerView_font.setHasFixedSize(true);
        recyclerView_font.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        ColorAdapter colorAdapter =new ColorAdapter(getActivity(),this);
        recyclerView_color.setAdapter(colorAdapter);

        FontAdapter fontAdapter=new FontAdapter(getContext(),this);
        recyclerView_font.setAdapter(fontAdapter);



        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnAddTextButtonClick(typefaceSelected,edit_add_text.getText().toString(),colorSelected);
            }
        });

        return  itemview;
    }

       @Override
       public void onColorSelected(int color) {
        colorSelected=color;

       }

       @Override
       public void onFontSelected(String fontName) {
           typefaceSelected=Typeface.createFromAsset(getContext().getAssets(),new StringBuilder("fonts/")
                   .append(fontName).toString());

       }
   }
