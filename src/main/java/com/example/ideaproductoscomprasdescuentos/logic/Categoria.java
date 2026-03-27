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
@Table(name = "categoria")
public class Categoria
{
    @Id
    @Size(max = 10)
    @Column(name = "id", nullable = false, length = 10)
    private String id;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "cant_lleva")
    private Integer cantLleva;

    @Column(name = "cant_paga")
    private Integer cantPaga;

    @Column(name = "oferta_activa")
    private Boolean ofertaActiva = false;

    @OneToMany(mappedBy = "categoria")
    private Set<Producto> productos = new LinkedHashSet<>();


    public int getCantRegalo()
    {
        if (cantLleva == null || cantPaga == null) return 0;
        return cantLleva - cantPaga;
    }

    public String getDescripcionOferta()
    {
        if (!Boolean.TRUE.equals(ofertaActiva)) return "Sin oferta";
        return cantLleva + "x" + cantPaga;
    }
}
