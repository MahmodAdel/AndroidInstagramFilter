package com.example.mahmoud.androidinstagramfilter.Interface;

/**
 * Created by mahmoud on 3/09/18.
 */

public interface BrushFragmentListener {
    void onBrushSizeChangeListener(Float size);
    void onBrushopacityChangeListener(int opacity);
    void onBrushColorChangedListener(int color);
    void onBrushStateChangedListener(boolean isEraser);



}
