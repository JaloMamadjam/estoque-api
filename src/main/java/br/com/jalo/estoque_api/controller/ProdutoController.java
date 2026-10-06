package br.com.jalo.estoque_api.controller;

import br.com.jalo.estoque_api.dto.ProdutoRequest;
import br.com.jalo.estoque_api.dto.ProdutoResponse;
import br.com.jalo.estoque_api.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    public List<ProdutoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(
            @PathVariable Long id
    ) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @RequestBody ProdutoRequest request
    ) {

        ProdutoResponse response =
                service.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request
    ) {

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id
    ) {

        service.remover(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProdutoResponse> buscarPorCategoria(
            @PathVariable String categoria
    ) {
        return service.buscarPorCategoria(categoria);
    }

    @GetMapping("/busca")
    public List<ProdutoResponse> buscarPorNome(
            @RequestParam String nome
    ) {
        return service.buscarPorNome(nome);
    }
}