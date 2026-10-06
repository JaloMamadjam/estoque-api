package br.com.jalo.estoque_api.dto;

import br.com.jalo.estoque_api.model.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        BigDecimal preco,
        int quantidade,
        String categoria
) {

    public static ProdutoResponse from(Produto produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria()
        );
    }
}