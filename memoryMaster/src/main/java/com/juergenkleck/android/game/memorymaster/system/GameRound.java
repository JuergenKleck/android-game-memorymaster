package com.juergenkleck.android.game.memorymaster.system;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class GameRound extends com.juergenkleck.android.gameengine.system.GameRound {

    public boolean done;
    public int clicks;

    public GameRound(int round, long time, int background) {
        super(round, time, background);
    }

    public GameRound(int round, long time) {
        super(round, time, 0);
    }

    public GameRound() {
        super(0, 0, 0);
    }

}
