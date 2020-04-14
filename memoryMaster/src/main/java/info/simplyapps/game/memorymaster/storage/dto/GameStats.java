package info.simplyapps.game.memorymaster.storage.dto;

import java.io.Serializable;

import info.simplyapps.appengine.storage.dto.BasicTable;

public class GameStats extends BasicTable implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6974204243261183588L;

    public long playtime;
    public long totalclicks;
    public long games;

}
