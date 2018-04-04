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
    private static final int SHAKE_THRESHOLD = 0;

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
    public void onSensorChanged(SensorEvent event){

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE){

            last_y=event.values[2]*posx;
            last_x=-event.values[2]*posy;
            last_z=0.f;
        }

        if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR){

            last_y += -Math.pow(event.values[2],2)*posy;
            last_x += -Math.pow(event.values[2],2)*posx;


        }

        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){


            /*if (event.values[0] < -0.1 || event.values[0]>0.1 || event.values[1] < -0.1 || event.values[1]>0.1 || event.values[2] < -0.1 || event.values[2]>0.1){


            }

            else {



*/

            for (int i=0 ; i<=3; i++){

                acceltable.set(i,acceltable.get(i+1));

            }

            kalman = estimate/(estimate+variance);

            //accel[0] = alpha * event.values[0] + (1 - alpha) * accel[0];
            //accel[1] = alpha * event.values[1] + (1 - alpha) * accel[1];
            //accel[2] = alpha * event.values[2] + (1 - alpha) * accel[2];
            float x;
            float y;
            float z;
//x axis
            float coef = 0.04f;


            last_x+=event.values[0];
            last_y+=event.values[1];
            last_z+=event.values[2];




            if (event.values[0] > -coef && event.values[0]<coef)
            {
                accel[0] = 0;
                x = 0;



            }

            else
            {

                x = gain * last_x + (1 - gain) * accel[0] + kalman*(accel[0]- last_x);
                accel[0] = x;


            }

//y axis
            if (event.values[1] > -coef && event.values[1]<coef)
            {

                accel[1] = 0;
                y = 0;


            }
            else
            {

                y = gain * last_y + (1 - gain) * accel[1] + kalman*(accel[1]- last_y);
                accel[1] = y;

            }

//z axis
            if (event.values[2] > -coef && event.values[2]<coef)
            {

                accel[2] = 0;
                z = 0;

            }
            else
            {

                z = gain * last_z + (1 - gain) * accel[2] + kalman*(accel[2]- last_z);
                accel[2] = z;



            }

            estimate = estimate*(1-kalman);







           // acceltable.set(4,new Float [] {accel[0],accel[1],accel[2]});





            int test = 1;

            if (event.values[0] > -coef && event.values[0]<coef &&
                    event.values[1] > -coef && event.values[1]<coef &&
                        event.values[2] > -coef && event.values[2]<coef)
            {
                test = 0;
            }


           /* for (int i=0; i<=4; i++){

                Float[] a = acceltable.get(i);
                for(int j=0; j<=2; j++){

                    if (a[j]!=0){
                        test =1;
                    }

                }

            }*/









            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;



                vx = Vx.get(Vx.size() - 1) + x * diffTime / 1000;


                vy = Vy.get(Vy.size() - 1) + y * diffTime / 1000;

                vz = Vz.get(Vz.size() - 1) + z * diffTime / 1000;


                posx = Xx.get(Xx.size() - 1) + vx * diffTime / 1000;
                posy = Xy.get(Xy.size() - 1) + vy * diffTime / 1000;
                posz = Xz.get(Xz.size() - 1) + vz * diffTime / 1000;


                if (test ==0)
                {
                    vx=vy=vz=0;

                }

                if (vy < 50 && vx < 50 && vz < 50 && vy > -50 && vz > -50 && vx > -50) {

                    Vy.add(vy);

                    Vx.add(vx);

                    Vz.add(vz);

                    Xy.add(posy);

                    Xx.add(posx);

                    Xz.add(posz);

                    speed = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));

                    double s = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) );

                    last_x = x;
                    last_y = y;
                    last_z = z;

                    write2(Float.toString(last_x), Float.toString(last_y), Float.toString(vx), Float.toString(vy), Double.toString(speed), Float.toString(posx), Float.toString(posy), Float.toString(posz)) ;
                    Log.d("log", "Freinage brusque détecté" + Double.toString(speed));


                }



            }




            //}




        }

    }

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

    public void write2(String messageX,String messageY,String messageZ,String msg, String speed, String posx, String posy, String posz) {
        //ancienne fonction
        simpleTextView.setText("ax : " +
                                messageX +
                                "\n" +
                                "ay : "
                                + messageY +
                                "\n" +
                                "vx : "
                                 + messageZ +
                                "\n"+

                                "vy :  "
                                 + msg +
                                     "\n"

                                + "vitesse: "
                                + speed +
                                        "\n"

                                + "x: "
                                + posx +
                                        "\n"

                                + "y: "
                                + posy +
                                        "\n"

                                + "z: "
                                + posz +
                                        "\n"

        ); //set the text after clicking button

    }






}
