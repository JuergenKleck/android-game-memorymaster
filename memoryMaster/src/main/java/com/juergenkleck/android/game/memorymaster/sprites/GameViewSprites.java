package com.juergenkleck.android.game.memorymaster.sprites;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import com.juergenkleck.android.game.memorymaster.rendering.objects.CardObject;
import com.juergenkleck.android.gameengine.rendering.objects.Graphic;
import com.juergenkleck.android.gameengine.sprites.ViewSprites;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class GameViewSprites implements ViewSprites {

    // main
    public Graphic gBackground;
    public Paint pBackground;
    public Rect rBackground;

    // generic buttons
    public Graphic gButton;
    public Graphic gButtonOverlay;

    public Rect rBtnPause;
    public Rect rBtnResume;
    public Rect rBtnBack;

    public Rect rMessage;

    public List<Graphic> gCardGraphics;
    public List<CardObject> gCards;
    public Graphic gDeck;

    @Override
    public void clean() {
        gBackground = null;

        if (gCardGraphics != null) {
            gCardGraphics.clear();
        }
        if (gCards != null) {
            gCards.clear();
        }
        gCardGraphics = new ArrayList<Graphic>();
        gCards = new ArrayList<CardObject>();
        gDeck = null;
    }

    @Override
    public void init() {
    }

}
