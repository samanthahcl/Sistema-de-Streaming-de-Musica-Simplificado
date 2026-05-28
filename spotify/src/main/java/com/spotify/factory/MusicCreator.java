package com.spotify.factory;

import com.spotify.model.Content;
import com.spotify.model.Music;


public class MusicCreator extends ContentCreator {

    private final String title;
    private final String author;
    private final int    durationSeconds;
    private final String genre;
    private final String album;

    public MusicCreator(String title, String author, int durationSeconds,
                        String genre, String album) {
        this.title           = title;
        this.author          = author;
        this.durationSeconds = durationSeconds;
        this.genre           = genre;
        this.album           = album;
    }


    @Override
    public Content factoryMethod() {
        return new Music(title, author, durationSeconds, genre, album);
    }
}