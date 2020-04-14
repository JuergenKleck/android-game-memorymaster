package info.simplyapps.game.memorymaster.storage;

import android.provider.BaseColumns;

public class StorageContract extends
        info.simplyapps.appengine.storage.StorageContract {

    public static abstract class TableInventoryV1 implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String COLUMN_MIGRATED = "migrated";
        public static final String COLUMN_SELECTED_DECK = "selecteddeck";
        public static final String COLUMN_SELECTED_SIZE = "selectedsize";
    }

    public static abstract class TableInventoryV2 implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String COLUMN_MIGRATED = "migrated";
        public static final String COLUMN_SELECTED_DECK = "selecteddeck";
        public static final String COLUMN_FREE_SIZE_X = "freesizex";
        public static final String COLUMN_FREE_SIZE_Y = "freesizey";
    }

    public static abstract class TableGameStatsV1 implements BaseColumns {
        public static final String TABLE_NAME = "gamestats";
        public static final String COLUMN_SELECTED_DECK = "selecteddeck";
        public static final String COLUMN_SELECTED_SIZE = "selectedsize";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_CLICKS = "clicks";
    }

    public static abstract class TableGameStatsV2 implements BaseColumns {
        public static final String TABLE_NAME = "gamestats";
        public static final String COLUMN_PLAYTIME = "playtime";
        public static final String COLUMN_TOTALCLICKS = "totalclicks";
        public static final String COLUMN_GAMES = "games";
    }

    public static abstract class TableCurrentGame implements BaseColumns {
        public static final String TABLE_NAME = "currentgame";
        public static final String COLUMN_FLIPPED = "flipped";
        public static final String COLUMN_FOUND = "found";
        public static final String COLUMN_GREFERENCE = "greference";
        public static final String COLUMN_TX = "tx";
        public static final String COLUMN_TY = "ty";
        public static final String COLUMN_TYPE = "type";
    }

}
