--- 1. Création des ENUMS (Types personnalisés) ---
CREATE TYPE dish_type AS ENUM ('START', 'MAIN', 'DESSERT', 'BEVERAGE');
CREATE TYPE ingredient_category AS ENUM ('VEGETABLE', 'MEAT', 'FRUIT', 'DAIRY', 'OTHER');
CREATE TYPE unit_type AS ENUM ('KG', 'L', 'PCS');
CREATE TYPE mouvement_type AS ENUM ('IN', 'OUT');

CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price NUMERIC(10, 2) NOT NULL,
                            category ingredient_category NOT NULL
);

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      dish_type dish_type NOT NULL,
                      price NUMERIC(10, 2) -- Prix de vente (peut être NULL selon TD3 Q5)
);

CREATE TABLE DishIngredient (
                                id SERIAL PRIMARY KEY,
                                id_dish INT REFERENCES dish(id),
                                id_ingredient INT REFERENCES ingredient(id),
                                quantity_required NUMERIC(10, 2) NOT NULL,
                                unit unit_type NOT NULL
);

CREATE TABLE stock_movement (
                                id SERIAL PRIMARY KEY,
                                id_ingredient INT REFERENCES ingredient(id),
                                quantity NUMERIC(10, 2) NOT NULL,
                                type mouvement_type NOT NULL,
                                unit unit_type NOT NULL,
                                creation_datetime TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);