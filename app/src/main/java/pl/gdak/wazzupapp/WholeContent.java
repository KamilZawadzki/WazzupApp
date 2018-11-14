package pl.gdak.wazzupapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WholeContent extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SQLiteDatabaseHandler db;
    private TextView bannerText;
    private ImageView bannerImage;
    private static boolean showFav = false;

    private Map<String, String> mapForRawFiles;
    private List<Sounds> allOfSongs;

    private final int STORAGE_PERMISSION_CODE = 1;

    Dialog setRingtone;
    private Boolean choose;

    private enum fileNames {
            RINGTONE("WazzAppRingtone.mp3"), NOTIFICATION("WazzAppNotification.mp3"), SOUND("SoundFromWazzApp.mp3");

        private final String name;
        fileNames(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_whole_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setRingtone = new Dialog(this);
        allOfSongs = new ArrayList<>();
        mapForRawFiles = new HashMap<>();
        mapForRawFiles.put("ŁYSY", "lysy");
        mapForRawFiles.put("WazzApp", "other");
        mapForRawFiles.put("GIENEK", "gienek");
        mapForRawFiles.put("STASIO", "stasio");
        mapForRawFiles.put("RAFAŁEK", "rafalek");
        mapForRawFiles.put("POLICJANT", "policjant");
        mapForRawFiles.put("ULUBIONE", "");


        //zainicjuj baze
        db = new SQLiteDatabaseHandler(this);
        bannerText = findViewById(R.id.bannerTxt);
        bannerImage = findViewById(R.id.bannerImg);
        getExtras();




       /* changeSourceButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFav = !showFav;
                if(!showFav){
                    Intent goToChooser = new Intent(getApplicationContext(),ChooserActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToChooser);

                }else{
                    Intent goToFavourites = new Intent(getApplicationContext(),WholeContent.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    goToFavourites.putExtra("buttonid","Ulubione");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(goToFavourites,ActivityOptions.makeSceneTransitionAnimation(WholeContent.this).toBundle());
                    }else{
                        startActivity(goToFavourites);
                    }
                }
            }
        });*/

        //ZAINICJUJ MEDIA PLAYER
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.other_tuning_luksusowy);
        generateSoundPlayingButtonsFromRawDirectoryFiles();

        ImageButton likeGdakBtn = findViewById(R.id.likeGDAK_img);
        likeGdakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/244418192888243"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/GeneralnieDobreAplikacje/")));
                }
               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/244418192888243"));
                startActivity(browserIntent);*/
            }
        });
        ImageButton likeWazzup = findViewById(R.id.likeWazzup_img);
        likeWazzup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/804213503040461"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/wazzupek/")));
                }
              /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/804213503040461"));
                startActivity(browserIntent);*/
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
        String extrasText = (extras == null) ? getString(R.string.app_name) : extras.getString("buttonid");
        Boolean isFav = (extras != null) && extras.getBoolean("FAV");
        if (!extrasText.equals(getString(R.string.app_name))) {
            switch (extrasText) {
                case "GIENEK":
                    bannerImage.setBackgroundResource(R.drawable.gieniu_anim_1);
                    break;
                case "POLICJANT":
                    bannerImage.setBackgroundResource(R.drawable.policjant_anim_1);
                    break;
                case "STASIO":
                    bannerImage.setBackgroundResource(R.drawable.stasio_anim_1);
                    break;
                case "RAFAŁEK":
                    bannerImage.setBackgroundResource(R.drawable.rafalek_anim_1);
                    break;
                case "ŁYSY":
                    bannerImage.setBackgroundResource(R.drawable.lysy_anim_1);
                    break;
            }
        }
        bannerText.setText(extrasText);
        if (isFav)
            showFav = isFav;
    }

    private void generateSoundPlayingButtonsFromRawDirectoryFiles() {
        List<Track> tracks;
        if (!showFav) {
            Field[] fields = R.raw.class.getFields();
            tracks = convertFieldsToList(fields);
        } else {
            tracks = db.allTracks();
        }
        tracks = filterTracks(tracks);
        for (Track track : tracks) {
            String btn_name = track.getName();
            Log.i("BTN_NAME: ", btn_name);
            ImageButton addToFavoritesButton = new ImageButton(this);
            if (bannerText.getText().equals("Ulubione")) {
                if (db.getFavouriteTrackName("_" + btn_name) != null) {
                    addToFavoritesButton.setImageResource(R.drawable.ic_favorite_full_black);
                } else {
                    addToFavoritesButton.setImageResource(R.drawable.ic_favorite_black);
                }
            } else {
                if (db.isTrackFav(mapForRawFiles.get(bannerText.getText()) + "_" + btn_name)) {
                    addToFavoritesButton.setImageResource(R.drawable.ic_favorite_full_black);
                } else {
                    addToFavoritesButton.setImageResource(R.drawable.ic_favorite_black);
                }
            }

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3);
            params2.setMargins(0, 80, 0, 0);
            addToFavoritesButton.setLayoutParams(params2);
            addToFavoritesButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addToFavoritesButton.setBackground(getResources().getDrawable(R.drawable.button_shadow));
            }

            //PlaySound Button
            Typeface font = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                font = getResources().getFont(R.font.opensanssemibold);
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
                    String name;
                    if (bannerText.getText().equals("Ulubione")) {
                        name = db.getFavouriteTrackName("_" + soundBtn.getText().toString().replaceAll(" ", "_"));
                    } else {
                        name = mapForRawFiles.get(bannerText.getText()) + "_" + soundBtn.getText().toString().replaceAll(" ", "_");
                    }
                    //drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_favourite).getConstantState()
                    if (!db.isTrackFav(name)) {
                        Track newFavouriteTrack = new Track();

                        newFavouriteTrack.setName(name);
                        Log.i("BTN_NAME_SAVE: ", name);
                        db.addTrack(newFavouriteTrack);
                        imageButton.setImageResource(R.drawable.ic_favorite_full_black);
                    } else {

                        imageButton.setImageResource(R.drawable.ic_favorite_black);
                        if (bannerText.getText().equals("Ulubione")) {
                            for (Sounds hideThisElement : allOfSongs) {
                                if (hideThisElement.getAddToFavBtn() == imageButton) {
                                    final LinearLayout hideThisLayout = hideThisElement.getEachHorizontal();
                                    int count = hideThisLayout.getChildCount();
                                    View elemOfLayout = null;
                                    for (int i = 0; i < count; i++) {
                                        elemOfLayout = hideThisLayout.getChildAt(i);
                                        elemOfLayout.setEnabled(false);
                                    }
                                    hideThisLayout.animate()
                                            .translationY(hideThisLayout.getHeight())
                                            .alpha(0.0f)
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    hideThisLayout.setVisibility(View.GONE);
                                                }
                                            });
                                    break;
                                }

                            }
                        }
                        db.deleteOne(name);
                    }
                }
            });


            soundBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Button b = (Button) v;
                        String buttonText = b.getText().toString();
                        if (bannerText.getText().equals("Ulubione")) {
                            buttonText = db.getFavouriteTrackName("_" + buttonText.replaceAll(" ", "_").toLowerCase());
                        } else {
                            buttonText = mapForRawFiles.get(bannerText.getText()) + "_" + buttonText.replaceAll(" ", "_").toLowerCase();
                        }
                        int resID = getResources().getIdentifier(buttonText, "raw", getPackageName());
                        if(mediaPlayer.isPlaying())
                            mediaPlayer.reset();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
                        mediaPlayer.start();
                    } else {
                        requestStoragePermission();
                    }
                }
            });
            soundBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Check whether has the write settings permission or not.
                    boolean settingsCanWrite = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        settingsCanWrite = Settings.System.canWrite(getApplicationContext());
                    }

                    if (!settingsCanWrite) {
                        // If do not have write settings permission then open the Can modify system settings panel.

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            Toast.makeText(getApplicationContext(), "Nadaj aplikacji WazzApp uprawnienia dostępu do ustawień systemowych aby móc skonfigurować nowy dzwonek.", Toast.LENGTH_LONG).show();
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Wymaga zezwolenia do ustawień systemowych", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // If has permission then set
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            ShowPopup(v, soundBtn);

                        } else {
                            requestStoragePermission();
                        }
                    }

                    return true;
                }
            });


            ImageButton shareButton = new ImageButton(this);
            shareButton.setImageResource(R.drawable.messenger);

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
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        exportRawToFile(soundBtn,fileNames.SOUND.name);
                        //Log.i("Raw Asset: ",Environment.getExternalStorageDirectory()+"/sound.mp3".toString());
                        Intent shareAudioIntent = new Intent(Intent.ACTION_SEND);
                        shareAudioIntent.setType("audio/mp3");
                        shareAudioIntent.putExtra(Intent.EXTRA_STREAM,
                                Uri.parse("file://" + Environment.getExternalStorageDirectory()+"/"+getPackageName() + "/"+fileNames.SOUND.name));
                        shareAudioIntent.setPackage("com.facebook.orca");
                        try {
                            startActivity(Intent.createChooser(shareAudioIntent, getString(R.string.shareChooserText)));
                        } catch (Exception x) {
                            Toast.makeText(getApplicationContext(), "ERROR: błąd komunikacji z messenger", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        requestStoragePermission();
                    }
               /*     if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) { //brak uprawnień dostępu do plików
                        Toast.makeText(getApplicationContext(), "Aby móc udostępniać pliki zmień uprawnienia aplikacji: Ustawienia -> Aplikacje -> WazzupApp -> Zezwolenia", Toast.LENGTH_LONG).show();
                    } else { //udostępnij plik

                    }*/
                }
            });

            LinearLayout rootButtonsLayout = findViewById(R.id.soundbar_rootLinearLayout);

            LinearLayout eachHorizontalLayout = new LinearLayout(this, null, R.style.layout_horizontal);
            eachHorizontalLayout.addView(addToFavoritesButton);
            eachHorizontalLayout.addView(soundBtn);
            eachHorizontalLayout.addView(shareButton);
            Sounds newObject = new Sounds(eachHorizontalLayout, addToFavoritesButton);
            allOfSongs.add(newObject);

            rootButtonsLayout.addView(eachHorizontalLayout, 0);
        }
    }

    private void exportRawToFile(Button soundBtn, String fileName) {
        File directory = new File(Environment.getExternalStorageDirectory(),getPackageName());
        if (! directory.exists()){
            directory.mkdir();
        }
        String name;
        if (bannerText.getText().equals("Ulubione")) {
            name = db.getFavouriteTrackName("_" + soundBtn.getText().toString().replaceAll(" ", "_"));
        } else {
            name = mapForRawFiles.get(bannerText.getText()) + "_" + soundBtn.getText().toString().replaceAll(" ", "_");
        }
        int resID = getResources().getIdentifier(name, "raw", getPackageName());
        //nie wiem czemu ale naprawia bug na emulatorze \/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //nie wiem czemu ale naprawia bug na emulatorze /\
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        try {
            inputStream = getResources().openRawResource(resID);
            fileOutputStream = new FileOutputStream(
                    new File(Environment.getExternalStorageDirectory()+"/"+getPackageName(), fileName));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Wystąpił błąd " + e, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private List<Track> filterTracks(List<Track> alltracks) {
        List<Track> filteredTracks = new ArrayList<>();
        for (Track track : alltracks) {
            String splitArray[] = track.getName().split("_");
            if (splitArray[0].equals(mapForRawFiles.get(bannerText.getText())) || bannerText.getText().equals("Ulubione")) {
                StringBuilder filteredName = new StringBuilder();
                for (int i = 1; i < splitArray.length; i++) {
                    filteredName.append(splitArray[i]).append("_");
                }
                filteredName = new StringBuilder(filteredName.substring(0, filteredName.length() - 1));
                track.setName(filteredName.toString());
                filteredTracks.add(track);
            }
        }
        return filteredTracks;
    }

    private List<Track> convertFieldsToList(Field[] fields) {
        List<Track> tracks = new ArrayList<>();
        for (Field field : fields) {
            Track track = new Track();
            track.setName(field.getName());
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.other_gdzie_trytytki);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (bannerText.getText().equals("Ulubione")) {
            showFav = false;
        }
        super.onBackPressed();
    }

    public void ShowPopup(View v, final Button sound) {
        choose = null;
        final TextView mainTextBold;
        final TextView firstChoose;
        final TextView secondChoose;
        TextView txtClose;
        Button btnSet;
        setRingtone.setContentView(R.layout.custompopup);
        txtClose = setRingtone.findViewById(R.id.txtclose);
        mainTextBold = setRingtone.findViewById(R.id.customPopupTVbold);
        mainTextBold.setGravity(Gravity.CENTER);
        mainTextBold.setText("");
        firstChoose = setRingtone.findViewById(R.id.firstChoose);
        secondChoose = setRingtone.findViewById(R.id.secondChoose);
        final LinearLayout firstL = setRingtone.findViewById(R.id.firstLayout);
        final LinearLayout secondL = setRingtone.findViewById(R.id.secondLayout);
        firstChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose = true;
                mainTextBold.setText("Wybrano: \"Dźwięk połączenia\"");
                firstL.setBackgroundColor(getResources().getColor(R.color.bluelight));
                firstChoose.setTextColor(getResources().getColor(R.color.black));
                secondChoose.setTextColor(getResources().getColor(R.color.white));
                secondL.setBackgroundColor(getResources().getColor(R.color.darkerbluedark));
            }
        });
        secondChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose = false;
                mainTextBold.setText("Wybrano: \"Dźwięk powiadomień\"");
                secondL.setBackgroundColor(getResources().getColor(R.color.bluelight));
                secondChoose.setTextColor(getResources().getColor(R.color.black));
                firstChoose.setTextColor(getResources().getColor(R.color.white));
                firstL.setBackgroundColor(getResources().getColor(R.color.darkerbluedark));
            }
        });
        btnSet = setRingtone.findViewById(R.id.btnSet);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRingtone.dismiss();
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choose != null) {
                    String fileName;


                    if(choose) {
                       fileName = fileNames.RINGTONE.name;
                    }else {
                        fileName = fileNames.NOTIFICATION.name;
                    }
                    File ringFile =  new File(Environment.getExternalStorageDirectory()+"/"+getPackageName(), fileName);
                    exportRawToFile(sound,fileName);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DATA, ringFile.getAbsolutePath());
                    values.put(MediaStore.MediaColumns.TITLE, "MadeWithWazzApp");
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
                    values.put(MediaStore.Audio.Media.ARTIST, "Wazzup");
                    if (choose) {
                        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    } else {
                        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                    }
                    values.put(MediaStore.Audio.Media.IS_ALARM, false);
                    values.put(MediaStore.Audio.Media.IS_MUSIC, false);

                    Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringFile.getAbsolutePath());
                    getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ringFile.getAbsolutePath() + "\"", null);
                    Uri newUri = getContentResolver().insert(uri, values);

                    try {
                        if (choose) {
                            RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
                        } else {
                            RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION, newUri);
                        }
                        if (choose) {
                            mainTextBold.setText("Zmieniono \"Dźwięk połączenia\"");
                        } else {
                            mainTextBold.setText("Zmieniono \"Dźwięk powiadomień\"");
                        }

                    } catch (Throwable t) {
                        mainTextBold.setText("Dzwonek nie został zmieniony");
                    }
                } else {
                    mainTextBold.setText("Wybierz rodzaj dzwonka, który chcesz zmienić.");
                }
                choose = null;
                firstL.setBackgroundColor(getResources().getColor(R.color.darkerbluedark));
                secondL.setBackgroundColor(getResources().getColor(R.color.darkerbluedark));
                firstChoose.setTextColor(getResources().getColor(R.color.white));
                secondChoose.setTextColor(getResources().getColor(R.color.white));
            }
        });

        if (setRingtone.getWindow() != null) {
            setRingtone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setRingtone.show();
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Wymagane zezwolenie")
                    .setMessage("Zezwolenie dostępu do plików jest wymagane do poprawnego funkcjonowania aplikacji..")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(WholeContent.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Nadano uprawnienia.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nie uzyskano dostępu.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
