package info.simplyapps.game.memorymaster.sprites;

import android.graphics.Paint;
import android.graphics.Rect;

import info.simplyapps.gameengine.rendering.objects.Graphic;
import info.simplyapps.gameengine.sprites.ViewSprites;

public class HomeViewSprites implements ViewSprites {

    // main
    public Graphic gBackground;
    public Graphic gLogo;
    public Paint pBackground;
    public Rect rBackground;

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
