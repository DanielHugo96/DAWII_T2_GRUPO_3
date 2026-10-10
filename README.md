# DAWII T2 — PAYGO Perú

Implementación de los tres requerimientos: Eureka Server, servicio REST de reclamos y servicio REST de riesgos que resuelve reclamos mediante OpenFeign.

## Arquitectura

    Postman / atención al cliente
              |
              +-- POST/GET/PUT /api/reclamos --> ms-reclamos :8084 --> H2
              |                                      ^
              +-- PUT /api/riesgo/reclamos/{id}/resolver
                                                     |
                                    OpenFeign + Eureka
                                                     |
                                                     +-- ms-reclamos

Eureka Server en 8761 registra ms-reclamos y ms-riesgo.

## Requisitos y ejecución

- Java 17 y Maven 3.9 o superior.
- Iniciar cada módulo desde esta carpeta en una terminal distinta y en este orden. Esperar a que cada registro aparezca en Eureka antes de iniciar el siguiente:

    mvn -pl eureka-server spring-boot:run
    mvn -pl ms-reclamos spring-boot:run
    mvn -pl ms-riesgo spring-boot:run

Esperar a que los tres procesos arranquen. El panel de Eureka queda en http://localhost:8761; deben aparecer MS-RECLAMOS y MS-RIESGO. Reclamos escucha en 8084 y Riesgo en 8085.

Ejecutar pruebas y compilación:

    mvn clean verify

## Contrato REST

### Reclamos — http://localhost:8084

POST /api/reclamos registra un reclamo. El identificador se genera automáticamente y la fecha queda en la hora del servidor.

    {
      "nomcliente": "Ana Pérez",
      "ultimos4tarjeta": "1234",
      "motivo": "Consumo desconocido"
    }

Devuelve 201 Created. Por defecto analista es No asignado, estado es 1 (pendiente) y observacion es por evaluar.

GET /api/reclamos/{numreclamo} consulta por número. Devuelve 404 si no existe.

PUT /api/reclamos/{numreclamo} actualiza la resolución; solo acepta un reclamo pendiente y un estado final 2 o 3:

    {
      "analista": "María Riesgos",
      "estado": 2,
      "observacion": "Consumo no reconocido; procede el reclamo"
    }

### Riesgos — http://localhost:8085

PUT /api/riesgo/reclamos/{numreclamo}/resolver recibe el mismo cuerpo de resolución. Primero consulta el reclamo con OpenFeign (GET al servicio ms-reclamos descubierto por Eureka), verifica que exista y siga pendiente, y después actualiza el analista, estado y observacion por OpenFeign. Riesgo agrega automáticamente el prefijo Procedente o Improcedente a la observación para mantenerla coherente con el estado elegido.

- Estado 2: procedente.
- Estado 3: improcedente.
- 200: respuesta con mensaje de éxito y reclamo actualizado.
- 400: datos o estado inválidos.
- 404: reclamo inexistente.
- 409: reclamo ya resuelto.
- 503: ms-reclamos no localizable/disponible en Eureka.
- 502: error no esperado del servicio remoto.

Los servicios publican GET /actuator/health. El servicio de reclamos habilita la consola H2 local en http://localhost:8084/h2-console (JDBC URL jdbc:h2:mem:reclamosdb, usuario sa, contraseña vacía).

## Decisiones explícitas

- Las capturas no fijan nombres de rutas ni motor de base de datos; se eligieron rutas REST consistentes y H2 en memoria para que la evaluación sea reproducible sin configurar otro servidor.
- La captura define los estados finales, pero no una regla automática para decidirlos. Por ello el analista envía estado (2 o 3) y observacion; Riesgo valida y coordina la consulta/actualización, sin inventar criterios de negocio.
- La base H2 es en memoria: los registros de prueba se reinician cuando se reinicia ms-reclamos.
