create type dish_type as enum ('STARTER', 'MAIN', 'DESSERT');


create table dish
(
    id        serial primary key,
    name      varchar(255),
    dish_type dish_type
);

create type ingredient_category as enum ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

create table ingredient
(
    id       serial primary key,
    name     varchar(255),
    price    numeric(10, 2),
    category ingredient_category,
    id_dish  int references dish (id)
);

alter table dish
    add column if not exists price numeric(10, 2);


alter table ingredient
    add column if not exists required_quantity numeric(10, 2);


-- Suppression de la colonne id_dish dans ingredient pour permettre la réutilisation (Normalisation)
ALTER TABLE ingredient DROP COLUMN id_dish;
ALTER TABLE dish RENAME COLUMN price TO selling_price;

-- Création du type ENUM pour les unités [cite: 36]
DO $$ BEGIN
    CREATE TYPE unit_type AS ENUM ('PCS', 'KG', 'L');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Création de la table de jointure normalisée [cite: 24, 30]
CREATE TABLE DishIngredient (
                                id SERIAL PRIMARY KEY,
                                id_dish INT REFERENCES dish(id),
                                id_ingredient INT REFERENCES ingredient(id),
                                quantity_required NUMERIC(10, 2) NOT NULL,
                                unit unit_type NOT NULL
);