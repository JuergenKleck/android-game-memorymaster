package com.juergenkleck.android.game.memorymaster;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class Constants {

    public static final String DATABASE = "memorymaster.db";
    public static final int DATABASE_VERSION = 2;

    public static final String PACKAGE_NAME = "com.juergenkleck.android.game.memorymaster";
    public static final String PREFERENCE_NS = "http://com.juergenkleck.android.game.memorymaster.rendering";

    public static final int ACTION_RESUME = 202;
    public static final int ACTION_HOME = 300;

    public static final int spaceLR = 10;
    public static final int spaceTB = 8;
    public static final int spaceTBWide = 48;

    public static final float CHAR_SPACING = 0.35f;

    public enum RenderMode {
        HOME, GAME, MENU, WAIT;
    }

    public enum SubRenderMode {
        NONE, DECK, SIZE;
    }

}
