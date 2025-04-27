# 🚀 Tenpo Calculator API

Proyecto backend para realizar cálculos de suma + porcentaje dinámico consumido desde un servicio externo.  
Además registra el historial de llamadas exitosas y con error en una base de datos PostgreSQL.

Tecnologías principales:
- Java 21
- Spring WebFlux
- PostgreSQL
- Redis
- Docker & Docker Compose
- DockerHub (para publicación de imagen)

---

## 🛠️ Levantar el proyecto localmente

### 1. Clonar el repositorio

```bash
git clone https://github.com/JuliansM/calculator.git
cd calculator
```

### 2. Construir el proyecto

```
./gradlew build
```
Se generará el .jar en la carpeta build/libs/.

### 3. Configuración de la base de datos y caché
El proyecto utiliza PostgreSQL y Redis levantados vía docker-compose.

Archivo deploy/docker-compose.yml incluído:

```
services:

  postgres:
    image: postgres:15
    container_name: postgres_history
    restart: always
    environment:
      POSTGRES_USER: history_user
      POSTGRES_PASSWORD: history_pass
      POSTGRES_DB: calculator_db
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql

  redis:
    image: redis:7
    container_name: redis_cache
    restart: always
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  app:
    image: houssitcol2019/calculator_app:latest
    container_name: spring_calculator_app
    restart: always
    ports:
      - "8080:8080"
    environment:
      POSTGRES_HOST: postgres
      POSTGRES_PORT: 5432
      POSTGRES_DB: calculator_db
      POSTGRES_USER: history_user
      POSTGRES_PASSWORD: history_pass
      REDIS_HOST: redis
      REDIS_PORT: 6379
      LAMBDA_ENDPOINT_URL: https://bxnbq4uy6jhisd5mlnk7273zoi0jyfyr.lambda-url.us-east-1.on.aws
    depends_on:
      - postgres
      - redis

volumes:
  postgres_data:
  redis_data:
```
#### Importante:

- Se incluye un script init.sql que crea la tabla call_histories automáticamente al levantar PostgreSQL.
- PostgreSQL escucha en puerto 5433 para no colisionar si ya se tiene otro PostgreSQL local en 5432.

### 4. Levantar contenedores
```
docker-compose up -d
```
Este comando iniciará los contenedores postgres, redis y además levantará el contenedor del proyecto a partir de la imagen publicada en dockerhub.

Imagen de docker publicada:
```
houssitcol2019/calculator_app:latest
```

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

## Colección de postman para realizar pruebas de los servicios
Archivo deploy/Tenpo_Calculator_Api.postman_collection.json incluído:
```
{
	"info": {
		"_postman_id": "33fb5f07-a76b-46a0-897e-2d40b88d8fef",
		"name": "Tenpo_Calculator_Api",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "8285597"
	},
	"item": [
		{
			"name": "calculate_success",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"num1\": 656,\r\n    \"num2\": 55\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/calculate",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"calculate"
					]
				}
			},
			"response": []
		},
		{
			"name": "get-call-history_success",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/v1/get-call-history",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"get-call-history"
					]
				}
			},
			"response": []
		},
		{
			"name": "get_percentage_external_api_success",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "https://bxnbq4uy6jhisd5mlnk7273zoi0jyfyr.lambda-url.us-east-1.on.aws/",
					"protocol": "https",
					"host": [
						"bxnbq4uy6jhisd5mlnk7273zoi0jyfyr",
						"lambda-url",
						"us-east-1",
						"on",
						"aws"
					],
					"path": [
						""
					]
				}
			},
			"response": []
		},
		{
			"name": "calculate_error_500",
			"request": {
				"auth": {
					"type": "noauth"
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"num1\": 656,\r\n    \"num2\": \"dd\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/calculate",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"calculate"
					]
				}
			},
			"response": []
		}
	]
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

## 📎 Consideraciones extra
- WebClient implementado con WebFlux reactive, manejo de errores y fallback adecuado.
- Redis usado para cachear porcentaje consultado durante un tiempo configurable.
- PostgreSQL crea call_histories automáticamente al inicializarse gracias al volumen init.sql.
Archivo deploy/init.sql incluído:
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
- Código limpio siguiendo buenas prácticas de arquitectura reactiva.

## ✨ Autor
Desarrollado por: @Juliansm