package br.com.jalo.estoque_api.dto;

import java.time.LocalDateTime;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String mensagem
) {
}