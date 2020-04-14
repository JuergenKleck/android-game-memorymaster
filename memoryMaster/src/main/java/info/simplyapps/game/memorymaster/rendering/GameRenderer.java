package info.simplyapps.game.memorymaster.rendering;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import info.simplyapps.game.memorymaster.Constants;
import info.simplyapps.game.memorymaster.R;
import info.simplyapps.game.memorymaster.engine.GameValues;
import info.simplyapps.game.memorymaster.rendering.kits.Renderkit;
import info.simplyapps.game.memorymaster.rendering.objects.CardObject;
import info.simplyapps.game.memorymaster.sprites.GameViewSprites;
import info.simplyapps.game.memorymaster.storage.DBDriver;
import info.simplyapps.game.memorymaster.storage.StorageUtil;
import info.simplyapps.game.memorymaster.storage.StoreData;
import info.simplyapps.game.memorymaster.storage.dto.GameStats;
import info.simplyapps.game.memorymaster.system.Game;
import info.simplyapps.game.memorymaster.system.GameRound;
import info.simplyapps.gameengine.EngineConstants;
import info.simplyapps.gameengine.engine.GameEngine;
import info.simplyapps.gameengine.rendering.kits.ScreenKit;
import info.simplyapps.gameengine.rendering.kits.ScreenKit.ScreenPosition;
import info.simplyapps.gameengine.rendering.objects.Graphic;
import info.simplyapps.gameengine.system.BasicGame;
import info.simplyapps.gameengine.system.GameState;
import info.simplyapps.gameengine.system.GameSubState;

public class GameRenderer extends MemoryRendererTemplate implements GameEngine {

    private GameState mMode;
    private GameSubState mSubMode;

    private static Random RND = new Random();

    private Game mGame;

    private long delay = 0l;
    private long lastTime;

    int tileWidth;
    int tileHeight;

    int borderLR;
    int borderTB;

    int gameMapWidth;
    int gameMapHeight;

    long unflip;

    boolean continueGame;
    List<CardObject> resumeCards;

    public GameRenderer(Context context, Properties p) {
        super(context, p);
        mMode = GameState.NONE;
        mSubMode = GameSubState.NONE;
    }

    public GameViewSprites getSprites() {
        return GameViewSprites.class.cast(super.sprites);
    }

    private void createGame() {
        mGame = new Game(new GameRound[]{
                new GameRound(0, GameValues.roundTime, 0)
        });
        delay = GameValues.gamePauseDelay;
    }

    public synchronized BasicGame getGame() {
        return mGame;
    }

