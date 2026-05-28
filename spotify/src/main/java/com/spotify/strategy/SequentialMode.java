package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;

/**
 * Estratégia Concreta: reprodução sequencial.
 *
 * Avança para a próxima faixa em ordem linear.
 * Ao chegar na última, volta para a primeira (loop).
 */
public class SequentialMode implements PlaybackMode {

    /**
     * Retorna o índice seguinte de forma circular.
     * Ex.: [0, 1, 2, 3] → depois do 3 volta ao 0.
     */
    @Override
    public int next(List<Content> playlist, int currentIndex) {
        if (playlist == null || playlist.isEmpty()) {
            throw new IllegalStateException("Playlist está vazia.");
        }
        return (currentIndex + 1) % playlist.size();
    }

    @Override
    public String getModeName() {
        return "Sequencial 🔁";
    }
}
