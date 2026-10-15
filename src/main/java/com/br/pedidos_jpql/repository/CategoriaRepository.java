package com.br.pedidos_jpql.repository;

import com.br.pedidos_jpql.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


    Optional<Categoria> findByNome(String nome);

}
