package com.electronics.invento.merosaathi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView rotatetyre1, rotatetyre2, translatelandscape, translatelandscape2, translatelandscape3;
    Button startBtn;
    MediaPlayer ourSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FullScreen view
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ourSong = MediaPlayer.create(SplashActivity.this, R.raw.splashsound);
        ourSong.start();

        rotatetyre1 = findViewById(R.id.tyre1);
        rotatetyre2 = findViewById(R.id.tyre2);
        translatelandscape = findViewById(R.id.landscape);
        translatelandscape2 = findViewById(R.id.landscape2);
        translatelandscape3 = findViewById(R.id.landscape3);

        Animation rotate_Tyre1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_tyrea);
        Animation rotate_Tyre2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_tyreb);
        Animation translate_Landscape = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromrightroad);
        Animation translate_Landscape2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frommiddleroad);
        Animation translate_Landscape3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tolastroad);

        rotatetyre1.startAnimation(rotate_Tyre1);
        rotatetyre2.startAnimation(rotate_Tyre2);
        translatelandscape.startAnimation(translate_Landscape);     //this is second
        translatelandscape2.startAnimation(translate_Landscape2);   //this is first
        translatelandscape3.startAnimation(translate_Landscape3);   //this is last

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3500);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } finally {
                    startBtn = findViewById(R.id.start_btn);
                    startBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent openMainActivity = new Intent("com.electronics.invento.merosaathi.MAINACTIVITY");
                            startActivity(openMainActivity);
                        }
                    });
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ourSong.release();
        finish();
    }
}