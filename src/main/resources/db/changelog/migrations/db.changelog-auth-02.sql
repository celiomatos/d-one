--liquibase formatted sql

--changeset celio:10

INSERT INTO oauth.oauth_client(client_id, resource_ids, client_secret, client_scope,
    authorized_grant_types, access_token_validity_seconds, refresh_token_validity_seconds, secret_required)
 VALUES ('admin', 'd_gateway',
    /*admin1234*/'$2a$10$zelII7t6Duv2H.JEF2x9RuNseSLRRf90rC6PZj0ILhHtqSvJnUWs6',
    'read,write', 'password,authorization_code,refresh_token,implicit', 2592000, 2593800, true);

--rollback delete from oauth.oauth_client where client_id = 'admin';

--changeset celio:11

INSERT INTO oauth.oauth_module(mod_id, mod_name) VALUES (1, 'USUARIO');

--rollback delete from oauth.oauth_module where mod_id = 1;

--changeset celio:12

INSERT INTO oauth.oauth_permission(perm_id, perm_name) VALUES (1, 'CREATE');

--rollback delete from oauth.oauth_permission where perm_id = 1;

--changeset celio:13

INSERT INTO oauth.oauth_authority(authority_id, mod_id, perm_id) VALUES (1, 1, 1);

--rollback delete from oauth.oauth_authority where authority_id = 1;

--changeset celio:14

INSERT INTO oauth.oauth_user(user_id, user_name, user_password, user_full_name,
    account_non_expired, account_non_locked , credentials_non_expired, enabled, super_user)
  VALUES (1, 'user', /*1234*/'$2a$10$UtSgFInoXOGAzuEA93Hg7.Z6lPcWUIgaPttERZvLnx3KuKsmAVo8a',
  'user principal', true, true, true, true, false);

--rollback delete from oauth.oauth_user where user_id = 1;

--changeset celio:15

INSERT INTO oauth.oauth_user_authority(user_id, authority_id) VALUES (1, 1);

--rollback delete from oauth.oauth_user_authority where user_id = 1;
