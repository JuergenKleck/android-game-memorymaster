package info.simplyapps.game.memorymaster.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Properties;

import info.simplyapps.game.memorymaster.Constants;
import info.simplyapps.game.memorymaster.R;
import info.simplyapps.game.memorymaster.engine.GameValues;
import info.simplyapps.game.memorymaster.rendering.kits.Renderkit;
import info.simplyapps.game.memorymaster.sprites.HomeViewSprites;
import info.simplyapps.gameengine.EngineConstants;
import info.simplyapps.gameengine.rendering.kits.ScreenKit;
import info.simplyapps.gameengine.rendering.kits.ScreenKit.ScreenPosition;

public class StatisticRenderer extends MemoryRendererTemplate {

    public StatisticRenderer(Context context, Properties p) {
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
                    if (containsClick(getSprites().rBtnBack, event.getX(), event.getY())) {
                        delayedActionHandler(Constants.ACTION_HOME, Constants.ACTION_HOME);
                    }
                    break;
            }

        }

        return true;
    }

    @Override
    public void doUpdateRenderState() {
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {


        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }
        if (getSprites().gBackground != null) {
            getSprites().gBackground.image.draw(canvas);
        }


        choiceBaseDraw(canvas, getSprites().rBtnBack, getSprites().gButtonOverlay, getSprites().gButton, activeButton, Constants.ACTION_HOME, GameValues.cFilterBlue);
        drawText(canvas, getSprites().rBtnBack, mContext.getString(R.string.menubutton_back), GameValues.cFilterBlue);
    }


    @Override
    public void restoreGameState() {
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
        getSprites().gButton = Renderkit.loadButtonGraphic(mContext.getResources(), R.drawable.button_background, 0, 0, EngineConstants.ACTION_NONE);
        getSprites().gButtonOverlay = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_background, 0, 0);

        getSprites().rMessage = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER, 0.5f, 0, 0, getSprites().rMessage);

        getSprites().rBtnStart = getSprites().gButton.image.copyBounds();
        getSprites().rBtnOptions = getSprites().gButton.image.copyBounds();
        getSprites().rBtnBack = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.4f, 0, 130, getSprites().rBtnStart);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER_TOP, 0.3f, 0, 380, getSprites().rBtnOptions);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.BOTTOM_LEFT, 0.25f, 50, 50, getSprites().rBtnBack);

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

}
