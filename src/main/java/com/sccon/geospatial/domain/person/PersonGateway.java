package com.sccon.geospatial.domain.person;

import java.util.List;
import java.util.Optional;

public interface PersonGateway {
    Pessoa salvar(Pessoa pessoa);
    Optional<Pessoa> buscarPorId(Long id);
    List<Pessoa> listarTodas();
    void deletar(Long id);
    boolean existePorId(Long id);
}

