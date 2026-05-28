package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;
import java.util.Random;

/**
 * Estratégia Concreta: reprodução aleatória.
 *
 * Escolhe aleatoriamente a próxima faixa, garantindo que
 * não seja a mesma que a atual (quando há mais de 1 item).
 */
public class RandomMode implements PlaybackMode {

    private final Random random;

    public RandomMode() {
        this.random = new Random();
    }

    /** Construtor alternativo para testes determinísticos. */
    public RandomMode(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Sorteia um índice diferente do atual.
     * Se a playlist tiver apenas 1 item, retorna 0.
     */
    @Override
    public int next(List<Content> playlist, int currentIndex) {
        if (playlist == null || playlist.isEmpty()) {
            throw new IllegalStateException("Playlist está vazia.");
        }
        if (playlist.size() == 1) {
            return 0;
        }

        int nextIndex;
        do {
            nextIndex = random.nextInt(playlist.size());
        } while (nextIndex == currentIndex);

        return nextIndex;
    }

    @Override
    public String getModeName() {
        return "Aleatório 🔀";
    }
}
