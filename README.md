# Holy Wars

Juego hecho con libGDX en Java 25.

## Jugar

```
mvn -q compile exec:exec
```

Abre una ventana con el juego.

## Testear

```
mvn -B test
```

Cada merge a `main` publica un release semántico según el prefijo del commit (`feat:` sube minor,
`fix:` y `perf:` suben patch; `chore:`, `docs:`, `ci:`, `test:`, `refactor:` no publican).

## Instalación

Requiere Java 25. Bajá el `.deb` del [último release](https://github.com/gstn-caruso/holy-wars/releases/latest)
en GitHub e instalalo:

```
sudo apt install ./holy-wars_<versión>_all.deb
```

Esto deja el comando `holy-wars` disponible y agrega Holy Wars al menú de aplicaciones.
