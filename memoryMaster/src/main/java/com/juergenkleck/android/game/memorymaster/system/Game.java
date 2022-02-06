package com.juergenkleck.android.game.memorymaster.system;

import com.juergenkleck.android.gameengine.system.BasicGame;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class Game extends BasicGame {

    public GameRound[] rounds;
    public int currentRound;
    public boolean finished;

    public Game(GameRound[] rounds) {
        this.rounds = rounds;
        this.currentRound = 0;
    }

    public GameRound getCurrentRound() {
        if (currentRound >= rounds.length) {
            currentRound = 0;
        }
        return rounds[currentRound];
    }

    public GameRound getNextRound() {
        currentRound++;
        if (currentRound >= rounds.length) {
            currentRound = 0;
        }
        return rounds[currentRound];
    }

    public boolean finished() {
        return finished;
    }

    @Override
    public boolean hasGame() {
        return currentRound >= 0;
    }

//	public boolean gameWon() {
//		return currentRound > rounds.length && life > 0;
//	}

}
