package pl.gdak.wazzupapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }

        loading_app();
    }
    private void loading_app() {
        new CountDownTimer(2000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
//                progress = progress + 1;
//                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {


                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }.start();
    }



}
