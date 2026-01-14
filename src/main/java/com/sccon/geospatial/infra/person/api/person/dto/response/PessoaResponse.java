package com.sccon.geospatial.infra.person.api.person.dto.response;

import com.sccon.geospatial.domain.person.Pessoa;

import java.time.LocalDate;

public record PessoaResponse(
        Long id,
        String nome,
        LocalDate dataNascimento,
        LocalDate dataAdmissao
) {
    public static PessoaResponse fromDomain(Pessoa pessoa) {
        return new PessoaResponse(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getDataNascimento(),
                pessoa.getDataAdmissao()
        );
    }
}
