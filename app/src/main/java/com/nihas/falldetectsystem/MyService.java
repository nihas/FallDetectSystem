package com.nihas.falldetectsystem;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nihas on 01-01-2016.
 */
public class MyService extends Service implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    public MediaPlayer m1_fall;
    public static final int REQUEST_CODE = 0;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    int status_person=0; //0 : initial value, 1: Falling detected, 2: Falling Confirmed,3:Hit detected,4:Inactivity,5:geting response

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

        m1_fall=MediaPlayer.create(getBaseContext(), R.raw.fall);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
//        Intent panel = new Intent(this, Panel.class);
//        startActivity(panel);


        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

//            if ((curTime - lastUpdate) > 50) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            Log.i("Accel", "A Fall detected. " + Math.sqrt((x * x) + (y * y) + (z * z)));
            if(status_person==0) {
                if (Math.sqrt((x * x) + (y * y) + (z * z)) < 5) {
                    Log.i("Accel", "Falling. " + Math.sqrt((last_x * last_x) + (last_y * last_y) + (last_z * last_z)));
                    status_person = 1;
                }else {
                    status_person=0;
                }
            }else if(status_person==1) {

                if (Math.sqrt((x * x) + (y * y) + (z * z)) < 5) {
                    Log.i("Accel", "Falling Confirmed. " + Math.sqrt((x * x) + (y * y) + (z * z)) + " Waiting for hit");
                    status_person=2;
                }else {
                    status_person=0;
                }
            }else if(status_person==2) {
                if (Math.sqrt((x * x) + (y * y) + (z * z)) > 10.5) {
                    Log.i("Accel", "Hitted on ground. " + Math.sqrt((x * x) + (y * y) + (z * z)));
                    status_person=3;
                    m1_fall.start();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE,
                            intent, 0);
                    try {
                        pendingIntent.send(getApplicationContext(),0,intent);
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }else {
                    status_person=2;
                }
            }else if(status_person==3){
                status_person=0;
            }




            if((x-last_x>3) || (x-last_x< -3)){
                if((y-last_y>3) || (y-last_y<-3)) {
                    if((z-last_z>3) || (z-last_z<-3)) {
//                            Log.i("OH MA GOD", "A Fall detected.");
//                            Log.i("X", "x: "+x+" lastX: "+last_x);
//                            Log.i("Y", "y: "+y+" lastX: "+last_y);
//                            Log.i("Z", "z: "+z+" lastX: "+last_z);
                    }
                }
            }
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {

            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }

}