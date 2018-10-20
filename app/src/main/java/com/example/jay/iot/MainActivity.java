package com.example.jay.iot;

import android.app.Dialog;
//import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog ;
import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;
import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;
import retrofit2.http.Url;

import com.hamsa.twosteppickerdialog.OnStepDataRequestedListener;
import com.hamsa.twosteppickerdialog.OnStepPickListener;
import com.hamsa.twosteppickerdialog.TwoStepPickerDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;


import android.os.Handler;
import android.os.Message;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    public boolean ck_light = false;
    public boolean ck_misk = false;
    public ImageButton light , misk , phButton;

    Button button;

    int hour_on ;
    int min_on ;
    int hour_off ;
    int min_off ;

    String phStart , phStop;

    int eve_time = 30;

    TextView time_light1,time_misk,phSet;

    public  static TextView temp_fetch,hum_fetch,ph_fetch;

    public TextView action_settings,back;


    String hourString ;
    String minuteString ;
    String hourStringEnd ;
    String minuteStringEnd ;
    String time ;

    private Microgear microgear = new Microgear(this);
    private String appid = "Aeroponics"; //APP_ID
    private String key = "UNjM7YDlpbaqrhC"; //KEY
    private String secret = "Rf1hyCtXeVhRTaoAXvKtcALSb"; //SECRET
    private String alias = "AeroGear";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            TextView myTextView =
                    (TextView)findViewById(R.id.ph);
            myTextView.append(string+"\n");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Aeroponics");
        setSupportActionBar(toolbar);
        OnClickHourOn();






        temp_fetch = (TextView) findViewById(R.id.temp);
        hum_fetch = (TextView) findViewById(R.id.hum);
        ph_fetch = (TextView) findViewById(R.id.ph);




        Thread t=new Thread(){


            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(4000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                FetchData process = new FetchData();
                                process.execute();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start();



//        TextView action_settings = (TextView)findViewById(R.id.action_settings);
//        action_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        TextView action_settings = (TextView) findViewById(R.id.action_settings);
        switch (item.getItemId()) {
            case R.id.action_settings :
//                onBackPressed();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
//                break;
        }
        return true;
    }

    //---------------------------------------------------------------------

    public void OnClickHourOn() {

        time_light1 = (TextView) findViewById(R.id.time_light1);
        time_light1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog tpd = TimePickerDialog.newInstance(MainActivity.this, hour_on, min_on, false, hour_off, min_off);
                        tpd.show(getFragmentManager(), "Timepickerdialog");


                    }
                }
        );

        time_misk = (TextView) findViewById(R.id.time_misk);

        time_misk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPickerDialog dialog = new NumberPickerDialog(MainActivity.this, 30, 60, new NumberPickerDialog.NumberPickerCallBack() {
                            @Override
                            public void onSelectingValue(int value) {
                                eve_time = value;
                                time_misk.setText(eve_time + "  MIN");
//                                Toast.makeText(MainActivity.this, "Selected "+String.valueOf(value), Toast.LENGTH_SHORT).show();

                            }
                        });
                        dialog.show();
                    }
                }
        );

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SentTime.lightTime = hourString+minuteString+hourStringEnd+minuteStringEnd+eve_time+phStart+phStop;
                SentMisk.miskTime = Integer.toString(eve_time);
                SentTime processLight = new SentTime();
                processLight.execute();
                SentMisk.miskTime = Integer.toString(eve_time);
                SentMisk processMisk = new SentMisk();
                processMisk.execute();

                Toast.makeText(MainActivity.this, "Successes", Toast.LENGTH_LONG).show();

            }
        });



    //-------------------------------------------------------------------------------------------------------------------------
        DecimalFormat df1 = new DecimalFormat(".#");
        final List<String> baseData = new ArrayList<>();
        for (double i = 1.0 ; i < 14.0 ; i = i+0.1){

            baseData.add(String.valueOf(df1.format(i)));
        }

        final TwoStepPickerDialog pickThing = new TwoStepPickerDialog
                .Builder(this)
                .withBaseData(baseData)
                .withOkButton("OK")
                .withCancelButton("Cancel")
                .withBaseOnLeft(true) // if you want it RTL like, set it to false
                .withInitialBaseSelected(2)
                .withInitialStepSelected(1)
                .withOnStepDataRequested(new OnStepDataRequestedListener() {
                    @Override
                    public List<String> onStepDataRequest(int baseDataPos) {
                        return baseData;
                    }
                })
                .withDialogListener(new OnStepPickListener() {
                    @Override
                    public void onStepPicked(int step, int pos) {
                        Toast.makeText(MainActivity.this, baseData.get(step) + " - " + pos, Toast.LENGTH_SHORT).show();
                        phSet.setText(baseData.get(step) + " - " + baseData.get(pos));
                        phStart = baseData.get(step);
                        phStop = baseData.get(pos);
                    }

                    @Override
                    public void onDismissed() {
                        Toast.makeText(MainActivity.this, "Dismised!", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        phButton = (ImageButton) findViewById(R.id.phButton);
        phSet = (TextView) findViewById(R.id.phSet);
        phButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickThing.show();

            }
        });

    }
    //-------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        hour_on = hourOfDay;
        min_on = minute;
        hour_off  = hourOfDayEnd;
        min_off  = minuteEnd;
        hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        minuteString = minute < 10 ? "0"+minute : ""+minute;
        hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        time = hourString+":"+minuteString+" - "+hourStringEnd+":"+minuteStringEnd;

        time_light1 = (TextView) findViewById(R.id.time_light1);
        time_light1.setText(time);
//        Toast.makeText(MainActivity.this,time,Toast.LENGTH_LONG).show();
    }

    //------------------------------------------------------------------------------------------------------------------------




    public void OnClickLight(View v) {
        light = (ImageButton) findViewById(R.id.light_bt);

        if (ck_light == false){
            light.setImageResource(R.drawable.light_on);
            ck_light = true;
            SentOnLight process = new SentOnLight();
            process.execute();
        }else if (ck_light == true){
            light.setImageResource(R.drawable.light_off);
            ck_light = false;
            SentOffLight process = new SentOffLight();
            process.execute();
        }

    }
    public void OnClicMisk(View v) {
        misk = (ImageButton) findViewById(R.id.misk_bt);

        if (ck_misk == false){
            misk.setImageResource(R.drawable.misk_on);
            ck_misk = true;
            SentOnMisk process = new SentOnMisk();
            process.execute();
        }else if (ck_misk == true){
            misk.setImageResource(R.drawable.misk_off);
            ck_misk = false;
            SentOffMisk process = new SentOffMisk();
            process.execute();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //-------------------------------------------------------------------------


    public List<String> getRandomList(String perFix) {
        List<String> randomData = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i < r.nextInt(100); i++) {
            randomData.add(perFix + i);
        }
        return randomData;
    }
}



