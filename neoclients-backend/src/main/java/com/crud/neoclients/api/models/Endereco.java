package com.crud.neoclients.api.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
public class Endereco {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @OneToOne(mappedBy = "endereco")
    private Cliente cliente;

    @Column(nullable = false)
    @NotBlank(message = "CEP é requerido")
    private String cep;

    @Column(nullable = false)
    @NotBlank(message = "Logradouro é requerido")
    private String logradouro;

    @Column(nullable = false)
    @NotBlank(message = "Bairro é requerido")
    private String bairro;

    @Column(nullable = false)
    @NotBlank(message = "Cidade é requerido")
    private String cidade;

    @Column(nullable = false)
    @NotBlank(message = "UF é requerida")
    private String uf;

    @Column
    private String complemento;
}
