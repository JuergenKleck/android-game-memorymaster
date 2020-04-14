package info.simplyapps.game.memorymaster.storage;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import info.simplyapps.appengine.AppEngineConstants;
import info.simplyapps.appengine.storage.DBDriver;
import info.simplyapps.appengine.storage.dto.Configuration;
import info.simplyapps.game.memorymaster.Constants;
import info.simplyapps.game.memorymaster.SystemHelper;
import info.simplyapps.game.memorymaster.rendering.objects.CardObject;
import info.simplyapps.game.memorymaster.storage.dto.CurrentGame;
import info.simplyapps.gameengine.EngineConstants;

public final class StorageUtil {

    public synchronized static void prepareStorage(Context context) {
        if (DBDriver.getInstance() == null) {
            DBDriver.createInstance(new info.simplyapps.game.memorymaster.storage.DBDriver(Constants.DATABASE, Constants.DATABASE_VERSION, context));
        }

        // try to load
        if (StoreData.getInstance() == null) {
            StoreData.createInstance(DBDriver.getInstance().read());
        }
        // create
        if (StoreData.getInstance() == null) {
            StoreData.createInstance(new StoreData());
        }
        // migration
        Configuration cMig = SystemHelper.getConfiguration(AppEngineConstants.CONFIG_MIGRATION, AppEngineConstants.DEFAULT_CONFIG_MIGRATION);
        if (!SystemHelper.hasConfiguration(AppEngineConstants.CONFIG_MIGRATION)) {
            // will store automatically if migration is lower than this value
            StoreData.getInstance().configuration.add(cMig);
        }
        StoreData.getInstance().migration = Integer.valueOf(cMig.value);

        // update
        if (StoreData.getInstance().update()) {
            // store back the migration value
            Configuration c = SystemHelper.getConfiguration(AppEngineConstants.CONFIG_MIGRATION, AppEngineConstants.DEFAULT_CONFIG_MIGRATION);
            c.value = Integer.toString(StoreData.getInstance().migration);
            DBDriver.getInstance().write(StoreData.getInstance());
        }
        if (!SystemHelper.hasConfiguration(EngineConstants.CONFIG_MUSIC)) {
            Configuration c = new Configuration(EngineConstants.CONFIG_MUSIC, EngineConstants.DEFAULT_CONFIG_MUSIC);
            if (DBDriver.getInstance().store(c)) {
                StoreData.getInstance().configuration.add(c);
            }
        }
        if (!SystemHelper.hasConfiguration(EngineConstants.CONFIG_DIFFICULTY)) {
            Configuration c = new Configuration(EngineConstants.CONFIG_DIFFICULTY, EngineConstants.DEFAULT_CONFIG_DIFFICULTY);
            if (DBDriver.getInstance().store(c)) {
                StoreData.getInstance().configuration.add(c);
            }
        }
        if (!SystemHelper.hasConfiguration(AppEngineConstants.CONFIG_FORCE_UPDATE)) {
            Configuration c = new Configuration(AppEngineConstants.CONFIG_FORCE_UPDATE, AppEngineConstants.DEFAULT_CONFIG_FORCE_UPDATE);
            if (DBDriver.getInstance().store(c)) {
                StoreData.getInstance().configuration.add(c);
            }
        }
        if (!SystemHelper.hasConfiguration(AppEngineConstants.CONFIG_LAST_CHECK)) {
            Configuration c = new Configuration(AppEngineConstants.CONFIG_LAST_CHECK, AppEngineConstants.DEFAULT_CONFIG_LAST_CHECK);
            if (DBDriver.getInstance().store(c)) {
                StoreData.getInstance().configuration.add(c);
            }
        }
        if (!SystemHelper.hasConfiguration(AppEngineConstants.CONFIG_ON_SERVER)) {
            Configuration c = new Configuration(AppEngineConstants.CONFIG_ON_SERVER, AppEngineConstants.DEFAULT_CONFIG_ON_SERVER);
            if (DBDriver.getInstance().store(c)) {
                StoreData.getInstance().configuration.add(c);
            }
        }
        // store additional data

    }

    public static List<CardObject> restoreCurrentGame(CurrentGame cg) {
        List<CardObject> list = new ArrayList<CardObject>();

        if (cg.flipped != null) {
            for (int i = 0; i < cg.flipped.length; i++) {
                CardObject card = new CardObject();
                card.flipped = cg.flipped[i];
                card.found = cg.found[i];
                card.gReference = cg.gReference[i];
                card.tx = cg.tx[i];
                card.ty = cg.ty[i];
                card.type = cg.type[i];
                list.add(card);
            }
        }

        return list;
    }

    public static CurrentGame storeCurrentGame(List<CardObject> cards) {
        CurrentGame cg = new CurrentGame();

        cg.flipped = new boolean[cards.size()];
        cg.found = new boolean[cards.size()];
        cg.gReference = new int[cards.size()];
        cg.tx = new int[cards.size()];
        cg.ty = new int[cards.size()];
        cg.type = new int[cards.size()];

        for (int i = 0; i < cards.size(); i++) {
            cg.flipped[i] = cards.get(i).flipped;
            cg.found[i] = cards.get(i).found;
            cg.gReference[i] = cards.get(i).gReference;
            cg.tx[i] = cards.get(i).tx;
            cg.ty[i] = cards.get(i).ty;
            cg.type[i] = cards.get(i).type;
        }

        return cg;
    }

}
