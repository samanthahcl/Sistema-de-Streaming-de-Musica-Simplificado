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

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FULL_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Registro imutável de uma faixa ouvida. */
    public record HistoryEntry(String contentTitle,
                               String contentType,
                               String author,
                               LocalDateTime playedAt) {
        @Override
        public String toString() {
            return String.format("  [%s]  %-30s  %-18s  (%s)",
                    playedAt.format(FULL_FMT),
                    contentTitle,
                    author,
                    contentType);
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
                entry.playedAt().format(TIME_FMT));
    }

    /** Retorna o histórico completo (somente leitura). */
    public List<HistoryEntry> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /** Imprime o histórico formatado no console. */
    public void printHistory() {
        System.out.printf("%n📖 Histórico de Reprodução (%d faixa(s)):%n", history.size());
        if (history.isEmpty()) {
            System.out.println("  (nenhuma faixa reproduzida ainda)");
        } else {
            history.forEach(System.out::println);
        }
    }
}