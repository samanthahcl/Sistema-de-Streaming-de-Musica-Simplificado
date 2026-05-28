package com.spotify.observer;

import com.spotify.model.Content;


public interface TrackObserver {


    void onTrackStarted(Content content);
}
