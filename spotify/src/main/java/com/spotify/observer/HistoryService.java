package com.spotify.observer;

import com.spotify.model.Content;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observador Concreto: serviço de histórico de reprodução.
 *
 * Registra cada faixa reproduzida com timestamp, permitindo
 * consulta posterior do histórico completo.
 */
public class HistoryService implements TrackObserver {

    /** Registro imutável de uma faixa ouvida. */
    public record HistoryEntry(String contentTitle, String contentType,
                               String author, LocalDateTime playedAt) {
        @Override
        public String toString() {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return String.format("  [%s] %s — %s (%s)",
                    playedAt.format(fmt), contentTitle, author, contentType);
        }
    }

    private final List<HistoryEntry> history = new ArrayList<>();

    @Override
    public void onTrackStarted(Content content) {
        HistoryEntry entry = new HistoryEntry(
                content.getTitle(),
                content.getContentType(),
                content.getAuthor(),
                LocalDateTime.now()
        );
        history.add(entry);
        System.out.printf("  📋 [HistoryService] Registrado: \"%s\" às %s%n",
                content.getTitle(),
                entry.playedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /**
     * Retorna o histórico completo de reprodução (somente leitura).
     */
    public List<HistoryEntry> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Imprime o histórico formatado no console.
     */
    public void printHistory() {
        System.out.println("\n📖 Histórico de Reprodução (" + history.size() + " faixas):");
        if (history.isEmpty()) {
            System.out.println("  (nenhuma faixa reproduzida ainda)");
        } else {
            history.forEach(System.out::println);
        }
    }
}
