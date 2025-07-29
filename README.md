# Franchises API

API desarrollada con **Spring WebFlux** y **DynamoDB** siguiendo los principios de **arquitectura limpia** utilizando el Scaffold de Bancolombia. Permite gestionar franquicias, sucursales y productos, incluyendo operaciones CRUD y consultas especializadas como obtener el producto con mayor stock por sucursal.

---

## 🚀 Tecnologías

- Java 17
- Spring Boot 3 (WebFlux)
- DynamoDB (AWS o local con DynamoDB Local)
- Reactor (Mono / Flux)
- Arquitectura hexagonal (domain, application, infrastructure)

---

## Ejecución en local

### ✅ Requisitos previos

- Java 17
- Gradle (o usar wrapper `./gradlew`)
- DynamoDB local o AWS (ver abajo)
- Variables de entorno configuradas

### 📦 Instalación

```bash
git clone https://github.com/Jose-DeAvila/franchises-api
cd franchises-api
./gradlew clean build
```

---

## 🧪 Ejecución

### 🖥️ 1. Configurar variables de entorno

Debes definir las siguientes variables de entorno antes de iniciar la aplicación:

```bash
export AWS_ACCESS_KEY=tu-access-key
export AWS_SECRET_KEY=tu-secret-key
export AWS_REGION=us-east-1
export AWS_DYNAMO_ENDPOINT=endpoint-dynamo
```

> Puedes definirlas en tu `.bashrc`, `.zshrc`, o directamente en tu IDE si prefieres.

---

### ⚙️ 2. Ejecutar la app

```bash
./gradlew bootRun
```

O si ya compilaste y tienes el `.jar` generado:

```bash
java -jar build/libs/franchises-api.jar
```

---

## 🧪 DynamoDB Local (opcional)

Si no quieres usar AWS directamente, puedes usar DynamoDB local:

1. Descarga DynamoDB Local:
   https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html

2. Ejecuta el servidor:

```bash
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
```

3. Asegúrate de que tu variable `AWS_DYNAMO_ENDPOINT` apunte a `http://localhost:8000`.

---

## 📂 Estructura del proyecto

```
franchises-api/
├── domain/             # Modelos y puertos del dominio (puros)
├── application/        # Casos de uso / servicios de aplicación
├── infrastructure/     # Adapters, configuraciones, mappers, controladores
├── build.gradle        # Configuración de dependencias
└── README.md
```

---

## 🧪 Endpoints disponibles

- `POST /api/v1/franchise` - Crear franquicia
- `PUT /api/v1/franchise/{id}` - Renombrar franquicia
- `POST /api/v1/branch` - Crear sucursal
- `PUT /api/v1/branch/{id}` - Renombrar sucursal
- `POST /api/v1/product` - Crear producto
- `PUT /api/v1/product/{branchId}/{productId}` - Renombrar producto
- `DELETE /api/v1/product/{branchId}/{productId}` - Eliminar producto
- `GET /api/v1/product/max-stock/{franchiseId}` - Obtener producto con más stock por sucursal

---

## 📬 Contacto

Si tienes dudas o sugerencias, no dudes en abrir un issue o contactarme.