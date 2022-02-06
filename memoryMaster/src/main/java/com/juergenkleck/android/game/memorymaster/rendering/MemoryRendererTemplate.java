package com.juergenkleck.android.game.memorymaster.rendering;

import android.content.Context;

import java.util.Properties;

import com.juergenkleck.android.game.memorymaster.Constants;
import com.juergenkleck.android.game.memorymaster.screens.HomeScreen;
import com.juergenkleck.android.gameengine.rendering.GenericRendererTemplate;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public abstract class MemoryRendererTemplate extends GenericRendererTemplate {

    public MemoryRendererTemplate(Context context, Properties properties) {
        super(context, properties);
    }

    public HomeScreen getScreen() {
        return HomeScreen.class.cast(mContext);
    }

    @Override
    public boolean logEnabled() {
        return false;
    }

    @Override
    public float getCharSpacing() {
        return Constants.CHAR_SPACING;
    }

}
