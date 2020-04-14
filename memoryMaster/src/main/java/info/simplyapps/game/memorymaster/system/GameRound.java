package info.simplyapps.game.memorymaster.system;

public class GameRound extends info.simplyapps.gameengine.system.GameRound {

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
