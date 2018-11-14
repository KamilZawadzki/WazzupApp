package pl.gdak.wazzupapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Objects;

public class ChooserActivity extends AppCompatActivity  {

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


        ImageButton stasioBtn = findViewById(R.id.imgBtnStasio);
        ImageButton rafalekBtn = findViewById(R.id.imgBtnRafalek);
        ImageButton lysyBtn = findViewById(R.id.imgBtnLysy);
        ImageButton policjantBtn = findViewById(R.id.imgBtnPolicjant);
        ImageButton wholeContentBtn = findViewById(R.id.imgBtnWholeContent);
        ImageButton geniuBtn = findViewById(R.id.imgBtnGeniu);

        stasioBtn.setOnClickListener(awesomeOnClickListener);
        rafalekBtn.setOnClickListener(awesomeOnClickListener);
        lysyBtn.setOnClickListener(awesomeOnClickListener);
        policjantBtn.setOnClickListener(awesomeOnClickListener);
        wholeContentBtn.setOnClickListener(awesomeOnClickListener);
        geniuBtn.setOnClickListener(awesomeOnClickListener);

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
