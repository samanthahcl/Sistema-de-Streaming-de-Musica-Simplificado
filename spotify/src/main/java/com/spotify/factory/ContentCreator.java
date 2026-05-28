package com.spotify.factory;

import com.spotify.model.Content;

/**
 * ============================================================
 * PADRÃO: FACTORY METHOD (GoF — Criacional)
 * ============================================================
 *
 * O QUÊ:
 *   ContentCreator é a classe CRIADORA abstrata do padrão
 *   Factory Method. Ela declara o método abstrato
 *   factoryMethod(), que as subclasses concretas devem
 *   implementar para instanciar o tipo correto de Content.
 *
 * POR QUÊ:
 *   - Separa a lógica de uso (createContent) da lógica de
 *     criação (factoryMethod), respeitando o princípio
 *     Aberto/Fechado e o princípio de Responsabilidade Única.
 *   - O cliente (Main) depende apenas de ContentCreator,
 *     nunca das classes concretas Music ou Podcast.
 *   - Adicionar um novo tipo de conteúdo (ex.: Audiobook)
 *     exige apenas criar a classe modelo e um novo Creator
 *     concreto, sem alterar código existente.
 *
 * ESTRUTURA GoF:
 *   Creator (abstrato)  →  ContentCreator        (este arquivo)
 *   ConcreteCreator     →  MusicCreator           (MusicCreator.java)
 *   ConcreteCreator     →  PodcastCreator         (PodcastCreator.java)
 *   Product (abstrato)  →  Content                (Content.java)
 *   ConcreteProduct     →  Music, Podcast
 * ============================================================
 */
public abstract class ContentCreator {

    /**
     * O FACTORY METHOD — método abstrato que cada subclasse
     * concreta implementa para criar seu tipo de conteúdo.
     *
     * Este é o núcleo do padrão GoF Factory Method.
     *
     * @return instância concreta de Content
     */
    public abstract Content factoryMethod();

    /**
     * Operação de template que usa o factory method.
     *
     * O criador abstrato não sabe qual tipo será criado;
     * apenas garante que a validação comum é aplicada antes
     * de entregar o produto ao cliente.
     *
     * @return Content validado e pronto para uso
     */
    public Content createContent() {
        Content content = factoryMethod();
        validateContent(content);
        return content;
    }

    /**
     * Validação comum a todos os tipos de conteúdo.
     * Centralizada no Creator abstrato — benefício do padrão.
     */
    private void validateContent(Content content) {
        if (content.getTitle() == null || content.getTitle().isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        if (content.getAuthor() == null || content.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Autor não pode ser vazio.");
        }
        if (content.getDurationSeconds() <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero.");
        }
    }
}