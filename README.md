## ORY Hydra spring boot example application

### 1. Run example hydra service

```shell script
docker-compose -f hydra-service/quickstart.yml -f hydra-service/quickstart-postgres.yml up -d
```

### 2. Create example oidc client

```shell script
curl --location --request POST 'http://127.0.0.1:4445/clients' \
--header 'Content-Type: application/json' \
--data-raw '{
"client_id": "my-client-id",
"client_name": "example-hydra-client",
"client_secret": "my-client-secret",
"client_uri": "https://example.io",
"grant_types": ["authorization_code"],
"redirect_uris": ["http://127.0.0.1:8080/callback"],
"response_types": ["code","id_token"],
"scope": "openid profile"
}'
```

### 3. Start example client

```shell script
gradlew :hydra-client:bootJar && java -jar hydra-client/build/libs/hydra-client-0.0.1-SNAPSHOT.jar
```

### 4. Start consent and login provider

```shell script
gradlew :hydra-consent:bootJar && java -jar hydra-consent/build/libs/hydra-consent-0.0.1-SNAPSHOT.jar
```

### 5. Visit endpoint
http://127.0.0.1:8080/oidc-principal
