package com.example.mahmoud.androidinstagramfilter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mahmoud.androidinstagramfilter.Adapter.ViewPagerAdapter;
import com.example.mahmoud.androidinstagramfilter.Interface.AddFrameListener;
import com.example.mahmoud.androidinstagramfilter.Interface.AddTextFragmentListener;
import com.example.mahmoud.androidinstagramfilter.Interface.BrushFragmentListener;
import com.example.mahmoud.androidinstagramfilter.Interface.EditImageFragmentListener;
import com.example.mahmoud.androidinstagramfilter.Interface.EmojiFragmentListener;
import com.example.mahmoud.androidinstagramfilter.Interface.FiltersListFragmentListener;
import com.example.mahmoud.androidinstagramfilter.Utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;


public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener,EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener, AddFrameListener {

    public static final String picture_name="flash.png";

    public static final int PERMISSION_PICK_IMAGE=1000;
    public static final int PERMISSION_INSERT_IMAGE=1001;


    PhotoEditorView phoroEditorView;

    CoordinatorLayout coordinatorLayout;
    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;
    CardView btn_filter_list,bnt_edit,btn_brush,btn_emoji,btn_add_text,btn_add_image,btn_add_frame,btn_crop;


    PhotoEditor photoEditor;


    int brightnessFinal=0;
    float saturationFinal=1.0f;
    float constrantFinal=1.0f;

    Uri image_selected_uri;


    //load native image filter lib

    static {
        System.loadLibrary("NativeImageProcessor");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Instagram Filter");


        //view
        phoroEditorView=(PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor=new PhotoEditor.Builder(this,phoroEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator);
        bnt_edit=(CardView)findViewById(R.id.btn_edit);
        btn_filter_list=(CardView)findViewById(R.id.btn_filters_list);
        btn_brush=(CardView)findViewById(R.id.btn_brunsh);

        btn_emoji=(CardView)findViewById(R.id.btn_emoji);
        btn_add_text=(CardView)findViewById(R.id.btn_add_text);
        btn_add_image=(CardView)findViewById(R.id.btn_add_image);

        btn_add_frame=(CardView)findViewById(R.id.btn_add_frame);

        btn_crop=(CardView)findViewById(R.id.btn_crop);


        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop(image_selected_uri);
            }
        });

        btn_filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(filterListFragment != null){

                   filterListFragment.show(getSupportFragmentManager(),filterListFragment.getTag());
               }else {
                   FilterListFragment filterListFragment= (FilterListFragment) FilterListFragment.getInstance(null);
                   filterListFragment.setListener(MainActivity.this);
                   filterListFragment.show(getSupportFragmentManager(),filterListFragment.getTag());
               }

            }
        });

        bnt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditImageFragment editImageFragment=EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });


        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment=BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiFragment emojiFragment=EmojiFragment.getInstance();
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTextFragment addTextFragment=AddTextFragment.getInstance();
                addTextFragment.setListener(MainActivity.this);
                addTextFragment.show(getSupportFragmentManager(),addTextFragment.getTag());
            }
        });


        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageToPicture();
            }
        });

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameFragment frameFragment=FrameFragment.getInstance();
                frameFragment.setListener(MainActivity.this);
                frameFragment.show(getSupportFragmentManager(),frameFragment.getTag());
            }
        });


        loadImage();

        




    }

    private void startCrop(Uri uri) {
        String destinationFileName=new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop uCrop=UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.start(MainActivity.this);


    }

    private void addImageToPicture() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_INSERT_IMAGE);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(MainActivity.this, "Permission Denaid", Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void loadImage() {
        originalBitmap= BitmapUtils.getBitmapFromAssets(this,picture_name,300,300);
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        phoroEditorView.getSource().setImageBitmap(originalBitmap);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());

        filterListFragment=new FilterListFragment();
        filterListFragment.setListener(this);

        editImageFragment=new EditImageFragment();
        editImageFragment.setListener(this);


        adapter.addFragment(filterListFragment,"FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);

    }


    @Override
    public void onBrightnessChanged(int brightness) {

        brightnessFinal=brightness;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new BrightnessSubFilter(brightness));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal=saturation;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new SaturationSubfilter(saturation));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal=constrant;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new ContrastSubFilter(constrant));
        phoroEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onEditStarted() {



    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter=new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

        finalBitmap=myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        phoroEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);


    }

    private void resetControl() {
        if(editImageFragment != null){
            editImageFragment.resetControls();
        }
        brightnessFinal=0;
        saturationFinal=1.0f;
        constrantFinal=1.0f;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.action_open){
            openImageFromGallery();
            return true;
        }

        if(id== R.id.action_save){
            SaveImageFromGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                           photoEditor.saveAsBitmap(new OnSaveBitmap() {
                               @Override
                               public void onBitmapReady(Bitmap saveBitmap) {
                                   try {

                                       phoroEditorView.getSource().setImageBitmap(saveBitmap);
                                       final  String path=BitmapUtils.insertImage(getContentResolver(),saveBitmap
                                               ,System.currentTimeMillis()+"_profile.jpg",null);
                                       if (!TextUtils.isEmpty(path)) {
                                           Snackbar snackbar=Snackbar.make(coordinatorLayout,"Image Save to gallery",
                                                   Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   openImage(path);

                                               }
                                           });
                                           snackbar.show();

                                       }
                                       else {
                                           Snackbar snackbar=Snackbar.make(coordinatorLayout,"Unable to save Image",
                                                   Snackbar.LENGTH_LONG);
                                           snackbar.show();

                                       }
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }

                               @Override
                               public void onFailure(Exception e) {

                               }
                           });
                        }else {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                    }
                }).check();


    }

    private void openImage(String path) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);

    }

    private void openImageFromGallery() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }else {
                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if( requestCode == PERMISSION_PICK_IMAGE) {

                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                image_selected_uri=data.getData();

                //clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                phoroEditorView.getSource().setImageBitmap(originalBitmap);

                bitmap.recycle();

                //fix crush

                filterListFragment= (FilterListFragment) FilterListFragment.getInstance(originalBitmap);
                filterListFragment.setListener(this);

            }else if(requestCode == PERMISSION_INSERT_IMAGE) {
                Bitmap bitmap=BitmapUtils.getBitmapFromGallery(this,data.getData(),300,300);
                photoEditor.addImage(bitmap);

            }
            else if (requestCode == UCrop.REQUEST_CROP){
                handleCropResult(data);
            }
            else if(requestCode == UCrop.RESULT_ERROR){
                handleCropError(data);
            }
        }
    }

    private void handleCropError(Intent data) {
        final Throwable cropError=UCrop.getError(data);
        if(cropError != null){
            Toast.makeText(this, ""+cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri=UCrop.getOutput(data);
        if(resultUri != null){
            phoroEditorView.getSource().setImageURI(resultUri);
            
        }else {
            Toast.makeText(this, "Cant retrive crop image", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBrushSizeChangeListener(Float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushopacityChangeListener(int opacity) {

        photoEditor.setOpacity(opacity);

    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);

    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if(isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }


    @Override
    public void OnAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface,text,color);
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),frame);
        photoEditor.addImage(bitmap);
    }
}
