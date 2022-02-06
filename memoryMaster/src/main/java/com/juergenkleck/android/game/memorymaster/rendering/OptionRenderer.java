package com.juergenkleck.android.game.memorymaster.rendering;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.juergenkleck.android.game.memorymaster.Constants;
import com.juergenkleck.android.game.memorymaster.Constants.SubRenderMode;
import com.juergenkleck.android.game.memorymaster.R;
import com.juergenkleck.android.game.memorymaster.engine.GameHelper;
import com.juergenkleck.android.game.memorymaster.engine.GameValues;
import com.juergenkleck.android.game.memorymaster.rendering.kits.Renderkit;
import com.juergenkleck.android.game.memorymaster.sprites.HomeViewSprites;
import com.juergenkleck.android.game.memorymaster.storage.DBDriver;
import com.juergenkleck.android.game.memorymaster.storage.StoreData;
import com.juergenkleck.android.game.memorymaster.storage.dto.CurrentGame;
import com.juergenkleck.android.gameengine.EngineConstants;
import com.juergenkleck.android.gameengine.rendering.kits.ScreenKit;
import com.juergenkleck.android.gameengine.rendering.kits.ScreenKit.ScreenPosition;
import com.juergenkleck.android.gameengine.rendering.objects.Graphic;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class OptionRenderer extends MemoryRendererTemplate {

    SubRenderMode mSubMode;
    int gameSizeX = 0;
    int gameSizeY = 0;
    int gameSizeMax = 0;
    int gameDeck = 0;
    boolean expansionDecks = false;
    boolean valid = false;

    public OptionRenderer(Context context, Properties p) {
        super(context, p);
        mSubMode = SubRenderMode.NONE;
    }

    public HomeViewSprites getSprites() {
        return HomeViewSprites.class.cast(super.sprites);
    }

    @Override
    public void doStart() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void unpause() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (delayedAction == EngineConstants.ACTION_NONE) {
            // determine button click

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                    // move
                    break;
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    if (mSubMode.equals(SubRenderMode.NONE)) {
                        if (containsClick(getSprites().rBtnChooseDeck, event.getX(), event.getY())) {
                            mSubMode = SubRenderMode.DECK;
                        }
                        if (containsClick(getSprites().rBtnChooseSize, event.getX(), event.getY())) {
                            mSubMode = SubRenderMode.SIZE;
                        }
                        if (containsClick(getSprites().rBtnReset, event.getX(), event.getY())) {
                            StoreData.getInstance().currentGame = new CurrentGame();
                            DBDriver.getInstance().store(StoreData.getInstance().currentGame);
                            Toast.makeText(mContext, R.string.game_resetted, Toast.LENGTH_LONG).show();
                        }
                    }
                    // select game size
                    else if (mSubMode.equals(SubRenderMode.SIZE)) {
                        if (!expansionDecks) {
                            for (int size : GameValues.ALL_SIZES) {
                                if (containsClick(getDrawingRect(size), event.getX(), event.getY()) && hasGameSize(gameDeck, size)) {
                                    mSubMode = SubRenderMode.NONE;
                                    gameSizeX = GameHelper.convertGameSize(size, true);
                                    gameSizeY = GameHelper.convertGameSize(size, false);
                                    StoreData.getInstance().inventory.freeSizeX = gameSizeX;
                                    StoreData.getInstance().inventory.freeSizeY = gameSizeY;
                                    DBDriver.getInstance().store(StoreData.getInstance().inventory);
                                    break;
                                }
                            }
                        } else {
                            if (containsClick(getSprites().rButtonDone, event.getX(), event.getY()) && valid) {
                                mSubMode = SubRenderMode.NONE;
                                StoreData.getInstance().inventory.freeSizeX = gameSizeX;
                                StoreData.getInstance().inventory.freeSizeY = gameSizeY;
                                DBDriver.getInstance().store(StoreData.getInstance().inventory);
                                break;
                            }
                            if (containsClick(getSprites().rButtonSizeXLeft, event.getX(), event.getY())) {
                                gameSizeX += 1;
                                if (gameSizeX > GameValues.GAMESIZE_MAX_X) {
                                    gameSizeX = GameValues.GAMESIZE_MAX_X;
                                }
                            }
                            if (containsClick(getSprites().rButtonSizeXRight, event.getX(), event.getY())) {
                                gameSizeX -= 1;
                                if (gameSizeX < 1) {
                                    gameSizeX = 1;
                                }
                                if (gameSizeY == 1 && gameSizeX == 1) {
                                    gameSizeX = 2;
                                }
                            }
                            if (containsClick(getSprites().rButtonSizeYLeft, event.getX(), event.getY())) {
                                gameSizeY += 1;
                                if (gameSizeY > GameValues.GAMESIZE_MAX_Y) {
                                    gameSizeY = GameValues.GAMESIZE_MAX_Y;
                                }
                            }
                            if (containsClick(getSprites().rButtonSizeYRight, event.getX(), event.getY())) {
                                gameSizeY -= 1;
                                if (gameSizeY < 1) {
                                    gameSizeY = 1;
                                }
                                if (gameSizeX == 1 && gameSizeY == 1) {
                                    gameSizeY = 2;
                                }
                            }
                        }
                    }
                    // select game deck
                    else if (mSubMode.equals(SubRenderMode.DECK)) {
                        for (int deck : GameValues.ALL_DECKS) {
                            if (containsClick(getDrawingRect(deck), event.getX(), event.getY())) {
                                mSubMode = SubRenderMode.NONE;
                                gameDeck = deck;
                                gameSizeX = GameValues.GAMESIZE_X_DEF;
                                gameSizeY = GameValues.GAMESIZE_Y_DEF;
                                gameSizeMax = 0;
                                StoreData.getInstance().inventory.selectedDeck = deck;
                                StoreData.getInstance().inventory.freeSizeX = gameSizeX;
                                StoreData.getInstance().inventory.freeSizeY = gameSizeY;
                                DBDriver.getInstance().store(StoreData.getInstance().inventory);
                                break;
                            }
                        }
                    }

                    if (containsClick(getSprites().rBtnBack, event.getX(), event.getY())) {
                        if (!mSubMode.equals(SubRenderMode.NONE)) {
                            mSubMode = SubRenderMode.NONE;
                        } else {
                            delayedActionHandler(Constants.ACTION_HOME, Constants.ACTION_HOME);
                        }
                    }
                    break;
            }

        }

        return true;
    }

    @Override
    public void doUpdateRenderState() {
        if (mSubMode.equals(SubRenderMode.SIZE)) {
            valid = (gameSizeX * gameSizeY) % 2 == 0 && ((gameSizeX * gameSizeY) <= gameSizeMax);
        }
        if (gameSizeMax == 0) {
            TypedArray arr = mContext.getResources().obtainTypedArray(GameValues.DECK_CARDS[StoreData.getInstance().inventory.selectedDeck]);
            gameSizeMax = arr.length() * 2;
            arr.recycle();
        }
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {

        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }
        if (getSprites().gBackground != null && getSprites().rBackgrounds != null) {
            for (int i = 0; i < getSprites().rBackgrounds.length; i++) {
                Rect r = getSprites().rBackgrounds[i];
                getSprites().gBackground.image.setColorFilter(getSprites().cfOptions);
                getSprites().gBackground.image.setBounds(r);
                getSprites().gBackground.image.draw(canvas);
            }
        }


        switch (mSubMode) {
            case DECK:
                choiceBaseDraw(canvas, getSprites().rOptionPane, getSprites().gButton2, getSprites().gButton2, true, true, GameValues.cFilterSilver);
                for (int deck : GameValues.ALL_DECKS) {
                    Rect rText = new Rect(getDrawingRect(deck));
                    rText.left += scaleWidth(25);
                    choiceDraw(canvas, getDrawingRect(deck), getSprites().gButtonOverlay, getSprites().gButton, gameDeck, deck, false, true, GameValues.cFilterBlue, GameValues.cFilterRed);
                    drawText(canvas, rText, getString(GameHelper.getGameDeckTitle(deck)));
                    Rect bounds = getSprites().gDecks[deck].image.copyBounds();
                    bounds.offsetTo(rText.left - bounds.width() + scaleWidth(5), rText.top + scaleHeight(10));
                    getSprites().gDecks[deck].image.setBounds(bounds);
                    getSprites().gDecks[deck].image.draw(canvas);
                }
                break;
            case SIZE:
                choiceBaseDraw(canvas, getSprites().rOptionPane, getSprites().gButton2, getSprites().gButton2, true, true, GameValues.cFilterSilver);
                if (!expansionDecks) {
                    for (int size : GameValues.ALL_SIZES) {
                        if (hasGameSize(gameDeck, size)) {
                            choiceDraw(canvas, getDrawingRect(size), getSprites().gButtonOverlay, getSprites().gButton, GameHelper.convertGameSize(gameSizeX, gameSizeY), size, false, true, GameValues.cFilterBlue, GameValues.cFilterRed);
                            drawTextUnbounded(canvas, getDrawingRect(size), getString(GameHelper.getGameSizeTitle(size)));
                        } else {
                            choiceBaseDraw(canvas, getDrawingRect(size), getSprites().gButtonOverlay, getSprites().gButton, true, true, GameValues.cFilterInactive);
                            drawTextUnbounded(canvas, getDrawingRect(size), getString(GameHelper.getGameSizeTitle(size)));
                        }
                    }
                } else {
                    // width - x
                    getSprites().gButtonSize.image.setBounds(getSprites().rButtonSizeXLeft);
                    canvas.save();
                    canvas.rotate(90f, getSprites().gButtonSize.image.getBounds().exactCenterX(), getSprites().gButtonSize.image.getBounds().exactCenterY());
                    getSprites().gButtonSize.image.draw(canvas);
                    canvas.restore();
                    getSprites().gButtonSize.image.setBounds(getSprites().rButtonSizeXRight);
                    canvas.save();
                    canvas.rotate(270f, getSprites().gButtonSize.image.getBounds().exactCenterX(), getSprites().gButtonSize.image.getBounds().exactCenterY());
                    getSprites().gButtonSize.image.draw(canvas);
                    canvas.restore();
                    // height - y 
                    getSprites().gButtonSize.image.setBounds(getSprites().rButtonSizeYLeft);
                    canvas.save();
                    canvas.rotate(90f, getSprites().gButtonSize.image.getBounds().exactCenterX(), getSprites().gButtonSize.image.getBounds().exactCenterY());
                    getSprites().gButtonSize.image.draw(canvas);
                    canvas.restore();
                    getSprites().gButtonSize.image.setBounds(getSprites().rButtonSizeYRight);
                    canvas.save();
                    canvas.rotate(270f, getSprites().gButtonSize.image.getBounds().exactCenterX(), getSprites().gButtonSize.image.getBounds().exactCenterY());
                    getSprites().gButtonSize.image.draw(canvas);
                    canvas.restore();

                    // draw gamesize settings
                    drawTextUnbounded(canvas, getSprites().rOptionSizeX, Integer.toString(gameSizeX), GameValues.cFilterRed);
                    drawTextUnbounded(canvas, getSprites().rOptionSizeY, Integer.toString(gameSizeY), GameValues.cFilterRed);
                    drawTextUnbounded(canvas, getSprites().rOptionSizeMid, "x", GameValues.cFilterRed);
                    drawTextUnbounded(canvas, getSprites().rSizeTitle, getString(R.string.option_gamesize), GameValues.cFilterBlue);
                    drawTextUnbounded(canvas, getSprites().rSelectedSize, MessageFormat.format(getString(R.string.option_gamesize_selected), (gameSizeX * gameSizeY)), GameValues.cFilterBlue);
                    drawTextUnbounded(canvas, getSprites().rMaxSize, MessageFormat.format(getString(R.string.option_gamesize_max), gameSizeMax), GameValues.cFilterBlue);

                    choiceBaseDraw(canvas, getSprites().rButtonDone, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterBlue);
                    drawText(canvas, getSprites().rButtonDone, getString(R.string.option_gamesize_done), valid ? GameValues.cFilterBlue : GameValues.cFilterRed);
                }
                break;
            default:
                drawText(canvas, getSprites().rMsgOptions, getString(R.string.options), GameValues.cFilterBlue);
                choiceBaseDraw(canvas, getSprites().rBtnChooseDeck, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterBlue);
                drawText(canvas, getSprites().rBtnChooseDeck, getString(R.string.option_deck));
                choiceBaseDraw(canvas, getSprites().rBtnChooseSize, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterBlue);
                drawText(canvas, getSprites().rBtnChooseSize, getString(R.string.option_gamesize));
                choiceBaseDraw(canvas, getSprites().rBtnReset, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterBlue);
                drawText(canvas, getSprites().rBtnReset, getString(R.string.menubutton_reset));
                break;
        }

        choiceBaseDraw(canvas, getSprites().rBtnBack, getSprites().gButtonOverlay, getSprites().gButton, activeButton, Constants.ACTION_HOME, GameValues.cFilterBlue);
        drawText(canvas, getSprites().rBtnBack, getString(R.string.menubutton_back), GameValues.cFilterBlue);
    }


    @Override
    public void restoreGameState() {
        gameDeck = StoreData.getInstance().inventory.selectedDeck;
        gameSizeX = StoreData.getInstance().inventory.freeSizeX;
        gameSizeY = StoreData.getInstance().inventory.freeSizeY;
    }

    @Override
    public void saveGameState() {
    }

    @Override
    public void doInitThread(long time) {
        super.sprites = new HomeViewSprites();

        getSprites().gBackground = loadGraphic(R.drawable.background_2);

        Shader shader = new LinearGradient(0, 0, 0, realScreenHeight, Color.parseColor("#2dd0f4"), Color.parseColor("#19bff8"), Shader.TileMode.CLAMP);
        getSprites().pBackground = new Paint();
        getSprites().pBackground.setShader(shader);
        getSprites().rBackground = new Rect(0, 0, screenWidth, realScreenHeight);

        getSprites().cfOptions = new LightingColorFilter(Color.argb(0, 199, 194, 0), Color.argb(0, 150, 150, 150));

        // calculate the rectangles only once at the beginning, cpu > memory
        int totalRects = 0;
        List<Rect> rects = new ArrayList<>();
        if (getSprites().gBackground != null) {
            // draw image across screen
            int shift = 0;
            int h = 0;
            int v = 0;
            Rect r = getSprites().gBackground.image.copyBounds();

            shift -= Float.valueOf(r.width() * 0.3f).intValue();
            h = shift;
            r.offsetTo(h, v);
            rects.add(r);
            totalRects++;
            while (h < screenWidth && v - r.height() < screenHeight) {
                r = new Rect(r);
                h = r.right - Float.valueOf(r.width() * 0.2f).intValue();
                if (h > screenWidth) {
                    v = r.bottom;
                    shift -= Float.valueOf(r.width() * 0.3f).intValue();
                    h = shift;
                }
                r.offsetTo(h, v);
                if (v - r.height() > screenHeight) {
                    r.offsetTo(0, 0);
                    break;
                }
                rects.add(r);
                totalRects++;
            }
        }
        getSprites().rBackgrounds = rects.toArray(new Rect[]{});

        // button backgrounds
        getSprites().gButton = Renderkit.loadButtonGraphic(mContext.getResources(), R.drawable.button_background, 0, 0, EngineConstants.ACTION_NONE);
        getSprites().gButton2 = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_flat, 0, 0);
        getSprites().gButtonOverlay = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_background, 0, 0);

        getSprites().rMessage = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER, 0.5f, 0, 0, getSprites().rMessage);

        getSprites().rMsgOptions = getSprites().gButton.image.copyBounds();
        getSprites().rBtnChooseDeck = getSprites().gButton.image.copyBounds();
        getSprites().rBtnChooseSize = getSprites().gButton.image.copyBounds();
        getSprites().rBtnReset = getSprites().gButton.image.copyBounds();
        getSprites().rBtnBack = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.4f, 30, 070, getSprites().rMsgOptions);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.4f, 30, 270, getSprites().rBtnChooseDeck);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.4f, 30, 500, getSprites().rBtnChooseSize);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.4f, 30, 730, getSprites().rBtnReset);
        ScreenKit.scaleRect(screenWidth, realScreenHeight, ScreenPosition.BOTTOM_LEFT, 0.30f, 50, 30, getSprites().rBtnBack);

        getSprites().rOptionPane = new Rect(0, 0, screenWidth, screenHeight);
        getSprites().rOptionValue1 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue2 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue3 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue4 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue5 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue6 = getSprites().gButton.image.copyBounds();
        getSprites().rOptionValue7 = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER, 0.8f, 0, 0, getSprites().rOptionPane);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 75, 130, getSprites().rOptionValue1);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 280, 130, getSprites().rOptionValue2);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 75, 300, getSprites().rOptionValue3);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 280, 300, getSprites().rOptionValue4);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 75, 470, getSprites().rOptionValue5);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 280, 470, getSprites().rOptionValue6);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 150, 650, getSprites().rOptionValue7);

        getSprites().gButtonSize = loadGraphic(R.drawable.leftbutton, 0, 0);
        getSprites().rButtonSizeXLeft = getSprites().gButtonSize.image.copyBounds();
        getSprites().rButtonSizeXRight = getSprites().gButtonSize.image.copyBounds();
        getSprites().rButtonSizeYLeft = getSprites().gButtonSize.image.copyBounds();
        getSprites().rButtonSizeYRight = getSprites().gButtonSize.image.copyBounds();
        getSprites().rOptionSizeX = getSprites().gButton.image.copyBounds();
        getSprites().rOptionSizeMid = getSprites().gButton.image.copyBounds();
        getSprites().rOptionSizeY = getSprites().gButton.image.copyBounds();
        getSprites().rButtonDone = getSprites().gButton.image.copyBounds();
        getSprites().rSelectedSize = getSprites().gButton.image.copyBounds();
        getSprites().rMaxSize = getSprites().gButton.image.copyBounds();
        getSprites().rSizeTitle = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.05f, 80, 150, getSprites().rButtonSizeXLeft);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.05f, 80, 550, getSprites().rButtonSizeXRight);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 70, 350, getSprites().rOptionSizeX);

        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.05f, 180, 150, getSprites().rButtonSizeYLeft);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.05f, 180, 550, getSprites().rButtonSizeYRight);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 170, 350, getSprites().rOptionSizeY);

        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_LEFT, 0.35f, 120, 350, getSprites().rOptionSizeMid);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.BOTTOM_RIGHT, 0.30f, 50, 200, getSprites().rButtonDone);

        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_RIGHT, 0.30f, 120, 150, getSprites().rSizeTitle);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_RIGHT, 0.30f, 150, 300, getSprites().rSelectedSize);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_RIGHT, 0.30f, 140, 450, getSprites().rMaxSize);

        getSprites().rOptionPane.bottom -= scaleHeight(75);
        getSprites().rButtonDone.right -= getSprites().rButtonDone.width() / 3;
        getSprites().rBtnBack.right -= getSprites().rBtnBack.width() / 3;

        getSprites().gDecks = new Graphic[GameValues.ALL_DECKS.length];
        for (int i = 0; i < GameValues.ALL_DECKS.length; i++) {
            getSprites().gDecks[i] = Renderkit.loadGraphic(mContext.getResources(), GameHelper.getGameDeckIcon(i), 0, 0);
        }
        ScreenKit.scaleImage(screenWidth, screenHeight, ScreenPosition.CENTER, 0.05f, 0, 0, getSprites().gDecks);
    }

    private Rect getDrawingRect(int i) {
        switch (i) {
            case 0:
                return getSprites().rOptionValue1;
            case 1:
                return getSprites().rOptionValue2;
            case 2:
                return getSprites().rOptionValue3;
            case 3:
                return getSprites().rOptionValue4;
            case 4:
                return getSprites().rOptionValue5;
            case 5:
                return getSprites().rOptionValue6;
            case 6:
                return getSprites().rOptionValue7;
        }
        return null;
    }

    private boolean hasGameSize(int checkDeck, int checkSize) {
        boolean found = false;

        for (int size : GameValues.DECK_GAMESIZES[checkDeck]) {
            if (size == checkSize) {
                found = true;
                break;
            }
        }

        return found;
    }

    @Override
    public void reset() {

    }

}
