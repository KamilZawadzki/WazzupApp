package pl.gdak.wazzupapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WholeContent extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SQLiteDatabaseHandler db;
    private TextView bannerText;
    private ImageView bannerImage;
    private static boolean showFav = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_whole_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        //zainicjuj baze
        db = new SQLiteDatabaseHandler(this);
        bannerText = findViewById(R.id.bannerTxt);
        bannerImage = findViewById(R.id.bannerImg);
        getExtras();


        Button changeSourceButtons = findViewById(R.id.changeSourceButtons);
        changeSourceButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFav = !showFav;
                startActivity(new Intent(getApplicationContext(),WholeContent.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        //ZAINICJUJ MEDIA PLAYER
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oh_shit);
        generateSoundPlayingButtonsFromRawDirectoryFiles();

        ImageButton likeGdakBtn = findViewById(R.id.likeGDAK_img);
        likeGdakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/244418192888243"));
                startActivity(browserIntent);
            }
        });
        ImageButton likeWazzup = findViewById(R.id.likeWazzup_img);
        likeWazzup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/804213503040461"));
                startActivity(browserIntent);
            }
        });

        ImageButton subWazzup = findViewById(R.id.subWazzup_img);
        subWazzup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/channel/UC1Fv5ATgw8RyX4Cj0xGo1WQ?sub_confirmation=1"));
                startActivity(browserIntent);
            }
        });
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        String extrasText = (extras == null) ? getString(R.string.app_name) :  extras.getString("buttonid");
        if(!extrasText.equals(getString(R.string.app_name))){
            switch (extrasText){
                case "GIENEK":
                    bannerImage.setBackgroundResource(R.drawable.gienek);
                    break;
            }
        }
        bannerText.setText(extrasText);
    }

    public void generateSoundPlayingButtonsFromRawDirectoryFiles() {
        List<Track> tracks;
        if(!showFav){
            Field[] fields = R.raw.class.getFields();
             tracks = convertFieldsToList(fields);
        }
        else{
            tracks = db.allTracks();
        }
        for (Track track : tracks) {
            String btn_name = track.getName();
            //Log.i("Raw Asset: ", fields[count].getName());
            ImageButton addToFavoritesButton = new ImageButton(this);
            if(db.isTrackFav(btn_name)){
                addToFavoritesButton.setImageResource(R.drawable.ic_favourite_full);
            }else
            {
                addToFavoritesButton.setImageResource(R.drawable.ic_favourite);
            }

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            params2.setMargins(0, 80, 0, 0);
            addToFavoritesButton.setLayoutParams(params2);
            addToFavoritesButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addToFavoritesButton.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }

            //PlaySound Button
            Typeface font = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                font = getResources().getFont(R.font.vacerseriffatpersonal);
            }
            final Button soundBtn = new Button(this);
            soundBtn.setText(btn_name.replaceAll("_", " "));
            soundBtn.setTypeface(font);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                soundBtn.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(40, 80, 40, 0);
            soundBtn.setLayoutParams(params);



            addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imageButton = (ImageButton) v;
                    Drawable drawable = imageButton.getDrawable();
                    String name = soundBtn.getText().toString().replaceAll(" ", "_");
                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_favourite).getConstantState())){
                        Track newFavouriteTrack = new Track();
                        newFavouriteTrack.setName(name);
                        db.addTrack(newFavouriteTrack);
                        Toast.makeText(getApplicationContext(),"Fav added",Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.ic_favourite_full);
                    }else
                    {
                        db.deleteOne(name);
                        imageButton.setImageResource(R.drawable.ic_favourite);
                    }
                }
            });


            soundBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    String buttonText = b.getText().toString();
                    buttonText = buttonText.replaceAll(" ", "_").toLowerCase();
                    int resID = getResources().getIdentifier(buttonText, "raw", getPackageName());
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
                    mediaPlayer.start();
                }
            });

            ImageButton shareButton = new ImageButton(this);
            shareButton.setImageResource(R.drawable.ic_share_mac);

            //LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 4);
            //params2.setMargins(0, 80, 60, 0);
            shareButton.setLayoutParams(params2);
            shareButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                shareButton.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) { //brak uprawnień dostępu do plików
                        Toast.makeText(getApplicationContext(), "Aby móc udostępniać pliki zmień uprawnienia aplikacji: Ustawienia -> Aplikacje -> WazzupApp -> Zezwolenia", Toast.LENGTH_LONG).show();
                    } else { //udostępnij plik
                        int resID = getResources().getIdentifier(soundBtn.getText().toString().replaceAll(" ", "_"), "raw", getPackageName());
                        InputStream inputStream;
                        FileOutputStream fileOutputStream;
                        try {
                            inputStream = getResources().openRawResource(resID);
                            fileOutputStream = new FileOutputStream(
                                    new File(Environment.getExternalStorageDirectory(), "SoundFromWazzupApp.mp3"));

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, length);
                            }

                            inputStream.close();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Log.i("Raw Asset: ",Environment.getExternalStorageDirectory()+"/sound.mp3".toString());
                        Intent shareAudioIntent = new Intent(Intent.ACTION_SEND);
                        shareAudioIntent.setType("audio/mp3");
                        shareAudioIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareExtraText));
                        shareAudioIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareExtraSubject));
                        shareAudioIntent.putExtra(Intent.EXTRA_STREAM,
                                Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/SoundFromWazzupApp.mp3"));
                        startActivity(Intent.createChooser(shareAudioIntent, getString(R.string.shareChooserText)));
                    }
                }
            });

            LinearLayout rootButtonsLayout = findViewById(R.id.soundbar_rootLinearLayout);

            LinearLayout eachHorizontalLayout = new LinearLayout(this, null, R.style.layout_horizontal);
            eachHorizontalLayout.addView(addToFavoritesButton);
            eachHorizontalLayout.addView(soundBtn);
            eachHorizontalLayout.addView(shareButton);

            rootButtonsLayout.addView(eachHorizontalLayout, 0);
        }
    }

    private List<Track> convertFieldsToList(Field[] fields) {
        List<Track> tracks = new ArrayList<>();
        for(Field field : fields){
            Track track = new Track();
            track.setName(field.getName());
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oh_shit);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }
}
