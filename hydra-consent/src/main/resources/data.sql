INSERT INTO USERS (id, email, password, firstname, lastname, address, enabled)
VALUES ('foo_id', 'foo@bar.com', '$2a$10$6a3gw.01WeLXEwyEdU3mfOmlhRmmEUEGLLYwPBR5Ici4B3mhtZfny', 'foo', 'bar', 'Russia, Moscow', true);
INSERT INTO ROLES(user_id, roles)
VALUES ('foo_id', 'USER');