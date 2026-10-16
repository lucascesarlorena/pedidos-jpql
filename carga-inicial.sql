-- =========================================
-- Carga inicial do banco - pedidos-jpql
-- Rodar com o banco vazio (tabelas já criadas pelo Hibernate)
-- =========================================

-- Categorias
INSERT INTO categoria (nome) VALUES
                                 ('Eletronico'),
                                 ('Livros'),
                                 ('Alimentos'),
                                 ('Moveis')
    ON CONFLICT (nome) DO NOTHING;

-- Produtos
INSERT INTO produto (nome, preco, categoria_id) VALUES
-- Eletronico (12 produtos, para o exercício 9)
('Celular Samsung S23 FE',     3000.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Notebook Dell Inspiron',     4500.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Monitor LG 27',              1200.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Mouse Logitech',              150.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Teclado Mecanico Redragon',   280.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Fone JBL Bluetooth',          350.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Smart TV Samsung 50',        2800.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Tablet Galaxy Tab',          1900.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Carregador Turbo USB-C',       89.90, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Caixa de Som Bluetooth',      220.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('Webcam Full HD',              199.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
('MacBook Air M2',             7500.00, (SELECT id FROM categoria WHERE nome = 'Eletronico')),
-- Livros
('Codigo Limpo',                 95.00, (SELECT id FROM categoria WHERE nome = 'Livros')),
('Java Como Programar',         320.00, (SELECT id FROM categoria WHERE nome = 'Livros')),
('Arquitetura Limpa',            89.00, (SELECT id FROM categoria WHERE nome = 'Livros')),
('O Programador Pragmatico',    110.00, (SELECT id FROM categoria WHERE nome = 'Livros')),
-- Alimentos
('Cafe Pilao 500g',              18.90, (SELECT id FROM categoria WHERE nome = 'Alimentos')),
('Chocolate Lacta',               8.50, (SELECT id FROM categoria WHERE nome = 'Alimentos')),
('Arroz Camil 5kg',              27.00, (SELECT id FROM categoria WHERE nome = 'Alimentos')),
('Macarrao Barilla',             12.00, (SELECT id FROM categoria WHERE nome = 'Alimentos')),
-- Moveis
('Cadeira Gamer',              1100.00, (SELECT id FROM categoria WHERE nome = 'Moveis')),
('Mesa Escritorio',             650.00, (SELECT id FROM categoria WHERE nome = 'Moveis')),
('Estante de Livros',           420.00, (SELECT id FROM categoria WHERE nome = 'Moveis'))
    ON CONFLICT (nome) DO NOTHING;

-- Pedidos (datas espalhadas, para o exercício 5)
INSERT INTO pedido (data) VALUES
                              ('2010-10-10'),
                              ('2025-01-15'),
                              ('2025-06-20'),
                              ('2025-12-05'),
                              ('2026-03-10'),
                              ('2026-08-01'),
                              (CURRENT_DATE);