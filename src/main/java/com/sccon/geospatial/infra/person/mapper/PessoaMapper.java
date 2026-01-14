package com.sccon.geospatial.infra.person.mapper;

import com.sccon.geospatial.domain.person.Pessoa;
import com.sccon.geospatial.infra.person.persistence.entity.PessoaEntity;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {

    public Pessoa toDomain(PessoaEntity entity) {
        if (entity == null) return null;

        return Pessoa.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .dataNascimento(entity.getDataNascimento())
                .dataAdmissao(entity.getDataAdmissao())
                .build();
    }

    public PessoaEntity toEntity(Pessoa domain) {
        if (domain == null) return null;

        return PessoaEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .dataNascimento(domain.getDataNascimento())
                .dataAdmissao(domain.getDataAdmissao())
                .build();
    }
}