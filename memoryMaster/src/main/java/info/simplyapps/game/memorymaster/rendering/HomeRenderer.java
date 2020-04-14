package info.simplyapps.game.memorymaster.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Properties;

import info.simplyapps.game.memorymaster.R;
import info.simplyapps.game.memorymaster.rendering.kits.Renderkit;
import info.simplyapps.game.memorymaster.sprites.HomeViewSprites;
import info.simplyapps.gameengine.EngineConstants;
import info.simplyapps.gameengine.rendering.kits.ScreenKit;
import info.simplyapps.gameengine.rendering.kits.ScreenKit.ScreenPosition;

public class HomeRenderer extends MemoryRendererTemplate {

    long lastTime = 0l;

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

        lastTime = time;
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {

//        final long time = System.currentTimeMillis();

        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }
        if (getSprites().gBackground != null) {
            getSprites().gBackground.image.draw(canvas);
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

        getSprites().gBackground = loadGraphic(R.drawable.background);
        getSprites().gBackground.image.setBounds(0, 0, screenWidth, realScreenHeight);

//        Shader shader = new LinearGradient(0, 0, 0, realScreenHeight, Color.parseColor("#2dd0f4"), Color.parseColor("#19bff8"), TileMode.CLAMP);
//        getSprites().pBackground = new Paint(); 
//        getSprites().pBackground.setShader(shader);
//        getSprites().rBackground = new Rect(0, 0, screenWidth, realScreenHeight);

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


}
