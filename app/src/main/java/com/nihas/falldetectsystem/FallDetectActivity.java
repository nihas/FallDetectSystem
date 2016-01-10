package com.nihas.falldetectsystem;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by snyxius on 6/1/16.
 */
public class FallDetectActivity extends AppCompatActivity implements View.OnClickListener {

    Button negativ,positiv;
    Handler mHandler = new Handler();
    int WAITING_TIME=10;
    Boolean isRunning=true;
    TextView wait;
    Thread myThread;
    Runnable myRunnable=new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            // Write your code here to update the UI.
                            if(WAITING_TIME<0){
                                Toast.makeText(FallDetectActivity.this,"Emergency Message initialised",Toast.LENGTH_LONG).show();
                                isRunning=false;
                            }else{
                                wait.setVisibility(View.VISIBLE);
                                wait.setText(String.valueOf(WAITING_TIME));
                                WAITING_TIME--;
                            }
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detect);

        initialise();


        myThread=new Thread(myRunnable);//.start();
        myThread.start();
    }

    public void initialise(){
        findViewById(R.id.negative).setOnClickListener(this);
        findViewById(R.id.positive).setOnClickListener(this);
        wait=(TextView)findViewById(R.id.wait);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.negative:
                Toast.makeText(FallDetectActivity.this,"Thank you for your response",Toast.LENGTH_LONG).show();
                break;
            case R.id.positive:
                Toast.makeText(FallDetectActivity.this,"Emergency Message initialised",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
