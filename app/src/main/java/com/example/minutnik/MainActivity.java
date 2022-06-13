package com.example.minutnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView TimeView, SoftText, MediumText, HardText;
    private Button StartButton;
    private SeekBar TimeBar;
    private ImageView EggImage;
    private boolean isTimeGoing = false;
    private CountDownTimer Timer;
    private Uri notification;
    private Ringtone ringtone;
    private Vibrator vibrator;
    long[] pattern = {0, 1000, 1000};
    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);

        TimeView = findViewById(R.id.textViewTime);
        TimeView.setText(parseTime(time));

        SoftText = findViewById(R.id.textViewSoft);
        MediumText = findViewById(R.id.textViewMedium);
        HardText = findViewById(R.id.textViewHard);

        StartButton = findViewById(R.id.buttonStart);

        EggImage = findViewById(R.id.imageViewEgg);
        EggImage.setImageResource(R.drawable.smiling_egg_vertical_2);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        TimeBar = findViewById(R.id.seekBarTime);
        TimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                time = i * 1000;
                TimeView.setText(parseTime(time));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                SoftText.setTextColor(Color.parseColor("#000000"));
                MediumText.setTextColor(Color.parseColor("#000000"));
                HardText.setTextColor(Color.parseColor("#000000"));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setTimeOnClick(View view){
        int id = view.getId();
        if(id == R.id.textViewSoft){
            SoftText.setTextColor(Color.parseColor("#FF9800"));
            MediumText.setTextColor(Color.parseColor("#000000"));
            HardText.setTextColor(Color.parseColor("#000000"));
            TimeBar.setProgress(180);
        }else if(id == R.id.textViewMedium){
            SoftText.setTextColor(Color.parseColor("#000000"));
            MediumText.setTextColor(Color.parseColor("#FF9800"));
            HardText.setTextColor(Color.parseColor("#000000"));
            TimeBar.setProgress(240);
        }else if(id == R.id.textViewHard){
            SoftText.setTextColor(Color.parseColor("#000000"));
            MediumText.setTextColor(Color.parseColor("#000000"));
            HardText.setTextColor(Color.parseColor("#FF9800"));
            TimeBar.setProgress(300);
        }
    }

    public void eggImageOnClick(View view){
        ringtone.stop();
        vibrator.cancel();
    }

    public String parseTime(int miliSeconds){
        String parsedTime = "00:00";
        int minutes = miliSeconds / 1000 / 60;
        int seconds = (miliSeconds / 1000) % 60;
        //int mili = miliSeconds % 10;

        //parsedTime = String.valueOf(minutes) + ":" + String.valueOf(seconds) + ":" + String.valueOf(mili);
        if(minutes < 10){
            parsedTime = "0" + String.valueOf(minutes);
        }else{
            parsedTime = String.valueOf(minutes);
        }

        parsedTime += ":";

        if(seconds < 10){
            parsedTime += "0" + String.valueOf(seconds);
        }else{
            parsedTime += String.valueOf(seconds);
        }
        //parsedTime = String.valueOf(minutes) + ":" + String.valueOf(seconds);

        return  parsedTime;
    }

    public void buttonStartOnClick(View view){
        if(isTimeGoing){
            StartButton.setText("Start ▶");
            isTimeGoing = false;
            Timer.cancel();
        }else{
            StartButton.setText("Stop ■");
            isTimeGoing = true;
            Timer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long l) {
                    //TimeView.setText("seconds remaining: " + l / 1000);
                    time = (int) l;
                    //TimeView.setText(parseTime((int)l));
                    TimeView.setText(parseTime(time));
                    TimeBar.setProgress((time/1000));
                    if((time/1000) % 2 == 0){
                        EggImage.setImageResource(R.drawable.smiling_egg_vertical_2);
                    }else{
                        EggImage.setImageResource(R.drawable.smiling_egg_tilted_right);
                    }
                }

                @Override
                public void onFinish() {
                    TimeView.setText("00:00");
                    StartButton.setText("Start ▶");
                    isTimeGoing = false;
                    ringtone.play();
                    vibrator.vibrate(pattern, 0);
                }
            };
            Timer.start();
        }
    }



}