package com.juergenkleck.android.game.memorymaster.screens;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Properties;

import com.juergenkleck.android.game.memorymaster.Constants;
import com.juergenkleck.android.game.memorymaster.Constants.RenderMode;
import com.juergenkleck.android.game.memorymaster.R;
import com.juergenkleck.android.game.memorymaster.rendering.GameRenderer;
import com.juergenkleck.android.game.memorymaster.rendering.HomeRenderer;
import com.juergenkleck.android.game.memorymaster.rendering.LoadingRenderer;
import com.juergenkleck.android.game.memorymaster.rendering.OptionRenderer;
import com.juergenkleck.android.game.memorymaster.storage.StorageUtil;
import com.juergenkleck.android.gameengine.EngineConstants;
import com.juergenkleck.android.gameengine.RenderingSystem;
import com.juergenkleck.android.gameengine.engine.GameEngine;
import com.juergenkleck.android.gameengine.screens.HomeScreenTemplate;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class HomeScreen extends HomeScreenTemplate {

    // Application Data
    public static String ICICLE_KEY = "memorymaster-view";

    public static boolean mGameModeContinue = false;

    RenderMode mLastRenderMode;

    public synchronized void prepareStorage(Context context) {
        StorageUtil.prepareStorage(getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // super create must be the first call for android > 4.0
        super.onCreate(savedInstanceState);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLastRenderMode != null) {
            mLastRenderMode = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        getScreenView().getBasicEngine().saveState(outState);
    }

    @Override
    public void actionNewGame() {
        activateGame();
    }

    @Override
    public void actionContinueGame() {
        activateGame();
    }

    @Override
    public void actionOptions() {
        getScreenView().changeEngine(new OptionRenderer(this, getEngineProperties()));
    }

    @Override
    public void actionQuit() {
        finish();
    }

    @Override
    public void actionAdditionalAction(int action) {
        switch (action) {
            case Constants.ACTION_HOME:
                actionHome();
                break;
            case Constants.ACTION_RESUME:
                actionResume();
                break;
        }
    }

    public void actionHome() {
        if (getGameEngine() != null) {
            getScreenView().getBasicEngine().saveGameState();
            getScreenView().changeEngine(new LoadingRenderer(this, getEngineProperties()));
        }
        getScreenView().changeEngine(new HomeRenderer(this, getEngineProperties()));
    }

    public void actionResume() {
        if (mLastRenderMode != null && RenderMode.GAME.equals(mLastRenderMode)) {
            mLastRenderMode = null;
            getScreenView().changeEngine(new GameRenderer(this, getEngineProperties()));
            getScreenView().getBasicEngine().unpause();
        } else {
            actionOptions();
        }
    }

    @Override
    public String getViewKey() {
        return ICICLE_KEY;
    }

    @Override
    public int getScreenLayout() {
        return R.layout.homescreen;
    }

    @Override
    public int getViewLayoutId() {
        return R.id.homeview;
    }

    @Override
    public void doUpdateChecks() {
    }

    @Override
    public void activateGame() {
        mGameModeContinue = true;

        // ONE GAME SCREEN
        getScreenView().changeEngine(new LoadingRenderer(this, getEngineProperties()));

        // install renderer
        getScreenView().changeEngine(new GameRenderer(this, getEngineProperties()));
        // start renderer
        getScreenView().getBasicEngine().doStart();
    }

    /**
     * Get the game engine instance
     *
     * @return
     */
    public GameEngine getGameEngine() {
        return GameEngine.class.isInstance(mScreenView.getBasicEngine()) ? GameEngine.class.cast(mScreenView.getBasicEngine()) : null;
    }

    @Override
    public Properties getEngineProperties() {
        Properties p = new Properties();
        p.put(EngineConstants.GameProperties.RENDERING_SYSTEM, RenderingSystem.SINGLE_PLAYER);
        p.put(EngineConstants.GameProperties.SCREEN_SCALE, getScreenView().getScreenScaleValue());
        p.put(EngineConstants.GameProperties.LEVEL, 0);
        p.put(EngineConstants.GameProperties.SPACE_LR, Constants.spaceLR);
        p.put(EngineConstants.GameProperties.SPACE_TB, Constants.spaceTB);
        return p;
    }

}