package com.example.ideaproductoscomprasdescuentos.data;


import com.example.ideaproductoscomprasdescuentos.logic.Categoria;
import com.example.ideaproductoscomprasdescuentos.logic.Producto;
import com.example.ideaproductoscomprasdescuentos.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner
{
    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        cargarCategorias();
        cargarUsuarios();
        cargarProductos();
    }

    private void cargarCategorias() {
        // Pantalones con oferta 4x3 (lleva 4, paga 3, regalo = 1 por ciclo)
        Categoria pantalones = new Categoria();
        pantalones.setId("PANT");
        pantalones.setNombre("Pantalones");
        pantalones.setCantLleva(4);
        pantalones.setCantPaga(3);
        pantalones.setOfertaActiva(true);
        categoriaRepository.save(pantalones);

        // Camisas con oferta 3x2 (lleva 3, paga 2, regalo = 1 por ciclo)
        Categoria camisas = new Categoria();
        camisas.setId("CAMI");
        camisas.setNombre("Camisas");
        camisas.setCantLleva(3);
        camisas.setCantPaga(2);
        camisas.setOfertaActiva(true);
        categoriaRepository.save(camisas);

        // Zapatos sin oferta activa
        Categoria zapatos = new Categoria();
        zapatos.setId("ZAP");
        zapatos.setNombre("Zapatos");
        zapatos.setCantLleva(null);
        zapatos.setCantPaga(null);
        zapatos.setOfertaActiva(false);
        categoriaRepository.save(zapatos);
    }
    
    private void cargarUsuarios() {
        Usuario Usuario1 = new Usuario();
        Usuario1.setId("C001");
        Usuario1.setClave(passwordEncoder.encode("1234"));
        Usuario1.setRol("Usuario");
        UsuarioRepository.save(Usuario1);

        Usuario cajero = new Usuario();
        cajero.setId("cajero1");
        cajero.setClave(passwordEncoder.encode("admin"));
        cajero.setRol("CAJERO");
        UsuarioRepository.save(cajero);
    }

    private void cargarProductos() {
        // Recuperar categorías ya guardadas
        Categoria pantalones = categoriaRepository.findById("PANT").orElseThrow();
        Categoria camisas    = categoriaRepository.findById("CAMI").orElseThrow();
        Categoria zapatos    = categoriaRepository.findById("ZAP").orElseThrow();

        // --- Pantalones (con oferta 4x3) ---
        crearProducto("P001", "Pantalón Jean Clásico",    15000.0, 20, pantalones);
        crearProducto("P002", "Pantalón Casual Beige",    18000.0, 15, pantalones);
        crearProducto("P003", "Pantalón Deportivo Negro", 12000.0, 30, pantalones);

        // --- Camisas (con oferta 3x2) ---
        crearProducto("C001", "Camisa Formal Blanca",     9500.0,  25, camisas);
        crearProducto("C002", "Camisa Casual a Cuadros",  8000.0,  18, camisas);

        // --- Zapatos (sin oferta) ---
        crearProducto("Z001", "Zapato de Cuero Negro",   35000.0,  10, zapatos);
    }

    private void crearProducto(String id, String nombre, Double precio, Integer stock, Categoria categoria) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCategoria(categoria);
        productoRepository.save(p);
    }


}