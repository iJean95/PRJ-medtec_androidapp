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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import java.io.FileOutputStream;
import java.util.UUID;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.view.CaptureButton;

import java.util.ArrayList;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MainActivity class handles the main screen of the app.
 * Tools are called in the following order :
 * - initSwipeViews() handles the gesture tricks via GestureDetector class
 * - initViewComponents() mainly sets the clickable elements
 * - initActionController() and setupContainer() : in order to separate concerns, View parts are handled by the initActionController()
 * method which calls the MainActionController class that deals with MainActivity Views,
 * especially handles the display of the main screen picture
 * These two methods should be refactored into one
 */

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EchographyImageStreamingService mEchographyImageStreamingService;
    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter
            ;

  //  private EchographyImageCaptureContract.Presenter mEchographyImageCapturePresenter;

    private ImageView mCaptureButton;
    private ImageView mPregnantWomanButton;
    private ImageView mEndExamButton;
    private ImageView mBatteryButton;
    private ImageView mSelectButton;
//   private CaptureButton mCaptureShadow;
    private MainFragment mMainFragment;
    public ImageFragment mValidationFragment;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    public ImageView mImageToValidate;
    public Bitmap iBitmap;

    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;
    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValidationFragment = new ImageFragment();
        mMainFragment = new MainFragment();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mEchographyImageStreamingService = ((EchOpenApplication) getApplication()).getEchographyImageStreamingService();
        mEchographyImageStreamingService.connect(new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT), this);
        mEchographyImageStreamingService.getRenderingContextController().setLinearLutSlope(RenderingContext.DEFAULT_LUT_SLOPE);
        mEchographyImageStreamingService.getRenderingContextController().setLinearLutOffset(RenderingContext.DEFAULT_LUT_OFFSET);


        setContentView(R.layout.activity_main);
        goToMainFragment();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();







        mEchographyImageStreamingService.getRenderingContextController().setLinearLutSlope(RenderingContext.DEFAULT_LUT_SLOPE);
        mEchographyImageStreamingService.getRenderingContextController().setLinearLutOffset(RenderingContext.DEFAULT_LUT_OFFSET);
    }

    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode, to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,       Intent instance
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }








    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }

    public void goToMainFragment(){
        Log.d(TAG, "GOtoMainFragment");
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mMainFragment.stopped = Boolean.FALSE;

        ft.replace(R.id.main_container, mMainFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void GotoImageFragment(Bitmap imageCaptured) {
        Log.d(TAG, "GOtoImageFragment");
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.d("az", "goto image fragment 1");

       // mValidationFragment.mImageToValidate.setImageBitmap(imageCaptured); // image affichée

        Log.d("za", "goto image fragment 2");


        mValidationFragment = ImageFragment.newInstance(imageCaptured);


      //  Fragment newFragment = this;

        ft.replace(R.id.main_container, mValidationFragment);

        ft.addToBackStack(null);
        ft.commit();
    }


    public void GotoSequenceFragment(SequenceImage imageArray) {
                Log.d(TAG, "GOtoImageFragment");
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        // iBitmap = imageCaptured;
                                SequenceFragment fragment = SequenceFragment.newInstance(imageArray);

                        ft.replace(R.id.main_container, fragment);

        ft.addToBackStack(null);
        ft.commit();

                    }

    public void writeToFile(Bitmap data) {

        FileOutputStream outputStream = null;

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");

        String strDt = dateFormat.format(currentTime);
        strDt = strDt + ".png";

        FileOutputStream out = null;
        try {
            Log.d("try", " saving");

            out = getApplicationContext().openFileOutput(strDt, Context.MODE_PRIVATE);
           // out = new FileOutputStream(strDt );

            data.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("exception", "prioblem saving");

        }

}


    public void writeSequenceToFile(SequenceImage data) {


        FileOutputStream out = null;
        try {
            Log.d("try", " saving");

            // out = new FileOutputStream(strDt );
            UUID patientID = UUID.randomUUID();
            File mydir = getApplicationContext().getDir("patient-" + patientID, Context.MODE_PRIVATE);
            mydir.mkdirs();
            for (Bitmap image : data.mCapturedSequence) {
                Log.d("captureseq", " saving");

                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm:ss");
                UUID imageID = UUID.randomUUID();

                String strDt = dateFormat.format(currentTime);
                strDt = strDt + imageID + ".png";
                File path = new File(mydir, strDt);
                //out = getApplicationContext().openFileOutput(path.getAbsolutePath().toString(), Context.MODE_PRIVATE);
                out = new FileOutputStream(path.getAbsolutePath().toString());

                image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                out.close();

            }
            // PNG is a lossless format, the compression factor (100) is ignored

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("exception", "prioblem saving");

        }
    }
}



