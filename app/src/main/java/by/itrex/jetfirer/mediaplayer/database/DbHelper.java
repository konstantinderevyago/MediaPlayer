package by.itrex.jetfirer.mediaplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static by.itrex.jetfirer.mediaplayer.database.TableConstants.CREATE_PLAYLIST_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.CREATE_PLAYLIST_TRACk_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.CREATE_TRACK_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DROP_PLAYLIST_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DROP_PLAYLIST_TRACK_TABLE;
import static by.itrex.jetfirer.mediaplayer.database.TableConstants.DROP_TRACK_TABLE;

/**
 * Created by Konstantin on 17.12.2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MediaPlayer";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRACK_TABLE);
        db.execSQL(CREATE_PLAYLIST_TABLE);
        db.execSQL(CREATE_PLAYLIST_TRACk_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TRACK_TABLE);
        db.execSQL(DROP_PLAYLIST_TABLE);
        db.execSQL(DROP_PLAYLIST_TRACK_TABLE);
        onCreate(db);
    }
}
