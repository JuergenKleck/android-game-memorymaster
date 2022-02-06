package com.juergenkleck.android.game.memorymaster.storage.dto;

import java.io.Serializable;

import com.juergenkleck.android.appengine.storage.dto.BasicTable;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
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
