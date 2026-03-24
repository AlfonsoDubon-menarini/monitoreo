Bitácora de Desarrollo — Monitor de Salud de Facturación Regional

Proyecto: Sistema de Monitoreo de Grado Empresarial
Alcance: GT · SV · CR · PA · DO
Arquitectura: Hexagonal (Puertos y Adaptadores)
Stack: Java 22 · Spring Boot 3.3 · Maven · H2


Índice

Contexto del Negocio
Arquitectura
Stack Tecnológico
Contrato de API
Historial de Decisiones
Registro de Cambios
Problemas Conocidos y Resoluciones
Próximos Pasos


1. Contexto del Negocio
   Objetivo del MVP
   Detectar en tiempo real si los nodos de facturación electrónica de 5 países están operativos.
   PaísEnte TributarioGT — GuatemalaSATSV — El SalvadorDGII / MHCR — Costa RicaHaciendaPA — PanamáDGIDO — República DominicanaDGII
   Métricas Clave por Nodo
   MétricaDescripciónente_tributario_activoConexión operativa con el ente tributariolatencia_msLatencia de respuesta en milisegundosdocumentos_pendientesDocumentos fiscales en cola pendientes de envío
   Motor de Reglas
   El sistema evalúa automáticamente cada reporte y asigna uno de tres estados:
   EstadoSignificadoOKTodo operativoWARNDegradación detectada — requiere atenciónCRITICALFallo crítico — intervención inmediata

Nota: Las reglas específicas de umbral (ej. latencia > X ms = WARN) se definirán al implementar la capa de dominio.


2. Arquitectura
   Arquitectura Hexagonal (Puertos y Adaptadores)
   El núcleo de la aplicación está aislado de herramientas externas. Ninguna dependencia externa penetra el dominio.
   com.monitoreo.facturacion/
   │
   ├── domain/                  ← Java puro. Sin dependencias de frameworks.
   │   ├── model/               Entidades: NodoFacturacion, EstadoSalud
   │   └── service/             Motor de reglas: EvaluadorSalud
   │
   ├── application/             ← Orquestación de casos de uso
   │   ├── port/in/             Puertos de entrada (interfaces)
   │   ├── port/out/            Puertos de salida (interfaces)
   │   └── usecase/             Implementaciones: ProcesarReporteSalud
   │
   └── infrastructure/          ← Spring Boot, H2, REST
   ├── adapter/in/rest/     Controladores REST
   ├── adapter/out/db/      Repositorios JPA / H2
   └── config/              Configuración de Spring
   Principio Guía

La capa domain no conoce a Spring. La capa application no conoce a H2. Solo infrastructure conoce las herramientas externas.


3. Stack Tecnológico
   ComponenteTecnologíaVersiónLenguajeJava22 (LTS)FrameworkSpring Boot3.3.xGestor de dependenciasMavenpom.xmlBase de datos (desarrollo)H2In-memoryDocumentación de APIOpenAPI / Swagger3.0.3Control de versionesGit + GitFlow—

4. Contrato de API
   Archivo: docs/openapi.yaml
   Versión del contrato: 1.0.0
   Endpoints
   MétodoRutaDescripciónPOST/api/v1/reportes-saludRecibir reporte de salud de un nodo regionalGET/api/v1/estado-regionalObtener el estado actual de toda la región
   Schemas Principales
   SchemaDescripciónReporteSaludPayload enviado por cada nodoSaludPaisEstado consolidado por paísRespuestaIngestaConfirmación con estado calculadoErrorEstructura estándar de error
   Convenciones del Contrato

Idioma: español en rutas, campos y descripciones
Formato de campos: snake_case
Países como enum explícito: [GT, SV, CR, PA, DO]
Todos los endpoints definen respuestas de error: 400, 422, 500