    /**
     * Starts the game, setting parameters for the current difficulty.
     */
    public void doStart() {
        if (mMode == GameState.NONE) {
            setMode(GameState.INIT);
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    public synchronized void pause() {
        saveGameState();
        setSubMode(GameSubState.PAUSE);
    }

    /**
     * Resumes from a pause.
     */
    public synchronized void unpause() {
        //set state back to running
        delay = GameValues.gamePauseDelay;
        lastTime = System.currentTimeMillis();
        setSubMode(GameSubState.NONE);
    }

    public synchronized void restoreGameState() {
        resumeCards = StorageUtil.restoreCurrentGame(StoreData.getInstance().currentGame);
        if (isGameComplete(resumeCards)) {
            continueGame = false;
            createGame();
        } else {
            continueGame = true;
        }
        updateRoundGraphic();
    }

    public synchronized void saveGameState() {
        StoreData.getInstance().currentGame = StorageUtil.storeCurrentGame(getSprites().gCards);
        DBDriver.getInstance().store(StoreData.getInstance().currentGame);
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when the
     * Activity is being restored after having been previously destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    public synchronized void restoreState(Bundle savedState) {
        setMode(GameState.INIT);
        restoreGameState();
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     *
     * @return Bundle with this view's state
     */
    public Bundle saveState(Bundle map) {
        if (map != null) {
            saveGameState();
        }
        return map;
    }

    private void updateStatistics() {
        GameStats stats = new GameStats();
        stats.playtime += mGame.getCurrentRound().time;
        stats.totalclicks += Integer.valueOf(mGame.getCurrentRound().clicks).longValue();
        stats.games += 1;
        DBDriver.getInstance().store(stats);
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     */
    public synchronized void setMode(GameState mode) {
        mMode = mode;
    }

    public synchronized GameState getMode() {
        return mMode;
    }

    public synchronized void setSubMode(GameSubState mode) {
        mSubMode = mode;
    }

    public synchronized GameSubState getSubMode() {
        return mSubMode;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean blocked = false;

        final long time = System.currentTimeMillis();

        if (mMode == GameState.PLAY) {
            if (mSubMode == GameSubState.NONE) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        // block click if flipped
                        if (unflip < time) {
                            int x = Float.valueOf(event.getX()).intValue();
                            int y = Float.valueOf(event.getY()).intValue();

                            int clicks = 0;
                            for (CardObject cg : getSprites().gCards) {
                                if (!cg.found && cg.flipped) {
                                    clicks += 1;
                                }
                            }

                            if (clicks < 2) {
                                // determine tile
                                for (CardObject cg : getSprites().gCards) {
                                    if (!cg.found && !cg.flipped && containsClick(calculateCardRect(cg.tx, cg.ty), x, y)) {
                                        cg.flipped = true;
                                        clicks += 1;
                                        mGame.getCurrentRound().clicks += 1;
                                        break;
                                    }
                                }
                            }
                            if (clicks >= 2) {
                                // determine tile
                                for (CardObject cg1 : getSprites().gCards) {
                                    if (!cg1.found && cg1.flipped) {
                                        // find matching tile
                                        for (CardObject cg2 : getSprites().gCards) {
                                            if (!cg2.found && cg2.flipped && cg1.type == cg2.type && cg1.id != cg2.id) {
                                                cg1.found = true;
                                                cg2.found = true;
                                                break;
                                            }
                                        }

                                    }
                                }
                                // unflip delay for graphic rendering
                                unflip = time + GameValues.flipDelay;
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (containsClick(getSprites().rBtnPause, event.getX(), event.getY())) {
                            setSubMode(GameSubState.PAUSE);
                            blocked = true;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

            }
        }

        if (mMode == GameState.END && mGame.finished()) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
//                    if(containsClick(getSprites().rBtnBack, event.getX(), event.getY())) {
                    delayedActionHandler(Constants.ACTION_HOME, Constants.ACTION_HOME);
//                    }
            }
        }

        if (!blocked && mSubMode == GameSubState.PAUSE) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    if (containsClick(getSprites().rBtnResume, event.getX(), event.getY())) {
                        unpause();
                    }
                    if (containsClick(getSprites().rBtnBack, event.getX(), event.getY())) {
                        delayedActionHandler(Constants.ACTION_HOME, Constants.ACTION_HOME);
                    }
                    break;
            }
        }

        return true;
    }

    /**
     * Update the graphic x/y values in real time. This is called before the
     * draw() method
     */
    private void updatePhysics() {
        // the fixed time for drawing this frame
        final long time = System.currentTimeMillis();

        if (mMode == GameState.PLAY) {

            // unflip cards
            if (unflip > 0l && unflip < time) {
                for (CardObject cg : getSprites().gCards) {
                    if (cg.flipped) {
                        cg.flipped = false;
                    }
                }
                unflip = 0l;
            }

            // check if all are clicked
            if (unflip <= 0l && isGameComplete(getSprites().gCards)) {
                mGame.getCurrentRound().done = true;
                mGame.finished = true;
            }

            // TODO make card flip animated


        }
    }

    private boolean isGameComplete(List<CardObject> list) {
        boolean complete = true;
        if (list != null && list.size() > 0) {
            for (CardObject cg : list) {
                if (!cg.found) {
                    complete = false;
                }
            }
        }
        return complete;
    }

    public void setBonus(boolean b) {
    }

    @Override
    public void doInitThread(long time) {
        super.sprites = new GameViewSprites();

        // button backgrounds
        getSprites().gButton = Renderkit.loadButtonGraphic(mContext.getResources(), R.drawable.button_flat, 0, 0, EngineConstants.ACTION_NONE);
        getSprites().gButtonOverlay = Renderkit.loadGraphic(mContext.getResources(), R.drawable.button_flat, 0, 0);

        getSprites().rBtnBack = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.BOTTOM_LEFT, 0.20f, 50, 25, getSprites().rBtnBack);

        getSprites().rBtnPause = getSprites().gButton.image.copyBounds();
        getSprites().rBtnResume = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, realScreenHeight, ScreenPosition.BOTTOM_LEFT, 0.2f, 20, 0, getSprites().rBtnPause);
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.TOP_RIGHT, 0.3f, 30, 30, getSprites().rBtnResume);
        getSprites().rBtnPause.bottom -= getSprites().rBtnPause.height() / 3;
        getSprites().rBtnResume.bottom -= getSprites().rBtnResume.height() / 3;

        getSprites().rMessage = getSprites().gButton.image.copyBounds();
        ScreenKit.scaleRect(screenWidth, screenHeight, ScreenPosition.CENTER, 0.6f, 0, 0, getSprites().rMessage);

        if (mGame == null) {
            createGame();
        }

        // from home store data
//        int size = StoreDataNew.getInstance().inventory.selectedSize;
        gameMapWidth = StoreData.getInstance().inventory.freeSizeX;//GameValues.GAMESIZES[size][0];
        gameMapHeight = StoreData.getInstance().inventory.freeSizeY;//GameValues.GAMESIZES[size][1];

        // calculate optimum tile size
        int h = screenHeight / gameMapHeight;
        int reduce = ((gameMapWidth * h) - screenWidth) / gameMapWidth;
        if (reduce > 0) {
            h -= reduce;
        }

        tileHeight = h;
        tileWidth = tileHeight;

        borderLR = (screenWidth - (gameMapWidth * tileWidth)) / 2;
        borderTB = (screenHeight - (gameMapHeight * tileHeight)) / 2;


        // create graphic objects
        if (getSprites().gCardGraphics == null) {
            getSprites().gCardGraphics = new ArrayList<Graphic>();
        }
        if (getSprites().gCards == null) {
            getSprites().gCards = new ArrayList<CardObject>();
        }

    }


