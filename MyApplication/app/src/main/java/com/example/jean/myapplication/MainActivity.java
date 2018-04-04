package com.example.jean.myapplication;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;

    private Sensor senAccelerometer;

    private Sensor senGyroscope;

    private Sensor senRotation;

    private TextView simpleTextView;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 100;

    private double speed;



    private float vx;
    private float vy;
    private float vz;
    private  ArrayList<Float> Vy = new ArrayList<>();
    private  ArrayList<Float> Vx = new ArrayList<>();
    private  ArrayList<Float> Vz = new ArrayList<>();

    private float posx;
    private float posy;
    private float posz;



    private  ArrayList<Float> Xy = new ArrayList<>();
    private  ArrayList<Float> Xx = new ArrayList<>();
    private  ArrayList<Float> Xz = new ArrayList<>();

    private float gain;
    private float variance;

    private float kalman;
    private float estimate;
    private float estivit;

    private float[] accel = new float[3];

    private float[] vit = new float[3];

    private ArrayList<Float[]> acceltable = new ArrayList<>();

    @Override

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    protected void onPause(){
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleTextView = (TextView) findViewById(R.id.simpleTextView); //get the id for TextView

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senRotation = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senRotation, SensorManager.SENSOR_DELAY_NORMAL);



        speed = 0;
        vx = 0;
        vy = 0;
        vz = 0;
        Vy.add(0.f);
        Vx.add(0.f);
        Vz.add(0.f);
        Xy.add(0.f);
        Xx.add(0.f);
        Xz.add(0.f);
        posx=0;
        posy=0;
        posz=0;

        accel[0]=0;
        accel[1]=0;
        accel[2]=0;

        vit[0]=0;
        vit[1]=0;
        vit[2]=0;

        gain = 0.715f;

        variance = 0.1f;

        estimate = 0.2f;




        acceltable.add(new Float [] {0.f,0.f,0.f});
        acceltable.add(new Float [] {0.f,0.f,0.f});
        acceltable.add(new Float [] {0.f,0.f,0.f});
        acceltable.add(new Float [] {0.f,0.f,0.f});
        acceltable.add(new Float [] {0.f,0.f,0.f});


       // write2("12","32","43");

    }
    public void writeFunction(String message) {
        //ancienne fonction
        simpleTextView.setText(simpleTextView.getText() + "\n" + message); //set the text after clicking button

    }

    public void write2(String messageX) {
        //ancienne fonction
        simpleTextView.setText("arrêt brusque à  : " +
                                messageX +
                                "\n"
        ); //set the text after clicking button

    }






}
