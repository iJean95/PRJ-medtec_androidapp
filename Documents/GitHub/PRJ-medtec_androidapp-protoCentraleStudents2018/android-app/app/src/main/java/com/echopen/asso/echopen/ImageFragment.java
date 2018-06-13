package com.echopen.asso.echopen;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.support.annotation.Nullable;
import android.graphics.drawable.BitmapDrawable;

import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    public ImageView mImageToValidate;
    private ImageView mButtonCancel;
    private ImageView mButtonCheck;
    private int number;
    private Bitmap mBitmap;

    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;

    public ImageFragment() {
        // Required empty public constructor
        number = 321;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                        if(getArguments() != null)
                    {
                                mBitmap = getArguments().getParcelable("capturedImage");
                }
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView =  inflater.inflate(R.layout.fragment_image, container, false);

        mButtonCancel =  rootView.findViewById(R.id.button_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ((MainActivity) getActivity()).goToMainFragment();


            }
        });

        mButtonCheck =  rootView.findViewById(R.id.button_check);
        mButtonCheck.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                saveImage();
                ((MainActivity) getActivity()).goToMainFragment();


            }
        });

        //ImageView imageview = new ImageView(MainActivity.this);
        //RelativeLayout relativelayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        //LinearLayout.LayoutParams params = new LinearLayout
          //      .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mImageToValidate = rootView.findViewById(R.id.imageToValidate);


        mImageToValidate.setImageBitmap(mBitmap);
        mImageToValidate.setRotation(IMAGE_ROTATION_FACTOR);
        mImageToValidate.setScaleX(IMAGE_ZOOM_FACTOR);
        mImageToValidate.setScaleY(IMAGE_ZOOM_FACTOR);

        //mImageToValidate.setImageResource(R.drawable.button_jauge);


        return rootView;


    }
    public void saveImage()
    {
               Bitmap bitmap = ((BitmapDrawable)mImageToValidate.getDrawable()).getBitmap();

                        ((MainActivity) getActivity()).writeToFile(bitmap);
    }

    public void changeImage(Bitmap iBitmap) {


        //mImageToValidate = getView().findViewById(R.id.imageToValidate);

        //mImageToValidate.setImageBitmap(iBitmap);

    }
    public static ImageFragment newInstance(Bitmap iBitmap)
    {
                ImageFragment myFragment = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("capturedImage" , iBitmap);
                myFragment.setArguments(bundle);
                return myFragment;

    }


}
