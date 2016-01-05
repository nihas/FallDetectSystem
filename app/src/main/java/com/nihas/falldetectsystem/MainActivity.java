package com.nihas.falldetectsystem;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity  implements View.OnClickListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    int status_person=0; //0 : initial value, 1: Falling detected, 2: Falling Confirmed,3:Hit detected,4:Inactivity,5:geting response
    Button start,quit;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=(Button)findViewById(R.id.start);
        quit=(Button)findViewById(R.id.quit);
        start.setOnClickListener(this);
        quit.setOnClickListener(this);

//        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
////        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

//            if ((curTime - lastUpdate) > 50) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                Log.i("Accel", "A Fall detected. "+Math.sqrt((x*x)+(y*y)+(z*z)));
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
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }*/




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
//                senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                startService(new Intent(this, MyService.class));
                break;
            case R.id.quit:
//                senSensorManager.unregisterListener(this);
                stopService(new Intent(this, MyService.class));
                break;
        }
    }
//   public void start_app(View view){
//
//
//	   Intent intent = new Intent(this, ReadAccelData.class);
//        startActivity(intent);
//
//
//
//   }
//
//
//   public void exit_app(View view){
//	   finish();
//
//
//   }
    
}
