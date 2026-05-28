package com.spotify.main;

import com.spotify.factory.ContentCreator;
import com.spotify.factory.MusicCreator;
import com.spotify.factory.PodcastCreator;
import com.spotify.model.Content;
import com.spotify.observer.HistoryService;
import com.spotify.observer.Player;
import com.spotify.observer.UIObserver;
import com.spotify.strategy.RandomMode;
import com.spotify.strategy.SequentialMode;


public class Main {

    public static void main(String[] args) {

        printBanner();

        // ══════════════════════════════════════════════════════
        // 1. FACTORY METHOD — Criação de conteúdos via Creators
        //
        //    O cliente depende apenas de ContentCreator (abstrato).
        //    Nunca usa "new Music(...)" ou "new Podcast(...)" diretamente.
        //
        //    Estrutura GoF:
        //      ContentCreator (Creator abstrato)
        //        ├── MusicCreator   → factoryMethod() → new Music(...)
        //        └── PodcastCreator → factoryMethod() → new Podcast(...)
        // ══════════════════════════════════════════════════════
        printSection("1. Factory Method — Criando Conteúdos via Creators");

        ContentCreator musicCreator1 = new MusicCreator(
                "Bohemian Rhapsody", "Queen", 354, "Rock", "A Night at the Opera");

        ContentCreator musicCreator2 = new MusicCreator(
                "Blinding Lights", "The Weeknd", 200, "Pop/Synthwave", "After Hours");

        ContentCreator musicCreator3 = new MusicCreator(
                "Lose Yourself", "Eminem", 326, "Hip-Hop", "8 Mile Soundtrack");

        ContentCreator podcastCreator1 = new PodcastCreator(
                "Inteligência Artificial em 2025", "Lex Fridman", 4500,
                "Lex Fridman Podcast", 412,
                "Avanços recentes em LLMs e robótica");

        ContentCreator podcastCreator2 = new PodcastCreator(
                "Clean Code na Prática", "Uncle Bob", 3600,
                "Software Engineering Daily", 87,
                "Princípios SOLID e padrões de projeto");

        // createContent() invoca factoryMethod() internamente e valida o produto
        Content music1   = musicCreator1.createContent();
        Content music2   = musicCreator2.createContent();
        Content music3   = musicCreator3.createContent();
        Content podcast1 = podcastCreator1.createContent();
        Content podcast2 = podcastCreator2.createContent();

        System.out.println("\n  Conteúdos criados com sucesso:");
        System.out.println("  → " + music1);
        System.out.println("  → " + music2);
        System.out.println("  → " + music3);
        System.out.println("  → " + podcast1);
        System.out.println("  → " + podcast2);

        // ══════════════════════════════════════════════════════
        // 2. OBSERVER — Criação do Player e registro de observadores
        //
        //    Player   = Subject  (publica eventos)
        //    UIObserver     = Observer concreto (atualiza a "tela")
        //    HistoryService = Observer concreto (registra histórico)
        // ══════════════════════════════════════════════════════
        printSection("2. Observer — Configurando Player e Observadores");

        Player player = new Player();

        UIObserver     ui      = new UIObserver("Spotify Simplificado");
        HistoryService history = new HistoryService();

        player.addObserver(ui);
        player.addObserver(history);

        System.out.println("  🔔 UIObserver registrado.");
        System.out.println("  🔔 HistoryService registrado.");

        // Montando a playlist com conteúdos criados pelo Factory Method
        printSection("  Montando a Playlist");
        player.addContent(music1);
        player.addContent(podcast1);
        player.addContent(music2);
        player.addContent(music3);
        player.addContent(podcast2);

        player.printPlaylist();

        // ══════════════════════════════════════════════════════
        // 3. STRATEGY — Modo Sequencial
        //
        //    Player usa SequentialMode (padrão) para avançar faixas.
        //    Cada chamada a next() é delegada ao Strategy — nenhum
        //    if/else no Player.
        // ══════════════════════════════════════════════════════
        printSection("3. Strategy — Reprodução Sequencial");

        player.setPlaybackMode(new SequentialMode());

        System.out.println("\n  ▷ Iniciando reprodução...");
        player.play();   // faixa 1
        player.next();   // faixa 2
        player.next();   // faixa 3

        // ══════════════════════════════════════════════════════
        // 4. STRATEGY — Troca para Modo Aleatório em runtime
        //
        //    Nenhuma alteração no Player necessária.
        //    O Context (Player) simplesmente substitui o objeto
        //    Strategy e continua chamando playbackMode.next().
        // ══════════════════════════════════════════════════════
        printSection("4. Strategy — Troca para Modo Aleatório em Runtime");

        player.setPlaybackMode(new RandomMode(42L)); // seed fixa p/ reproduzibilidade

        player.next();   // faixa aleatória
        player.next();   // faixa aleatória

        // ══════════════════════════════════════════════════════
        // 5. OBSERVER — Remoção dinâmica de observador
        //
        //    Remove UIObserver em tempo de execução.
        //    O Player não precisa saber quem foi removido;
        //    apenas HistoryService continuará recebendo eventos.
        // ══════════════════════════════════════════════════════
        printSection("5. Observer — Removendo UIObserver Dinamicamente");

        player.removeObserver(ui);
        System.out.println("  🔕 UIObserver removido. Apenas HistoryService receberá eventos.");

        player.setPlaybackMode(new SequentialMode());
        player.next(); // somente HistoryService é notificado

        // ══════════════════════════════════════════════════════
        // 6. Histórico completo (HistoryService)
        // ══════════════════════════════════════════════════════
        printSection("6. Histórico Final de Reprodução");
        history.printHistory();

        // ══════════════════════════════════════════════════════
        // 7. Demonstração de validação no Factory Method
        //
        //    O Creator abstrato valida após factoryMethod().
        //    O Creator concreto (PodcastCreator) valida
        //    regras específicas dentro do próprio factoryMethod().
        // ══════════════════════════════════════════════════════
        printSection("7. Factory Method — Demonstrando Validação");

        try {
            // Título vazio — validação no ContentCreator (abstrato)
            new MusicCreator("", "Artista", 180, "Pop", "Album").createContent();
        } catch (IllegalArgumentException e) {
            System.out.println("  ❌ Erro esperado (título vazio): " + e.getMessage());
        }

        try {
            // Episódio inválido — validação no PodcastCreator (concreto)
            new PodcastCreator("Ep.1", "Host", 1200, "Show", -1, "desc").createContent();
        } catch (IllegalArgumentException e) {
            System.out.println("  ❌ Erro esperado (episódio negativo): " + e.getMessage());
        }

        try {
            // Duração zero — validação no ContentCreator (abstrato)
            new MusicCreator("Título", "Artista", 0, "Pop", "Album").createContent();
        } catch (IllegalArgumentException e) {
            System.out.println("  ❌ Erro esperado (duração zero): " + e.getMessage());
        }

        printFooter();
    }

