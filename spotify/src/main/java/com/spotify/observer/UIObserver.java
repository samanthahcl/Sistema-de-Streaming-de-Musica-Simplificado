package com.spotify.observer;

import com.spotify.model.Content;

/**
 * Observador Concreto: simula a interface gráfica do usuário.
 *
 * Quando notificado, exibe no console as informações da faixa
 * como se fossem atualizações na tela do aplicativo.
 */
public class UIObserver implements TrackObserver {

    private final String displayName;

    public UIObserver(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void onTrackStarted(Content content) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.printf ("║  🖥  %-44s║%n", displayName + " — Tocando agora");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.printf ("║  🎵  %-44s║%n", content.getTitle());
        System.out.printf ("║  👤  %-44s║%n", content.getAuthor());
        System.out.printf ("║  ⏱  %-44s║%n", content.getFormattedDuration());
        System.out.printf ("║  🏷  %-44s║%n", content.getContentType());
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
