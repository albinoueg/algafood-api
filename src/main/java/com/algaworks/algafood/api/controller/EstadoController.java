package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    EstadoRepository estadoRepository;
    @Autowired
    CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar(){
        return estadoRepository.todos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id){
        Estado estado = estadoRepository.porId(id);
        if(estado != null){
            return ResponseEntity.ok(estado);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody Estado estado){
        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long id, @RequestBody Estado estado){
        Estado estadoAtual = estadoRepository.porId(id);
        if(estadoAtual != null){
            BeanUtils.copyProperties(estado, estadoAtual, "id");
            estadoAtual = cadastroEstadoService.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Estado> remover(@PathVariable Long id){
        try{
            cadastroEstadoService.excluir(id);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
