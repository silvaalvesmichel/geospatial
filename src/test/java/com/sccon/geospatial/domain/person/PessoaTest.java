package com.sccon.geospatial.domain.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    @Test
    @DisplayName("Deve calcular o salário corretamente após 2 anos de casa")
    void deveCalcularSalarioCorretamente() {
        // Regra do desafio:
        // Base: 1558.00
        // Ano 1: (1558 * 1.18) + 500 = 2338.44
        // Ano 2: (2338.44 * 1.18) + 500 = 3259.3592 -> CEILING -> 3259.36
        Pessoa pessoa = Pessoa.builder()
                .nome("João Silva")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataAdmissao(LocalDate.now().minusYears(2))
                .build();

        BigDecimal salarioCalculado = pessoa.calcularSalario(LocalDate.now());
        assertEquals(new BigDecimal("3259.36"), salarioCalculado);
    }

    @Test
    @DisplayName("Deve retornar salário base se tiver menos de 1 ano de casa")
    void deveRetornarSalarioBase() {
        Pessoa pessoa = Pessoa.builder()
                .nome("Novato")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .dataAdmissao(LocalDate.now().minusMonths(6))
                .build();

        BigDecimal salario = pessoa.calcularSalario(LocalDate.now());
        assertEquals(new BigDecimal("1558.00"), salario);
    }

    @Test
    @DisplayName("Deve calcular a idade em diferentes formatos")
    void deveCalcularIdadeFormatos() {
        LocalDate nascimento = LocalDate.now().minusYears(10);
        Pessoa pessoa = Pessoa.builder()
                .nome("Criança")
                .dataNascimento(nascimento)
                .dataAdmissao(LocalDate.now().minusYears(1))
                .build();

        assertEquals(10, pessoa.calcularIdade(LocalDate.now(), "years"));
        // Aproximadamente 3652 ou 3653 dias (considerando anos bissextos)
        assertTrue(pessoa.calcularIdade(LocalDate.now(), "days") >= 3650);
    }

    @Test
    @DisplayName("Deve lançar erro para formato de idade desconhecido")
    void deveLancarErroIdadeFormatoInvalido() {
        Pessoa pessoa = Pessoa.builder()
                .nome("Teste")
                .dataNascimento(LocalDate.of(2000,1,1))
                .dataAdmissao(LocalDate.of(2020,1,1))
                .build();

        assertThrows(IllegalArgumentException.class, () -> pessoa.calcularIdade(null, "semanas"));
    }

    @Test
    @DisplayName("Não deve permitir nome vazio ou nulo")
    void deveValidarNomeObrigatorio() {
        assertThrows(IllegalArgumentException.class, () ->
                Pessoa.builder().nome("").dataNascimento(LocalDate.now()).dataAdmissao(LocalDate.now()).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
                Pessoa.builder().nome(null).dataNascimento(LocalDate.now()).dataAdmissao(LocalDate.now()).build()
        );
    }

    @Test
    @DisplayName("Não deve permitir data de nascimento futura")
    void deveValidarDataNascimentoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            Pessoa.builder()
                    .nome("Marty McFly")
                    .dataNascimento(LocalDate.now().plusDays(1))
                    .dataAdmissao(LocalDate.now())
                    .build();
        });
    }

    @Test
    @DisplayName("Não deve permitir data de admissão anterior ao nascimento")
    void deveValidarDataAdmissao() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Pessoa.builder()
                    .nome("Invalido")
                    .dataNascimento(LocalDate.of(2020, 1, 1))
                    .dataAdmissao(LocalDate.of(2010, 1, 1))
                    .build();
        });
        assertEquals("Data de admissão não pode ser anterior ao nascimento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar estado ao atualizar dados cadastrais")
    void deveValidarAoAtualizar() {
        Pessoa pessoa = Pessoa.builder()
                .nome("Valido")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataAdmissao(LocalDate.of(2010, 1, 1))
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                pessoa.atualizarDadosCadastrais(null, null, null)
        );
    }
}