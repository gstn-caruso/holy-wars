# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

El juego se está reescribiendo desde cero; todavía no hay contenido jugable. El estado vive en memoria
(H2) y se pierde al cerrar el server; con Docker corriendo hay una forma de arrancarlo en Postgres, ver
"Base de datos" abajo.

## Jugar

```
mvn -B -pl server -am package -DskipTests
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Abrí `http://localhost:8080` en el navegador.

## Base de datos

Con Docker corriendo, `mvn -pl server -am spring-boot:run` desde la raíz del repo levanta un Postgres 17
solo (usa el `compose.yaml` de la raíz) y guarda ahí el estado del juego entre reinicios. El jar empaquetado
(`java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar`) y el `.deb` siempre usan H2 en memoria: el
estado se pierde al cerrar el server. `docker compose down -v` borra los datos guardados en Postgres.

## Testear

```
mvn -B test
```

Los releases están pausados mientras dura la reescritura: el `.deb` del
[último release publicado](https://github.com/gstn-caruso/holy-wars/releases/latest) sigue siendo el de
`v0.14.0`, la última versión con paridad de juego completa. Van a volver cuando la reescritura alcance esa
paridad.

## Instalación

Requiere Java 25. Bajá el `.deb` del [último release](https://github.com/gstn-caruso/holy-wars/releases/latest)
en GitHub e instalalo:

```
sudo apt install ./holy-wars_<versión>_all.deb
```

Esto deja el comando `holy-wars` disponible y agrega Holy Wars al menú de aplicaciones. Al ejecutarlo,
levanta el server local y abre el navegador.
