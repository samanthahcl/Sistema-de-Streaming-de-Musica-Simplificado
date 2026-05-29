package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;


public class SequentialMode implements PlaybackMode {


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
