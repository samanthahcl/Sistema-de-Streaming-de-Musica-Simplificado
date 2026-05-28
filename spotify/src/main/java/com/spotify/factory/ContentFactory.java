package com.spotify.factory;

import com.spotify.model.Content;
import com.spotify.model.Music;
import com.spotify.model.Podcast;

/**
 * ============================================================
 * PADRÃO: FACTORY METHOD (GoF — Criacional)
 * ============================================================
 *
 * O QUÊ:
 *   A ContentFactory centraliza a criação de objetos de conteúdo
 *   (Music, Podcast) através de métodos de fábrica estáticos.
 *   O cliente nunca usa "new Music(...)" diretamente; ele delega
 *   essa responsabilidade à fábrica.
 *
 * POR QUÊ:
 *   - Evita espalhamento de lógica de criação pelo código.
 *   - Se precisarmos validar parâmetros ou aplicar defaults,
 *     fazemos em UM único lugar.
 *   - Adicionar um novo tipo (ex.: Audiobook) exige apenas criar
 *     a classe e um novo método aqui, sem tocar no restante.
 *
 * COMO:
 *   - createMusic() e createPodcast() são os "factory methods".
 *   - Ambos retornam o tipo base Content, mantendo o código
 *     cliente desacoplado das implementações concretas.
 * ============================================================
 */
public class ContentFactory {

    // Impede instanciação — classe utilitária
    private ContentFactory() {}

    /**
     * Factory Method para criação de músicas.
     *
     * @param title           Título da música
     * @param author          Nome do artista
     * @param durationSeconds Duração em segundos
     * @param genre           Gênero musical
     * @param album           Nome do álbum
     * @return instância de Music encapsulada como Content
     */
    public static Content createMusic(String title, String author,
                                      int durationSeconds, String genre, String album) {
        validateBasicFields(title, author, durationSeconds);
        return new Music(title, author, durationSeconds, genre, album);
    }

    /**
     * Factory Method para criação de podcasts.
     *
     * @param title           Título do episódio
     * @param author          Nome do apresentador
     * @param durationSeconds Duração em segundos
     * @param showName        Nome do programa
     * @param episodeNumber   Número do episódio
     * @param description     Descrição curta do episódio
     * @return instância de Podcast encapsulada como Content
     */
    public static Content createPodcast(String title, String author,
                                        int durationSeconds, String showName,
                                        int episodeNumber, String description) {
        validateBasicFields(title, author, durationSeconds);
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Número do episódio deve ser positivo.");
        }
        return new Podcast(title, author, durationSeconds, showName, episodeNumber, description);
    }

    /**
     * Validação comum a todos os tipos de conteúdo.
     * Centralizada aqui — mais um benefício do Factory Method.
     */
    private static void validateBasicFields(String title, String author, int durationSeconds) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Autor não pode ser vazio.");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero.");
        }
    }
}