    // ── Helpers de formatação do console ──────────────────────

    private static void printBanner() {
        System.out.println("""
                
                ╔══════════════════════════════════════════════════════════╗
                ║         🎵   SISTEMA DE STREAMING DE MÚSICA   🎵         ║
                ║               Padrões GoF — Java Simulation              ║
                ╠══════════════════════════════════════════════════════════╣
                ║   Padrões:  Factory Method  │  Strategy  │  Observer    ║
                ╚══════════════════════════════════════════════════════════╝
                """);
    }

    private static void printSection(String title) {
        System.out.println("\n┌──────────────────────────────────────────────────────┐");
        System.out.printf ("│  %-53s│%n", title);
        System.out.println("└──────────────────────────────────────────────────────┘");
    }

    private static void printFooter() {
        System.out.println("""
                
                ╔══════════════════════════════════════════════════════════╗
                ║  ✅  Simulação concluída com sucesso!                     ║
                ║                                                           ║
                ║  Padrões demonstrados:                                    ║
                ║   • Factory Method → ContentCreator + MusicCreator       ║
                ║                      + PodcastCreator                    ║
                ║   • Strategy       → SequentialMode / RandomMode         ║
                ║   • Observer       → UIObserver / HistoryService         ║
                ╚══════════════════════════════════════════════════════════╝
                """);
    }
}