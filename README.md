# 🚀 Tenpo Calculator API

Proyecto backend para realizar cálculos de suma + porcentaje dinámico consumido desde un servicio externo.  
Además registra el historial de llamadas exitosas y con error en una base de datos PostgreSQL.

Tecnologías principales:
- Java 21
- Spring WebFlux
- Gradle v8.13
- Postgres v15
- Redis v7
- Docker & Docker Compose
- DockerHub (para publicación de imagen)

---

## Ejecutar Despliegue Localmente

#### Importante:
- El archivo docker-compose.yml se encuentra en la ruta deploy/docker-compose.yml de este repositorio.
  Es importante que comente dentro del archivo docker-compose.yml la sección de definición del contenedor de la app.
  Esto, debido a que se pretende levantar el proyecto API localmente a modo de desarrollo.
- Se incluye un script init.sql que crea la tabla call_histories automáticamente al levantar PostgreSQL.
- PostgreSQL escucha en puerto 5433 para no colisionar si ya se tiene otro PostgreSQL local en 5432.

#### Comando para levantar los contenedores
Ubicado dentro del directorio deploy/ del proyecto, ejecutar el siguiente comando:
```
docker-compose up -d
```
Este comando iniciará los contenedores postgres, redis y además levantará el contenedor del proyecto a partir de la imagen publicada en dockerhub.

---
## 📈 Endpoints principales

| Método |           Ruta           | Descripción                        |
|:------:|:------------------------:|:----------------------------------:|
| GET    | /api/v1/get-call-history | Obtener historial de llamadas      |
| POST   |   /api/v1/calculate      | Realizar cálculo suma + porcentaje |

## 📈 Endpoints externos

| Método |                                Ruta                                 |                   Descripción                    |
|:------:|:-------------------------------------------------------------------:|:------------------------------------------------:|
| GET    |https://bxnbq4uy6jhisd5mlnk7273zoi0jyfyr.lambda-url.us-east-1.on.aws | Servicio externo que devuelve un porcentaje fijo |

Respuesta estándar:
```
{
  "percentage": 0.75
}
```

## 🛡️ Manejo de errores
La aplicación maneja correctamente errores técnicos y de negocio aplicando @ExceptionHandler
para interceptar las excepciones de la aplicación y redirigir el flujo hacia una respuesta
estandar con los estados HTTP correspondientes:
- BusinessException (errores controlados del servicio externo o validaciones)
- TechnicalException (errores internos de base de datos, red, etc.)

Respuesta estándar:
```
{
  "message": "Mensaje de error",
  "data": null
}
```

## 🧪 Pruebas Unitarias
- Tests de servicios principales usando Mockito y Spring Boot Test.
- Mock de WebClient, ReactiveRedisTemplate, CallHistoryRepository.
- Simulación de respuestas exitosas y fallidas.

Para correr tests:
```
./gradlew test
```

- Código limpio siguiendo buenas prácticas de arquitectura reactiva.

## 🧪 Pruebas de la aplicación y documentación

- ### Colección de postman:
El archivo de colección de postman se encuentra en la ruta deploy/Tenpo_Calculator_Api.postman_collection.json
de este repositorio.

- ### Documentación Swagger:
La documentación de Swagger será accesible mediante la URL: http://localhost:8080/webjars/swagger-ui/index.html 
una vez sea la aplicación levantada.

## 📎 Consideraciones extra
- WebClient implementado con WebFlux reactive, manejo de errores y fallback adecuado.
- Redis usado para cachear porcentaje consultado durante un tiempo configurable.
- PostgreSQL crea call_histories automáticamente al inicializarse gracias al volumen init.sql, este
  archivo se encuentra en la ruta deploy/init.sql de este repositorio.

Contenido del archivo:
```
CREATE TABLE IF NOT EXISTS call_histories (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    endpoint VARCHAR(255),
    parameters TEXT,
    response TEXT,
    error TEXT
);
```