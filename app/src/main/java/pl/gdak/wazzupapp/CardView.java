package pl.gdak.wazzupapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class CardView extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_card_view);


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
