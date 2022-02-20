INSERT INTO role(id,name) VALUES (1,'ROLE_USER');
INSERT INTO role(id,name) VALUES (2,'ROLE_ADMIN');
INSERT INTO role(id,name) VALUES (3,'ROLE_MANAGER');
INSERT INTO role(id,name) VALUES (4,'ROLE_GUEST');
INSERT INTO user(user_id,username,password,name,enabled) VALUES (1,'1@1','$2a$10$rXmA6ehB5NZ4bbVGM4ttDO8FaqMN7S.szlXI3bDAYWa8FoaMbVkTy','1',true);
INSERT INTO user_role(user_id,role_id) VALUES (1,4)