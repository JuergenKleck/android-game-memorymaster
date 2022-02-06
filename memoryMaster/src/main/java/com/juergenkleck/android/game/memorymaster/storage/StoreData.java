package com.juergenkleck.android.game.memorymaster.storage;

import java.util.ArrayList;
import java.util.List;

import com.juergenkleck.android.appengine.AppEngineConstants;
import com.juergenkleck.android.appengine.storage.dto.Configuration;
import com.juergenkleck.android.game.memorymaster.SystemHelper;
import com.juergenkleck.android.game.memorymaster.storage.dto.CurrentGame;
import com.juergenkleck.android.game.memorymaster.storage.dto.GameStats;
import com.juergenkleck.android.game.memorymaster.storage.dto.Inventory;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class StoreData extends com.juergenkleck.android.appengine.storage.StoreData {

    public Inventory inventory;
    public List<GameStats> gameStats;
    public CurrentGame currentGame;

    public StoreData() {
        super();
        if (inventory == null) {
            inventory = new Inventory();
        }
        if (gameStats == null) {
            gameStats = new ArrayList<GameStats>();
        }
        if (currentGame == null) {
            currentGame = new CurrentGame();
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = 2982830586304674266L;

    public static StoreData getInstance() {
        return (StoreData) com.juergenkleck.android.appengine.storage.StoreData.getInstance();
    }

    @Override
    public boolean update() {
        boolean persist = false;

        // For initial data creation
        if (migration < 1) {
            persist = true;
        }

        // Release 7 - 1.3.0
        if (migration < 7) {
            persist = true;
        }

        // Release 8 - 1.3.1
        if (migration < 8) {
            SystemHelper.setConfiguration(new Configuration(AppEngineConstants.CONFIG_ON_SERVER, AppEngineConstants.DEFAULT_CONFIG_ON_SERVER));
            SystemHelper.setConfiguration(new Configuration(AppEngineConstants.CONFIG_FORCE_UPDATE, AppEngineConstants.DEFAULT_CONFIG_FORCE_UPDATE));
            SystemHelper.setConfiguration(new Configuration(AppEngineConstants.CONFIG_LAST_CHECK, AppEngineConstants.DEFAULT_CONFIG_LAST_CHECK));
            persist = true;
        }

        // Release 9 - 1.3.2
        if (migration < 9) {
            persist = true;
        }

        // Release 10 - 1.4.0
        if (migration < 10) {
            persist = true;
        }

        migration = 10;
        return persist;
    }

}
