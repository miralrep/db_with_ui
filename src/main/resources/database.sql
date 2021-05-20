

--subscriber
--id|surname|name|patronymic|address|privileges
CREATE TABLE IF NOT EXISTS subscriber
(
    id              SERIAL PRIMARY KEY NOT NULL,
    surname         VARCHAR(100) NOT NULL,
    name            VARCHAR(100) NOT NULL,
    patronymic      VARCHAR(100) NOT NULL,
    address         VARCHAR(200) NOT NULL
);

--subscriber
--id|name|discount
CREATE TABLE IF NOT EXISTS privilege
(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(200) NOT NULL UNIQUE,
    discount DECIMAL(5,2)
);

CREATE TABLE IF NOT EXISTS subscriber_privilege
(
    subscriber_id   INT NOT NULL,
    privilege_id      INT NOT NULL,

    FOREIGN KEY(subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY(privilege_id) REFERENCES privilege(id),

    PRIMARY KEY (subscriber_id, privilege_id)

);
-------------------------------------------------------------
--categoryId
--id|name|subscription_fee
CREATE TABLE IF NOT EXISTS category
(
    id              SERIAL PRIMARY KEY NOT NULL,
    name            VARCHAR(100) NOT NULL UNIQUE,
    subscription_fee DECIMAL(7,2) NOT NULL
);
-------------------------------------------------------------
--phone_number
--number|subscriber_id|category_id
CREATE TABLE IF NOT EXISTS phone_number
(
    number          VARCHAR(50) NOT NULL PRIMARY KEY ,
    subscriber_id   INT NOT NULL,
    category_id     INT NOT NULL,

    FOREIGN KEY(subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);
-------------------------------------------------------------


CREATE TABLE IF NOT EXISTS call_type
(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS category_call_type_fee
(
    category_id INT NOT NULL,
    call_type_id INT NOT NULL,
    fee DECIMAL(5,2),

    FOREIGN KEY(category_id) REFERENCES category(id),
    FOREIGN KEY(call_type_id) REFERENCES call_type(id),

    PRIMARY KEY (category_id, call_type_id)

);
--conversation
--calling_phone|taking_phone|type|duration|date
CREATE TABLE IF NOT EXISTS conversation
(
    id              SERIAL PRIMARY KEY NOT NULL,
    calling_phone   VARCHAR(50) NOT NULL,
    taking_phone    VARCHAR(50) NOT NULL,
    call_type_id            INT NOT NULL,
    duration        TIMESTAMP NOT NULL,
    date            INTERVAL NOT NULL,

    FOREIGN KEY(call_type_id) references call_type(id),
    FOREIGN KEY(calling_phone) references phone_number(number),
    FOREIGN KEY(taking_phone) references phone_number(number)
);
-------------------------------------------------------------
