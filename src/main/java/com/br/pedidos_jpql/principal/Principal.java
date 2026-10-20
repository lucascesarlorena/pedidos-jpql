package com.br.pedidos_jpql.principal;

import com.br.pedidos_jpql.model.Categoria;
import com.br.pedidos_jpql.model.Pedido;
import com.br.pedidos_jpql.model.Produto;
import com.br.pedidos_jpql.repository.CategoriaRepository;
import com.br.pedidos_jpql.repository.PedidoRepository;
import com.br.pedidos_jpql.repository.ProdutoRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            var menu = """
                    
                    === MENU ===
                    1 - Cadastrar categoria
                    2 - Cadastrar produto
                    3 - Cadastrar pedido
                    4 - Listar produtos
                    5 - Produtos com preço maior que um valor
                    6 - Listar produtos ordenados por preço crescente
                    7 - Listar produtos ordenados por preço decrescente
                    8 - Produtos que começam com uma letra
                    9 - Pedidos entre duas datas
                    10 - Média de preços dos produtos
                    11 - Preço máximo de uma categoria
                    12 - Contar produtos por categoria
                    13 - Buscar categorias com mais de 10 produtos
                    14 - Buscar produtos por nome ou categoria
                    15 - Buscar top 5 produtos mais caros
                    
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
                case 5 -> buscarProdutosComPrecoMaiorQue();
                case 6 -> buscarProdutosOrdenadosPorPrecoCrescente();
                case 7 -> buscarProdutosOrdenadosPorPrecoDecrescente();
                case 8 -> buscarProdutosQueComecamComUmaLetra();
                case 9 -> buscarPedidosEntreDatas();
                case 10 -> buscarMediaDePrecoDosProdutos();
                case 11 -> buscarPrecoMaximoPorCategoria();
                case 12 -> contarProdutosPorCategoria();
                case 13 -> buscarCategoriasComMaisDe10Produtos();
                case 14 -> buscarProdutosPorNomeOuCategoria();
                case 15 -> buscarTop5ProdutosMaisCaros();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarCategoria() {
        System.out.println("Qual o nome da categoria: ");
        String nomeCategoria = scanner.nextLine().trim();
        if (nomeCategoria.isBlank()) {
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
        if (nomeProduto.isBlank()) {
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
        if (precoProduto <= 0) {
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
        System.out.println("...Cadastrando pedido...");
        System.out.println("Qual a data do pedido: ");
        String dataPedido = scanner.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataDoPedido;

        if (dataPedido.isBlank()) {
            dataDoPedido = LocalDate.now();
            System.out.println("Data do pedido não informada, usando data atual: " + dataDoPedido.format(formatter));
        } else {
            dataDoPedido = LocalDate.parse(dataPedido, formatter);
        }
        var pedido = new Pedido(dataDoPedido);
        pedidoRepository.save(pedido);
        System.out.println("Pedido cadastrado com sucesso: " + pedido);
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


    private void buscarProdutosComPrecoMaiorQue() {
        System.out.println("qual o valor do preço?");
        String precoTexto = scanner.nextLine().trim();
        double preco = Double.parseDouble(precoTexto.replace(".", "").replace(",", "."));
        var produtos = produtoRepository.buscarProdutosComPrecoMaiorQue(preco);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado com preço maior que: " + preco);
        } else {
            System.out.println("Produtos com preço maior que " + preco + ":");
            produtos.forEach(System.out::println);
        }
    }

    private void buscarProdutosOrdenadosPorPrecoCrescente() {
        var produtos = produtoRepository.buscarProdutosOrdenadosPorPrecoCrescente();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Produtos ordenados por preço crescente:");
            produtos.forEach(System.out::println);
        }
    }

    private void buscarProdutosOrdenadosPorPrecoDecrescente() {
        var produtos = produtoRepository.buscarProdutosOrdenadosPorPrecoDecrescente();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Produtos ordenados por preço decrescente:");
            produtos.forEach(System.out::println);
        }
    }

    private void buscarProdutosQueComecamComUmaLetra() {
        System.out.println("Qual a letra inicial dos produtos que deseja buscar?");
        String letra = scanner.nextLine().trim();
        if (letra.isBlank()) {
            System.out.println("Letra não pode ser vazia!");
            return;
        }
        var produtos = produtoRepository.buscarProdutosQueComecamComUmaLetra(letra);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado que comece com a letra: " + letra);
        } else {
            System.out.println("Produtos que começam com a letra " + letra + ":");
            produtos.forEach(System.out::println);
        }

    }

    private void buscarPedidosEntreDatas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Qual a data inicial (dd/MM/yyyy): ");
        String dataInicioTexto = scanner.nextLine().trim();
        LocalDate dataInicio = LocalDate.parse(dataInicioTexto, formatter);
        System.out.println("Qual a data final (dd/MM/yyyy): ");
        String dataFimTexto = scanner.nextLine().trim();
        LocalDate dataFim = LocalDate.parse(dataFimTexto, formatter);
        var pedidos = pedidoRepository.buscarPedidosEntreDatas(dataInicio, dataFim);
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado entre as datas: " + dataInicio.format(formatter) + " e " + dataFim.format(formatter));
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private void buscarMediaDePrecoDosProdutos() {
        Double mediaPreco = produtoRepository.buscarMediaDePrecoDosProdutos();
        if (mediaPreco == null) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.printf("Média de preços dos produtos: %.2f%n", mediaPreco);

        }
    }

    private void buscarPrecoMaximoPorCategoria() {
        System.out.println("Qual o nome da categoria para buscar o preco maximo?");
        String nomeCategoria = scanner.nextLine().trim();
        if (nomeCategoria.isBlank()) {
            System.out.println("Nome da categoria não pode ser vazio!");
            return;
        }
        Double precoMaximo = produtoRepository.buscarPrecoMaximoPorCategoria(nomeCategoria);
        if (precoMaximo == null) {
            System.out.println("Nenhum produto encontrado na categoria: " + nomeCategoria);
        } else {
            System.out.printf("Preço máximo da categoria %s: %.2f%n", nomeCategoria, precoMaximo);
        }
    }


    private void contarProdutosPorCategoria(){
        var resultado = produtoRepository.contarProdutosPorCategoria();
        if(resultado.isEmpty()){
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Contagem de produtos por categoria:");
            for (Object[] linha : resultado) {
                System.out.printf("Categoria: %s, Quantidade: %d%n", linha[0], linha[1]);
            }
        }
    }

    private void buscarCategoriasComMaisDe10Produtos(){
        var categorias = categoriaRepository.buscarCategoriasComMaisDe10Produtos();
        if(categorias.isEmpty()){
            System.out.println("Nenhuma categoria encontrada com mais de 10 produtos.");
        } else {
            System.out.println("Categorias com mais de 10 produtos:");
            categorias.forEach(categoria -> System.out.println(categoria.getNome()));
        }
    }

    private void buscarProdutosPorNomeOuCategoria(){
        System.out.println("Digite o termo para buscar produtos por nome ou categoria: ");
        String termo = scanner.nextLine().trim();
        if(termo.isBlank()){
            System.out.println("Termo não pode ser vazio!");
            return;
        }
        var produtos = produtoRepository.buscarProdutosPorNomeOuCategoria(termo);
        if(produtos.isEmpty()){
            System.out.println("Nenhum produto encontrado com o termo: " + termo);
        } else {
            System.out.println("Produtos encontrados com o termo " + termo + ":");
            produtos.forEach(System.out::println);
        }
    }

    private void buscarTop5ProdutosMaisCaros(){
        var produtos = produtoRepository.buscarTop5ProdutosMaisCaros();
        if(produtos.isEmpty()){
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Top 5 produtos mais caros:");
            produtos.forEach(System.out::println);
        }
    }

}