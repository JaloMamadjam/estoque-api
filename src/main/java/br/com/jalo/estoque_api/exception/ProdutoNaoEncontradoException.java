package br.com.jalo.estoque_api.exception;

public class ProdutoNaoEncontradoException
        extends RuntimeException {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}