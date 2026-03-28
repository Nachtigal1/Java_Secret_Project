

INSERT INTO categories(name) VALUES ('Дошки') ON CONFLICT DO NOTHING;
INSERT INTO categories(name) VALUES ('Костюми') ON CONFLICT DO NOTHING;
INSERT INTO categories(name) VALUES ('Аксесуари') ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Shortboard Pro X', 'Карбонова дошка для досвідчених серферів', 24500, 100, '', (SELECT id FROM categories WHERE name = 'Дошки'))
    ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Longboard Classic', 'Класична довга дошка для початківців', 18900, 100, '', (SELECT id FROM categories WHERE name = 'Дошки'))
    ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Гідрокостюм Winter 5mm', 'Теплий костюм для холодної води', 8900, 100, '', (SELECT id FROM categories WHERE name = 'Костюми'))
    ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Springsuit Summer 3mm', 'Літній костюм для теплих вод', 5400, 100, '', (SELECT id FROM categories WHERE name = 'Костюми'))
    ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Лиш FCS II', 'Надійний шнур для будь-якої дошки', 890, 100, '', (SELECT id FROM categories WHERE name = 'Аксесуари'))
    ON CONFLICT DO NOTHING;

INSERT INTO products (name, description, price, quantity, image, category_id)
VALUES ('Рюкзак Boardbag', 'Захисний чохол для дошки', 2100, 100, '', (SELECT id FROM categories WHERE name = 'Аксесуари'))
    ON CONFLICT DO NOTHING;