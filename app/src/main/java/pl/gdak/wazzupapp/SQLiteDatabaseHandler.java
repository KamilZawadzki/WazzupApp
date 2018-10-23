package pl.gdak.wazzupapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WazzApek";
    private static final String TABLE_NAME = "Favourites";
    private static final String KEY_NAME = "name";
    private static final String[] COLUMNS = {KEY_NAME};

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "+ "name TEXT PRIMARY KEY)";
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(String track) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name = ?", new String[]{track});
        db.close();
    }

 /*   public Track getTrack(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Track track = new Track();
        track.setName(cursor.getString(0));
        return track;
    }*/

    public List<Track> allTracks() {

        List<Track> tracks = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Track track;

        if (cursor.moveToFirst()) {
            do {
                track = new Track();
                track.setName(cursor.getString(0));
                tracks.add(track);
            } while (cursor.moveToNext());
        }

        return tracks;
    }

    public void addTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, track.getName());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateTrack(Track player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "name = ?", // selections
                new String[]{String.valueOf(player.getName())});

        db.close();

        return i;
    }
    public boolean isTrackFav(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " name = ?", // c. selections
                new String[]{name}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor.moveToFirst();
    }

    public String getFavouriteTrackName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT name FROM "+TABLE_NAME+" WHERE name LIKE \'%"+name+"\'";
        Cursor cursor = db.rawQuery(query, null);

        return (cursor.moveToFirst()) ? cursor.getString(0) : null;
    }
}