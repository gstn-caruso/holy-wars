# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

Empezás una partida nueva en un mapa de 10x10 con 20 islas; cada isla tiene 16 parcelas y tu aldea,
14 parcelas de edificio. Los recursos (madera, lujo, oro) se producen en tiempo real. Desde cualquier
parcela libre abrís un panel htmx para construir, y la escena y los recursos se actualizan solos por SSE,
sin recargar la página. El look de las pantallas sigue el estilo de Ikariam: recursos, breadcrumb y
brújula enmarcando cada vista de la ciudad capital.

## Jugar

```
mvn -B -pl server -am package -DskipTests
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Abrí `http://localhost:8080` en el navegador. El jar empaquetado usa H2 en memoria: el estado
se pierde al cerrar el server.

## Base de datos

Con Docker corriendo, desde la raíz del repo:

```
mvn -B -pl server -am install -DskipTests
mvn -pl server spring-boot:run
```

El `install` deja `holy-wars-domain` en el repositorio local, así el segundo comando puede correr el
módulo `server` solo. `spring-boot:run` levanta un Postgres 17 (usa el `compose.yaml` de la raíz) y guarda
ahí el esquema y los datos entre reinicios; al cortarlo, para el contenedor solo. `docker compose down -v`
borra los datos guardados en Postgres.

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

## Releases

Cada merge a `main` que incluya un commit `feat` o `fix` publica un
[GitHub Release](https://github.com/gstn-caruso/holy-wars/releases/latest) nuevo con su tag y las
release notes generadas del historial de commits, sin artefactos adjuntos.
