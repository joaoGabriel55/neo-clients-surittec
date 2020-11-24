package com.crud.neoclients.api.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome é requerido")
    @Length(min = 3, max = 100)
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "CPF é requerido")
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    @NotNull(message = "Endereço é requerido")
    private Endereco endereco;
    
    @Size(min = 1, message = "Ao menos um telefone é requerido")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "cliente")
    private Set<Telefone> telefones;

    @Size(min = 1, message = "Ao menos um email é requerido")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "cliente")
    private Set<Email> emails;

}
