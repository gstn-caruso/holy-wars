# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

El juego se está reescribiendo desde cero; todavía no hay contenido jugable. Sin Docker el estado vive
en memoria (H2) y se pierde al cerrar el server; con Docker, ver "Base de datos" abajo.

## Jugar

```
mvn -B -pl server -am package -DskipTests
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Abrí `http://localhost:8080` en el navegador.

## Base de datos

`docker compose up -d` es opcional. Con Docker corriendo y `compose.yaml` en el directorio de trabajo,
`java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar` (o `mvn -pl server spring-boot:run`) levanta
un Postgres 17 solo y guarda ahí el estado del juego entre reinicios. Sin Docker, el server arranca con
H2 en memoria y el estado se pierde al cerrar. `docker compose down -v` borra los datos guardados.

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
