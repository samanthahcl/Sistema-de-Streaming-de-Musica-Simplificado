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
 * Integra três responsabilidades:
 *
 *  1. SUBJECT (Observer): mantém lista de observadores e os
 *     notifica sempre que uma faixa começa a tocar.
 *
 *  2. CONTEXT (Strategy): delega ao PlaybackMode como calcular
 *     a próxima faixa, podendo trocar a estratégia em runtime.
 *
 *  3. CONSUMIDOR (Factory): aceita qualquer Content criado
 *     pela ContentFactory, sem depender de tipos concretos.
 */
public class Player {

    // ── Playlist ──────────────────────────────────────────────
    private final List<Content> playlist = new ArrayList<>();
    private int currentIndex = -1; // -1 = nenhuma faixa ativa

    // ── Strategy: modo de reprodução ─────────────────────────
    private PlaybackMode playbackMode;

    // ── Observer: lista de observadores ──────────────────────
    private final List<TrackObserver> observers = new ArrayList<>();

    // ─────────────────────────────────────────────────────────

    public Player() {
        this.playbackMode = new SequentialMode(); // padrão
    }

    // ══════════════════════════════════════════════════════════
    // Gerenciamento de Playlist
    // ══════════════════════════════════════════════════════════

    /** Adiciona uma faixa ao final da playlist. */
    public void addContent(Content content) {
        if (content == null) throw new IllegalArgumentException("Conteúdo não pode ser nulo.");
        playlist.add(content);
        System.out.printf("  ➕ Adicionado à playlist: %s%n", content.getTitle());
    }

    /** Retorna visualização somente-leitura da playlist. */
    public List<Content> getPlaylist() {
        return Collections.unmodifiableList(playlist);
    }

    // ══════════════════════════════════════════════════════════
    // Strategy: troca de modo de reprodução
    // ══════════════════════════════════════════════════════════

    /**
     * Troca a estratégia de reprodução em tempo de execução.
     * Nenhuma lógica condicional — apenas substitui o objeto.
     */
    public void setPlaybackMode(PlaybackMode mode) {
        if (mode == null) throw new IllegalArgumentException("Modo não pode ser nulo.");
        this.playbackMode = mode;
        System.out.printf("%n  🔄 Modo de reprodução alterado para: %s%n", mode.getModeName());
    }

    public PlaybackMode getPlaybackMode() { return playbackMode; }

    // ══════════════════════════════════════════════════════════
    // Observer: gerenciamento de observadores (subscribe/unsubscribe)
    // ══════════════════════════════════════════════════════════

    /** Registra um observador para receber notificações de faixa. */
    public void addObserver(TrackObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /** Remove um observador previamente registrado. */
    public void removeObserver(TrackObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica TODOS os observadores registrados.
     * O Player não sabe o que cada um fará — apenas publica o evento.
     */
    private void notifyObservers(Content content) {
        for (TrackObserver observer : observers) {
            observer.onTrackStarted(content);
        }
    }

    // ══════════════════════════════════════════════════════════
    // Controles de Reprodução
    // ══════════════════════════════════════════════════════════

    /**
     * Inicia a reprodução da primeira faixa da playlist.
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
     * Avança para a próxima faixa usando a estratégia atual (Strategy).
     * Em seguida, notifica todos os observadores (Observer).
     */
    public void next() {
        if (playlist.isEmpty()) {
            System.out.println("  ⚠️  Playlist vazia.");
            return;
        }
        if (currentIndex < 0) {
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
        System.out.printf("%n  ▶  Reproduzindo [%d/%d] via modo %s%n",
                currentIndex + 1, playlist.size(), playbackMode.getModeName());

        // Notifica observadores (Observer pattern)
        notifyObservers(current);
    }

    /**
     * Exibe todas as faixas da playlist com seus índices.
     */
    public void printPlaylist() {
        System.out.println("\n📀 Playlist (" + playlist.size() + " itens):");
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
