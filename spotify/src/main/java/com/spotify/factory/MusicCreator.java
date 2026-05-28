package com.spotify.factory;

import com.spotify.model.Content;
import com.spotify.model.Music;

/**
 * ============================================================
 * PADRÃO: FACTORY METHOD — Criador Concreto para Música
 * ============================================================
 *
 * Implementa factoryMethod() instanciando especificamente
 * um objeto Music. O cliente nunca usa "new Music(...)"
 * diretamente; delega a criação a este creator.
 * ============================================================
 */
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

    /**
     * Implementação concreta do Factory Method.
     * Cria e retorna uma instância de Music.
     */
    @Override
    public Content factoryMethod() {
        return new Music(title, author, durationSeconds, genre, album);
    }
}