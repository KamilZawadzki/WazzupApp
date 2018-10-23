package pl.gdak.wazzupapp;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

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

      /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }*/


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

        CardView favourites = findViewById(R.id.favouritesCV);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFavourites = new Intent(getApplicationContext(),WholeContent.class);
                goToFavourites.putExtra("buttonid","Ulubione");
                goToFavourites.putExtra("FAV",true);
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(goToFavourites,ActivityOptions.makeSceneTransitionAnimation(ChooserActivity.this).toBundle());
                }else{*/
                    startActivity(goToFavourites);
                //}
            }
        });

    }

}
