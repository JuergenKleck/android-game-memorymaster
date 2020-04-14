package info.simplyapps.game.memorymaster;


public class Constants {

    public static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    public static final String UPDATE_CHECK_URL = "http://game.simplyapps.info/memorymaster.xml";

    public static final String DATABASE = "memorymaster.db";
    public static final int DATABASE_VERSION = 2;

    public static final String PACKAGE_NAME = "info.simplyapps.game.memorymaster";
    public static final String PREFERENCE_NS = "http://info.simplyapps.game.memorymaster.rendering";

    public static final int ACTION_STATISTIC = 100;
    public static final int ACTION_RESUME = 202;
    public static final int ACTION_HOME = 300;

    public static final int spaceLR = 10;
    public static final int spaceTB = 8;
    public static final int spaceTBWide = 48;

    public static final float CHAR_SPACING = 0.35f;

    public enum RenderMode {
        HOME, GAME, MENU, WAIT;
    }

    public enum SubRenderMode {
        NONE, DECK, SIZE;
    }

}
