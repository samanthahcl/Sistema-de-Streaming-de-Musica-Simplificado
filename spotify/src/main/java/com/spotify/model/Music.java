package com.spotify.model;

/**
 * Representa uma faixa musical na plataforma.
 * Estende Content adicionando atributos específicos de música.
 */
public class Music extends Content {

    private String genre;
    private String album;

    public Music(String title, String author, int durationSeconds, String genre, String album) {
        super(title, author, durationSeconds);
        this.genre = genre;
        this.album = album;
    }

    public String getGenre() { return genre; }
    public String getAlbum() { return album; }

    @Override
    public String getContentType() {
        return "Música";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Álbum: %s | Gênero: %s", album, genre);
    }
}
