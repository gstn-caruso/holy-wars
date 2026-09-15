# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

Empezás una partida nueva en un mapa de 10x10 con 20 islas; cada isla tiene 16 parcelas y tu aldea,
14 parcelas de edificio. Los recursos (madera, lujo, oro) se producen en tiempo real. Desde cualquier
parcela libre abrís un panel htmx para construir, y la escena y los recursos se actualizan solos por SSE,
sin recargar la página. El look de las pantallas sigue el estilo de Ikariam: recursos, breadcrumb y
brújula enmarcando cada vista de la ciudad capital.

## Jugar

Con Docker corriendo, desde la raíz del repo:

```
mvn -B -pl server -am install -DskipTests
mvn -pl server spring-boot:run
```

El `install` deja `holy-wars-domain` en el repositorio local, así el segundo comando puede correr el
módulo `server` solo. `spring-boot:run` levanta un Postgres 17 (usa el `compose.yaml` de la raíz) y las
migraciones de Flyway arman el esquema al arrancar. Abrí `http://localhost:8080` en el navegador. Los
datos quedan en el volumen `holywars-postgres` entre reinicios: al cortar el server, Spring frena el
contenedor sin borrarlo. `docker compose down -v` los borra.

## Base de datos

PostgreSQL es la única base con la que corre la app; en producción no hay compose, así que el server
se conecta por variables de entorno:

```
mvn -B -pl server -am package -DskipTests
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<base> \
SPRING_DATASOURCE_USERNAME=<usuario> \
SPRING_DATASOURCE_PASSWORD=<password> \
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Sin esas tres variables el server no arranca. El esquema lo crean y actualizan las migraciones de
`server/src/main/resources/db/migration` al arrancar; un cambio de esquema entra como una migración
nueva numerada arriba de la última, nunca editando una ya aplicada.

## Perfil de desarrollo

```
mvn -pl server spring-boot:run -Dspring-boot.run.profiles=dev
```

Con el perfil `dev` el server sirve los assets estáticos que encuentre en
`~/.local/share/holy-wars/dev-assets` antes que los del classpath, si ese directorio existe. Sirve para
probar assets locales (por ejemplo, imágenes de referencia) sin empaquetarlos en el repo.

## Testear

```
mvn -B verify
```

Corre sin Docker: los tests de integración usan H2 en memoria en modo de compatibilidad PostgreSQL,
con las mismas migraciones que corren en dev y en producción.

## Releases

Cada merge a `main` que incluya un commit `feat` o `fix` publica un
[GitHub Release](https://github.com/gstn-caruso/holy-wars/releases/latest) nuevo con su tag y las
release notes generadas del historial de commits, sin artefactos adjuntos.
