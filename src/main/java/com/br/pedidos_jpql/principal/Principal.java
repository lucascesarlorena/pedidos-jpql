package com.br.pedidos_jpql.principal;

import com.br.pedidos_jpql.model.Categoria;
import com.br.pedidos_jpql.model.Produto;
import com.br.pedidos_jpql.repository.CategoriaRepository;
import com.br.pedidos_jpql.repository.PedidoRepository;
import com.br.pedidos_jpql.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;

import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);

    private CategoriaRepository categoriaRepository;
    private PedidoRepository pedidoRepository;
    private ProdutoRepository produtoRepository;

    public Principal(CategoriaRepository categoriaRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public void exibirMenu(){
        int opcao = -1;

        while (opcao != 0) {
            var menu = """
                
                === MENU ===
                1 - Cadastrar categoria
                2 - Cadastrar produto
                3 - Cadastrar pedido
                4 - Listar produtos
                
                0 - Sair
                """;

            System.out.println(menu);
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarCategoria();
                case 2 -> cadastrarProduto();
                case 3 -> cadastrarPedido();
                case 4 -> listarProdutos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }


    private void cadastrarCategoria() {
        System.out.println("Qual o nome da categoria: ");
        String nomeCategoria = scanner.nextLine().trim();
        if(nomeCategoria.isBlank()){
            System.out.println("Nome da categoria não pode ser vazio!");
            return;
        }
        categoriaRepository.findByNome(nomeCategoria).ifPresentOrElse(
                categoria -> System.out.println("Categoria já cadastrada: " + categoria.getNome()),
                () -> {
                    var categoria = new Categoria(nomeCategoria);
                    categoriaRepository.save(categoria);
                    System.out.println("Categoria cadastrada com sucesso: " + categoria);
                }
        );

    }

    private void cadastrarProduto() {
        System.out.println("Qual o nome do produto: ");
        String nomeProduto = scanner.nextLine().trim();
        if(nomeProduto.isBlank()) {
            System.out.println("Nome do produto não pode ser vazio!");
            return;
        }
        if (produtoRepository.findByNome(nomeProduto).isPresent()) {
            System.out.println("Produto já cadastrado: " + nomeProduto);
            return;
        }
        System.out.println("Qual o preço do produto (ex: 3.000,50): ");
        String precoTexto = scanner.nextLine().trim();
        double precoProduto = Double.parseDouble(precoTexto.replace(".", "").replace(",", "."));
        if(precoProduto <= 0) {
            System.out.println("Preço do produto deve ser maior que zero!");
            return;
        }
        System.out.println("Qual a categoria do produto: ");
        String nomeCategoria = scanner.nextLine().trim();
        if (nomeCategoria.isBlank()) {
            System.out.println("Nome da categoria não pode ser vazio!");
            return;
        }
        var categoria = categoriaRepository.findByNome(nomeCategoria).orElseGet(() -> {
            System.out.println("Categoria não encontrada, cadastrando nova categoria: " + nomeCategoria);
            var novaCategoria = new Categoria(nomeCategoria);
            categoriaRepository.save(novaCategoria);
            return novaCategoria;
        });
        var produto = new Produto(nomeProduto, precoProduto, categoria);
        produtoRepository.save(produto);
        System.out.println("Produto cadastrado com sucesso: " + produto);
    }

    private void cadastrarPedido() {
        System.out.println("Cadastrando pedido...");
    }

    private void listarProdutos() {
        System.out.println("...Listando produtos...");

        var produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado!");
        } else {
            produtos.forEach(System.out::println);
        }

    }
}
