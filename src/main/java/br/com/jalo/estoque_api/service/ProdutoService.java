package br.com.jalo.estoque_api.service;

import br.com.jalo.estoque_api.dto.ProdutoRequest;
import br.com.jalo.estoque_api.dto.ProdutoResponse;
import br.com.jalo.estoque_api.exception.ProdutoNaoEncontradoException;
import br.com.jalo.estoque_api.model.Produto;
import br.com.jalo.estoque_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public List<ProdutoResponse> listar() {

        return repository.findAll()
                .stream()
                .map(ProdutoResponse::from)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {

        Produto produto = buscarEntidadePorId(id);

        return ProdutoResponse.from(produto);
    }

    public ProdutoResponse criar(ProdutoRequest request) {

        Produto produto = new Produto(
                request.nome(),
                request.preco(),
                request.quantidade(),
                request.categoria()
        );

        Produto salvo = repository.save(produto);

        return ProdutoResponse.from(salvo);
    }

    public ProdutoResponse atualizar(
            Long id,
            ProdutoRequest request
    ) {

        Produto produto = buscarEntidadePorId(id);

        produto.atualizar(
                request.nome(),
                request.preco(),
                request.quantidade(),
                request.categoria()
        );

        Produto atualizado = repository.save(produto);

        return ProdutoResponse.from(atualizado);
    }

    public void remover(Long id) {

        Produto produto = buscarEntidadePorId(id);

        repository.delete(produto);
    }

    private Produto buscarEntidadePorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado: " + id
                        )
                );
    }

    public List<ProdutoResponse> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria)
                .stream()
                .map(ProdutoResponse::from)
                .toList();
    }
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(ProdutoResponse::from)
                .toList();
    }
}