    private void updateRoundGraphic() {
        // reset values
        mGame.getCurrentRound().done = false;

        // create background
        TypedArray backgrounds = mContext.getResources().obtainTypedArray(GameValues.DECK_BACKGROUNDS[StoreData.getInstance().inventory.selectedDeck]);
        Drawable bg = backgrounds.getDrawable(RND.nextInt(backgrounds.length()));

        getSprites().gBackground = new Graphic(bg);
        getSprites().gBackground.image.setBounds(0, 0, screenWidth, realScreenHeight);

        buildLevel();

        backgrounds.recycle();
    }


    /**
     * Invoked from 2. Step
     */
    protected void buildLevel() {

        // load from selection
        int game = StoreData.getInstance().inventory.selectedDeck;

        TypedArray cards = mContext.getResources().obtainTypedArray(GameValues.DECK_CARDS[game]);
        TypedArray decks = mContext.getResources().obtainTypedArray(GameValues.DECK_DECKS[game]);

        // design level
        Drawable deck = decks.getDrawable(RND.nextInt(decks.length()));
        getSprites().gDeck = new Graphic(deck);

        // prepare level
        List<String> totalCards = new ArrayList<String>();

        // safety check
        if (continueGame && gameMapHeight * gameMapWidth != resumeCards.size()) {
            continueGame = false;
        }

        // re-create cache
        getSprites().gCardGraphics.clear();
        getSprites().gCards.clear();

        if (!continueGame) {

            // select game cards
            int totalCardSize = (gameMapWidth * gameMapHeight) / 2;
            while (totalCards.size() < totalCardSize) {
                int card = RND.nextInt(cards.length());
                if (!totalCards.contains(Integer.toString(card))) {
                    totalCards.add(Integer.toString(card));
                }
                if (totalCards.size() == cards.length()) {
                    // security reason for endless loop
                    break;
                }
            }

            // duplicate
            totalCards.addAll(totalCards);

            // random shuffle
            Collections.shuffle(totalCards, RND);
            Collections.shuffle(totalCards, RND);
            Collections.shuffle(totalCards, RND);
            Collections.shuffle(totalCards, RND);

            // create game cards
            int card = 0;
            // vertical
            for (int i = 0; i < gameMapHeight; i++) {
                if (card >= totalCards.size()) {
                    break;
                }
                // horizontal
                for (int j = 0; j < gameMapWidth; j++, card++) {
                    if (card >= totalCards.size()) {
                        break;
                    }
                    // new card 
                    createCard(cards, j, i, GameValues.scaleCards, Integer.parseInt(totalCards.get(card)));
                }
            }

        } else {
            final long time = System.currentTimeMillis();
            // just restore the graphics
            Collections.sort(resumeCards, new Comparator<CardObject>() {
                @Override
                public int compare(CardObject lhs, CardObject rhs) {
                    return Integer.valueOf(lhs.gReference).compareTo(Integer.valueOf(rhs.gReference));
                }
            });
            int clicks = 0;
            for (CardObject card : resumeCards) {
                createCardGraphic(cards, card.type);
                getSprites().gCards.add(card);
                if (!card.found && card.flipped) {
                    clicks += 1;
                }
            }
            if (clicks >= 2) {
                unflip = time + GameValues.flipDelay;
            }
        }

        decks.recycle();
        cards.recycle();
    }

