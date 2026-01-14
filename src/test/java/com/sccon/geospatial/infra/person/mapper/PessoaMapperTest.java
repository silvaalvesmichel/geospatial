package com.sccon.geospatial.infra.person.mapper;

import com.sccon.geospatial.domain.person.Pessoa;
import com.sccon.geospatial.infra.person.persistence.entity.PessoaEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PessoaMapperTest {

    private final PessoaMapper mapper = new PessoaMapper();

    @Test
    void deveConverterEntidadeParaDominio() {
        PessoaEntity entity = PessoaEntity.builder()
                .id(10L)
                .nome("Teste")
                .dataNascimento(LocalDate.of(1985, 5, 20))
                .dataAdmissao(LocalDate.of(2010, 1, 1))
                .build();

        Pessoa domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
    }
}