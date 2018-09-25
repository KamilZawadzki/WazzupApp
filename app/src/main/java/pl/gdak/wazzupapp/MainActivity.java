package pl.gdak.wazzupapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ZAINICJUJ MEDIA PLAYER
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oh_shit);
        generateSoundPlayingButtonsFromRawDirectoryFiles();

        ImageButton likeGdakBtn = findViewById(R.id.likeGDAK_img);
        likeGdakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("fb://page/244418192888243"));
                startActivity(browserIntent);
            }
        });
        ImageButton likeWazzup = findViewById(R.id.likeWazzup_img);
        likeWazzup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("fb://page/804213503040461"));
                startActivity(browserIntent);
            }
        });

        ImageButton subWazzup = findViewById(R.id.subWazzup_img);
        subWazzup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/channel/UC1Fv5ATgw8RyX4Cj0xGo1WQ?sub_confirmation=1"));
                startActivity(browserIntent);
            }
        });
        }


    public void generateSoundPlayingButtonsFromRawDirectoryFiles() {
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            //Log.i("Raw Asset: ", fields[count].getName());

            //PlaySound Button
            String btn_name = field.getName().replaceAll("_", " ");
            final Button soundBtn = new Button(this);
            soundBtn.setText(btn_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                soundBtn.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            params.setMargins(40, 80, 40, 0);
            soundBtn.setLayoutParams(params);


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
            shareButton.setImageResource(R.drawable.ic_share);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(600,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            params2.setMargins(40, 80, 40, 0);
            shareButton.setLayoutParams(params2);
            shareButton.setScaleType(ImageView.ScaleType.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                shareButton.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resID = getResources().getIdentifier(soundBtn.getText().toString().replaceAll(" ", "_"), "raw", getPackageName());
                    InputStream inputStream;
                    FileOutputStream fileOutputStream;
                    try {
                        inputStream = getResources().openRawResource(resID);
                        fileOutputStream = new FileOutputStream(
                                new File(Environment.getExternalStorageDirectory(), "soundx.mp3"));

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
                            Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/soundx.mp3"));
                    startActivity(Intent.createChooser(shareAudioIntent, getString(R.string.shareChooserText)));
                }
            });

            LinearLayout rootButtonsLayout =  findViewById(R.id.soundbar_rootLinearLayout);

            LinearLayout eachHorizontalLayout = new LinearLayout(this,null, R.style.layout_horizontal);
            eachHorizontalLayout.addView(soundBtn);
            eachHorizontalLayout.addView(shareButton);

            rootButtonsLayout.addView(eachHorizontalLayout,0);
        }
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
