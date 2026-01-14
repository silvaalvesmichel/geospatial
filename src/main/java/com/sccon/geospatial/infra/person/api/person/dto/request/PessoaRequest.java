package com.sccon.geospatial.infra.person.api.person.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record PessoaRequest(
        Long id, // Opcional, pois o usuário pode ou não passar no POST

        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Data de admissão é obrigatória")
        @PastOrPresent(message = "Data de admissão não pode ser futura")
        LocalDate dataAdmissao
) {}