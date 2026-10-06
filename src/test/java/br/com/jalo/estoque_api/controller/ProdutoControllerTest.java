package br.com.jalo.estoque_api.controller;

import br.com.jalo.estoque_api.dto.ProdutoRequest;
import br.com.jalo.estoque_api.dto.ProdutoResponse;
import br.com.jalo.estoque_api.exception.ProdutoNaoEncontradoException;
import br.com.jalo.estoque_api.service.ProdutoService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProdutoService service;

    @Test
    void deveListarProdutos() throws Exception {

        ProdutoResponse produto = new ProdutoResponse(
                1L,
                "Notebook",
                new BigDecimal("3500.00"),
                10,
                "Eletrônicos"
        );

        given(service.listar())
                .willReturn(List.of(produto));

        mockMvc.perform(
                        get("/api/produtos")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Notebook"));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {

        ProdutoResponse produto = new ProdutoResponse(
                1L,
                "Notebook",
                new BigDecimal("3500.00"),
                10,
                "Eletrônicos"
        );

        given(service.buscarPorId(1L))
                .willReturn(produto);

        mockMvc.perform(
                        get("/api/produtos/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }

    @Test
    void deveCriarProduto() throws Exception {

        ProdutoRequest request = new ProdutoRequest(
                "SSD",
                new BigDecimal("500.00"),
                20,
                "Eletrônicos"
        );

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "SSD",
                new BigDecimal("500.00"),
                20,
                "Eletrônicos"
        );

        given(service.criar(any(ProdutoRequest.class)))
                .willReturn(response);

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("SSD"));
    }

    @Test
    void deveAtualizarProduto() throws Exception {

        ProdutoRequest request = new ProdutoRequest(
                "SSD NVMe",
                new BigDecimal("550.00"),
                25,
                "Eletrônicos"
        );

        ProdutoResponse response = new ProdutoResponse(
                1L,
                "SSD NVMe",
                new BigDecimal("550.00"),
                25,
                "Eletrônicos"
        );

        given(service.atualizar(1L, request))
                .willReturn(response);

        mockMvc.perform(
                        put("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("SSD NVMe"))
                .andExpect(jsonPath("$.quantidade").value(25));
    }

    @Test
    void deveRemoverProduto() throws Exception {

        doNothing()
                .when(service)
                .remover(1L);

        mockMvc.perform(
                        delete("/api/produtos/1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar400QuandoDadosForemInvalidos() throws Exception {

        ProdutoRequest request = new ProdutoRequest(
                "",
                new BigDecimal("-100"),
                -5,
                ""
        );

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExistir() throws Exception {

        given(service.buscarPorId(999L))
                .willThrow(
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado: 999"
                        )
                );

        mockMvc.perform(
                        get("/api/produtos/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.mensagem")
                                .value("Produto não encontrado: 999")
                );
    }
}