package com.spotify.observer;

import com.spotify.model.Content;
import com.spotify.strategy.PlaybackMode;
import com.spotify.strategy.SequentialMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    public void addContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Conteúdo não pode ser nulo.");
        }
        playlist.add(content);
        System.out.printf("  ➕ Adicionado: %s%n", content.getTitle());
    }

    public List<Content> getPlaylist() {
        return Collections.unmodifiableList(playlist);
    }

    // ══════════════════════════════════════════════════════════
    // Strategy — troca do modo de reprodução em runtime
    // ══════════════════════════════════════════════════════════


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

    public void addObserver(TrackObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(TrackObserver observer) {
        observers.remove(observer);
    }


    private void notifyObservers(Content content) {
        for (TrackObserver observer : new ArrayList<>(observers)) {
            observer.onTrackStarted(content);
        }
    }

    // ══════════════════════════════════════════════════════════
    // Controles de Reprodução
    // ══════════════════════════════════════════════════════════


    public void play() {
        if (playlist.isEmpty()) {
            System.out.println("  ⚠️  Playlist vazia. Adicione conteúdo primeiro.");
            return;
        }
        currentIndex = 0;
        playCurrentTrack();
    }


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


    private void playCurrentTrack() {
        Content current = playlist.get(currentIndex);
        System.out.printf("%n  ▶  [%d/%d] via modo %s%n",
                currentIndex + 1, playlist.size(), playbackMode.getModeName());

        // Notifica observadores (Observer pattern)
        notifyObservers(current);
    }

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