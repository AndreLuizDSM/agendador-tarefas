package com.javanauta.agendadortarefas.controller;

import com.javanauta.agendadortarefas.business.TarefaService;
import com.javanauta.agendadortarefas.business.dto.TarefaDTO;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDTO> salvarTarefa (@RequestBody TarefaDTO tarefaDTO,
                                                    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(tarefaService.gravarTarefa(tarefaDTO, token));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefaDTO>> buscaListaDeTarefaPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)OffsetDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)OffsetDateTime dataFinal){

        return ResponseEntity.ok(tarefaService.buscarTarefaGravadaPorPerido(dataInicial, dataFinal));
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> buscaListaDeTarefaPorEmail(@RequestHeader("Authorization")String token){

        //List<TarefaDTO> listaDto = tarefaService.buscarTarefaGravadaPorEmail(token); Jeito simples
        return ResponseEntity.ok(tarefaService.buscarTarefaGravadaPorEmail(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam String id) {
        tarefaService.deletarTarefaPorId(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<TarefaDTO> alteraStatus (@RequestParam("id") String id,
                                                   @RequestParam("status")StatusNotificacaoEnum status){

        return ResponseEntity.ok(tarefaService.alterarStatus(status, id));
    }

    @PutMapping
    public ResponseEntity<TarefaDTO> alteraTarefa (@RequestBody TarefaDTO tarefaDTO,
                                                   @RequestParam("id") String id) {
        return ResponseEntity.ok(tarefaService.alterarTarefa(tarefaDTO, id));
    }
}
