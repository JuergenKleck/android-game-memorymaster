package info.simplyapps.game.memorymaster.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.view.MotionEvent;

import java.util.Properties;

import info.simplyapps.game.memorymaster.R;
import info.simplyapps.game.memorymaster.rendering.kits.Renderkit;
import info.simplyapps.game.memorymaster.sprites.HomeViewSprites;
import info.simplyapps.gameengine.EngineConstants;
import info.simplyapps.gameengine.rendering.kits.ScreenKit;
import info.simplyapps.gameengine.rendering.kits.ScreenKit.ScreenPosition;

public class LoadingRenderer extends MemoryRendererTemplate {

    public LoadingRenderer(Context context, Properties p) {
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
        return true;
    }

    @Override
    public void doUpdateRenderState() {
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {

        if (getSprites().gBackground != null) {
            getSprites().gBackground.image.setBounds(0, 0, screenWidth, screenHeight);
            getSprites().gBackground.image.draw(canvas);
        }
        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }

        drawText(canvas, getSprites().rMessage, mContext.getString(R.string.message_loading));
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

        Shader shader = new LinearGradient(0, 0, screenWidth, screenHeight, Color.parseColor("#bdcce3"), Color.parseColor("#0598ff"), TileMode.CLAMP);
        getSprites().pBackground = new Paint();
        getSprites().pBackground.setShader(shader);
        getSprites().rBackground = new Rect(0, 0, screenWidth, screenHeight);

        // button backgrounds
        getSprites().gButton = Renderkit.loadButtonGraphic(mContext.getResources(), R.drawable.button_flat, 0, 0, EngineConstants.ACTION_NONE);
        getSprites().gButtonOverlay = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_flat, 0, 0);

        getSprites().rMessage = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER, 0.5f, 0, 0, getSprites().rMessage);
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}
