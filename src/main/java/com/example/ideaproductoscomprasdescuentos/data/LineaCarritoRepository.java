package com.example.ideaproductoscomprasdescuentos.data;

import com.example.ideaproductoscomprasdescuentos.logic.LineaCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineaCarritoRepository extends JpaRepository<LineaCarrito, Integer> {
    List<LineaCarrito> findByUsuarioId(String usuarioId);

    Optional<LineaCarrito> findByUsuarioIdAndProductoId(String usuarioId, String productoId);
}
