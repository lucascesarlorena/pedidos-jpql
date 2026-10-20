package com.br.pedidos_jpql.repository;

import com.br.pedidos_jpql.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {


    @Query("SELECT p FROM Pedido p WHERE p.data BETWEEN :dataInicio AND :dataFim ORDER BY p.data")
    List<Pedido> buscarPedidosEntreDatas(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);


}
