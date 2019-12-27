package com.android.advancedgalleryglide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//implementing  GalleryStripAdapter.GalleryStripCallBacks to fragment  for communication of fragment and GalleryStripAdapter
public class SlideShowFragment extends DialogFragment {

    private static SlideShowFragment fragment;

    //declare static variable which will serve as key of current position argument
    private static final String ARG_CURRENT_POSITION = "position";
    //Declare list of GalleryItems
    List<GalleryItem> galleryItems;
    //Deceleration of  Slide show View Pager Adapter
    SlideShowPagerAdapter mSlideShowPagerAdapter;
    //Deceleration of viewPager
    ViewPager mViewPagerGallery;
    TextView textViewImageName;
    RecyclerView recyclerViewGalleryStrip;

    private int mCurrentPosition;
    //set bottom to visible of first load
    boolean isBottomBarVisible = true;


    public SlideShowFragment() {
        // Required empty public constructor
    }

    //Create new instance of SlideShowFragment
    public static SlideShowFragment newInstance(int position) {
        fragment = new SlideShowFragment();
        //Create bundle
        Bundle args = new Bundle();
        //put Current Position in the bundle
        args.putInt(ARG_CURRENT_POSITION, position);
        //set arguments of SlideShowFragment
        fragment.setArguments(args);
        //return fragment instance
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialise GalleryItems List

        galleryItems = new ArrayList<>();
        if (getArguments() != null) {
            //get Current selected position from arguments
            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            //get GalleryItems from activity
            galleryItems = ((MainActivity) getActivity()).galleryItems;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_silde_show, container, false);

        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragment = (SlideShowFragment) fragmentManager.findFragmentById(R.id.slideShow);
        } else {
            fragment = new SlideShowFragment();
        }
        //Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mViewPagerGallery = view.findViewById(R.id.viewPagerGallery);
        //Initialise View Pager Adapter
        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);
        //set adapter to Viewpager
        mViewPagerGallery.setAdapter(mSlideShowPagerAdapter);
        //tell viewpager to open currently selected item and pass position of current item
        mViewPagerGallery.setCurrentItem(mCurrentPosition);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
