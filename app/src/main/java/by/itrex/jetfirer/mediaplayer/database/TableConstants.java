package by.itrex.jetfirer.mediaplayer.database;

/**
 * Created by Konstantin on 17.12.2014.
 */
public class TableConstants {

    private TableConstants() {}

    public static final String CREATE = "CREATE TABLE ";

    public static final String GEN_ID = " (_id integer primary key autoincrement, ";

    public static final String DROP = "DROP TABLE IF EXISTS ";

    public static final String DELETE_FROM ="DELETE FROM ";

    public static final String TRACK_TABLE = "track";
    public static final String PLAYLIST_TABLE = "playlist";
    public static final String PLAYLIST_TRACK_TABLE = "playlist_track";

    public static final String ID = "(_id)";
    public static final String ARTIST = "artist";
    public static final String TITLE = "title";
    public static final String ALBUM = "album";
    public static final String DATA = "data";
    public static final String DURATION = "duration";
    public static final String NAME = "name";
    public static final String TRACK_ID = "track_id";
    public static final String PLAYLIST_ID = "playlist_id";

    public static final String CREATE_TRACk_TABLE =
            CREATE + TRACK_TABLE
                    + GEN_ID
                    + ARTIST + " TEXT, "
                    + TITLE + " TEXT,"
                    + ALBUM + " TEXT,"
                    + DATA + " TEXT,"
                    + DURATION + " INTEGER)";

    public static final String CREATE_PLAYLIST_TABLE =
            CREATE + PLAYLIST_TABLE
                    + GEN_ID
                    + NAME + " TEXT)";

    public static final String CREATE_PLAYLIST_TRACk_TABLE =
            CREATE + PLAYLIST_TRACK_TABLE
                    + GEN_ID
                    + PLAYLIST_ID + " INTEGER, "
                    + TRACK_ID + " INTEGER "
//                    + TRACK_ID + " INTEGER, "
//                    + " FOREIGN KEY ( " + PLAYLIST_ID + " ) REFERENCES " +  PLAYLIST_TABLE + ID
//                    + " FOREIGN KEY ( " + TRACK_ID + " ) REFERENCES " +  TRACK_TABLE + ID
                    + ")";

    public static final String DROP_TRACK_TABLE = DROP + TRACK_TABLE;
    public static final String DROP_PLAYLIST_TABLE = DROP + PLAYLIST_TABLE;
    public static final String DROP_PLAYLIST_TRACK_TABLE = DROP + PLAYLIST_TRACK_TABLE;

}
