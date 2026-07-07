package com.javanauta.agendadortarefas.infrastructure.entity;

import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("tarefa")
@Builder
public class TarefaEntity {

    @Id
    private String id;
    private String nomeTarefa;
    private String descricao;
    private LocalDateTime dataCriacao;
    private Instant dataEvento;
    private String emailUsuario;
    private LocalDateTime dataAlteracao;
    private StatusNotificacaoEnum statusNotificacaoEnum;
}

