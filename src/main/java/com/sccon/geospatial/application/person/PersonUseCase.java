package com.sccon.geospatial.application.person;

import com.sccon.geospatial.domain.person.PersonGateway;
import com.sccon.geospatial.domain.person.Pessoa;
import com.sccon.geospatial.infra.person.api.exception.PersonConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PersonUseCase {

    private final PersonGateway personGateway;

    public PersonUseCase(PersonGateway personGateway) {
        this.personGateway = personGateway;
    }

    // --- LEITUAS (Queries) ---

    @Transactional(readOnly = true)
    public List<Pessoa> listarTodas() {
        return personGateway.listarTodas();
    }

    @Transactional(readOnly = true)
    public Pessoa buscarPorId(Long id) {
        return personGateway.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com id: " + id));
    }

    public long calcularIdade(Long id, String output) {
        Pessoa pessoa = buscarPorId(id);
        return pessoa.calcularIdade(null, output);
    }

    public java.math.BigDecimal calcularSalario(Long id, String output) {
        Pessoa pessoa = buscarPorId(id);

        java.math.BigDecimal salarioFull = pessoa.calcularSalario(null);

        if ("full".equalsIgnoreCase(output)) {
            return salarioFull;
        } else if ("min".equalsIgnoreCase(output)) {
            java.math.BigDecimal salarioMinimo = new java.math.BigDecimal("1302.00");
            return salarioFull.divide(salarioMinimo, 2, java.math.RoundingMode.CEILING);
        } else {
            throw new IllegalArgumentException("Formato de saída desconhecido: " + output);
        }
    }

    // --- ESCRITAS (Commands) ---

    @Transactional
    public Pessoa criar(Pessoa pessoa) {
        if (pessoa.getId() != null && personGateway.existePorId(pessoa.getId())) {
            throw new PersonConflictException("Conflito: Já existe uma pessoa com o ID " + pessoa.getId());
        }
        return personGateway.salvar(pessoa);
    }

    @Transactional
    public Pessoa atualizarTotal(Long id, Pessoa dadosNovos) {
        Pessoa pessoaExistente = buscarPorId(id);
        pessoaExistente.atualizarDadosCadastrais(
                dadosNovos.getNome(),
                dadosNovos.getDataNascimento(),
                dadosNovos.getDataAdmissao()
        );
        return personGateway.salvar(pessoaExistente);
    }

    @Transactional
    public Pessoa atualizarParcial(Long id, Map<String, Object> updates) {
        Pessoa pessoa = buscarPorId(id);

        String novoNome = updates.containsKey("nome") ? (String) updates.get("nome") : pessoa.getNome();

        LocalDate novoNascimento = pessoa.getDataNascimento();
        if (updates.containsKey("dataNascimento")) {
            String val = (String) updates.get("dataNascimento");
            novoNascimento = (val != null) ? LocalDate.parse(val) : null;
        }

        LocalDate novaAdmissao = pessoa.getDataAdmissao();
        if (updates.containsKey("dataAdmissao")) {
            String val = (String) updates.get("dataAdmissao");
            novaAdmissao = (val != null) ? LocalDate.parse(val) : null;
        }

        pessoa.atualizarDadosCadastrais(novoNome, novoNascimento, novaAdmissao);
        return personGateway.salvar(pessoa);
    }

    @Transactional
    public void deletar(Long id) {
        if (!personGateway.existePorId(id)) {
            throw new EntityNotFoundException("Pessoa não encontrada com id: " + id);
        }
        personGateway.deletar(id);
    }
}
