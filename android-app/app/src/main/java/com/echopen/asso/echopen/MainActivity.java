package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.util.List;
import java.util.Vector;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_IP;
import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

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

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init first fragment
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).addToBackStack(null).commit();

//        //launch probe
        EchOpenApplication echOpenApplication = ( EchOpenApplication ) getApplication();

        final EchographyImageStreamingService serviceEcho = echOpenApplication.getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(serviceEcho, new EchographyImageVisualisationContract.View() {
            @Override
            public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {
                Log.e("wowowowowowow", "une image woiwowoow");
            }

            @Override
            public void refreshImage(final Bitmap iBitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView picture = (ImageView) findViewById(R.id.echo);
                        picture.setImageBitmap(iBitmap);
                        Log.e("Rendu Bitmap", "yooyoyoyoy");
                    }
                });
            }
        });
        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode(REDPITAYA_IP, REDPITAYA_PORT);
        serviceEcho.connect(mode, this);

        presenter.start();
    }

    @Override
    protected void onResume(){
        super.onResume();

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

    // onClick management
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_scan :
                buttonScan();
                break;
            case R.id.button_connexion :
                buttonConnexion();
                break;
            case R.id.button_inscription :
                buttonInscription();
                break;
            case R.id.button_connexion_form :
                buttonConnexionForm();
                break;
            case R.id.button_register :
                buttonRegister();
                break;
            case R.id.left_hand :
                buttonLeftHand();
                break;
            case R.id.right_hand :
                buttonValidSeances();
                break;
            case R.id.valid_param :
                buttonValidParam();
                break;
            case R.id.skip :
                buttonScan();
                break;
            case R.id.gallery :
                buttonGallery();
                break;
            case R.id.button_seances :
                buttonSeances();
                break;
            case R.id.button_valid_seance :
                buttonSeances();
                break;
            case R.id.seances :
                buttonSeances();
                break;
            case R.id.profil :
                buttonProfil();
                break;
            case R.id.faq :
                buttonFaq();
                break;
            case R.id.back :
                onBackPressed();
                break;
        }
    }

    // fragment setup
    private void buttonScan(){
        //set default parameter
        //###
        PagerFragment pagerFragment = new PagerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagerFragment).addToBackStack(null).commit();
    }

    private void buttonConnexion(){
        ConnexionFragment connexionFragment = new ConnexionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, connexionFragment).addToBackStack(null).commit();
    }

    private void buttonInscription(){
        InscriptionFragment inscriptionFragment = new InscriptionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, inscriptionFragment).addToBackStack(null).commit();
    }

    private void buttonRegister(){
        //register
        //###
        //login
        //###
        HandFragment handFragment = new HandFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, handFragment).addToBackStack(null).commit();
    }

    private void buttonConnexionForm(){
        //login
        //###
        HandFragment handFragment = new HandFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, handFragment).addToBackStack(null).commit();
    }

    private void buttonLeftHand(){
        //set left hand
        //###
        ParameterFragment parameterFragment = new ParameterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, parameterFragment).addToBackStack(null).commit();
    }

    private void buttonRightHand(){
        //set right hand
        //###
        ParameterFragment parameterFragment = new ParameterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, parameterFragment).addToBackStack(null).commit();
    }

    private void buttonValidParam(){
        //set parameter
        //###
        PagerFragment pagerFragment = new PagerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagerFragment).addToBackStack(null).commit();
    }

    private void buttonGallery(){
        //get load stored seances
        //###
        GalleryFragment galleryFragment = new GalleryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galleryFragment).addToBackStack(null).commit();
    }

    private void buttonSeances(){
        //get load stored seances
        //###
        Pager2Fragment pager2Fragment = new Pager2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("button_selected", "seances");
        pager2Fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pager2Fragment).addToBackStack(null).commit();
    }

    private void buttonValidSeances(){
        //get load stored seances
        //###
        Pager2Fragment pager2Fragment = new Pager2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("button_selected", "seances");
        pager2Fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pager2Fragment).addToBackStack(null).commit();
    }

    private void buttonProfil(){
        //get profil
        //###
        Pager2Fragment pager2Fragment = new Pager2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("button_selected", "profil");
        pager2Fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pager2Fragment).addToBackStack(null).commit();

    }

    private void buttonFaq(){
        //get faq
        //###
        Pager2Fragment pager2Fragment = new Pager2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("button_selected", "faq");
        pager2Fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pager2Fragment).addToBackStack(null).commit();
    }

/*    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        Log.e("###view name ?", name);

        return super.onCreateView(name, context, attrs);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (findViewById(R.id.pager)!= null || findViewById(R.id.pager2) != null){
            PagerFragment pagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pagerFragment).addToBackStack(null).commit();
        }
    }
}