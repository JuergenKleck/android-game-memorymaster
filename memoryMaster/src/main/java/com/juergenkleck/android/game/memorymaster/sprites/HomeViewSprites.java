package com.juergenkleck.android.game.memorymaster.sprites;

import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import com.juergenkleck.android.gameengine.rendering.objects.Graphic;
import com.juergenkleck.android.gameengine.sprites.ViewSprites;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class HomeViewSprites implements ViewSprites {

    // main
    public Graphic gBackground;
    public Graphic gLogo;
    public Paint pBackground;
    public Rect rBackground;

    public LightingColorFilter[] cfBackground;
    public int[] indicesBackground;
    public Rect[] rBackgrounds;
    public LightingColorFilter cfOptions;

    public Rect rMessage;

    // generic buttons
    public Graphic gButton;
    public Graphic gButtonOverlay;
    public Graphic gButton2;

    // main menu
    public Rect rBtnStart;
    public Rect rBtnOptions;
    public Rect rBtnBack;
    public Rect rBtnQuit;

    public Graphic gBtnStart;
    public Graphic gBtnOptions;
    public Graphic gBtnQuit;

    public Rect rMsgOptions;
    public Rect rBtnChooseDeck;
    public Rect rBtnChooseSize;
    public Rect rBtnReset;

    public Rect rOptionPane;
    public Rect rOptionValue1;
    public Rect rOptionValue2;
    public Rect rOptionValue3;
    public Rect rOptionValue4;
    public Rect rOptionValue5;
    public Rect rOptionValue6;
    public Rect rOptionValue7;
    public Graphic[] gDecks;
    // Free game size selection
    public Graphic gButtonSize;
    public Rect rButtonSizeXLeft;
    public Rect rButtonSizeXRight;
    public Rect rButtonSizeYLeft;
    public Rect rButtonSizeYRight;
    public Rect rOptionSizeX;
    public Rect rOptionSizeMid;
    public Rect rOptionSizeY;
    public Rect rButtonDone;
    public Rect rSelectedSize;
    public Rect rMaxSize;
    public Rect rSizeTitle;

    @Override
    public void init() {
    }

    @Override
    public void clean() {
    }

}
