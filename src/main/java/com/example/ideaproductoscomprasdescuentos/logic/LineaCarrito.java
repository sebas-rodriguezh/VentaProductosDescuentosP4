package com.example.ideaproductoscomprasdescuentos.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "linea_carrito")
public class LineaCarrito
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Cantidad total que el cliente lleva (ej: 4 pantalones)
    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "cantidad_paga")
    private Integer cantidadPaga;

    @Column(name = "cantidad_regalo")
    private Integer cantidadRegalo;

    @Column(name = "oferta_aplicada")
    private Boolean ofertaAplicada = false;

    public Double getSubtotal() {
        if (producto == null || producto.getPrecio() == null || cantidadPaga == null) return 0.0;
        return producto.getPrecio() * cantidadPaga;
    }

    // Cuánto se ahorró el cliente gracias a la oferta
    public Double getAhorro() {
        if (producto == null || producto.getPrecio() == null || cantidadRegalo == null) return 0.0;
        return producto.getPrecio() * cantidadRegalo;
    }
}
