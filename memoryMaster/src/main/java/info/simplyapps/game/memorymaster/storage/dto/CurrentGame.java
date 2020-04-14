package info.simplyapps.game.memorymaster.storage.dto;

import java.io.Serializable;

import info.simplyapps.appengine.storage.dto.BasicTable;

public class CurrentGame extends BasicTable implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6974204243261183590L;

    public boolean[] flipped;
    public boolean[] found;
    public int[] type;
    public int[] tx;
    public int[] ty;
    public int[] gReference;

}
