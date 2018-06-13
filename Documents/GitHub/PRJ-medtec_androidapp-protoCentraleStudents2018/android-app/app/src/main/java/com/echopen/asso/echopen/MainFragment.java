package com.echopen.asso.echopen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.view.CaptureButton;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.echopen.asso.echopen.ui.AbstractActionController.findViewById;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements EchographyImageVisualisationContract.View {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCaptureButton =  rootView.findViewById(R.id.main_button_capture);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("captureButton", "Short Press");
                shortPressAction();
            }
        });

        mCaptureShadow = rootView.findViewById(R.id.main_button_shadow);
        mCaptureShadow.setListener(new CaptureButton.CaptureButtonListener() {
            @Override
            public void onTouchDown() {
                mEchographyImageVisualisationPresenter.toggleFreeze();
            }

            @Override
            public void onTouchUp() {
                mEchographyImageVisualisationPresenter.toggleFreeze();
            }
        });

        mPregnantWomanButton = rootView.findViewById(R.id.main_button_mode);
        mPregnantWomanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pregnantButton", "PregnantWomanButton Pressed");
            }
        });

        mSelectButton = rootView.findViewById(R.id.main_button_select);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("selectButton", "SelectButton Pressed");
            }
        });

        mEndExamButton = rootView.findViewById(R.id.main_button_end_exam);
        mEndExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("endExamButton", "EndExamButton Pressed");
            }
        });

        mBatteryButton = rootView.findViewById(R.id.main_button_battery);
        mBatteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("batteryButton", "BatteryButton Pressed");
            }
        });

        return rootView;
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;

    private ImageView mCaptureButton;
    private ImageView mPregnantWomanButton;
    private ImageView mEndExamButton;
    private ImageView mBatteryButton;
    private ImageView mSelectButton;
    private CaptureButton mCaptureShadow;
    private Bitmap bitmapTest;
    private Boolean isRecording;
    public Boolean stopped;


    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;
    private SequenceImage bitmapArray  = new SequenceImage();

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new EchographyImageVisualisationPresenter(  ((EchOpenApplication) getActivity().getApplication()).getEchographyImageStreamingService(), this));

        stopped = Boolean.FALSE;
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "refreshImage");
                if (         stopped == Boolean.FALSE) {
                    if (getView()==null)
                                            {
                                                        return;
                                            }
                    ImageView lEchOpenImage = (ImageView) getView().findViewById(R.id.echopenImage);
                    lEchOpenImage.setRotation(IMAGE_ROTATION_FACTOR);
                    lEchOpenImage.setScaleX(IMAGE_ZOOM_FACTOR);
                    lEchOpenImage.setScaleY(IMAGE_ZOOM_FACTOR);
                    lEchOpenImage.setImageBitmap(iBitmap); // image affichée

                    bitmapTest = iBitmap;

                    if (isRecording == Boolean.TRUE) {
                        //bitmapArray.add(iBitmap);
                        bitmapArray.mCapturedSequence.add(iBitmap);
                    }
                }

            }
        });
    }


    @Override
    public void displayFreezeButton() {
        mCaptureShadow.setImageResource(R.drawable.icon_arc_shadow);
        mCaptureButton.setImageResource(R.drawable.button_jauge);
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
        mEchographyImageVisualisationPresenter.start();
    }

    @Override
    public void displayUnfreezeButton() {
        mCaptureShadow.setImageResource(R.drawable.icon_save_image);
    }
    public void shortPressAction() {

    }

    public void longPressAction() {
        Log.d(TAG, "hello from longPressAction");

        //start recording Bitmap images until button stops


    }

    public void longPressBegins()
    {
        bitmapArray = new SequenceImage();

        isRecording = Boolean.TRUE;
        stopped = Boolean.FALSE;
    }

    public void longPressCompleted()
    {

        isRecording = Boolean.FALSE;
        stopped = Boolean.TRUE;



        ((MainActivity) getActivity()).GotoSequenceFragment(bitmapArray);

    }
    public void longPressInterrupted()
    {
        isRecording = Boolean.FALSE;
        bitmapArray = new SequenceImage();




        Log.d(TAG, "before getting image");
        Log.d(TAG, "after getting image");

        Bitmap imageCaptured = bitmapTest;
        Log.d(TAG, "after getting image");

        // Bitmap imageCaptured2 = BitmapFactory.decodeFile(R.drawable.flag);

       // mEchographyImageVisualisationPresenter.captureAction(imageCaptured);
        stopped = Boolean.TRUE;


        Log.d(TAG, "aaaaaaaaaaaaaaaaaaaa");

        //((MainActivity) getActivity()).mValidationFragment.mImageToValidate.setImageBitmap(imageCaptured);
//        mImageToValidate = rootView.findViewById(R.id.imageToValidate);
       // ((MainActivity) getActivity()).mValidationFragment.mImageToValidate =
        ((MainActivity) getActivity()).GotoImageFragment(imageCaptured);

    }



}