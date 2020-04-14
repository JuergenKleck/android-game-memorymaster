package info.simplyapps.game.memorymaster.system;

import info.simplyapps.gameengine.system.BasicGame;

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
