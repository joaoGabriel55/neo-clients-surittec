package com.crud.neoclients.api.models;

import com.crud.neoclients.api.models.enums.TipoTelefone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "telefone")
@Getter
@Setter
public class Telefone {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Numero é requerido")
    private String numero;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tipo de telefone é requerido")
    private TipoTelefone tipoTelefone;

}
