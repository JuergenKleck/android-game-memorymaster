package info.simplyapps.game.memorymaster.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import info.simplyapps.appengine.storage.dto.BasicTable;
import info.simplyapps.game.memorymaster.engine.GameHelper;
import info.simplyapps.game.memorymaster.storage.dto.CurrentGame;
import info.simplyapps.game.memorymaster.storage.dto.GameStats;
import info.simplyapps.game.memorymaster.storage.dto.Inventory;

public class DBDriver extends info.simplyapps.appengine.storage.DBDriver {

    private static final String SQL_CREATE_INVENTORYV1 =
            "CREATE TABLE " + StorageContract.TableInventoryV1.TABLE_NAME + " (" +
                    StorageContract.TableInventoryV1._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableInventoryV1.COLUMN_SELECTED_DECK + TYPE_INT + COMMA_SEP +
                    StorageContract.TableInventoryV1.COLUMN_SELECTED_SIZE + TYPE_INT + COMMA_SEP +
                    StorageContract.TableInventoryV1.COLUMN_MIGRATED + TYPE_INT +
                    " );";
    private static final String SQL_CREATE_INVENTORYV2 =
            "CREATE TABLE " + StorageContract.TableInventoryV2.TABLE_NAME + " (" +
                    StorageContract.TableInventoryV2._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableInventoryV2.COLUMN_SELECTED_DECK + TYPE_INT + COMMA_SEP +
                    StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_X + TYPE_INT + COMMA_SEP +
                    StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_Y + TYPE_INT + COMMA_SEP +
                    StorageContract.TableInventoryV2.COLUMN_MIGRATED + TYPE_INT +
                    " );";
    private static final String SQL_CREATE_GAMESTATSV1 =
            "CREATE TABLE " + StorageContract.TableGameStatsV1.TABLE_NAME + " (" +
                    StorageContract.TableGameStatsV1._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableGameStatsV1.COLUMN_SELECTED_DECK + TYPE_INT + COMMA_SEP +
                    StorageContract.TableGameStatsV1.COLUMN_SELECTED_SIZE + TYPE_INT + COMMA_SEP +
                    StorageContract.TableGameStatsV1.COLUMN_TIME + TYPE_INT + COMMA_SEP +
                    StorageContract.TableGameStatsV1.COLUMN_CLICKS + TYPE_INT +
                    " );";
    private static final String SQL_CREATE_GAMESTATSV2 =
            "CREATE TABLE " + StorageContract.TableGameStatsV2.TABLE_NAME + " (" +
                    StorageContract.TableGameStatsV2._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableGameStatsV2.COLUMN_GAMES + TYPE_INT + COMMA_SEP +
                    StorageContract.TableGameStatsV2.COLUMN_PLAYTIME + TYPE_INT + COMMA_SEP +
                    StorageContract.TableGameStatsV2.COLUMN_TOTALCLICKS + TYPE_INT +
                    " );";
    private static final String SQL_CREATE_CURRENTGAME =
            "CREATE TABLE " + StorageContract.TableCurrentGame.TABLE_NAME + " (" +
                    StorageContract.TableCurrentGame._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableCurrentGame.COLUMN_FLIPPED + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableCurrentGame.COLUMN_FOUND + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableCurrentGame.COLUMN_GREFERENCE + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableCurrentGame.COLUMN_TX + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableCurrentGame.COLUMN_TY + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableCurrentGame.COLUMN_TYPE + TYPE_TEXT +
                    " );";
    private static final String SQL_DELETE_INVENTORYV1 =
            "DROP TABLE IF EXISTS " + StorageContract.TableInventoryV1.TABLE_NAME;
    private static final String SQL_DELETE_GAMESTATSV1 =
            "DROP TABLE IF EXISTS " + StorageContract.TableGameStatsV1.TABLE_NAME;
    private static final String SQL_DELETE_CURRENTGAME =
            "DROP TABLE IF EXISTS " + StorageContract.TableCurrentGame.TABLE_NAME;


    public DBDriver(String dataBaseName, int dataBaseVersion, Context context) {
        super(dataBaseName, dataBaseVersion, context);
    }

    public static DBDriver getInstance() {
        return (DBDriver) info.simplyapps.appengine.storage.DBDriver.getInstance();
    }

