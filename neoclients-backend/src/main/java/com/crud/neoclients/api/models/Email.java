package com.crud.neoclients.api.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "email")
@Getter
@Setter
public class Email {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email é requerido")
    private String emailAddress;

}
