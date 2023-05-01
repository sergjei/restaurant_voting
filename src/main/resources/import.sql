INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

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
VALUES (CURRENT_DATE()-1, 'Hot-dog', 700, 1),
       (CURRENT_DATE()-1, 'pizza', 1200, 1),
       (CURRENT_DATE()-1, 'pasta', 1000, 1),
       (CURRENT_DATE()-1, 'rolls', 2000, 2),
       (CURRENT_DATE()-1, 'burger', 1200, 2),
       (CURRENT_DATE()-1, 'buritto', 1200, 2),
       (CURRENT_DATE(), 'bulmeni', 700, 1),
       (CURRENT_DATE(), 'ragu', 1200, 1),
       (CURRENT_DATE(), 'cotleties', 1000, 1),
       (CURRENT_DATE(), 'midiies', 2200, 2),
       (CURRENT_DATE(), 'shrimps', 1700, 2),
       (CURRENT_DATE(), 'calmar', 1200, 2);

INSERT INTO vote (vote_datetime, rest_id, user_id)
VALUES (CURRENT_DATE()-1, 1,  1),
       (CURRENT_DATE()-1, 2,  2),
       (CURRENT_DATE()-1, 2,  3);

