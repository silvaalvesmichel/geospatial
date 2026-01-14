package com.sccon.geospatial.infra.person.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_pessoa")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    public void sincronizarDados(String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }
}
