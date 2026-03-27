package com.example.ideaproductoscomprasdescuentos.presentation;

import com.example.ideaproductoscomprasdescuentos.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductoController {
    @Autowired
    private Service service;


    @GetMapping("/productos")
    public String mostrarProductos(Model model, HttpSession session)
    {
        String clienteId = (String) session.getAttribute("clienteId");

        if (clienteId == null)
        {
            return "redirect:/";
        }

        List<Categoria> categorias = service.obtenerCategorias();
        List<LineaCarrito> carrito = service.obtenerCarrito(clienteId);
        Double total = service.calcularTotalCarrito(clienteId);
        Double ahorro= service.calcularTotalAhorro(clienteId);

        model.addAttribute("categorias", categorias);
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        model.addAttribute("ahorro", ahorro);
        // No se manda "productos" aquí, el cliente debe seleccionar una categoría primero

        return "presentation/productos/productos";
    }

    @GetMapping("/filtrar")
    public String filtrarProductos(HttpSession session, Model model,
                                   @RequestParam String categoriaId)
    {
        String clienteId = (String) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/";
        }

        List<Categoria> categorias = service.obtenerCategorias();
        List<LineaCarrito> carrito = service.obtenerCarrito(clienteId);
        Double total = service.calcularTotalCarrito(clienteId);
        Double ahorro = service.calcularTotalAhorro(clienteId);
        List<Producto> productos = service.obtenerProductosPorCategoria(categoriaId);

        model.addAttribute("categorias", categorias);
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        model.addAttribute("ahorro", ahorro);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoriaId);

        return "presentation/productos/productos";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(HttpSession session,
                                   @RequestParam String productoId) {
        String clienteId = (String) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/";
        }

        service.agregarProductosAlCarrito(clienteId, productoId);
        return "redirect:/productos";
    }

    @PostMapping("/confirmar")
    public String confirmarCompra(HttpSession session, Model model) {
        String clienteId = (String) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/";
        }

        service.confirmarCompra(clienteId);
        model.addAttribute("mensaje", "¡Compra confirmada! Gracias por su preferencia.");
        return "redirect:/productos";
    }
}