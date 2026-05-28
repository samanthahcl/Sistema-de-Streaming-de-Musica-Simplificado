package com.spotify.model;

/**
 * Representa um episódio de podcast na plataforma.
 * Estende Content adicionando atributos específicos de podcast.
 */
public class Podcast extends Content {

    private String showName;
    private int episodeNumber;
    private String description;

    public Podcast(String title, String author, int durationSeconds,
                   String showName, int episodeNumber, String description) {
        super(title, author, durationSeconds);
        this.showName = showName;
        this.episodeNumber = episodeNumber;
        this.description = description;
    }

    public String getShowName()    { return showName; }
    public int getEpisodeNumber()  { return episodeNumber; }
    public String getDescription() { return description; }

    @Override
    public String getContentType() {
        return "Podcast";
    }

    @Override
    public String toString() {
        return super.toString()
                + String.format(" | Show: %s | Ep. %d", showName, episodeNumber);
    }
}
