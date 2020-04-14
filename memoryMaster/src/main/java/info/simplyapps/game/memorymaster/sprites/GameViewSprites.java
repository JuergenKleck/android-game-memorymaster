package info.simplyapps.game.memorymaster.sprites;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import info.simplyapps.game.memorymaster.rendering.objects.CardObject;
import info.simplyapps.gameengine.rendering.objects.Graphic;
import info.simplyapps.gameengine.sprites.ViewSprites;

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
