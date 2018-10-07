package pl.gdak.wazzupapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Objects;

public class ChooserActivity extends AppCompatActivity  {
    ImageButton StasioBtn,RafalekBtn,GeniuBtn,LysyBtn,PolicjantBtn,WholeContentBtn;

    private View.OnClickListener awesomeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton ib = (ImageButton) v;
            Intent goToSounds = new Intent(getApplicationContext(),WholeContent.class);
            goToSounds.putExtra("buttonid",String.valueOf(ib.getContentDescription()));
            startActivity(goToSounds);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chooser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }

        StasioBtn = findViewById(R.id.imgBtnStasio);
        RafalekBtn = findViewById(R.id.imgBtnRafalek);
        LysyBtn = findViewById(R.id.imgBtnLysy);
        PolicjantBtn = findViewById(R.id.imgBtnPolicjant);
        WholeContentBtn = findViewById(R.id.imgBtnWholeContent);
        GeniuBtn = findViewById(R.id.imgBtnGeniu);

        StasioBtn.setOnClickListener(awesomeOnClickListener);
        RafalekBtn.setOnClickListener(awesomeOnClickListener);
        LysyBtn.setOnClickListener(awesomeOnClickListener);
        PolicjantBtn.setOnClickListener(awesomeOnClickListener);
        WholeContentBtn.setOnClickListener(awesomeOnClickListener);
        GeniuBtn.setOnClickListener(awesomeOnClickListener);

    }

}
