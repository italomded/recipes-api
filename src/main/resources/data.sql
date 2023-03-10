START TRANSACTION;

INSERT INTO ingredient (id, name, category) VALUES (1, 'Apple', 'FRUITS');
INSERT INTO ingredient (id, name, category) VALUES (2, 'Jackfruit', 'FRUITS');
INSERT INTO ingredient (id, name, category) VALUES (3, 'Kiwi', 'FRUITS');
INSERT INTO ingredient (id, name, category) VALUES (4, 'Banana', 'FRUITS');
INSERT INTO ingredient (id, name, category) VALUES (5, 'Egg', 'MEAT_AND_EGGS');
INSERT INTO ingredient (id, name, category) VALUES (6, 'Sugar', 'SUGARS');
INSERT INTO ingredient (id, name, category) VALUES (7, 'Spring onion', 'VEGETABLES');
INSERT INTO ingredient (id, name, category) VALUES (8, 'Spinach', 'VEGETABLES');
INSERT INTO ingredient (id, name, category) VALUES (9, 'Cabbage', 'VEGETABLES');
INSERT INTO ingredient (id, name, category) VALUES (10, 'Mint', 'VEGETABLES');

INSERT INTO role (id, representation) VALUES (1, 'ROLE_USR');
INSERT INTO role (id, representation) VALUES (2, 'ROLE_ADM');

INSERT INTO user (id, username, password, role_id) VALUES (1, 'alvin', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 2);
INSERT INTO user (id, username, password, role_id) VALUES (2, 'george', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (3, 'alexa', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (4, 'matthew', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (5, 'john', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (6, 'connor', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (7, 'lucas', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (8, 'ben', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (9, 'greg', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);
INSERT INTO user (id, username, password, role_id) VALUES (10, 'thomas', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 1);

INSERT INTO recipe (id, description, title, creator_user_id) VALUES (1, 'A good recipe', 'Simple recipe', 1);
INSERT INTO recipe (id, description, title, creator_user_id) VALUES (2, 'A good recipe', 'Simple recipe', 2);
INSERT INTO recipe (id, description, title, creator_user_id) VALUES (3, 'A good recipe', 'Simple recipe', 3);
INSERT INTO recipe (id, description, title, creator_user_id) VALUES (4, 'A good recipe', 'Simple recipe', 4);
INSERT INTO recipe (id, description, title, creator_user_id) VALUES (5, 'A good recipe', 'Simple recipe', 5);
INSERT INTO recipe (id, description, title, creator_user_id) VALUES (6, 'A good recipe', 'Simple recipe', 5);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (1, 'Do this and to that', 4, 20, 'MG', 1, 1, 1);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (2, 'Do this and to that', 3, 3, 'KG', 2, 2, 1);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (3, 'Do this and to that', 1, 300, 'ML', 3, 3, 1);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (4, 'Do this and to that', 2, 20, 'G', 4, 4, 1);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (5, 'Do this and to that', 7, 1, 'L', 5, 5, 1);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (6, 'Do this and to that', 4, 20, 'MG', 1, 9, 2);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (7, 'Do this and to that', 3, 3, 'KG', 2, 2, 2);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (8, 'Do this and to that', 1, 300, 'ML', 3, 3, 2);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (9, 'Do this and to that', 2, 20, 'G', 4, 4, 2);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (10, 'Do this and to that', 7, 1, 'L', 5, 10, 2);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (11, 'Do this and to that', 4, 20, 'MG', 1, 9, 3);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (12, 'Do this and to that', 3, 3, 'KG', 2, 8, 3);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (13, 'Do this and to that', 1, 300, 'ML', 3, 7, 3);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (14, 'Do this and to that', 2, 20, 'G', 4, 4, 3);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (15, 'Do this and to that', 7, 1, 'L', 5, 5, 3);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (16, 'Do this and to that', 4, 20, 'MG', 1, 6, 4);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (17, 'Do this and to that', 3, 3, 'KG', 2, 7, 4);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (18, 'Do this and to that', 1, 300, 'ML', 3, 8, 4);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (19, 'Do this and to that', 2, 20, 'G', 4, 4, 4);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (20, 'Do this and to that', 7, 1, 'L', 5, 5, 4);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (21, 'Do this and to that', 4, 20, 'MG', 1, 7, 5);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (22, 'Do this and to that', 3, 3, 'KG', 2, 2, 5);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (23, 'Do this and to that', 1, 300, 'ML', 3, 8, 5);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (24, 'Do this and to that', 2, 20, 'G', 4, 4, 5);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (25, 'Do this and to that', 7, 1, 'L', 5, 5, 5);

INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (26, 'Do this and to that', 4, 20, 'MG', 1, 1, 6);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (27, 'Do this and to that', 3, 3, 'KG', 2, 2, 6);
INSERT INTO recipe_ingredient (id, instruction, prepare_minutes, amount, measure, `sequence`, ingredient_id, recipe_id)
	VALUES (28, 'Do this and to that', 1, 300, 'ML', 3, 3, 6);

INSERT INTO image (id, link, recipe_id) VALUES(1, "www.someimages.com/niceimage", 1);
INSERT INTO image (id, link, recipe_id) VALUES(2, "www.someimages.com/niceimage", 1);
INSERT INTO image (id, link, recipe_id) VALUES(3, "www.someimages.com/niceimage", 2);
INSERT INTO image (id, link, recipe_id) VALUES(4, "www.someimages.com/niceimage", 2);
INSERT INTO image (id, link, recipe_id) VALUES(5, "www.someimages.com/niceimage", 3);
INSERT INTO image (id, link, recipe_id) VALUES(6, "www.someimages.com/niceimage", 3);
INSERT INTO image (id, link, recipe_id) VALUES(7, "www.someimages.com/niceimage", 4);
INSERT INTO image (id, link, recipe_id) VALUES(8, "www.someimages.com/niceimage", 4);
INSERT INTO image (id, link, recipe_id) VALUES(9, "www.someimages.com/niceimage", 5);
INSERT INTO image (id, link, recipe_id) VALUES(10,"www.someimages.com/niceimage", 5);

INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(6, 1);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(7, 1);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(7, 2);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(8, 3);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(6, 3);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(7, 3);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(10, 3);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(9, 4);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(10, 5);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(5, 5);
INSERT INTO user_like_recipe (user_id, recipe_id) VALUES(6, 5);

COMMIT;