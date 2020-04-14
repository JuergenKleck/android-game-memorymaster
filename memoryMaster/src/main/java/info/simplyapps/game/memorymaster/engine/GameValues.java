package info.simplyapps.game.memorymaster.engine;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;

import info.simplyapps.game.memorymaster.R;

public class GameValues extends info.simplyapps.gameengine.system.GameValues {

    public static final int GAMESIZE_X_DEF = 3;
    public static final int GAMESIZE_Y_DEF = 2;
    public static final int GAMESIZE_3_2 = 0;
    public static final int GAMESIZE_4_2 = 1;
    public static final int GAMESIZE_4_3 = 2;
    public static final int GAMESIZE_4_4 = 3;
    public static final int GAMESIZE_5_4 = 4;
    public static final int GAMESIZE_6_4 = 5;
    public static final int GAMESIZE_7_4 = 6;
    public static final int GAMESIZE_MAX_X = 99;
    public static final int GAMESIZE_MAX_Y = 99;

    public static final int DECK_ANIMALS = 0;
    public static final int DECK_FRUITS = 1;
    public static final int DECK_FLOWERS = 2;
    public static final int DECK_STARSIGNS = 3;
    public static final int DECK_COLORS = 4;
    public static final int DECK_ALPHABET = 5;
    public static final int DECK_SIGNS = 6;

    public static final float scaleCards = 0.85f;
    public static final long gamePauseDelay = 0l;
    public static final long flipDelay = 1000l;

    public static final long roundTime = 300000l;

    public static final int EXPANSION_KIDS = 0;
    public static final int EXPANSION_MORE = 1;

    public static ColorFilter cFilterBlue = new LightingColorFilter(Color.parseColor("#4dcaff"), 255);
    public static ColorFilter cFilterRed = new LightingColorFilter(Color.parseColor("#fe5858"), 1);
    public static ColorFilter cFilterGreen = new LightingColorFilter(Color.parseColor("#67fe44"), 1);
    public static ColorFilter cFilterYellow = new LightingColorFilter(Color.parseColor("#fdff63"), 1);
    public static ColorFilter cFilterOrange = new LightingColorFilter(Color.parseColor("#ffa735"), 1);
    public static ColorFilter cFilterDarkRed = new LightingColorFilter(Color.parseColor("#cb2525"), 1);

    public static final ColorFilter cFilterGold = new LightingColorFilter(Color.parseColor("#e5e723"), 1);
    public static final ColorFilter cFilterSilver = new LightingColorFilter(Color.parseColor("#dedede"), 1);
    public static final ColorFilter cFilterBronze = new LightingColorFilter(Color.parseColor("#b77b13"), 1);
    public static final ColorFilter cFilterInactive = new LightingColorFilter(Color.parseColor("#999999"), 1);

    public static final int[] ALL_DECKS = {
            DECK_ANIMALS, DECK_FRUITS, DECK_FLOWERS, DECK_STARSIGNS, DECK_COLORS, DECK_ALPHABET, DECK_SIGNS
    };

    public static final int[] ALL_SIZES = {
            GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4, GAMESIZE_7_4
    };

    // game definitions
    public static final int[] BASIC_GAMESIZES = {
            GAMESIZE_3_2
            , GAMESIZE_4_3
            , GAMESIZE_5_4
    };

    public static final int[] BASIC_DECKS = {
            DECK_ANIMALS
            , DECK_FRUITS
    };

    public static final int[][] EXPANSIONS_DECKS = {
            {DECK_COLORS, DECK_ALPHABET, DECK_SIGNS} // KIDS
            , {DECK_FLOWERS, DECK_STARSIGNS} // MORE
    };

    public static final int[][] EXPANSIONS_GAMESIZES = {
            {GAMESIZE_4_2} // KIDS
            , {GAMESIZE_4_4, GAMESIZE_6_4, GAMESIZE_7_4} // MORE
    };

    // total definition
    public static final int[][] GAMESIZES = {
            {3, 2}  // 6 / 3
            , {4, 2} // 8 / 4
            , {4, 3} // 12 / 6
            , {4, 4} // 16 / 8
            , {5, 4} // 20 / 10
            , {6, 4} // 24 / 12
            , {7, 4} // 28 / 14
    };

    public static final int[][] DECK_GAMESIZES = {
            {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4, GAMESIZE_7_4} // animals
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4, GAMESIZE_7_4} // fruits
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4} // flowers
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4} // starsigns
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4} // colors
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4, GAMESIZE_7_4} // alphabet
            , {GAMESIZE_3_2, GAMESIZE_4_2, GAMESIZE_4_3, GAMESIZE_4_4, GAMESIZE_5_4, GAMESIZE_6_4} // signs
    };

    // get from XML definition
    public static final int[] DECK_DECKS = {
            R.array.deck_basic_animals // animals
            , R.array.deck_basic_fruits // fruits
            , R.array.deck_more_flowers // flowers
            , R.array.deck_more_starsigns // starsigns
            , R.array.deck_kids_colors // colors
            , R.array.deck_kids_alphabet // alphabet
            , R.array.deck_kids_signs // signs
    };

    // get from XML definition
    public static final int[] DECK_CARDS = {
            R.array.card_basic_animals // animals
            , R.array.card_basic_fruits // fruits
            , R.array.card_more_flowers // flowers
            , R.array.card_more_starsigns // starsigns
            , R.array.card_kids_colors // colors
            , R.array.card_kids_alphabet // alphabet
            , R.array.card_kids_signs // signs
    };

    // get from XML definition
    public static final int[] DECK_BACKGROUNDS = {
            R.array.background_basic_animals // animals
            , R.array.background_basic_fruits // fruits
            , R.array.background_more_flowers // flowers
            , R.array.background_more_starsigns // starsigns
            , R.array.background_kids_colors // colors
            , R.array.background_kids_alphabet // alphabet
            , R.array.background_kids_signs // signs
    };

}
