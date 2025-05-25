# ✈️ Agencia de Viajes – Sistema de Reservas

Este es un proyecto de backend desarrollado en **Spring Boot**, que permite gestionar una **agencia de viajes** con funcionalidades **CRUD completas** para hoteles, vuelos y reservas.

Incluye una base de datos inicial con datos precargados para facilitar las pruebas desde herramientas como **Postman**.

---

## 🗂 Estructura del proyecto

```
📁 reservas
├── creacion_db.sql               --> Script SQL para crear la base de datos
├── src/main/java                 --> Código fuente
│   └── com/agenciaviajes/reservas
│       ├── controller            --> Controladores REST
│       ├── init                 --> Inicialización de datos
│       ├── model                --> Entidades JPA
│       ├── repository           --> Interfaces de acceso a datos
│       ├── service              --> Lógica de negocio
│       └── ReservasApplication  --> Clase principal
└── src/main/resources
    └── application.properties   --> Configuración de la aplicación
```

---

## ⚙️ Pasos para ejecutar el proyecto

1. 📂 **Crear la base de datos**
   Ejecuta el script [`creacion_db.sql`](./creacion_db.sql) en tu gestor de bases de datos (MySQL, MariaDB, etc.).  
   > Este script crea una base de datos llamada `agencia`.

2. 🔧 **Configurar credenciales**
   Abre el proyecto en **IntelliJ IDEA** y modifica el archivo `application.properties` con tus credenciales de base de datos:

   ```properties
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

3. 🚀 **Ejecutar la aplicación**
   Lanza la clase principal `ReservasApplication.java` y el backend quedará disponible en:

   ```
   http://localhost:8080/
   ```

4. 🧪 **Probar con Postman**
   Puedes realizar peticiones a las rutas disponibles.  
   👉 Los datos se insertan automáticamente al arrancar la aplicación.

---

## 📌 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Postman
- Maven

---

## 🧭 Endpoints disponibles

### 🏨 Hoteles

| Método | Endpoint        | Descripción                   |
|--------|------------------|-------------------------------|
| GET    | `/hoteles`       | Listar todos los hoteles     |
| POST   | `/hoteles`       | Crear un nuevo hotel         |
| PUT    | `/hoteles/{id}`  | Actualizar un hotel por ID   |
| DELETE | `/hoteles/{id}`  | Eliminar un hotel por ID     |

---

### ✈️ Vuelos

| Método | Endpoint        | Descripción                  |
|--------|------------------|------------------------------|
| GET    | `/vuelos`        | Listar todos los vuelos      |
| POST   | `/vuelos`        | Crear un nuevo vuelo         |
| PUT    | `/vuelos/{id}`   | Actualizar un vuelo por ID   |
| DELETE | `/vuelos/{id}`   | Eliminar un vuelo por ID     |

---

### 📄 Reservas

| Método | Endpoint          | Descripción                        |
|--------|--------------------|------------------------------------|
| GET    | `/reservas`        | Listar todas las reservas          |
| POST   | `/reservas`        | Crear una nueva reserva            |
| PUT    | `/reservas/{id}`   | Actualizar una reserva por ID      |
| DELETE | `/reservas/{id}`   | Eliminar una reserva por ID        |

---

### 📝 Crear una reserva

Ejemplo de JSON a enviar en Postman para crear una reserva:

```json
{
  "dni": "12345678A",
  "usuario": "Laura Pérez",
  "vueloId": 1,
  "hotelId": 2
}
```

⚠️ El hotel debe estar disponible (`true`) y el vuelo debe tener plazas disponibles (`> 0`).  
En caso contrario, se devolverá un error con el motivo del fallo.

---

## 🧩 Lógica interna

- Una reserva se vincula con **un hotel** y **un vuelo**.
- Al crear una reserva:
  - Se reduce el número de plazas disponibles del vuelo.
  - Se marca el hotel como **no disponible**.
- Si el hotel no está disponible o el vuelo no tiene plazas, se lanza una excepción controlada (`400 Bad Request`) con un mensaje descriptivo.

---

## 🧠 Autor

Desarrollado por **Oscar Fernández** – Proyecto educativo para 1ºDAM 👨‍💻  
🔗 [GitHub](https://github.com/oscarfhdev)

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT – ver archivo [`LICENSE`](./LICENSE) para más detalles.
