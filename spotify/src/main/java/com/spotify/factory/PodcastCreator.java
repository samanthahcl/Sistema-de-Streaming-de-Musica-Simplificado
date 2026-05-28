package com.spotify.factory;

import com.spotify.model.Content;
import com.spotify.model.Podcast;

/**
 * ============================================================
 * PADRÃO: FACTORY METHOD — Criador Concreto para Podcast
 * ============================================================
 *
 * Implementa factoryMethod() instanciando especificamente
 * um objeto Podcast. Toda a lógica exclusiva de Podcast
 * (ex.: validação de número de episódio) fica isolada aqui.
 * ============================================================
 */
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

    /**
     * Implementação concreta do Factory Method.
     * Aplica validação específica de Podcast e cria a instância.
     */
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