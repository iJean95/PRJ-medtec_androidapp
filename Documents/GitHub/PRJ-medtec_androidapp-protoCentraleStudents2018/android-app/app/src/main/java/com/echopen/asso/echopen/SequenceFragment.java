package com.echopen.asso.echopen;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class SequenceFragment extends Fragment {

    public ImageView mImageToValidate;
    private ImageView mButtonCancel;
    private ImageView mButtonCheck;
    private int number;
    private SequenceImage mBitmapArray;
    private int sequenceIndex;
    public SequenceFragment() {
        // Required empty public constructor
        number = 321;

    }


    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
        {
            mBitmapArray = getArguments().getParcelable("capturedSequence");
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
        Log.d("imageFragment", "hello from imageFragment1");

        mImageToValidate = rootView.findViewById(R.id.imageToValidate);
       // mImageToValidate.setImageResource(R.drawable.hand_raw);
        //mImageToValidate.setImageBitmap(((MainActivity) getActivity()).iBitmap);



        // mImageToValidate.setImageBitmap(mBitmapArray);
        sequenceIndex = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                while (true)
                                {
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override

                                        public void run() {
                                            sequenceIndex++;
                                            if (sequenceIndex>mBitmapArray.mCapturedSequence.size()-1)
                                            {
                                                sequenceIndex = 0;
                                            }


                                            mImageToValidate.setImageBitmap(mBitmapArray.mCapturedSequence.get(sequenceIndex));

                                        }
                                    });
                                }
                            }
                        });


                Log.d("imageFragment", "hello from imageFragment2");
        mImageToValidate.setRotation(IMAGE_ROTATION_FACTOR);
        mImageToValidate.setScaleX(IMAGE_ZOOM_FACTOR);
        mImageToValidate.setScaleY(IMAGE_ZOOM_FACTOR);



        return rootView;


    }

    public void saveImage()
    {
        Bitmap bitmap = ((BitmapDrawable)mImageToValidate.getDrawable()).getBitmap();

        ((MainActivity) getActivity()).writeSequenceToFile(mBitmapArray);


    }
    public void changeImage(Bitmap iBitmap) {


       // mImageToValidate = getView().findViewById(R.id.imageToValidate);

     //   mImageToValidate.setImageBitmap(iBitmap);

    }
    public static SequenceFragment newInstance(SequenceImage bitmapArray)
    {

        SequenceFragment myFragment = new SequenceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("capturedSequence" ,  bitmapArray);
        myFragment.setArguments(bundle);
        return myFragment;

    }

}
