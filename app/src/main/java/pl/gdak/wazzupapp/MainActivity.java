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
    }

    public void generateSoundPlayingButtonsFromRawDirectoryFiles() {
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            //Log.i("Raw Asset: ", fields[count].getName());

            //PlaySound Button
            String btn_name = field.getName().replaceAll("_", " ");
            Button btn = new Button(this);
            btn.setText(btn_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            params.setMargins(20, 80, 20, 0);
            btn.setLayoutParams(params);


            btn.setOnClickListener(new View.OnClickListener() {
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

            //Share Button
            Button shareButton = new Button(getApplicationContext());
            //shareButton.setText("Share");
            shareButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share,0,0,0);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            params2.setMargins(40, 80, 40, 0);
            shareButton.setLayoutParams(params2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                shareButton.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputStream inputStream;
                    FileOutputStream fileOutputStream;
                    try {
                        inputStream = getResources().openRawResource(R.raw.oh_shit);
                        fileOutputStream = new FileOutputStream(
                                new File(Environment.getExternalStorageDirectory(), "sound.mp3"));

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
                    shareAudioIntent.putExtra(Intent.EXTRA_TEXT, "Nie bądź łoś, zainstaluj i cyk fuch!");
                    shareAudioIntent.putExtra(Intent.EXTRA_SUBJECT, "Wysłano z aplikacji WazzupApp");
                    shareAudioIntent.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/sound.mp3"));
                    startActivity(Intent.createChooser(shareAudioIntent, "Udostępnij przez:"));
                }
            });

            LinearLayout rootButtonsLayout =  findViewById(R.id.soundbar_rootLinearLayout);

            LinearLayout eachHorizontalLayout = new LinearLayout(this,null, R.style.layout_horizontal);
            eachHorizontalLayout.addView(btn);
            eachHorizontalLayout.addView(shareButton);

            rootButtonsLayout.addView(eachHorizontalLayout);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oh_shit_biegne);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

}
