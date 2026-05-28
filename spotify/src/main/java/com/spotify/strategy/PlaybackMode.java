package com.spotify.strategy;

import com.spotify.model.Content;
import java.util.List;

/**
 * ============================================================
 * PADRÃO: STRATEGY (GoF — Comportamental)
 * ============================================================
 *
 * O QUÊ:
 *   Interface que define o contrato para todas as estratégias
 *   de reprodução de playlist. Cada implementação encapsula
 *   um algoritmo diferente de "como avançar para a próxima faixa".
 *
 * POR QUÊ:
 *   - Sem Strategy, o Player teria um if/else ou switch gigante
 *     para cada modo de reprodução.
 *   - Com Strategy, trocar o modo em tempo de execução é simples:
 *     player.setPlaybackMode(new RandomMode()).
 *   - Novos modos (ex.: RepeatOne, ShuffleWeighted) não exigem
 *     alteração no Player — apenas uma nova classe que implemente
 *     esta interface (princípio Aberto/Fechado).
 *
 * COMO:
 *   - PlaybackMode é a interface Strategy.
 *   - SequentialMode e RandomMode são as estratégias concretas.
 *   - Player mantém uma referência a PlaybackMode e a utiliza
 *     ao chamar next().
 * ============================================================
 */
public interface PlaybackMode {

    /**
     * Calcula o índice da próxima faixa a ser reproduzida.
     *
     * @param playlist     Lista de conteúdos da playlist
     * @param currentIndex Índice da faixa atualmente em reprodução
     * @return Índice da próxima faixa
     */
    int next(List<Content> playlist, int currentIndex);

    /**
     * Retorna o nome legível da estratégia (para logs e UI).
     */
    String getModeName();
}
