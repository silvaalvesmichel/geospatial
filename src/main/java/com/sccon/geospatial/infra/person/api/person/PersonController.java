package com.sccon.geospatial.infra.person.api.person;

import com.sccon.geospatial.application.person.PersonUseCase;
import com.sccon.geospatial.infra.person.api.person.dto.request.PessoaRequest;
import com.sccon.geospatial.infra.person.api.person.dto.response.PessoaResponse;
import com.sccon.geospatial.domain.person.Pessoa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
@Tag(name = "Pessoas", description = "Gerenciamento de recursos humanos")
public class PersonController {

    private final PersonUseCase personUseCase;

    public PersonController(PersonUseCase personUseCase) {
        this.personUseCase = personUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar todas as pessoas")
    public ResponseEntity<List<PessoaResponse>> listarPessoas() {
        List<PessoaResponse> response = personUseCase.listarTodas().stream()
                .map(PessoaResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = personUseCase.buscarPorId(id);
        return ResponseEntity.ok(PessoaResponse.fromDomain(pessoa));
    }

    @PostMapping
    @Operation(summary = "Criar nova pessoa")
    public ResponseEntity<PessoaResponse> criarPessoa(@RequestBody @Valid PessoaRequest dto) {
        Pessoa novaPessoa = Pessoa.builder()
                .id(dto.id())
                .nome(dto.nome())
                .dataNascimento(dto.dataNascimento())
                .dataAdmissao(dto.dataAdmissao())
                .build();

        Pessoa pessoaSalva = personUseCase.criar(novaPessoa);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pessoaSalva.getId())
                .toUri();

        return ResponseEntity.created(location).body(PessoaResponse.fromDomain(pessoaSalva));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa (Total)")
    public ResponseEntity<PessoaResponse> atualizarTotal(@PathVariable Long id, @RequestBody @Valid PessoaRequest dto) {
        Pessoa dadosNovos = Pessoa.builder()
                .nome(dto.nome())
                .dataNascimento(dto.dataNascimento())
                .dataAdmissao(dto.dataAdmissao())
                .build();

        Pessoa atualizada = personUseCase.atualizarTotal(id, dadosNovos);

        return ResponseEntity.ok(PessoaResponse.fromDomain(atualizada));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar pessoa (Parcial)")
    public ResponseEntity<PessoaResponse> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {

        Pessoa atualizada = personUseCase.atualizarParcial(id, updates);

        return ResponseEntity.ok(PessoaResponse.fromDomain(atualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover pessoa")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        personUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/salary")
    @Operation(summary = "Calcular salário")
    public ResponseEntity<Object> calcularSalario(
            @PathVariable Long id,
            @RequestParam(name = "output") String output) {

        Object resultado = personUseCase.calcularSalario(id, output);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}/age")
    @Operation(summary = "Calcular idade")
    public ResponseEntity<Long> calcularIdade(
            @PathVariable Long id,
            @RequestParam(name = "output") String output) {

        long idade = personUseCase.calcularIdade(id, output);
        return ResponseEntity.ok(idade);
    }
}

