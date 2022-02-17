DROP TABLE IF EXISTS user CASCADE;
DROP TABLE IF EXISTS affiliate CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS comment CASCADE;

CREATE TABLE IF NOT EXISTS user (
    user_id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    affiliate_id INT,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS affiliate (
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE user ADD CONSTRAINT FK_user_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliate;

CREATE TABLE IF NOT EXISTS store (
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    affiliate_id INT,
    latitude VARCHAR(20),
    longitude VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE store ADD CONSTRAINT FK_store_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliate;

CREATE TABLE IF NOT EXISTS recipe (
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    affiliate_id INT,
    store_id INT,
    user_id INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE recipe ADD CONSTRAINT FK_recipe_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliate;
ALTER TABLE recipe ADD CONSTRAINT FK_recipe_store FOREIGN KEY (store_id) REFERENCES store;
ALTER TABLE recipe ADD CONSTRAINT FK_recipe_user FOREIGN KEY (user_id) REFERENCES user;

CREATE TABLE IF NOT EXISTS ingredient (
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    price INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS recipe_ingredient (
    id SERIAL NOT NULL,
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE recipe_ingredient ADD CONSTRAINT FK_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES recipe;
ALTER TABLE recipe_ingredient ADD CONSTRAINT FK_recipe_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient;

CREATE TABLE IF NOT EXISTS comment (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  recipe_id INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE comment ADD CONSTRAINT FK_comment_topic FOREIGN KEY (recipe_id) REFERENCES recipe;