    private int createCardGraphic(TypedArray objArray, int card) {
        Drawable draw = objArray.getDrawable(card);

        int reference = -1;
        for (CardObject tmp : getSprites().gCards) {
            if (tmp.type == card) {
                reference = tmp.gReference;
                break;
            }
        }
        if (reference < 0) {
            Graphic g = new Graphic(draw);
            g.image.setBounds(0, 0, tileWidth, tileHeight);
            ScreenKit.scaleImage(screenWidth, screenHeight, ScreenPosition.CENTER, 0.15f, 0, 0, g);
            getSprites().gCardGraphics.add(g);
            reference = getSprites().gCardGraphics.size() - 1;
        }
        return reference;
    }

    private void createCard(TypedArray objArray, int tx, int ty, float scale, int card) {
        CardObject c = new CardObject();
        c.type = card;
        c.tx = tx;
        c.ty = ty;
        c.flipped = false;
        c.found = false;
        c.gReference = createCardGraphic(objArray, card);
        getSprites().gCards.add(c);
    }


    @Override
    public void doUpdateRenderState() {
        final long time = System.currentTimeMillis();

        if (delay > 0l && lastTime > 0l) {
            delay -= time - lastTime;
        }


        switch (mMode) {
            case NONE: {
                // move to initialization
                setMode(GameState.INIT);
            }
            break;
            case INIT: {

                if (mGame.currentRound < 0) {
                    mGame.currentRound = 0;
                }

                updateRoundGraphic();

                // setup the game
                setMode(GameState.READY);

            }
            break;
            case READY: {
                if (delay <= 0l) {
                    setMode(GameState.PLAY);
                }
            }
            break;
            case PLAY: {
                // active gameplay
                // calculate game time
                if (delay <= 0l && lastTime > 0l) {
                    if (mSubMode == GameSubState.NONE) {
                        mGame.getCurrentRound().time -= time - lastTime;
                    }
                }

                // update graphic positions
                updatePhysics();

                if (mGame.getCurrentRound().done) {
                    setMode(GameState.END);
                }
            }
            break;
            case END: {
                if (!mGame.finished) {
                    updateStatistics();
                }
                mGame.finished = true;
            }
            break;
            default:
                setMode(GameState.NONE);
                break;
        }

        lastTime = time;
    }

