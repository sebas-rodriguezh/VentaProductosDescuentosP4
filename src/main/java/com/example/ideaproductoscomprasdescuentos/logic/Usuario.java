package com.example.ideaproductoscomprasdescuentos.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Size(max = 10)
    @Column(name = "id", nullable = false, length = 10)
    private String id;

    @Size(max = 100)
    @NotNull
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @Size(max = 10)
    @NotNull
    @Column(name = "rol", nullable = false, length = 10)
    private String rol;

    @OneToMany(mappedBy = "usuario")
    private Set<LineaCarrito> lineas = new LinkedHashSet<>();

}
