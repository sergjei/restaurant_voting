INSERT INTO users (name, email, password) VALUES ('User', 'user@yandex.ru', 'password'),       ('Admin', 'admin@gmail.com', 'admin'),       ('User2', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role,user_id)
VALUES('USER',1),
      ('ADMIN', 2),
      ('USER', 2),
      ('USER', 3);
--
INSERT INTO restaurant (name, address) 
VALUES ('Bistro', 'French str'),
       ('NeedForFood', 'Belfast str');


INSERT INTO meal (menu_date, description, price, rest_id) 
VALUES ('2023-01-30', 'Hot-dog', 700, 1),
       ('2023-01-30', 'pizza', 1200, 1),
       ('2023-01-30', 'pasta', 1000, 1),
       ('2023-01-30', 'rolls', 2000, 2),
       ('2023-01-30', 'burger', 1200, 2),
       ('2023-01-30', 'buritto', 1200, 2),
       ('2023-01-31', 'bulmeni', 700, 1),
       ('2023-01-31', 'ragu', 1200, 1),
       ('2023-01-31', 'cotleties', 1000, 1),
       ('2023-01-31', 'midiies', 2200, 2),
       ('2023-01-31', 'shrimps', 1700, 2),
       ('2023-01-31', 'calmar', 1200, 2);

INSERT INTO vote (vote_datetime, rest_id, user_id)
VALUES ('2023-04-30', 1,  1),
       ('2023-04-30', 2,  2),
       ('2023-04-30', 2,  3);

