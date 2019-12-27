package com.android.advancedgalleryglide;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

//implementing  GalleryAdapter.GalleryAdapterCallBacks to activity  for communication of Activity and Gallery Adapter
public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<GalleryItem> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    GalleryAdapter mGalleryAdapter;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_circular);

        //setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        //setup RecyclerView
        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 5));
        }
        //Create RecyclerView Adapter
        mGalleryAdapter = new GalleryAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);
        //check for read storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Get images
            galleryItems = GalleryUtils.getImages(this);
            // add images to gallery recyclerview using adapter
            mGalleryAdapter.addGalleryItems(galleryItems);
        } else {
            //request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }
    }

    public void showDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setMessage("Do you really want to quit?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemSelected(int position) {
        //create fullscreen SlideShowFragment dialog
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        //setUp style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally show dialogue
        slideShowFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Get images
                galleryItems = GalleryUtils.getImages(this);
                // add images to gallery recyclerview using adapter
                mGalleryAdapter.addGalleryItems(galleryItems);
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
