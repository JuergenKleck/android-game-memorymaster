package com.juergenkleck.android.game.memorymaster.rendering.objects;

/**
 * Android app - MemoryMaster
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class CardObject {

    public boolean flipped;
    public boolean found;
    public int type;

    public int tx;
    public int ty;

    public int gReference;

    public int id;

    public CardObject() {
        id = this.hashCode();
    }

}