    @Override
    public void createTables(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVENTORYV2);
        db.execSQL(SQL_CREATE_CURRENTGAME);
        db.execSQL(SQL_CREATE_GAMESTATSV2);
    }


    @Override
    public void upgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Release 10 - 1.4.0
        if (oldVersion < newVersion && newVersion == 2) {

            new Migration().migrateGameStats(db);
            new Migration().migrateInventory(db);

        }

    }

    @Override
    public String getExtendedTable(BasicTable data) {
        return Inventory.class.isInstance(data) ? StorageContract.TableInventoryV2.TABLE_NAME :
                List.class.isInstance(data) ? StorageContract.TableGameStatsV2.TABLE_NAME :
                        CurrentGame.class.isInstance(data) ? StorageContract.TableCurrentGame.TABLE_NAME : null;
    }

    @Override
    public void storeExtended(info.simplyapps.appengine.storage.StoreData data) {
        store(StoreData.class.cast(data).inventory);
        store(StoreData.class.cast(data).gameStats);
        store(StoreData.class.cast(data).currentGame);
    }

    @Override
    public void readExtended(info.simplyapps.appengine.storage.StoreData data, SQLiteDatabase db) {
        readInventory(StoreData.class.cast(data), db);
        readGameStats(StoreData.class.cast(data), db);
        readCurrentGame(StoreData.class.cast(data), db);
    }

    @Override
    public info.simplyapps.appengine.storage.StoreData createStoreData() {
        return new StoreData();
    }


    public boolean store(Inventory data) {
        ContentValues values = new ContentValues();
        values.put(StorageContract.TableInventoryV2.COLUMN_SELECTED_DECK, data.selectedDeck);
        values.put(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_X, data.freeSizeX);
        values.put(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_Y, data.freeSizeY);
        values.put(StorageContract.TableInventoryV2.COLUMN_MIGRATED, data.migrated);
        return persist(data, values, StorageContract.TableInventoryV2.TABLE_NAME);
    }

    private void readInventory(StoreData data, SQLiteDatabase db) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                StorageContract.TableInventoryV2._ID,
                StorageContract.TableInventoryV2.COLUMN_SELECTED_DECK,
                StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_X,
                StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_Y,
                StorageContract.TableInventoryV2.COLUMN_MIGRATED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                StorageContract.TableInventoryV2.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasResults = c.moveToFirst();
        while (hasResults) {
            Inventory i = new Inventory();
            i.id = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableInventoryV2._ID));
            i.selectedDeck = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV2.COLUMN_SELECTED_DECK));
            i.freeSizeX = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_X));
            i.freeSizeY = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_Y));
            i.migrated = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV2.COLUMN_MIGRATED)) == 1;
            data.inventory = i;
            hasResults = c.moveToNext();
        }
        c.close();
    }


    public boolean store(List<GameStats> list) {
        boolean stored = true;
        for (GameStats data : list) {
            stored &= store(data);
        }
        return stored;
    }

    public boolean store(GameStats data) {
        ContentValues values = new ContentValues();
        values.put(StorageContract.TableGameStatsV2.COLUMN_GAMES, data.games);
        values.put(StorageContract.TableGameStatsV2.COLUMN_PLAYTIME, data.playtime);
        values.put(StorageContract.TableGameStatsV2.COLUMN_TOTALCLICKS, data.totalclicks);
        return persist(data, values, StorageContract.TableGameStatsV2.TABLE_NAME);
    }

    private void readGameStats(StoreData data, SQLiteDatabase db) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                StorageContract.TableGameStatsV2._ID,
                StorageContract.TableGameStatsV2.COLUMN_GAMES,
                StorageContract.TableGameStatsV2.COLUMN_PLAYTIME,
                StorageContract.TableGameStatsV2.COLUMN_TOTALCLICKS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                StorageContract.TableGameStatsV2.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasResults = c.moveToFirst();
        while (hasResults) {
            GameStats i = new GameStats();
            i.id = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV2._ID));
            i.games = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV2.COLUMN_GAMES));
            i.totalclicks = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV2.COLUMN_TOTALCLICKS));
            i.playtime = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV2.COLUMN_PLAYTIME));
            data.gameStats.add(i);
            hasResults = c.moveToNext();
        }
        c.close();
    }


    public boolean store(CurrentGame data) {
        deleteAll(data);
        ContentValues values = new ContentValues();
        values.put(StorageContract.TableCurrentGame.COLUMN_FLIPPED, boolToString(data.flipped));
        values.put(StorageContract.TableCurrentGame.COLUMN_FOUND, boolToString(data.found));
        values.put(StorageContract.TableCurrentGame.COLUMN_GREFERENCE, intToString(data.gReference));
        values.put(StorageContract.TableCurrentGame.COLUMN_TX, intToString(data.tx));
        values.put(StorageContract.TableCurrentGame.COLUMN_TY, intToString(data.ty));
        values.put(StorageContract.TableCurrentGame.COLUMN_TYPE, intToString(data.type));
        return persist(data, values, StorageContract.TableCurrentGame.TABLE_NAME);
    }

    private void readCurrentGame(StoreData data, SQLiteDatabase db) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                StorageContract.TableCurrentGame._ID,
                StorageContract.TableCurrentGame.COLUMN_FLIPPED,
                StorageContract.TableCurrentGame.COLUMN_FOUND,
                StorageContract.TableCurrentGame.COLUMN_GREFERENCE,
                StorageContract.TableCurrentGame.COLUMN_TX,
                StorageContract.TableCurrentGame.COLUMN_TY,
                StorageContract.TableCurrentGame.COLUMN_TYPE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                StorageContract.TableCurrentGame.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasResults = c.moveToFirst();
        while (hasResults) {
            CurrentGame i = new CurrentGame();
            i.id = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame._ID));
            i.flipped = stringToSingleBool(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_FLIPPED)));
            i.found = stringToSingleBool(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_FOUND)));
            i.gReference = stringToInt(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_GREFERENCE)));
            i.tx = stringToInt(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_TX)));
            i.ty = stringToInt(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_TY)));
            i.type = stringToInt(c.getString(c.getColumnIndexOrThrow(StorageContract.TableCurrentGame.COLUMN_TYPE)));
            data.currentGame = i;
            hasResults = c.moveToNext();
        }
        c.close();
    }

    private final class Migration {

        private void migrateGameStats(SQLiteDatabase db) {

            GameStats stats = new GameStats();
            new Migration().readGameStats(stats, db);
            db.execSQL(SQL_DELETE_GAMESTATSV1);
            db.execSQL(SQL_CREATE_GAMESTATSV2);

            ContentValues values = new ContentValues();
            values.put(StorageContract.TableGameStatsV2.COLUMN_GAMES, stats.games);
            values.put(StorageContract.TableGameStatsV2.COLUMN_PLAYTIME, stats.playtime);
            values.put(StorageContract.TableGameStatsV2.COLUMN_TOTALCLICKS, stats.totalclicks);
            persist(db, stats, values, StorageContract.TableGameStatsV2.TABLE_NAME, false);

        }

        private void migrateInventory(SQLiteDatabase db) {

            Inventory data = new Inventory();
            new Migration().readInventory(data, db);
            db.execSQL(SQL_DELETE_INVENTORYV1);
            db.execSQL(SQL_CREATE_INVENTORYV2);

            ContentValues values = new ContentValues();
            values.put(StorageContract.TableInventoryV2.COLUMN_SELECTED_DECK, data.selectedDeck);
            values.put(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_X, data.freeSizeX);
            values.put(StorageContract.TableInventoryV2.COLUMN_FREE_SIZE_Y, data.freeSizeY);
            values.put(StorageContract.TableInventoryV2.COLUMN_MIGRATED, data.migrated);
            persist(db, data, values, StorageContract.TableInventoryV2.TABLE_NAME, false);

        }

        private void readGameStats(GameStats i, SQLiteDatabase db) {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    StorageContract.TableGameStatsV1._ID,
                    StorageContract.TableGameStatsV1.COLUMN_SELECTED_DECK,
                    StorageContract.TableGameStatsV1.COLUMN_SELECTED_SIZE,
                    StorageContract.TableGameStatsV1.COLUMN_TIME,
                    StorageContract.TableGameStatsV1.COLUMN_CLICKS
            };

            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            String selection = null;
            String[] selectionArgs = null;
            Cursor c = db.query(
                    StorageContract.TableGameStatsV1.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            boolean hasResults = c.moveToFirst();
            while (hasResults) {
                i.totalclicks += c.getInt(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV1.COLUMN_CLICKS));
                i.playtime += c.getLong(c.getColumnIndexOrThrow(StorageContract.TableGameStatsV1.COLUMN_TIME));
                i.games += 1;
                hasResults = c.moveToNext();
            }
            c.close();
        }

        private void readInventory(Inventory inventory, SQLiteDatabase db) {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    StorageContract.TableInventoryV1._ID,
                    StorageContract.TableInventoryV1.COLUMN_SELECTED_DECK,
                    StorageContract.TableInventoryV1.COLUMN_SELECTED_SIZE,
                    StorageContract.TableInventoryV1.COLUMN_MIGRATED
            };

            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            String selection = null;
            String[] selectionArgs = null;
            Cursor c = db.query(
                    StorageContract.TableInventoryV1.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            boolean hasResults = c.moveToFirst();
            while (hasResults) {

                inventory.selectedDeck = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV1.COLUMN_SELECTED_DECK));
                inventory.migrated = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV1.COLUMN_MIGRATED)) == 1;
                int selectedSize = c.getInt(c.getColumnIndexOrThrow(StorageContract.TableInventoryV1.COLUMN_SELECTED_SIZE));
                // translate size
                inventory.freeSizeX = GameHelper.convertGameSize(selectedSize, true);
                inventory.freeSizeY = GameHelper.convertGameSize(selectedSize, false);

                hasResults = c.moveToNext();
            }
            c.close();
        }

    }

}
