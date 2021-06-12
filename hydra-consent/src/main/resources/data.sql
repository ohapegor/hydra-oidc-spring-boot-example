INSERT INTO USERS (id, email, password, firstname, lastname, address, enabled)
VALUES ('1', 'foo@bar.com', '$2a$10$6a3gw.01WeLXEwyEdU3mfOmlhRmmEUEGLLYwPBR5Ici4B3mhtZfny', 'foo', 'bar', 'Russia, Moscow', true);
INSERT INTO AUTHORITIES(user_id, authorities)
VALUES ('1', 'USER');