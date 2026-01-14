package com.sccon.geospatial.domain.person;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class Pessoa {

    private final Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;

    @Builder
    private Pessoa(Long id, String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
        validarEstado();
    }

    public void atualizarDadosCadastrais(String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
        validarEstado();
    }

    private void validarEstado() {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório");
        if (dataNascimento == null) throw new IllegalArgumentException("Data de nascimento é obrigatória");
        if (dataAdmissao == null) throw new IllegalArgumentException("Data de admissão é obrigatória");

        if (dataNascimento.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data de nascimento não pode ser futura");

        if (dataAdmissao.isBefore(dataNascimento))
            throw new IllegalArgumentException("Data de admissão não pode ser anterior ao nascimento");
    }

    public BigDecimal calcularSalario(LocalDate dataReferencia) {
        if (dataReferencia == null) dataReferencia = LocalDate.now();
        BigDecimal salarioAtual = new BigDecimal("1558.00");
        long anosDeCasa = ChronoUnit.YEARS.between(dataAdmissao, dataReferencia);

        for (int i = 0; i < anosDeCasa; i++) {
            salarioAtual = salarioAtual.multiply(new BigDecimal("1.18")).add(new BigDecimal("500.00"));
        }
        return salarioAtual.setScale(2, RoundingMode.CEILING);
    }

    public long calcularIdade(LocalDate dataReferencia, String output) {
        if (dataReferencia == null) dataReferencia = LocalDate.now();
        return switch (output.toLowerCase()) {
            case "days" -> ChronoUnit.DAYS.between(dataNascimento, dataReferencia);
            case "months" -> ChronoUnit.MONTHS.between(dataNascimento, dataReferencia);
            case "years" -> ChronoUnit.YEARS.between(dataNascimento, dataReferencia);
            default -> throw new IllegalArgumentException("Formato de saída desconhecido: " + output);
        };
    }
}


