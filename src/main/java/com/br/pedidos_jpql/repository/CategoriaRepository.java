package com.br.pedidos_jpql.repository;

import com.br.pedidos_jpql.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


    Optional<Categoria> findByNome(String nome);

    @Query("SELECT p.categoria FROM Produto p GROUP BY p.categoria HAVING COUNT(p) > 10")
    List<Categoria> buscarCategoriasComMaisDe10Produtos();

}
