package com.example.ideaproductoscomprasdescuentos.logic;

import com.example.ideaproductoscomprasdescuentos.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@org.springframework.stereotype.Service
public class Service
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private LineaCarritoRepository lineaCarritoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario autenticar (String idUsuario, String password)
    {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario != null)
        {
            if (passwordEncoder.matches(password, usuario.getClave()))
            {
                return usuario;
            }
        }
        return null;
    }


    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosPorCategoria(String categoriaId)
    {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<LineaCarrito> obtenerCarrito(String usuarioId) {
        return lineaCarritoRepository.findByUsuarioId(usuarioId);
    }

    public void agregarProductosAlCarrito(String usuarioId, String productoId)
    {
        Usuario cliente = usuarioRepository.findById(usuarioId).orElse(null);
        Producto producto = productoRepository.findById(productoId).orElse(null);

        if (cliente == null || producto == null)
        {
            throw new RuntimeException("Cliente o producto no encontrado");
        }

        Optional<LineaCarrito> lineaExistente = lineaCarritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);

        LineaCarrito linea;
        if (lineaExistente.isPresent()) {
            linea = lineaExistente.get();
            linea.setCantidad(linea.getCantidad() + 1);
        } else {
            linea = new LineaCarrito();
            linea.setUsuario(cliente);
            linea.setProducto(producto);
            linea.setCantidad(1);
        }

        calcularRegalia(linea);
        lineaCarritoRepository.save(linea);
    }

    public void confirmarCompra(String usuarioId) {
        List<LineaCarrito> carrito = obtenerCarrito(usuarioId);
        for (LineaCarrito linea : carrito) {
            Producto producto = linea.getProducto();
            int cantidadComprada = linea.getCantidad();
            producto.setStock(producto.getStock() - cantidadComprada);
            productoRepository.save(producto);
        }
        vaciarCarrito(usuarioId);
    }

    // =============================================
    // NÚCLEO DEL MÓDULO CRM: CÁLCULO DE REGALÍAS
    //
    // Regla 4x3:
    //   ciclos        = cantidad / cant_lleva  (división entera)
    //   cant_regalo   = ciclos * (cant_lleva - cant_paga)
    //   cant_paga     = cantidad - cant_regalo
    //
    // Ejemplo con 4 pantalones en un 4x3:
    //   ciclos      = 4 / 4 = 1
    //   cant_regalo = 1 * 1 = 1
    //   cant_paga   = 4 - 1 = 3   → el cliente paga 3, lleva 4
    //
    // Ejemplo con 2 pantalones (no llega al mínimo):
    //   ciclos      = 2 / 4 = 0   → sin ciclos completos
    //   cant_regalo = 0
    //   cant_paga   = 2           → paga precio normal
    // =============================================

    private void calcularRegalia (LineaCarrito linea)
    {
        Categoria categoria = linea.getProducto().getCategoria();

        if (categoria != null && categoria.getCantLleva() != null && linea.getCantidad() >= categoria.getCantLleva())
        {
            int cantidad    = linea.getCantidad();
            int cantLleva   = categoria.getCantLleva();
            int cantPaga    = categoria.getCantPaga();
            int regalo1Ciclo = cantLleva - cantPaga; // cuántos se regalan por ciclo (ej: 1)

            int ciclos       = cantidad / cantLleva;           // ciclos completos de oferta
            int cantRegalo   = ciclos * regalo1Ciclo;          // total regalos
            int cantidadPaga = cantidad - cantRegalo;          // lo que realmente paga

            linea.setCantidadRegalo(cantRegalo);
            linea.setCantidadPaga(cantidadPaga);
            linea.setOfertaAplicada(true);
        } else {

            linea.setCantidadRegalo(0);
            linea.setCantidadPaga(linea.getCantidad());
            linea.setOfertaAplicada(false);
        }
    }

    public Double calcularTotalCarrito(String usuarioId) {
        List<LineaCarrito> carrito = lineaCarritoRepository.findByUsuarioId(usuarioId);
        double total = 0.0;
        for (LineaCarrito linea : carrito) {
            total += linea.getSubtotal(); // usa cantidadPaga * precio
        }
        return total;
    }

    public Double calcularTotalAhorro(String usuarioId) {
        List<LineaCarrito> carrito = lineaCarritoRepository.findByUsuarioId(usuarioId);
        double ahorro = 0.0;
        for (LineaCarrito linea : carrito) {
            ahorro += linea.getAhorro();
        }
        return ahorro;
    }


    public void vaciarCarrito(String usuarioId) {
        List<LineaCarrito> carrito = lineaCarritoRepository.findByUsuarioId(usuarioId);
        lineaCarritoRepository.deleteAll(carrito);
    }

}