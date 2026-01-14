package com.sccon.geospatial.infra.person.persistence;

import com.sccon.geospatial.domain.person.PersonGateway;
import com.sccon.geospatial.domain.person.Pessoa;
import com.sccon.geospatial.infra.person.mapper.PessoaMapper;
import com.sccon.geospatial.infra.person.persistence.entity.PessoaEntity;
import com.sccon.geospatial.infra.person.persistence.repository.PersonJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("prod")
public class PostgresPersonGatewayImpl implements PersonGateway {

    private final PersonJpaRepository jpaRepository;
    private final PessoaMapper mapper;

    public PostgresPersonGatewayImpl(PersonJpaRepository jpaRepository, PessoaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        PessoaEntity entityToSave;

        if (pessoa.getId() != null) {
            Optional<PessoaEntity> existingEntity = jpaRepository.findById(pessoa.getId());

            if (existingEntity.isPresent()) {
                entityToSave = existingEntity.get();
                entityToSave.sincronizarDados(pessoa.getNome(), pessoa.getDataNascimento(), pessoa.getDataAdmissao());
            } else {
                entityToSave = mapper.toEntity(pessoa);
                entityToSave.setId(pessoa.getId());
            }
        } else {
            Long maxId = jpaRepository.findMaxId();
            Long nextId  = (maxId == null) ? 1L : maxId + 1;
            entityToSave = mapper.toEntity(pessoa);
            entityToSave.setId(nextId);
        }

        PessoaEntity savedEntity = jpaRepository.save(entityToSave);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Pessoa> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pessoa> listarTodas() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return jpaRepository.existsById(id);
    }
}