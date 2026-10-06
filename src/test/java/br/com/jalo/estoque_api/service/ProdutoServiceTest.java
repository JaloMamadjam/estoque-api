package br.com.jalo.estoque_api.service;

import br.com.jalo.estoque_api.dto.ProdutoRequest;
import br.com.jalo.estoque_api.dto.ProdutoResponse;
import br.com.jalo.estoque_api.exception.ProdutoNaoEncontradoException;
import br.com.jalo.estoque_api.model.Produto;
import br.com.jalo.estoque_api.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deveCriarProduto() {

        ProdutoRequest request = new ProdutoRequest(
                "Notebook",
                new BigDecimal("3500.00"),
                10,
                "Eletrônicos"
        );

        Produto produto = new Produto(
                "Notebook",
                new BigDecimal("3500.00"),
                10,
                "Eletrônicos"
        );

        when(repository.save(any(Produto.class)))
                .thenReturn(produto);

        ProdutoResponse response =
                service.criar(request);

        assertEquals(
                "Notebook",
                response.nome()
        );

        verify(repository).save(any(Produto.class));
    }

    @Test
    void deveBuscarProduto() {

        Produto produto = new Produto(
                "Mouse",
                new BigDecimal("120.00"),
                20,
                "Periféricos"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(produto));

        ProdutoResponse response =
                service.buscarPorId(1L);

        assertEquals(
                "Mouse",
                response.nome()
        );
    }

    @Test
    void deveLancarExceptionQuandoNaoEncontrar() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );
    }

    @Test
    void deveAtualizarProduto() {

        Produto produto = new Produto(
                "Mouse",
                new BigDecimal("120.00"),
                20,
                "Periféricos"
        );

        ProdutoRequest request = new ProdutoRequest(
                "Mouse Gamer",
                new BigDecimal("180.00"),
                30,
                "Periféricos"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(repository.save(produto))
                .thenReturn(produto);

        ProdutoResponse response =
                service.atualizar(1L, request);

        assertEquals(
                "Mouse Gamer",
                response.nome()
        );

        assertEquals(
                new BigDecimal("180.00"),
                response.preco()
        );

        assertEquals(
                30,
                response.quantidade()
        );
    }

    @Test
    void deveRemoverProduto() {

        Produto produto = new Produto(
                "Teclado",
                new BigDecimal("250.00"),
                15,
                "Periféricos"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(produto));

        service.remover(1L);

        verify(repository).delete(produto);
    }
}