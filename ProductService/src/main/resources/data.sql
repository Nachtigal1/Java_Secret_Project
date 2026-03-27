INSERT INTO categories(name)
SELECT 'Доски'
    WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE name = 'Доски'
);

INSERT INTO categories(name)
SELECT 'Костюмы'
    WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE name = 'Костюмы'
);

INSERT INTO categories(name)
SELECT 'Аксессуары'
    WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE name = 'Аксессуары'
);