package com.spotify.model;

/**
 * Classe abstrata base para todos os tipos de conteúdo da plataforma.
 * Representa o contrato comum entre Música e Podcast.
 */
public abstract class Content {

    protected String title;
    protected String author;
    protected int durationSeconds;

    public Content(String title, String author, int durationSeconds) {
        this.title = title;
        this.author = author;
        this.durationSeconds = durationSeconds;
    }

    public String getTitle()  { return title; }
    public String getAuthor() { return author; }
    public int getDurationSeconds() { return durationSeconds; }

    /**
     * Formata a duração em MM:SS para exibição.
     */
    public String getFormattedDuration() {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Cada subclasse deve descrever seu tipo de conteúdo.
     */
    public abstract String getContentType();

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" — %s (%s)",
                getContentType(), title, author, getFormattedDuration());
    }
}
