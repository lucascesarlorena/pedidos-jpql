package com.br.pedidos_jpql.repository;

import com.br.pedidos_jpql.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNome(String nome);



    @Query("SELECT p FROM Produto p WHERE p.preco > :preco")
    List<Produto> buscarProdutosComPrecoMaiorQue(@Param("preco") Double preco);

    @Query("SELECT p FROM Produto p order by p.preco asc")
    List<Produto> buscarProdutosOrdenadosPorPrecoCrescente();

    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> buscarProdutosOrdenadosPorPrecoDecrescente();

    @Query("SELECT P FROM Produto P WHERE UPPER(P.nome) LIKE UPPER(CONCAT(:letra, '%'))")
    List<Produto> buscarProdutosQueComecamComUmaLetra(@Param("letra") String letra);

    @Query("SELECT AVG(p.preco) FROM Produto p")
    Double buscarMediaDePrecoDosProdutos();

}
