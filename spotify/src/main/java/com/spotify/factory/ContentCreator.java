package com.spotify.factory;

import com.spotify.model.Content;


public abstract class ContentCreator {

    public abstract Content factoryMethod();


    public Content createContent() {
        Content content = factoryMethod();
        validateContent(content);
        return content;
    }


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