package br.com.jalo.estoque_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal preco;

    private int quantidade;

    private String categoria;

    public Produto(
            String nome,
            BigDecimal preco,
            int quantidade,
            String categoria
    ) {

        validar(preco, quantidade);

        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public void atualizar(
            String nome,
            BigDecimal preco,
            int quantidade,
            String categoria
    ) {

        validar(preco, quantidade);

        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    private void validar(
            BigDecimal preco,
            int quantidade
    ) {

        if (preco == null || preco.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Preço deve ser maior que zero"
            );
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException(
                    "Quantidade não pode ser negativa"
            );
        }
    }
}