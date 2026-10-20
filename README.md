# Pedidos JPQL

Aplicação de linha de comando em **Java 17** e **Spring Boot** para praticar consultas com **JPQL** e **SQL nativo** usando **Spring Data JPA** e **PostgreSQL**.

> 📚 **Projeto de estudo.** Resolução de uma lista de exercícios do módulo de **Spring Data JPA** da formação Java da [Alura](https://www.alura.com.br/), construída do zero com um menu interativo no console para cadastrar dados e executar cada consulta.

## 📌 Sobre o projeto

O sistema gerencia **categorias**, **produtos** e **pedidos**. Pelo menu é possível cadastrar registros e rodar as 11 consultas da lista de exercícios, vendo no console tanto o resultado quanto o SQL gerado pelo Hibernate.

O foco do projeto está na camada de **repository**: filtros com parâmetros, ordenação, busca por padrão de texto, intervalo de datas, funções de agregação, agrupamento com `GROUP BY`/`HAVING`, navegação entre entidades e consulta nativa.

## 🛠️ Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven

## 🏗️ Estrutura

```
com.br.pedidos_jpql
├── model
│   ├── Categoria.java
│   ├── Produto.java
│   └── Pedido.java
├── repository
│   ├── CategoriaRepository.java
│   ├── ProdutoRepository.java
│   └── PedidoRepository.java
├── principal
│   └── Principal.java          ← menu interativo (Scanner)
└── PedidosJpqlApplication.java ← CommandLineRunner que abre o menu
```

- **`model`**: entidades JPA. `Produto` tem um relacionamento `@ManyToOne` com `Categoria`.
- **`repository`**: interfaces `JpaRepository` com as consultas em `@Query`.
- **`principal`**: menu no console. Recebe os repositories pelo construtor.
- **`PedidosJpqlApplication`**: implementa `CommandLineRunner`, recebe os repositories por injeção de dependência e inicia o menu.

### Modelo de dados

| Entidade    | Campos                                 | Observações                                       |
| ----------- | -------------------------------------- | ------------------------------------------------- |
| `Categoria` | `id`, `nome`                           | `nome` único e obrigatório                        |
| `Produto`   | `id`, `nome`, `preco`, `categoria`     | `nome` único; `@ManyToOne` com `Categoria`        |
| `Pedido`    | `id`, `data`                           | `data` do tipo `LocalDate`                        |

## 🔎 Consultas implementadas

| #  | Exercício                                    | Repository  | Recursos usados                          |
| -- | -------------------------------------------- | ----------- | ---------------------------------------- |
| 1  | Produtos com preço maior que um valor        | `Produto`   | `WHERE`, parâmetro nomeado, `@Param`     |
| 2  | Produtos ordenados por preço crescente       | `Produto`   | `ORDER BY ... ASC`                       |
| 3  | Produtos ordenados por preço decrescente     | `Produto`   | `ORDER BY ... DESC`                      |
| 4  | Produtos que começam com uma letra           | `Produto`   | `LIKE`, `CONCAT`, `UPPER`                |
| 5  | Pedidos feitos entre duas datas              | `Pedido`    | `BETWEEN`, parâmetros `LocalDate`        |
| 6  | Média de preços dos produtos                 | `Produto`   | `AVG`, retorno `Double`                  |
| 7  | Preço máximo de uma categoria                | `Produto`   | `MAX`, navegação `p.categoria.nome`      |
| 8  | Quantidade de produtos por categoria         | `Produto`   | `COUNT`, `GROUP BY`                      |
| 9  | Categorias com mais de 10 produtos           | `Categoria` | `GROUP BY`, `HAVING`                     |
| 10 | Produtos filtrados por nome ou categoria     | `Produto`   | `OR`, `LIKE` com `%` dos dois lados      |
| 11 | Os 5 produtos mais caros (consulta nativa)   | `Produto`   | `nativeQuery = true`, `LIMIT`            |

### Exemplos

**Busca por padrão, ignorando maiúsculas e minúsculas (exercício 4)**
```java
@Query("SELECT p FROM Produto p WHERE UPPER(p.nome) LIKE UPPER(CONCAT(:letra, '%'))")
List<Produto> buscarProdutosQueComecamComUmaLetra(@Param("letra") String letra);
```

**Agrupamento com filtro de grupo (exercício 9)**
```java
@Query("SELECT p.categoria FROM Produto p GROUP BY p.categoria HAVING COUNT(p) > 10")
List<Categoria> buscarCategoriasComMaisDe10Produtos();
```

**Consulta nativa (exercício 11)**
```java
@Query(nativeQuery = true, value = "SELECT * FROM produto ORDER BY preco DESC LIMIT 5")
List<Produto> buscarTop5ProdutosMaisCaros();
```

## 🖥️ Menu

```
=== MENU ===
1  - Cadastrar categoria
2  - Cadastrar produto
3  - Cadastrar pedido
4  - Listar produtos
5  - Produtos com preço maior que um valor
6  - Produtos ordenados por preço (crescente)
7  - Produtos ordenados por preço (decrescente)
8  - Produtos que começam com uma letra
9  - Pedidos entre duas datas
10 - Média de preços dos produtos
11 - Preço máximo de uma categoria
12 - Quantidade de produtos por categoria
13 - Categorias com mais de 10 produtos
14 - Buscar produtos por nome ou categoria
15 - Top 5 produtos mais caros

0  - Sair
```

Detalhes do cadastro:

- **Categoria**: valida nome vazio e não permite nomes repetidos.
- **Produto**: aceita preço no formato brasileiro (`3.000,50`). Se a categoria informada não existir, ela é criada automaticamente.
- **Pedido**: data no formato `dd/MM/yyyy`; ao deixar em branco, usa a data atual.

## ▶️ Como rodar

### Pré-requisitos

- Java 17+
- PostgreSQL rodando, com um banco criado para o projeto

### Configuração

Ajuste a conexão no `src/main/resources/application.properties`. Recomenda-se usar variáveis de ambiente para usuário e senha, em vez de deixar as credenciais no código:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/NOME_DO_BANCO
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

### Dados de exemplo

O arquivo `carga-inicial.sql`, na raiz do projeto, popula o banco com 4 categorias, 23 produtos e 7 pedidos com datas variadas, pensados para testar todas as consultas (por exemplo, uma categoria com mais de 10 produtos para o exercício 9).

1. Rode a aplicação uma vez para o Hibernate criar as tabelas.
2. Execute o `carga-inicial.sql` no banco (pgAdmin ou `psql`). O script foi feito para um banco vazio.

### Executando

```bash
git clone https://github.com/lucascesarlorena/pedidos-jpql.git
cd pedidos-jpql
./mvnw spring-boot:run
```

O menu aparece no console depois dos logs do Spring.

## 🔭 Possíveis próximos passos

- Ligar pedidos a produtos (`@ManyToMany` ou entidade `ItemPedido`) e consultar os itens de um pedido com `JOIN`
- Substituir o `Object[]` da contagem por categoria por um `record` com *constructor expression*
- Tratar entradas inválidas no menu (preço e data) com `try/catch`
- Testes de repository com `@DataJpaTest`

## 🎯 Contexto

Projeto de estudo feito durante minha transição de carreira para desenvolvimento backend com Java e Spring.