package com.sccon.geospatial.application.person;

import com.sccon.geospatial.domain.person.PersonGateway;
import com.sccon.geospatial.domain.person.Pessoa;
import com.sccon.geospatial.infra.person.api.exception.PersonConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

    @Mock
    private PersonGateway personGateway;

    @InjectMocks
    private PersonUseCase personUseCase;

    private Pessoa pessoaExemplo;

    @BeforeEach
    void setup() {
        pessoaExemplo = Pessoa.builder()
                .id(1L)
                .nome("José da Silva")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .dataAdmissao(LocalDate.of(2020, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Deve criar uma pessoa com sucesso")
    void deveCriarPessoaComSucesso() {
        when(personGateway.existePorId(1L)).thenReturn(false);
        when(personGateway.salvar(any(Pessoa.class))).thenReturn(pessoaExemplo);

        Pessoa resultado = personUseCase.criar(pessoaExemplo);

        assertNotNull(resultado);
        assertEquals("José da Silva", resultado.getNome());
        verify(personGateway).salvar(pessoaExemplo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pessoa com ID já existente")
    void deveLancarExcecaoAoCriarComIdExistente() {
        when(personGateway.existePorId(1L)).thenReturn(true);

        assertThrows(PersonConflictException.class, () -> personUseCase.criar(pessoaExemplo));
        verify(personGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve calcular idade em anos corretamente")
    void deveCalcularIdadeEmAnos() {
        when(personGateway.buscarPorId(1L)).thenReturn(Optional.of(pessoaExemplo));

        // Supondo que hoje seja 2024, a idade deve ser 24 (ou 23 dependendo do mês)
        // O método calcularIdade da classe Pessoa usa LocalDate.now()
        long idade = personUseCase.calcularIdade(1L, "years");

        assertTrue(idade > 0);
        verify(personGateway).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve calcular salário em formato FULL com juros compostos")
    void deveCalcularSalarioFull() {
        when(personGateway.buscarPorId(1L)).thenReturn(Optional.of(pessoaExemplo));

        BigDecimal salario = personUseCase.calcularSalario(1L, "full");

        // Regra do PDF: Base 1550 + juros compostos sobre tempo de serviço
        assertNotNull(salario);
        assertTrue(salario.compareTo(new BigDecimal("1550")) > 0);
    }

    @Test
    @DisplayName("Deve calcular salário em quantidade de salários mínimos")
    void deveCalcularSalarioMinimo() {
        when(personGateway.buscarPorId(1L)).thenReturn(Optional.of(pessoaExemplo));

        BigDecimal qtdSalariosMinimos = personUseCase.calcularSalario(1L, "min");

        // Salário Full / 1302.00
        assertNotNull(qtdSalariosMinimos);
        verify(personGateway).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve atualizar nome parcialmente e manter as datas originais")
    void deveAtualizarNomeParcialmente() {
        when(personGateway.buscarPorId(1L)).thenReturn(Optional.of(pessoaExemplo));
        when(personGateway.salvar(any(Pessoa.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<String, Object> updates = new HashMap<>();
        updates.put("nome", "Novo Nome");

        Pessoa resultado = personUseCase.atualizarParcial(1L, updates);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), resultado.getDataNascimento()); // Mantido
        verify(personGateway).salvar(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar data para null (Validação de Domínio)")
    void deveLancarErroAoAtualizarDataParaNull() {
        when(personGateway.buscarPorId(1L)).thenReturn(Optional.of(pessoaExemplo));

        Map<String, Object> updates = new HashMap<>();
        updates.put("dataNascimento", null);

        // O UseCase chama pessoa.atualizarDadosCadastrais que deve validar campos obrigatórios
        assertThrows(IllegalArgumentException.class, () -> personUseCase.atualizarParcial(1L, updates));
    }

    @Test
    @DisplayName("Deve deletar pessoa com sucesso")
    void deveDeletarPessoa() {
        when(personGateway.existePorId(1L)).thenReturn(true);

        assertDoesNotThrow(() -> personUseCase.deletar(1L));

        verify(personGateway).deletar(1L);
    }

    @Test
    @DisplayName("Deve lançar 404 ao buscar ID inexistente")
    void deveLancar404AoBuscarInexistente() {
        when(personGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personUseCase.buscarPorId(99L));
    }
}