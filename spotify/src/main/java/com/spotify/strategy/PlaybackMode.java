package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;


public interface PlaybackMode {


    int next(List<Content> playlist, int currentIndex);

    String getModeName();
}