    @Override
    public void doDrawRenderer(Canvas canvas) {

        if (getSprites().pBackground != null) {
            canvas.drawRect(getSprites().rBackground, getSprites().pBackground);
        }
        if (getSprites().gBackground != null) {
            getSprites().gBackground.image.draw(canvas);
        }

        if (mMode == GameState.END) {
            drawText(canvas, getSprites().rMessage, mContext.getString(R.string.message_finished));

            choiceBaseDraw(canvas, getSprites().rBtnBack, getSprites().gButtonOverlay, getSprites().gButton, activeButton, Constants.ACTION_HOME, GameValues.cFilterGreen);
            drawText(canvas, getSprites().rBtnBack, mContext.getString(R.string.menubutton_back), GameValues.cFilterRed);
        }

        if (mGame.hasGame()) {

            if (mMode == GameState.PLAY && mSubMode == GameSubState.PAUSE) {
                drawText(canvas, getSprites().rMessage, mContext.getString(R.string.message_pause));

                choiceBaseDraw(canvas, getSprites().rBtnResume, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterGreen);
                drawText(canvas, getSprites().rBtnResume, mContext.getString(R.string.menubutton_resume));

                choiceBaseDraw(canvas, getSprites().rBtnBack, getSprites().gButtonOverlay, getSprites().gButton, activeButton, Constants.ACTION_HOME, GameValues.cFilterGreen);
                drawText(canvas, getSprites().rBtnBack, mContext.getString(R.string.menubutton_back), GameValues.cFilterRed);

            }

            // draw round number
            if (mMode == GameState.READY) {
                drawText(canvas, getSprites().rMessage, mContext.getString(R.string.message_loading));
            }
        }


        // the fixed time for drawing this frame
//        final long time = System.currentTimeMillis();

        if (mMode == GameState.PLAY && mSubMode == GameSubState.NONE) {


            for (CardObject o : getSprites().gCards) {
                Rect card = calculateCardRect(o.tx, o.ty);
//                Rect bounds = getSprites().gCardGraphics.get(o.gReference).image.copyBounds();
//                bounds.offsetTo(card.left, card.top);
                if (o.found && o.flipped && unflip > 0l) {
                    getSprites().gCardGraphics.get(o.gReference).image.setBounds(card);
                    getSprites().gCardGraphics.get(o.gReference).image.draw(canvas);
                } else if (!o.found && o.flipped) {
                    getSprites().gCardGraphics.get(o.gReference).image.setBounds(card);
                    getSprites().gCardGraphics.get(o.gReference).image.draw(canvas);
                } else if (!o.found && !o.flipped) {
                    getSprites().gDeck.image.setBounds(card);
                    getSprites().gDeck.image.draw(canvas);
                }
            }

            // TODO make cardflip animated


            choiceBaseDraw(canvas, getSprites().rBtnPause, getSprites().gButtonOverlay, getSprites().gButton, true, false, GameValues.cFilterGreen);
            drawText(canvas, getSprites().rBtnPause, mContext.getString(R.string.menubutton_pause));
        }

        if (mMode == GameState.NONE) {
            drawText(canvas, getSprites().rMessage, mContext.getString(R.string.message_loading));
        }

    }

    private Rect calculateCardRect(int tx, int ty) {
        Rect r = new Rect();
        r.left = borderLR + tileWidth * tx;
        r.top = borderTB + tileHeight * ty;
        r.right = r.left + tileWidth;
        r.bottom = r.top + tileHeight;
        return r;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}

