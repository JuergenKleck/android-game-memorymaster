package com.juergenkleck.android.game.memorymaster.storage.dto;

import java.io.Serializable;

import com.juergenkleck.android.appengine.storage.dto.BasicTable;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
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
