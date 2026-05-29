package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;
import java.util.Random;


public class RandomMode implements PlaybackMode {

    private final Random random;

    public RandomMode() {
        this.random = new Random();
    }

    public RandomMode(long seed) {
        this.random = new Random(seed);
    }


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
