package com.javanauta.agendadortarefas.business;

import com.javanauta.agendadortarefas.business.dto.TarefaDTO;
import com.javanauta.agendadortarefas.business.mapper.TarefaConverter;
import com.javanauta.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefaEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.javanauta.agendadortarefas.infrastructure.exceptions.ResourceNotFound;
import com.javanauta.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    public final TarefasRepository repository;
    public final TarefaConverter tarefaConverter;
    public final TarefaUpdateConverter tarefaUpdateConverter;
    public final JwtUtil jwtUtil;

    // Gravar as tarefas
    public TarefaDTO gravarTarefa (TarefaDTO tarefaDTO, String token) {
        String email = extracaoEmail(token);

        tarefaDTO.setDataCriacao(LocalDateTime.now());
        tarefaDTO.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        tarefaDTO.setEmailUsuario(email);

        TarefaEntity tarefaEntity = tarefaConverter.paraTarefaEntity(tarefaDTO);
            return tarefaConverter.paraTarefaDTO(repository.save(tarefaEntity));
    }

    // Busca tarefas durante um tempo inicial , e tempo final dado
    public List<TarefaDTO> buscarTarefaGravadaPorPerido(Instant dataInicial, Instant dataFinal){

        return tarefaConverter.paraListaTarefaDto(
                repository.findByDataEventoBetweenAndStatusNotificacaoEnum(dataInicial, dataFinal,
                                                                            StatusNotificacaoEnum.PENDENTE));
    }

    // Busca todas as tarefas que estão no Email
    public List<TarefaDTO> buscarTarefaGravadaPorEmail(String token) {
        String email = extracaoEmail(token);

        List<TarefaEntity> lista = repository.findByEmailUsuario(email);

        return tarefaConverter.paraListaTarefaDto(lista);
    }

    public void deletarTarefaPorId(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFound("id não encontrado " + id);
        }
        repository.deleteById(id);
    }

    public TarefaDTO alterarStatus (StatusNotificacaoEnum status, String id) {
        try {
            TarefaEntity tarefaEntity = repository.findById(id).orElseThrow(
                    ()-> new ResourceNotFound("id não encontrado " + id));
            tarefaEntity.setStatusNotificacaoEnum(status);

                return tarefaConverter.paraTarefaDTO(repository.save(tarefaEntity));
        } catch (ResourceNotFound e) {
            throw new ResourceNotFound("id não encontrado " + id, e.getCause());
        }
    }

    public TarefaDTO alterarTarefa (TarefaDTO tarefaDTO, String id) {
        try {
            TarefaEntity tarefaEntity = repository.findById(id).orElseThrow(
                    ()-> new ResourceNotFound("id não encontrado" + id));

            tarefaUpdateConverter.updateTarefa(tarefaEntity, tarefaDTO);
            //  repository.save(tarefaEntity);  Jeito simples
                return tarefaConverter.paraTarefaDTO(repository.save(tarefaEntity));

        }   catch (ResourceNotFound e) {
            throw new ResourceNotFound("id não encontrado " + id, e.getCause());
        }
    }

    public String extracaoEmail (String token) {
        return jwtUtil.extrairEmailToken(token.substring(7));
    }
}
