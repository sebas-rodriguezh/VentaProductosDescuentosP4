package com.example.ideaproductoscomprasdescuentos.data;

import com.example.ideaproductoscomprasdescuentos.logic.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,String> {
    List<Producto> findByCategoriaId(String categoriaId);
}
