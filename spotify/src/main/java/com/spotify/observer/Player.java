package com.spotify.observer;

import com.spotify.model.Content;
import com.spotify.strategy.PlaybackMode;
import com.spotify.strategy.SequentialMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Player — núcleo do sistema de reprodução.
 *
 * Integra duas responsabilidades dos padrões GoF:
 *
 *  1. SUBJECT (Observer): mantém lista de observadores e os
 *     notifica sempre que uma nova faixa começa a tocar.
 *     Os observadores (UIObserver, HistoryService) reagem
 *     de forma independente, sem acoplamento ao Player.
 *
 *  2. CONTEXT (Strategy): delega a PlaybackMode o cálculo
 *     do próximo índice, permitindo trocar o algoritmo de
 *     reprodução em tempo de execução sem alterar esta classe.
 *
 *  3. CONSUMIDOR (Factory Method): aceita qualquer Content
 *     criado pelos creators concretos (MusicCreator,
 *     PodcastCreator), dependendo apenas da abstração.
 */
public class Player {

    // ── Playlist ─────────────────────────────────────────────
    private final List<Content> playlist = new ArrayList<>();
    private int currentIndex = -1; // -1 = nenhuma faixa ativa

    // ── Strategy: modo de reprodução (Context) ───────────────
    private PlaybackMode playbackMode;

    // ── Observer: lista de observadores (Subject) ─────────────
    private final List<TrackObserver> observers = new ArrayList<>();

    public Player() {
        this.playbackMode = new SequentialMode(); // estratégia padrão
    }

    // ══════════════════════════════════════════════════════════
    // Gerenciamento de Playlist
    // ══════════════════════════════════════════════════════════

    /** Adiciona uma faixa ao final da playlist. */
    public void addContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Conteúdo não pode ser nulo.");
        }
        playlist.add(content);
        System.out.printf("  ➕ Adicionado: %s%n", content.getTitle());
    }

    /** Retorna visualização somente-leitura da playlist. */
    public List<Content> getPlaylist() {
        return Collections.unmodifiableList(playlist);
    }

    // ══════════════════════════════════════════════════════════
    // Strategy — troca do modo de reprodução em runtime
    // ══════════════════════════════════════════════════════════

    /**
     * Substitui a estratégia de reprodução em tempo de execução.
     * Nenhuma lógica condicional no Player — apenas troca o objeto.
     */
    public void setPlaybackMode(PlaybackMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Modo de reprodução não pode ser nulo.");
        }
        this.playbackMode = mode;
        System.out.printf("%n  🔄 Modo alterado para: %s%n", mode.getModeName());
    }

    public PlaybackMode getPlaybackMode() { return playbackMode; }

    // ══════════════════════════════════════════════════════════
    // Observer — subscribe / unsubscribe / notify
    // ══════════════════════════════════════════════════════════

    /** Registra um observador para receber eventos de faixa. */
    public void addObserver(TrackObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /** Remove um observador previamente registrado. */
    public void removeObserver(TrackObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica TODOS os observadores registrados.
     * O Player publica o evento sem saber o que cada um fará.
     */
    private void notifyObservers(Content content) {
        for (TrackObserver observer : new ArrayList<>(observers)) {
            observer.onTrackStarted(content);
        }
    }

    // ══════════════════════════════════════════════════════════
    // Controles de Reprodução
    // ══════════════════════════════════════════════════════════

    /**
     * Inicia a reprodução a partir da primeira faixa.
     * Se já houver uma faixa tocando, reinicia do índice 0.
     */
    public void play() {
        if (playlist.isEmpty()) {
            System.out.println("  ⚠️  Playlist vazia. Adicione conteúdo primeiro.");
            return;
        }
        currentIndex = 0;
        playCurrentTrack();
    }

    /**
     * Avança para a próxima faixa delegando ao Strategy atual,
     * depois notifica todos os observadores (Observer).
     */
    public void next() {
        if (playlist.isEmpty()) {
            System.out.println("  ⚠️  Playlist vazia.");
            return;
        }
        if (currentIndex < 0) {
            // Nenhuma faixa foi iniciada ainda — começa do início
            play();
            return;
        }
        // Delega ao Strategy o cálculo do próximo índice
        currentIndex = playbackMode.next(playlist, currentIndex);
        playCurrentTrack();
    }

    /**
     * Reproduz a faixa no índice atual e dispara as notificações.
     */
    private void playCurrentTrack() {
        Content current = playlist.get(currentIndex);
        System.out.printf("%n  ▶  [%d/%d] via modo %s%n",
                currentIndex + 1, playlist.size(), playbackMode.getModeName());

        // Notifica observadores (Observer pattern)
        notifyObservers(current);
    }

    /** Exibe todas as faixas da playlist com seus índices. */
    public void printPlaylist() {
        System.out.printf("%n📀 Playlist (%d itens):%n", playlist.size());
        if (playlist.isEmpty()) {
            System.out.println("  (vazia)");
            return;
        }
        for (int i = 0; i < playlist.size(); i++) {
            String marker = (i == currentIndex) ? " ◀ tocando" : "";
            System.out.printf("  %d. %s%s%n", i + 1, playlist.get(i), marker);
        }
    }
}