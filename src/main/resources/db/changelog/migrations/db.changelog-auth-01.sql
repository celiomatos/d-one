--liquibase formatted sql

--changeset celio:1
CREATE SCHEMA oauth;

--rollback drop schema oauth;

--changeset celio:2

CREATE TABLE oauth.oauth_access_token
(
  token_id character varying(256) PRIMARY KEY,
  access_token bytea NOT NULL,
  authentication_id character varying(256) NOT NULL,
  user_name character varying(256) NOT NULL UNIQUE,
  client_id character varying(256) NOT NULL,
  authentication bytea NOT NULL,
  refresh_token character varying(256) NOT NULL
);

--rollback drop table auth.oauth_access_token;

--changeset celio:3

CREATE TABLE oauth.oauth_client
(
  client_id character varying(256) PRIMARY KEY,
  resource_ids character varying(256) NOT NULL,
  client_secret character varying(256) NOT NULL,
  client_scope character varying(256) NOT NULL,
  authorized_grant_types character varying(256) NOT NULL,
  registered_redirect_uri character varying(256),
  client_authorities character varying(256),
  access_token_validity_seconds integer NOT NULL,
  refresh_token_validity_seconds integer NOT NULL,
  additional_information character varying(4096),
  auto_approve character varying(256),
  secret_required boolean NOT NULL DEFAULT TRUE
);

--rollback drop table oauth.oauth_client;

--changeset celio:4

CREATE TABLE oauth.oauth_refresh_token
(
  token_id character varying(256) PRIMARY KEY,
  refresh_token bytea NOT NULL,
  authentication bytea NOT NULL
);

--rollback drop table oauth.oauth_refresh_token;

--changeset celio:5

CREATE TABLE oauth.oauth_user
(
  user_id bigserial PRIMARY KEY,
  user_name character varying(256) NOT NULL UNIQUE,
  user_password character varying(256) NOT NULL,
  user_full_name character varying(256) NOT NULL,
  account_non_expired boolean NOT NULL default true,
  account_non_locked boolean NOT NULL default true,
  credentials_non_expired boolean NOT NULL default true,
  enabled boolean NOT NULL default true,
  super_user boolean NOT NULL default false
);

--rollback drop table oauth.oauth_user;

--changeset celio:6

CREATE TABLE oauth.oauth_module
(
  mod_id bigserial PRIMARY KEY,
  mod_name character varying(256) NOT NULL UNIQUE
);

--rollback drop table oauth.oauth_module;

--changeset celio:7

CREATE TABLE oauth.oauth_permission
(
  perm_id bigserial PRIMARY KEY,
  perm_name character varying(256) NOT NULL UNIQUE
);

--rollback drop table oauth.oauth_permission;

--changeset celio:8

CREATE TABLE oauth.oauth_authority
(
  authority_id bigserial PRIMARY KEY,
  mod_id bigint NOT NULL,
  perm_id bigint NOT NULL,
  UNIQUE(mod_id, perm_id),
  FOREIGN KEY (mod_id) REFERENCES oauth.oauth_module (mod_id),
  FOREIGN KEY (perm_id) REFERENCES oauth.oauth_permission (perm_id)
);

--rollback drop table oauth.oauth_authority;

--changeset celio:9

CREATE TABLE oauth.oauth_user_authority
(
  user_id bigint NOT NULL,
  authority_id bigint NOT NULL,
  UNIQUE(user_id, authority_id),
  FOREIGN KEY (authority_id) REFERENCES oauth.oauth_authority (authority_id),
  FOREIGN KEY (user_id) REFERENCES oauth.oauth_user (user_id)
);

--rollback drop table oauth.oauth_user_authority;
