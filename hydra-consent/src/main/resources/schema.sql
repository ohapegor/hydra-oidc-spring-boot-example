DROP TABLE IF EXISTS ROLES;
DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS
(
    ID        VARCHAR(255) not null primary key,
    ADDRESS   VARCHAR(255),
    EMAIL     VARCHAR(255)
        constraint UNIQUE_USER_EMAIL unique,
    ENABLED   BOOLEAN      not null,
    FIRSTNAME VARCHAR(255),
    LASTNAME  VARCHAR(255),
    PASSWORD  VARCHAR(255)
);
CREATE TABLE ROLES
(
    USER_ID     VARCHAR(255) not null,
    ROLES VARCHAR(255),
    constraint FK_USERS_ID
        foreign key (USER_ID) references USERS (ID)
);

