package com.spotify.observer;

import com.spotify.model.Content;

/**
 * ============================================================
 * PADRÃO: OBSERVER (GoF — Comportamental)
 * ============================================================
 *
 * O QUÊ:
 *   Interface que define o contrato para todos os observadores
 *   do Player. Qualquer serviço interessado em saber "quando
 *   uma faixa começa a tocar" deve implementar esta interface.
 *
 * POR QUÊ:
 *   - Sem Observer, o Player precisaria conhecer e chamar
 *     diretamente cada serviço (UI, HistoryService, etc.),
 *     gerando alto acoplamento.
 *   - Com Observer, o Player não sabe quem está escutando —
 *     ele apenas publica o evento e os interessados reagem.
 *   - Adicionar um novo observador (ex.: RecommendationService)
 *     não requer nenhuma alteração no Player.
 *
 * COMO:
 *   - TrackObserver é a interface Observer.
 *   - UIObserver e HistoryService são os observadores concretos.
 *   - Player é o Subject (publicador de eventos).
 * ============================================================
 */
public interface TrackObserver {

    /**
     * Método chamado pelo Player (Subject) sempre que
     * uma nova faixa começa a ser reproduzida.
     *
     * @param content O conteúdo que acabou de iniciar reprodução
     */
    void onTrackStarted(Content content);
}
