# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25. Se está
rehaciendo desde un esqueleto vacío: por ahora no hay ningún contenido jugable, solo el stack que
arranca y buildea el jar.

## Correr en dev

Con Docker corriendo, desde la raíz del repo:

```
mvn -B -pl server -am install -DskipTests
mvn -pl server spring-boot:run
```

El `install` deja `holy-wars-domain` en el repositorio local, así el segundo comando puede correr el
módulo `server` solo. `spring-boot:run` levanta un PostgreSQL 18.6 (usa el `compose.yaml` de la raíz).
Abrí `http://localhost:8080` en el navegador. Si ya tenías el volumen `holywars-pgdata` de antes del
reset, tiene aplicadas migraciones que ya no están en el repo y Flyway no arranca: borralo una vez con
`docker compose down -v`.

## Base de datos (prod)

PostgreSQL es la única base con la que corre la app, en producción con la misma versión mayor, 18; ahí
no hay compose, así que el server se conecta por variables de entorno:

```
mvn -B -pl server -am package -DskipTests
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<base> \
SPRING_DATASOURCE_USERNAME=<usuario> \
SPRING_DATASOURCE_PASSWORD=<password> \
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Sin esas tres variables el server no arranca. El esquema lo crean y actualizan las migraciones de
`server/src/main/resources/db/migration` al arrancar; un cambio de esquema entra como una migración
nueva numerada arriba de la última, nunca editando una ya aplicada. Si la base ya tiene el historial de
Flyway de antes del reset, esas migraciones ya no están en el repo y Flyway no arranca.

## Testear

```
mvn -B verify
```

Corre sin Docker: los tests de integración usan H2 en memoria en modo de compatibilidad PostgreSQL.

## Releases

Cada merge a `main` que incluya un commit `feat` o `fix` publica un
[GitHub Release](https://github.com/gstn-caruso/holy-wars/releases/latest) nuevo con su tag y las
release notes generadas del historial de commits, sin artefactos adjuntos.
