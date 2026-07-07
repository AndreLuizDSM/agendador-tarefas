package com.javanauta.agendadortarefas.infrastructure.repository;

import com.javanauta.agendadortarefas.infrastructure.entity.TarefaEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TarefasRepository extends MongoRepository<TarefaEntity, String> {

    List<TarefaEntity> findByDataEventoBetweenAndStatusNotificacaoEnum(Instant dataInicial,
                                                                       Instant dataFinal,
                                                                       StatusNotificacaoEnum status);
    List<TarefaEntity> findByEmailUsuario(String email);
}