5. Historial de Decisiones
   ADR-001 — Arquitectura Hexagonal
   Fecha: Inicio del proyecto
   Decisión: Implementar Arquitectura Hexagonal (Puertos y Adaptadores).
   Razón: Aislar la lógica de negocio de frameworks y herramientas externas para garantizar mantenibilidad y resistencia al cambio tecnológico.
   Consecuencia: Mayor estructura inicial, pero el dominio es testeable de forma independiente.
   ADR-002 — Idioma del proyecto: Español
   Fecha: Fase de bootstrapping
   Decisión: Todos los nombres de clases, paquetes, rutas de API y documentación en español.
   Razón: El negocio es centroamericano, el equipo es hispanohablante, y la consistencia de idioma reduce la carga cognitiva.
   Consecuencia: Paquete raíz com.monitoreo.facturacion, rutas /reportes-salud, /estado-regional.
   ADR-003 — Contrato API primero (API-First)
   Fecha: Fase de bootstrapping
   Decisión: Definir openapi.yaml antes de implementar el backend.
   Razón: El contrato es la "promesa" que el backend debe cumplir. Permite desarrollo paralelo frontend/backend y documentación siempre actualizada.
   Consecuencia: El openapi.yaml en docs/ es la fuente de verdad de la API.
   ADR-004 — H2 como base de datos de desarrollo
   Fecha: Fase de bootstrapping
   Decisión: Usar H2 in-memory para desarrollo local.
   Razón: Sin dependencias externas para levantar el proyecto. Ideal para MVP.
   Consecuencia: Los datos no persisten entre reinicios. Para producción se deberá migrar a PostgreSQL u otro motor.

6. Registro de Cambios
   [Unreleased] — rama feature/setup-project-structure
   Agregado

Estructura de carpetas hexagonal (domain, application, infrastructure)
Definición del contrato OpenAPI docs/openapi.yaml
Clase de entrada MonitoreoApplication.java con @SpringBootApplication
Configuración de pom.xml con Spring Boot 3.3, H2 y Maven

Corregido

Paquete renombrado de com.monitoreo.billing a com.monitoreo.facturacion
Clase de entrada renombrada de MonitoringApplication a MonitoreoApplication
Eliminada carpeta residual billing del árbol de paquetes
Eliminada carpeta duplicada tests/ en la raíz del proyecto
Países corregidos en el contrato: removidos HN y NI, no son parte del MVP
Rutas de API traducidas al español: /health-reports → /reportes-salud
Campos del contrato traducidos al español con snake_case
Agregados códigos de error 400, 422, 500 a todos los endpoints
Agregado schema Error estandarizado
Agregado schema RespuestaIngesta con estado calculado en la respuesta 202


7. Problemas Conocidos y Resoluciones
   PKG-001 — Conflicto de paquete al crear la clase de entrada
   Síntoma: IntelliJ reportaba Package name 'com.monitoreo.facturacion' does not correspond to the file path 'com.monitoreo'
   Causa: El archivo MonitoreoApplication.java estaba físicamente en com/monitoreo/ pero declaraba package com.monitoreo.facturacion
   Resolución: Mover el archivo a src/main/java/com/monitoreo/facturacion/ para que la carpeta física coincida con la declaración del paquete.
   PKG-002 — Carpeta billing residual
   Síntoma: Subcarpeta billing aparecía como paquete dentro de com.monitoreo junto a las capas hexagonales.
   Causa: La clase de entrada fue creada originalmente con el paquete com.monitoreo.billing antes de la decisión de usar español.
   Resolución: Remove-Item -Recurse -Force src\main\java\com\monitoreo\billing
   PKG-003 — Carpeta tests/ duplicada en la raíz
   Síntoma: Dos carpetas de pruebas: src/test/ (Maven estándar) y tests/ (raíz del proyecto).
   Causa: Carpeta creada manualmente fuera de la estructura Maven.
   Resolución: Remove-Item -Recurse -Force tests
   ENV-001 — Comandos bash no funcionan en PowerShell
   Síntoma: rm -rf lanza error No se encuentra ningún parámetro que coincida con el nombre del parámetro 'rf'
   Causa: El entorno de desarrollo es Windows con PowerShell, no bash.
   Resolución: Usar Remove-Item -Recurse -Force o el alias rm -r -fo en PowerShell.

8. Próximos Pasos
   Sprint actual — Completar bootstrapping y dominio

Verificar compilación limpia: mvn clean compile
Implementar enum EstadoSalud (OK, WARN, CRITICAL) en domain/model
Implementar entidad NodoFacturacion en domain/model
Implementar EvaluadorSalud — motor de reglas en domain/service
Definir puerto de entrada ProcesarReporteSaludUseCase en application/port/in
Implementar caso de uso en application/usecase
Crear controlador REST en infrastructure/adapter/in/rest
Verificar que Swagger UI levanta correctamente en http://localhost:8080/swagger-ui.html

Backlog técnico

Migrar base de datos de H2 a PostgreSQL para ambiente de staging
Definir umbrales exactos del motor de reglas (latencia, documentos pendientes)
Implementar tests unitarios del dominio
Configurar pipeline CI en GitFlow (develop → main)
Evaluar estrategia de alertas (email, Slack, webhook)
