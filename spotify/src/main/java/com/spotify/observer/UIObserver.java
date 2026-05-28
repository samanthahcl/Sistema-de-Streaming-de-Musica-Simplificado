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

    // Largura interna da caixa (sem as bordas ║ e espaços)
    private static final int BOX_WIDTH = 46;

    public UIObserver(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void onTrackStarted(Content content) {
        String header = displayName + " — Tocando agora";
        System.out.println();
        System.out.println("  ╔" + "═".repeat(BOX_WIDTH + 2) + "╗");
        printRow("🖥  " + header);
        System.out.println("  ╠" + "═".repeat(BOX_WIDTH + 2) + "╣");
        printRow("🎵  " + content.getTitle());
        printRow("👤  " + content.getAuthor());
        printRow("⏱  " + content.getFormattedDuration());
        printRow("🏷  " + content.getContentType());
        System.out.println("  ╚" + "═".repeat(BOX_WIDTH + 2) + "╝");
    }

    /**
     * Imprime uma linha da caixa com padding fixo à direita.
     * Calcula o preenchimento manualmente para não depender de
     * %-Ns, que não leva em conta a largura real dos emojis.
     */
    private void printRow(String text) {
        // Emojis ocupam 2 colunas de terminal cada; ajustamos o padding
        int visualWidth = visualLength(text);
        int padding = Math.max(0, BOX_WIDTH - visualWidth);
        System.out.println("  ║ " + text + " ".repeat(padding) + " ║");
    }

    /**
     * Estima a largura visual de uma string no terminal,
     * contando cada emoji como 2 colunas em vez de 1.
     */
    private int visualLength(String s) {
        int len = 0;
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            // Faixa básica de emojis e símbolos wide
            if ((cp >= 0x1F000 && cp <= 0x1FFFF) || // Emojis misc (🎵 👤 ⏱ 🏷 🖥)
                    (cp >= 0x2600  && cp <= 0x27BF)  || // Símbolos miscelâneos
                    (cp >= 0x2B00  && cp <= 0x2BFF)) {  // Setas / miscelânea
                len += 2; // wide
            } else {
                len += 1;
            }
            i += Character.charCount(cp);
        }
        return len;
    }
}