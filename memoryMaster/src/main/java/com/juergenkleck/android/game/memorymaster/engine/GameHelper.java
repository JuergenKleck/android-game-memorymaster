package com.juergenkleck.android.game.memorymaster.engine;

import com.juergenkleck.android.game.memorymaster.R;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class GameHelper {

    public static int getGameSizeTitle(int i) {
        switch (i) {
            case GameValues.GAMESIZE_3_2:
                return R.string.option_gamesize_3_2;
            case GameValues.GAMESIZE_4_2:
                return R.string.option_gamesize_4_2;
            case GameValues.GAMESIZE_4_3:
                return R.string.option_gamesize_4_3;
            case GameValues.GAMESIZE_4_4:
                return R.string.option_gamesize_4_4;
            case GameValues.GAMESIZE_5_4:
                return R.string.option_gamesize_5_4;
            case GameValues.GAMESIZE_6_4:
                return R.string.option_gamesize_6_4;
            case GameValues.GAMESIZE_7_4:
                return R.string.option_gamesize_7_4;
        }
        return -1;
    }

    public static int getGameDeckTitle(int i) {
        switch (i) {
            case GameValues.DECK_ANIMALS:
                return R.string.option_screentitle_0;
            case GameValues.DECK_FRUITS:
                return R.string.option_screentitle_1;
            case GameValues.DECK_FLOWERS:
                return R.string.option_screentitle_2;
            case GameValues.DECK_STARSIGNS:
                return R.string.option_screentitle_3;
            case GameValues.DECK_COLORS:
                return R.string.option_screentitle_4;
            case GameValues.DECK_ALPHABET:
                return R.string.option_screentitle_5;
            case GameValues.DECK_SIGNS:
                return R.string.option_screentitle_6;
        }
        return -1;
    }

    public static int getGameDeckIcon(int i) {
        switch (i) {
            case GameValues.DECK_ANIMALS:
                return R.drawable.button_deck_animals;
            case GameValues.DECK_FRUITS:
                return R.drawable.button_deck_fruits;
            case GameValues.DECK_FLOWERS:
                return R.drawable.button_deck_flowers;
            case GameValues.DECK_STARSIGNS:
                return R.drawable.button_deck_starsigns;
            case GameValues.DECK_COLORS:
                return R.drawable.button_deck_colors;
            case GameValues.DECK_ALPHABET:
                return R.drawable.button_deck_alphabet;
            case GameValues.DECK_SIGNS:
                return R.drawable.button_deck_signs;
        }
        return -1;
    }

    public static int convertGameSize(int selectedSize, boolean x) {
        int freeX = GameValues.GAMESIZE_X_DEF;
        int freeY = GameValues.GAMESIZE_Y_DEF;
        switch (selectedSize) {
            case GameValues.GAMESIZE_3_2:
                freeX = 3;
                freeY = 2;
                break;
            case GameValues.GAMESIZE_4_2:
                freeX = 4;
                freeY = 2;
                break;
            case GameValues.GAMESIZE_4_3:
                freeX = 4;
                freeY = 3;
                break;
            case GameValues.GAMESIZE_4_4:
                freeX = 4;
                freeY = 4;
                break;
            case GameValues.GAMESIZE_5_4:
                freeX = 5;
                freeY = 4;
                break;
            case GameValues.GAMESIZE_6_4:
                freeX = 6;
                freeY = 4;
                break;
            case GameValues.GAMESIZE_7_4:
                freeX = 7;
                freeY = 4;
                break;
        }
        if (x) {
            return freeX;
        }
        return freeY;
    }

    public static int convertGameSize(int x, int y) {
        if (x == 3 && y == 2) {
            return GameValues.GAMESIZE_3_2;
        } else if (x == 4 && y == 2) {
            return GameValues.GAMESIZE_4_2;
        } else if (x == 4 && y == 3) {
            return GameValues.GAMESIZE_4_3;
        } else if (x == 4 && y == 4) {
            return GameValues.GAMESIZE_4_4;
        } else if (x == 5 && y == 4) {
            return GameValues.GAMESIZE_5_4;
        } else if (x == 6 && y == 4) {
            return GameValues.GAMESIZE_6_4;
        } else if (x == 7 && y == 4) {
            return GameValues.GAMESIZE_7_4;
        }
        return GameValues.GAMESIZE_3_2;
    }

}
