# release-management-service

## prerequisites

This application requires following system tools to be installed.

- bash
- docker
- docker-compose
- node >= 16.x
- maven >= 3.8.x

## clone

After cloning the repository please install husky commit message linting via npm.

```bash
npm install
npx husky install
```

## build

To build the application and run unit tests the side running services like databases and caches should be started via docker-compose.

```bash
docker-compose up
```

Now we can run the build including unit tests via maven.

```bash
mvn clean install
```

## run

After building the application and having the side running services started with docker-compose the application can be started with the following commands.

```bash
mvn --file ./app-database/bom.xml spring-boot:run
mvn --file ./app/bom.xml spring-boot:run
```

## release

In order to release an application we use the [semantic-release](https://semantic-release.gitbook.io) tool via pipeline that can be triggered manually at default branch runs.

To give this tool the right permission a `GITLAB_TOKEN` environment variable with a GitLab deploy token value needs to be present.
This can be added with following steps:

- Settings `>` Access Tokens
  - Token name: `release`
  - Select scopes: `api, read_api, read_repository, write_repository`
  - Create project access token
  - Your new project access token `>` Copy
- Settings `>` CI/CD `>` Variables `>` Expand
  - Add variable
  - Key: `GITLAB_TOKEN`
  - Value: `Paste the copied token value`
  - Flags: `Protect variable, Mask variable`
  - Add variable

## keycloak

The `./docker-compose.yml` is able to spawn a preconfigured KeyCloak that is defined in `./keycloak.json` and has the following entities configured:

### roles

| realm   | name      |
|---------|-----------|
| default | admin     |
| default | developer |
| default | viewer    |

### clients

| realm   | name         | type         | client.id    | client.secret      | roles |
|---------|--------------|--------------|--------------|--------------------|-------|
| default | public       | public       | public       | -                  | -     |
| default | confidential | confidential | confidential | Y29uZmlkZW50aWFsCg | *     |

## users

| realm   | username  | password  | roles     |
|---------|-----------|-----------|-----------|
| default | admin     | admin     | admin     |
| default | developer | developer | developer |
| default | viewer    | viewer    | viewer    |
