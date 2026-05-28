package com.spotify.factory;

import com.spotify.model.Content;
import com.spotify.model.Podcast;


public class PodcastCreator extends ContentCreator {

    private final String title;
    private final String author;
    private final int    durationSeconds;
    private final String showName;
    private final int    episodeNumber;
    private final String description;

    public PodcastCreator(String title, String author, int durationSeconds,
                          String showName, int episodeNumber, String description) {
        this.title           = title;
        this.author          = author;
        this.durationSeconds = durationSeconds;
        this.showName        = showName;
        this.episodeNumber   = episodeNumber;
        this.description     = description;
    }


    @Override
    public Content factoryMethod() {
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException(
                    "Número do episódio deve ser positivo. Recebido: " + episodeNumber);
        }
        return new Podcast(title, author, durationSeconds,
                showName, episodeNumber, description);
    }
}