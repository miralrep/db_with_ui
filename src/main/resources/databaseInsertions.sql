--------------------------
INSERT INTO subscriber(surname, name, patronymic, address)
VALUES ('Иванов', 'Иван', 'Иванович', 'ул. Пушкина, д 54');

INSERT INTO subscriber(surname, name, patronymic, address)
VALUES ('Иванов1', 'Иван1', 'Иванович1', '1ул. Пушкина, д 54');

INSERT INTO subscriber(surname, name, patronymic, address)
VALUES ('Иванов2', 'Иван2', 'Иванович2', '2ул. Пушкина, д 54');

INSERT INTO subscriber(surname, name, patronymic, address)
VALUES ('Иванов3', 'Иван3', 'Иванович3', '3ул. Пушкина, д 54');

--------------------------
INSERT INTO privilege(name, discount)
VALUES ('', 0.00);

INSERT INTO privilege(name, discount)
VALUES ('Инвалидность', 5.00);

INSERT INTO privilege(name, discount)
VALUES ('Многодетность', 2.00);

INSERT INTO privilege(name, discount)
VALUES ('Ветеран войны', 7.00);

--------------------------
INSERT INTO subscriber_privilege
VALUES (1, 1);

INSERT INTO subscriber_privilege
VALUES (2, 2);

INSERT INTO subscriber_privilege
VALUES (2, 3);
