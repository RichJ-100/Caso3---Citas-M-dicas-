# Caso 3 - Citas medicas

Sistema para una clinica que permite consultar disponibilidad, reservar y cancelar citas, enviar recordatorios con reintentos y registrar auditoria. Es un monolito modular Spring Boot con PostgreSQL; no usa RabbitMQ.

## Arquitectura

- `agenda`: profesionales y franjas reservables.
- `paciente`: datos minimos de contacto.
- `cita`: reservas, permisos por rol y cancelacion idempotente.
- `recordatorio`: tabla de estados, tarea programada y cinco intentos maximos.
- `auditoria`: registro de acciones, incluidos intentos rechazados.
- `integracion`: proveedor de correo simulado.
- `seguridad`: actores simulados mediante headers.
- `web`: salud y pagina inicial.

La reserva bloquea la fila del horario con `SELECT ... FOR UPDATE` y la tabla `slot` tiene una restriccion unica por profesional e inicio. Los roles son `PACIENTE`, `RECEPCION`, `MEDICO` y `AUDITOR`, usando `X-Rol` y `X-Usuario-Id`.

## Ejecutar

Requisitos: Docker y Docker Compose.

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f backend
```

Abrir `http://localhost:8080/`, la documentacion en `/swagger-ui/index.html` y la salud en `/api/health`. Para apagar:

```bash
docker compose down
```

## API principal

```bash
# Profesionales
curl http://localhost:8080/api/profesionales

# Disponibilidad
curl "http://localhost:8080/api/disponibilidad?profesionalId=1&fecha=2026-10-03"

# Reservar como paciente 1
curl -X POST http://localhost:8080/api/citas \
  -H 'Content-Type: application/json' \
  -H 'X-Rol: PACIENTE' -H 'X-Usuario-Id: 1' \
  -d '{"pacienteId":1,"slotId":1,"tipoConsulta":"Control general"}'

# Listar como recepcionista
curl http://localhost:8080/api/citas -H 'X-Rol: RECEPCION'

# Cancelar
curl -X POST 'http://localhost:8080/api/citas/1/cancelar?motivo=No%20puede%20asistir' \
  -H 'X-Rol: RECEPCION'

# Procesar recordatorios manualmente
curl -X POST http://localhost:8080/api/recordatorios/procesar -H 'X-Rol: RECEPCION'

# Consultar auditoria
curl 'http://localhost:8080/api/auditoria?entidad=CITA&entidadId=1' -H 'X-Rol: AUDITOR'
```

## Datos de demostracion

Al iniciar se crean tres pacientes: Ana (`ana@correo.com`), Luis (`reintento@correo.com`) y Eva (`falla@correo.com`), además de dos profesionales y franjas para los siguientes días. `CorreoSimulado` envía normalmente, falla dos veces para direcciones que empiezan por `reintento` y siempre falla para las que empiezan por `falla`.

## Alcance

Incluido: disponibilidad, reserva sin duplicados, cancelacion, control por roles, recordatorios con reintentos y auditoria. No incluido en esta version: autenticacion JWT real, frontend, SMS, reprogramacion, lista de espera e historia clinica. En producción los headers simulados deben sustituirse por Spring Security con JWT.
