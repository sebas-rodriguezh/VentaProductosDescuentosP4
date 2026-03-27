package com.example.ideaproductoscomprasdescuentos.presentation;

import jakarta.servlet.http.HttpSession;
import com.example.ideaproductoscomprasdescuentos.logic.Usuario;
import com.example.ideaproductoscomprasdescuentos.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private Service service;

    // GET / — página de inicio con formulario de login
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // Si ya hay sesión activa, redirige al catálogo de productos
        if (session.getAttribute("clienteId") != null) {
            return "redirect:/productos";
        }
        return "/index";
    }

    // GET /login — mismo que GET /, muestra el formulario
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("clienteId") != null) {
            return "redirect:/productos";
        }
        return "/index";
    }

    // POST /login — procesa las credenciales
    @PostMapping("/login")
    public String procesarLogin(HttpSession session, Model model,
                                @RequestParam String clienteId,
                                @RequestParam String password) {

        if (clienteId == null || clienteId.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Usuario y contraseña son requeridos");
            return "/index";
        }

        Usuario cliente = service.autenticar(clienteId, password);

        if (cliente != null) {
            session.setAttribute("clienteId", cliente.getId());
            session.setAttribute("clienteNombre", cliente.getRol());
            return "redirect:/productos";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "/index";
        }
    }

    // GET /logout — invalida la sesión y vuelve al inicio
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}