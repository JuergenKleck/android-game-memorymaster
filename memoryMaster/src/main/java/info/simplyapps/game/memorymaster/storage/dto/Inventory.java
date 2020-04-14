package info.simplyapps.game.memorymaster.storage.dto;

import java.io.Serializable;

import info.simplyapps.appengine.storage.dto.BasicTable;

public class Inventory extends BasicTable implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6974204243261183587L;

    public boolean migrated;

    public int selectedDeck;
    public int freeSizeX;
    public int freeSizeY;

}
