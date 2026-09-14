# Holy Wars

Clon de Ikariam single-player: server Spring Boot que sirve el juego en el browser, Java 25.

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

Cada merge a `main` publica un release semántico según el prefijo del commit (`feat:` sube minor,
`fix:` y `perf:` suben patch; `chore:`, `docs:`, `ci:`, `test:`, `refactor:`, `build:`, `style:` no publican).

## Instalación

Requiere Java 25. Bajá el `.deb` del [último release](https://github.com/gstn-caruso/holy-wars/releases/latest)
en GitHub e instalalo:

```
sudo apt install ./holy-wars_<versión>_all.deb
```

Esto deja el comando `holy-wars` disponible y agrega Holy Wars al menú de aplicaciones. Al ejecutarlo,
levanta el server local y abre el navegador; los datos del juego quedan en `~/.local/share/holy-wars/holy-wars.db`.
