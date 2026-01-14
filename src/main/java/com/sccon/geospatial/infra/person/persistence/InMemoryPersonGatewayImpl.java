package com.sccon.geospatial.infra.person.persistence;

import com.sccon.geospatial.domain.person.PersonGateway;
import com.sccon.geospatial.domain.person.Pessoa;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@Profile("!prod") // Ativa este bean se o profile 'prod' NÃO estiver ativo (Default)
public class InMemoryPersonGatewayImpl implements PersonGateway {

    private final Map<Long, Pessoa> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public InMemoryPersonGatewayImpl() {
        // Requisito 4: Inicializar com 3 pessoas
        salvarInterno(Pessoa.builder().nome("José da Silva").dataNascimento(LocalDate.of(2000, 4, 6)).dataAdmissao(LocalDate.of(2020, 5, 10)).build());
        salvarInterno(Pessoa.builder().nome("Maria Oliveira").dataNascimento(LocalDate.of(1995, 1, 20)).dataAdmissao(LocalDate.of(2019, 3, 15)).build());
        salvarInterno(Pessoa.builder().nome("Carlos Santos").dataNascimento(LocalDate.of(1988, 12, 10)).dataAdmissao(LocalDate.of(2018, 7, 1)).build());
    }

    private void salvarInterno(Pessoa p) {
        // Simula o comportamento do salvar com ID auto-gerado
        long novoId = idGenerator.incrementAndGet();
        // Usamos reflection ou recriamos o objeto pois o dominio é imutavel sem ID
        Pessoa pComId = Pessoa.builder()
                .id(novoId)
                .nome(p.getNome())
                .dataNascimento(p.getDataNascimento())
                .dataAdmissao(p.getDataAdmissao())
                .build();
        database.put(novoId, pComId);
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        Long idToSave;

        if (pessoa.getId() != null) {
            // Se veio ID, verificamos conflito ou update
            // Como é memória, aceitamos o ID imposto (comportamento do Requisito 3.b ajustado)
            idToSave = pessoa.getId();

            // Se o ID for maior que o gerador atual, atualizamos o gerador para evitar colisões futuras
            if (idToSave > idGenerator.get()) {
                idGenerator.set(idToSave);
            }
        } else {
            // Req 3.a: Maior ID + 1
            idToSave = idGenerator.incrementAndGet();
        }

        // Reconstrói o objeto para garantir que o ID está setado no dominio retornado
        Pessoa pessoaSalva = Pessoa.builder()
                .id(idToSave)
                .nome(pessoa.getNome())
                .dataNascimento(pessoa.getDataNascimento())
                .dataAdmissao(pessoa.getDataAdmissao())
                .build();

        database.put(idToSave, pessoaSalva);
        return pessoaSalva;
    }

    @Override
    public Optional<Pessoa> buscarPorId(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Pessoa> listarTodas() {
        return database.values().stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        database.remove(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return database.containsKey(id);
    }
}
