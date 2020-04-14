package info.simplyapps.game.memorymaster.rendering;

import android.content.Context;

import java.util.Properties;

import info.simplyapps.game.memorymaster.Constants;
import info.simplyapps.game.memorymaster.screens.HomeScreen;
import info.simplyapps.gameengine.rendering.GenericRendererTemplate;

public abstract class MemoryRendererTemplate extends GenericRendererTemplate {

    public MemoryRendererTemplate(Context context, Properties properties) {
        super(context, properties);
    }

    public HomeScreen getScreen() {
        return HomeScreen.class.cast(mContext);
    }

    @Override
    public boolean logEnabled() {
        return false;
    }

    @Override
    public float getCharSpacing() {
        return Constants.CHAR_SPACING;
    }

}
