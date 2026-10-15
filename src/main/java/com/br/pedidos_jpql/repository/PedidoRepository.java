package com.br.pedidos_jpql.repository;

import com.br.pedidos_jpql.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {


}
