# Envios Service — Diagnóstico y Mejora Guiada por Métricas

**Autor:** Abel Stiven Ayala Llanes
**Curso:** Patrones de Diseño de Software — UFPS

> **Antes de entregar:** reemplaza todo lo marcado como `[REEMPLAZAR ...]` con los valores reales de tu propio dashboard de SonarQube y de tus reportes de JaCoCo. Este README no trae números inventados a propósito: la actividad evalúa que las métricas documentadas coincidan con tu análisis real.

## Descripción

Proyecto Maven (`envios-service`) que calcula el costo de un envío según el método de transporte, el peso, la distancia, la fragilidad del paquete y el tipo de cliente. El trabajo cierra el ciclo completo de inspección de calidad en dos partes sobre el mismo proyecto:

- **Parte 1 — Diagnóstico:** configurar SonarQube y JaCoCo, analizar `ServicioCalculoEnvio` (que concentra toda la lógica en un solo método con una cadena larga de condicionales y una suite de solo dos pruebas) e interpretar el reporte real.
- **Parte 2 — Mejora:** elegir y aplicar el patrón de diseño que reduce genuinamente la complejidad, ampliar las pruebas sobre las ramas no cubiertas, configurar un Quality Gate y comparar las métricas antes y después.

## Estructura del proyecto

```
src/main/java/com/universidad/enviosservice/
├── service/
│   └── ServicioCalculoEnvio.java        orquestador (Parte 2); en la Parte 1 contenía toda la lógica
└── estrategia/                          patrón Strategy (Parte 2)
    ├── EstrategiaEnvio.java             interfaz común
    ├── EnvioEstandarStrategy.java
    ├── EnvioExpressStrategy.java
    ├── EnvioOvernightStrategy.java
    └── EnvioInternacionalStrategy.java

src/test/java/com/universidad/enviosservice/
├── service/ServicioCalculoEnvioTest.java
└── estrategia/                          una clase de pruebas por estrategia

pom.xml                       JaCoCo, plugins y propiedades de SonarQube
sonar-project.properties      configuración documentada del análisis
```

## Cómo ejecutar

Requisitos: JDK 21, Maven 3.9+ y un servidor SonarQube (Docker local) o una cuenta de SonarCloud.

```bash
mvn clean verify                                   # compila, prueba y genera el reporte de JaCoCo
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN   # además envía el análisis a SonarQube
```

El reporte de JaCoCo queda en `target/site/jacoco/index.html`. El token **no** se guarda en el repositorio: se pasa por línea de comandos.

## Herramientas utilizadas

- Java 21, Apache Maven, JUnit 5
- SonarQube Community (Docker) o SonarCloud, JaCoCo
- Git, GitHub

---

# Parte 1 — Diagnóstico con métricas reales

## Entorno de análisis (Decisión de diseño 1)

[REEMPLAZAR: conserva solo la opción que usaste y bórrala la otra]

**Opción A — SonarQube local con Docker.** Lo elegí porque no depende de la conectividad ni de los límites de un plan gratuito, y me da control total de la configuración (perfiles, gates y tokens), que es justo lo que se modifica en la Parte 2. El costo es que consume recursos de mi máquina y debe estar corriendo durante todo el laboratorio.

**Opción B — SonarCloud.** Lo elegí porque no requiere instalar nada y el dashboard queda accesible desde cualquier equipo con el mismo enlace, lo que facilita compartir la evidencia. El costo es que necesita cuenta y conexión a internet, y que un proyecto público es visible para cualquiera.

## Exclusiones del análisis (Decisión de diseño 2)

No excluí ninguna clase (no uso `sonar.exclusions`). El proyecto no tiene una clase de arranque (`Main`) ni código generado cuya falta de pruebas distorsione el porcentaje de cobertura: todo el código fuente es lógica de negocio, así que excluir algo ocultaría información real sobre la calidad. Si el proyecto llegara a incluir una clase de arranque sin lógica propia, esa sería la primera candidata a excluirse.

## Estado inicial del análisis

| Métrica | Valor real observado |
|---------|----------------------|
| Complejidad ciclomática de `calcularCosto()` | [REEMPLAZAR] |
| Cobertura de líneas de `ServicioCalculoEnvio` | [REEMPLAZAR]% |
| Cobertura de ramas de `ServicioCalculoEnvio` | [REEMPLAZAR]% |
| Code Smells reportados | [REEMPLAZAR] |

## Interpretación y señal principal (Decisión de diseño 3)

`ServicioCalculoEnvio.calcularCosto()` concentra el problema: es el único método de la clase y reúne en un solo cuerpo la selección del método de envío, los tramos de peso, los recargos por distancia y los ajustes por fragilidad y tipo de cliente. Cada condicional es un camino independiente que una suite completa debería ejercitar.

La suite inicial solo tiene dos pruebas (`calculaEnvioEstandarPesoBajo` y `aplicaDescuentoClientePremium`), y ambas recorren el mismo camino: `ESTANDAR`, peso ≤ 5 kg, distancia ≤ 500 km, sin fragilidad. Por eso el reporte HTML de JaCoCo marca como no cubiertas las ramas de `EXPRESS`, `OVERNIGHT` e `INTERNACIONAL`, los tramos de peso intermedio y alto, los recargos por distancia, la excepción de `OVERNIGHT` para más de 300 km, la excepción de método no soportado, la fragilidad y el descuento `CORPORATIVO`. [REEMPLAZAR: confirma esta lista contra tu captura de JaCoCo y ajusta lo que difiera]

**Señal principal elegida: complejidad ciclomática.** Como la clase tiene un solo método, las tres señales de SonarQube (complejidad ciclomática, complejidad cognitiva y cobertura) apuntan necesariamente al mismo lugar. Elegí la complejidad ciclomática porque cuenta los caminos independientes y, por lo tanto, el número mínimo de pruebas necesarias: conecta directamente el diagnóstico (el método es demasiado complejo) con la acción de la Parte 2 (cuántos caminos hay que probar y qué patrón los separa). La cobertura baja es consecuencia de esa complejidad, no su causa, y la complejidad cognitiva (que penaliza el anidamiento) habría llevado a la misma conclusión.

## Capturas del análisis inicial

| Evidencia | Captura |
|---|---|
| Dashboard de SonarQube tras el primer análisis | ![Dashboard inicial](capturas/sonar-dashboard-inicial.png) |
| Reporte HTML de JaCoCo con las ramas sin cubrir | ![JaCoCo inicial](capturas/jacoco-inicial.png) |
| Complejidad por método en la pestaña Code (opcional) | ![Complejidad inicial](capturas/sonar-complejidad-inicial.png) |

---

#
## Conclusiones


