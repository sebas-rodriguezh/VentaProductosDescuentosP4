package com.example.ideaproductoscomprasdescuentos.data;

import com.example.ideaproductoscomprasdescuentos.logic.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String>{
}
