package com.spotify.main;

import com.spotify.factory.ContentFactory;
import com.spotify.model.Content;
import com.spotify.observer.HistoryService;
import com.spotify.observer.Player;
import com.spotify.observer.UIObserver;
import com.spotify.strategy.RandomMode;
import com.spotify.strategy.SequentialMode;

/**
 * ============================================================
 * Classe Principal — Simulação do Sistema de Streaming
 * ============================================================
 *
 * Demonstra os três padrões GoF aplicados em conjunto:
 *
 *  ✅ Factory Method  → ContentFactory.createMusic / createPodcast
 *  ✅ Strategy        → SequentialMode / RandomMode no Player
 *  ✅ Observer        → UIObserver e HistoryService notificados pelo Player
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        printBanner();

        // ══════════════════════════════════════════════════════
        // 1. FACTORY METHOD — Criação de conteúdos
        //    O cliente usa a fábrica; nunca "new Music()" direto.
        // ══════════════════════════════════════════════════════
        printSection("1. Factory Method — Criando conteúdos");

        Content music1 = ContentFactory.createMusic(
                "Bohemian Rhapsody", "Queen", 354, "Rock", "A Night at the Opera");

        Content music2 = ContentFactory.createMusic(
                "Blinding Lights", "The Weeknd", 200, "Pop/Synthwave", "After Hours");

        Content music3 = ContentFactory.createMusic(
                "Lose Yourself", "Eminem", 326, "Hip-Hop", "8 Mile Soundtrack");

        Content podcast1 = ContentFactory.createPodcast(
                "Inteligência Artificial em 2025", "Lex Fridman", 4500,
                "Lex Fridman Podcast", 412,
                "Discussão sobre os avanços recentes em LLMs e robótica.");

        Content podcast2 = ContentFactory.createPodcast(
                "Clean Code na Prática", "Uncle Bob", 3600,
                "Software Engineering Daily", 87,
                "Princípios SOLID e padrões de projeto no dia a dia.");

        System.out.println("\n  Conteúdos criados com sucesso:");
        System.out.println("  → " + music1);
        System.out.println("  → " + music2);
        System.out.println("  → " + music3);
        System.out.println("  → " + podcast1);
        System.out.println("  → " + podcast2);

        // ══════════════════════════════════════════════════════
        // 2. OBSERVER — Criação do Player e registro de observadores
        //    Player = Subject; UIObserver e HistoryService = Observers
        // ══════════════════════════════════════════════════════
        printSection("2. Observer — Configurando Player e Observadores");

        Player player = new Player();

        UIObserver ui          = new UIObserver("Spotify Simplificado");
        HistoryService history = new HistoryService();

        // Registra os observadores no player (subscribe)
        player.addObserver(ui);
        player.addObserver(history);

        System.out.println("  🔔 UIObserver registrado no Player.");
        System.out.println("  🔔 HistoryService registrado no Player.");

        // Adiciona conteúdos à playlist
        printSection("  Montando a Playlist");
        player.addContent(music1);
        player.addContent(podcast1);
        player.addContent(music2);
        player.addContent(music3);
        player.addContent(podcast2);

        player.printPlaylist();

        // ══════════════════════════════════════════════════════
        // 3. STRATEGY — Modo Sequencial
        //    Player usa SequentialMode (padrão) para avançar faixas
        // ══════════════════════════════════════════════════════
        printSection("3. Strategy — Reprodução Sequencial");

        player.setPlaybackMode(new SequentialMode());

        System.out.println("\n  ▷ Iniciando reprodução...");
        player.play();   // faixa 1

        player.next();   // faixa 2
        player.next();   // faixa 3

        // ══════════════════════════════════════════════════════
        // 4. STRATEGY — Troca para Modo Aleatório em runtime
        //    Nenhuma alteração no Player necessária!
        // ══════════════════════════════════════════════════════
        printSection("4. Strategy — Troca para Modo Aleatório em Runtime");

        // Seed fixa para resultado reproduzível na demonstração
        player.setPlaybackMode(new RandomMode(42L));

        player.next();   // faixa aleatória
        player.next();   // faixa aleatória

        // ══════════════════════════════════════════════════════
        // 5. OBSERVER — Demonstrando remoção de observador
        //    Remove UIObserver; HistoryService continua escutando.
        // ══════════════════════════════════════════════════════
        printSection("5. Observer — Removendo UIObserver dinamicamente");

        player.removeObserver(ui);
        System.out.println("  🔕 UIObserver removido. Apenas HistoryService receberá eventos.");

        player.setPlaybackMode(new SequentialMode());
        player.next(); // Apenas o HistoryService será notificado

        // ══════════════════════════════════════════════════════
        // 6. Exibindo o Histórico completo (HistoryService)
        // ══════════════════════════════════════════════════════
        printSection("6. Histórico Final de Reprodução");
        history.printHistory();

        // ══════════════════════════════════════════════════════
        // 7. Demonstração de validação na Factory
        // ══════════════════════════════════════════════════════
        printSection("7. Factory — Demonstrando Validação");
        try {
            ContentFactory.createMusic("", "Artista", 180, "Pop", "Album");
        } catch (IllegalArgumentException e) {
            System.out.println("  ❌ Erro esperado: " + e.getMessage());
        }
        try {
            ContentFactory.createPodcast("Ep.1", "Host", 1200, "Show", -1, "desc");
        } catch (IllegalArgumentException e) {
            System.out.println("  ❌ Erro esperado: " + e.getMessage());
        }

        printFooter();
    }

    // ── Helpers de formatação do console ──────────────────────

    private static void printBanner() {
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════╗
                ║         🎵  SISTEMA DE STREAMING DE MÚSICA  🎵        ║
                ║              Padrões GoF — Java Simulation             ║
                ╠═══════════════════════════════════════════════════════╣
                ║  Padrões:  Factory Method  │  Strategy  │  Observer   ║
                ╚═══════════════════════════════════════════════════════╝
                """);
    }

    private static void printSection(String title) {
        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.printf ("│  %-52s│%n", title);
        System.out.println("└─────────────────────────────────────────────────────┘");
    }

    private static void printFooter() {
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════╗
                ║  ✅  Simulação concluída com sucesso!                  ║
                ║                                                        ║
                ║  Padrões demonstrados:                                 ║
                ║   • Factory Method → ContentFactory                   ║
                ║   • Strategy       → SequentialMode / RandomMode      ║
                ║   • Observer       → UIObserver / HistoryService       ║
                ╚═══════════════════════════════════════════════════════╝
                """);
    }
}
