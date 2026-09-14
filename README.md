# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

El juego se está reescribiendo desde cero. Por ahora el estado vive en memoria (H2 en memoria) y se
pierde al cerrar el server; todavía no hay contenido jugable.

## Jugar

```
mvn -B -pl server -am package -DskipTests
java -jar server/target/holy-wars-server-0.0.0-SNAPSHOT.jar
```

Abrí `http://localhost:8080` en el navegador.

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
