# Document S3

## Environment

### Application Metadata

| Environment                  | Описание          | Store     | Значение DEV ~ PROD                | Тип данных |
|------------------------------|-------------------|-----------|------------------------------------|------------|
| APPLICATION_METADATA_VERSION | Версия приложения | ConfigMap | `1.0.0`' ~ `BUILD_CURRENT_VERSION` | string     |
| APPLICATION_TARGET_PORT      | Порт запуска      | ConfigMap | `9005`' ~ `...`                    | integer    |

### IdP

| Environment          | Описание             | Store     | Значение DEV ~ PROD                             | Тип данных |
|----------------------|----------------------|-----------|-------------------------------------------------|------------|
| PASSPORT_SERVICE_URL | URL службы паспортов | ConfigMap | `localhost:8080` / `https://api.idp.sorface.ru` | string     |

### Yandex Document

| Environment       | Описание                               | Store | Тип данных |
|-------------------|----------------------------------------|-------|------------|
| BUCKET_ACCESS_KEY | Ключ доступа к Yandex Bucket           | Vault | string     |
| BUCKET_SECRET_KEY | Приватный ключ доступа к Yandex Bucket | Vault | string     |

# Запуск приложения

```
mvn spring-boot:run
```

После запуска перейдите на http://localhost:8080/swagger-ui/index.html
