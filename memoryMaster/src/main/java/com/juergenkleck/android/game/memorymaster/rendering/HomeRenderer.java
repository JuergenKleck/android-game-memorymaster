package com.juergenkleck.android.game.memorymaster.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.juergenkleck.android.game.memorymaster.R;
import com.juergenkleck.android.game.memorymaster.rendering.kits.Renderkit;
import com.juergenkleck.android.game.memorymaster.sprites.HomeViewSprites;
import com.juergenkleck.android.gameengine.EngineConstants;
import com.juergenkleck.android.gameengine.rendering.kits.ScreenKit;
import com.juergenkleck.android.gameengine.rendering.kits.ScreenKit.ScreenPosition;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class HomeRenderer extends MemoryRendererTemplate {

    long lastTime = 0L;
    int colorIdx = 0;
    Random rand;
    final long moveTime = 500L;
    final float fluctuationMin = 0.25f;
    final float fluctuationMax = 1.0f;
    long lastMove = 0L;

    public HomeRenderer(Context context, Properties p) {
        super(context, p);
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
                    if (containsClick(getSprites().gBtnStart, event.getX(), event.getY())) {
                        delayedActionHandler(EngineConstants.ACTION_START, EngineConstants.ACTION_START);
                    }
                    if (containsClick(getSprites().gBtnOptions, event.getX(), event.getY())) {
                        delayedActionHandler(EngineConstants.ACTION_OPTIONS, EngineConstants.ACTION_OPTIONS);
                    }
                    if (containsClick(getSprites().gBtnQuit, event.getX(), event.getY())) {
                        delayedActionHandler(EngineConstants.ACTION_QUIT, EngineConstants.ACTION_QUIT);
                    }
                    break;
            }

        }

        return true;
    }

    @Override
    public void doUpdateRenderState() {
        final long time = System.currentTimeMillis();

        if (lastMove < time) {
            for (int i = 0; i < getSprites().indicesBackground.length; i++) {
                colorIdx++;
                if (colorIdx + 1 >= getSprites().cfBackground.length) {
                    colorIdx = 0;
                }
                getSprites().indicesBackground[i] = colorIdx;
            }

            float fluctuation = rand.nextFloat();
            while (fluctuation < fluctuationMin || fluctuation > fluctuationMax) {
                fluctuation = rand.nextFloat();
            }


            lastMove = time + Float.valueOf(moveTime * fluctuation).longValue();
        }

        lastTime = time;
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {

        //final long time = System.currentTimeMillis();

        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }
        if (getSprites().gBackground != null && getSprites().rBackgrounds != null) {
            for (int i = 0; i < getSprites().rBackgrounds.length; i++) {
                Rect r = getSprites().rBackgrounds[i];
                if (getSprites().cfBackground != null) {
                    getSprites().gBackground.image.setColorFilter(getSprites().cfBackground[getSprites().indicesBackground[i]]);
                }
                getSprites().gBackground.image.setBounds(r);
                getSprites().gBackground.image.draw(canvas);
            }
        }


        // draw buttons last to overlay the background items
//        choiceBaseDraw(canvas, getSprites().rBtnStart, getSprites().gButtonOverlay, getSprites().gButton, activeButton, EngineConstants.ACTION_START, GameValues.cFilterBlue);
//        choiceBaseDraw(canvas, getSprites().rBtnOptions, getSprites().gButtonOverlay, getSprites().gButton, activeButton, EngineConstants.ACTION_OPTIONS, GameValues.cFilterBlue);
//        choiceBaseDraw(canvas, getSprites().rBtnQuit, getSprites().gButtonOverlay, getSprites().gButton, activeButton, EngineConstants.ACTION_QUIT, GameValues.cFilterBlue);
//
//        drawText(canvas, getSprites().rBtnStart, mContext.getString(R.string.menubutton_start));
//        drawText(canvas, getSprites().rBtnOptions, mContext.getString(R.string.menubutton_options));
//        drawText(canvas, getSprites().rBtnQuit, mContext.getString(R.string.menubutton_quit), GameValues.cFilterRed);

        getSprites().gBtnStart.image.draw(canvas);
        getSprites().gBtnOptions.image.draw(canvas);
        getSprites().gBtnQuit.image.draw(canvas);

    }


    @Override
    public void restoreGameState() {
        unpause();
    }

    @Override
    public void saveGameState() {
    }

    @Override
    public void doInitThread(long time) {
        super.sprites = new HomeViewSprites();

        rand = new Random();
        lastMove = 0L;

        getSprites().gBackground = loadGraphic(R.drawable.background_2);
//        getSprites().gBackground.image.setBounds(0, 0, screenWidth, realScreenHeight);

        // random amount of color filters
        int max = rand.nextInt(30);
        while (max < 10) {
            max = rand.nextInt(30);
        }
        getSprites().cfBackground = new LightingColorFilter[max];
        for (int i = 0; i < max; i++) {
            getSprites().cfBackground[i] = new LightingColorFilter(generateColor(), generateColor());
        }

        Shader shader = new LinearGradient(0, 0, 0, realScreenHeight, Color.parseColor("#2dd0f4"), Color.parseColor("#19bff8"), Shader.TileMode.CLAMP);
        getSprites().pBackground = new Paint();
        getSprites().pBackground.setShader(shader);
        getSprites().rBackground = new Rect(0, 0, screenWidth, realScreenHeight);

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
        getSprites().indicesBackground = new int[totalRects];
        for (int i = 0; i < getSprites().indicesBackground.length; i++) {
            getSprites().indicesBackground[i] = 0;
        }
        getSprites().rBackgrounds = rects.toArray(new Rect[]{});

        // button backgrounds
//        getSprites().gButton = Renderkit.loadButtonGraphic(mContext.getResources(), R.drawable.button_background, 0, 0, EngineConstants.ACTION_NONE);
//        getSprites().gButtonOverlay = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_background, 0, 0);
//
//        getSprites().rBtnStart = getSprites().gButton.image.copyBounds();
//        getSprites().rBtnOptions = getSprites().gButton.image.copyBounds();
//        getSprites().rBtnQuit = getSprites().gButton.image.copyBounds();
//        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.4f, 0, 130, getSprites().rBtnStart);
//        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.3f, 0, 380, getSprites().rBtnOptions);
//        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.BOTTOM_RIGHT, 0.25f, 50, 50, getSprites().rBtnQuit);

        getSprites().gBtnStart = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_play, 0, 0);
        getSprites().gBtnOptions = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_options, 0, 0);
        getSprites().gBtnQuit = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_quit, 0, 0);
        ScreenKit.scaleImage(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.4f, 0, 130, getSprites().gBtnStart);
        ScreenKit.scaleImage(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.3f, 0, 500, getSprites().gBtnOptions);
        ScreenKit.scaleImage(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.25f, 0, 750, getSprites().gBtnQuit);

//        getSprites().rBtnSwitch.bottom -= getSprites().rBtnSwitch.height() / 3;

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    private int generateColor() {
        int red = rand.nextInt(255);
        int green = rand.nextInt(255);
        int blue = rand.nextInt(255);
        int alpha = rand.nextInt(255);
        return Color.argb(alpha, red, green, blue);
    }

}
