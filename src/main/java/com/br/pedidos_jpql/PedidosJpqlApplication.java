package com.br.pedidos_jpql;

import com.br.pedidos_jpql.principal.Principal;
import com.br.pedidos_jpql.repository.CategoriaRepository;
import com.br.pedidos_jpql.repository.PedidoRepository;
import com.br.pedidos_jpql.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PedidosJpqlApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PedidosJpqlApplication.class, args);
	}

	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	ProdutoRepository produtoRepository;

	@Override
	public void run(String... args) throws Exception {


		Principal principal = new Principal(categoriaRepository, pedidoRepository, produtoRepository);
		principal.exibirMenu();
	}
}
