# FitSweet - Documento técnico

## Resumen
FitSweet es una aplicación Android nativa escrita en Java que combina navegación basada en `Activity` con persistencia local mediante SQLite. El objetivo es permitir a usuarias navegar un catálogo de productos dulces, añadirlos a un carrito de compras y, en el caso de administradoras, gestionar tanto el inventario como las cuentas de usuario.

## Arquitectura general
- **Presentación (Activities):** Cada pantalla principal es una `AppCompatActivity` dedicada (p. ej., `LoginActivity`, `ProductosActivity`, `CarritoActivity`, `UsuariosActivity`). La navegación se realiza con `Intent` explícitos y flags estándar para limpiar la pila cuando es necesario (por ejemplo, al volver desde el carrito al catálogo).
- **Vista de listas:** Las listas (productos, usuarios y carrito) usan `RecyclerView` con adaptadores especializados (`ProductoAdapter`, `UsuarioAdapter`, `CarritoAdapter`) que encapsulan la lógica de render y callbacks de edición/eliminación.
- **Persistencia:** Se utiliza un `SQLiteOpenHelper` (`DBHelper`) como capa de acceso a datos. Incluye creación de tablas, operaciones CRUD para productos y usuarios, además de utilidades para el carrito (agregar, actualizar cantidad, calcular totales).
- **Concurrencia:** Las operaciones de base de datos en `ProductosActivity` y `CarritoActivity` se ejecutan en un `ExecutorService` de un solo hilo para mantener la UI responsiva y publicar resultados con `runOnUiThread`.

## Flujo de usuario
1. **Onboarding:** `SplashActivity` muestra la introducción y pasa a `BienvenidaActivity`, que a su vez dirige a login o registro.
2. **Autenticación:** `LoginActivity` valida credenciales contra SQLite y determina el rol (`admin` o `usuario`). El registro crea cuentas nuevas desde `RegistroActivity` con manejo de unicidad de correo.
3. **Catálogo y carrito (rol usuario):** `ProductosActivity` lista los productos disponibles y permite añadirlos al carrito; el carrito se visualiza y actualiza en `CarritoActivity`, mostrando totales en tiempo real.
4. **Gestión (rol admin):** Las administradoras pueden agregar productos desde un diálogo modal en `ProductosActivity` y administrar cuentas mediante `UsuariosActivity`, con salvaguardas para impedir la eliminación o degradación del usuario administrador por defecto.

## Modelo de datos (SQLite)
- **Tabla `productos`:** `id` autoincremental, `nombre`, `descripcion`, `precio`, `imagen` (URL o recurso). CRUD completo disponible desde `DBHelper`.
- **Tabla `usuarios`:** `id`, `correo` único, `nombre`, `password_hash` (actualmente plano), `rol` con valor por defecto `usuario`. Se inserta un admin inicial (`correo` y `password_hash` = `Admin`).
- **Tabla `carrito`:** Relaciona `producto_id` con `cantidad`. Incluye lógica para combinar cantidades repetidas y eliminar registros cuando la cantidad llega a cero.

## Gestión del catálogo
`ProductosActivity` carga productos en segundo plano y muestra/oculta acciones según el rol. Para agregar productos se usa un `AlertDialog` con validación básica; tras guardar se reconstruye el adaptador para reflejar el inventario actualizado.

## Gestión de usuarios
`UsuariosActivity` obtiene la lista desde SQLite y la renderiza con `UsuarioAdapter`. Al editar, un diálogo permite modificar nombre, correo y rol con un `Spinner`. La actividad evita eliminar el administrador por defecto y exige mantener su rol como `admin`.

## Carrito de compras
`CarritoActivity` consulta el carrito y recalcula el total en un hilo dedicado. El adaptador recibe un callback para actualizar el total cuando cambian cantidades; los textos de estado se ajustan en función de la cantidad de ítems.

## Consideraciones de seguridad y futuras mejoras
- Las contraseñas se almacenan en texto plano (`password_hash`), por lo que es recomendable migrar a hashing seguro (p. ej., `BCrypt`) y usar un mecanismo de autenticación remota si se expande la app.
- Actualmente no hay validación avanzada de imágenes ni precios; se sugiere agregar validaciones y sanitización de entrada en los diálogos de creación/edición.

## Construcción y ejecución
El proyecto usa Gradle con el wrapper incluido (`./gradlew`). La build principal se define en `settings.gradle.kts` y `build.gradle.kts`. Para generar la app:

```bash
./gradlew assembleDebug
```

Esto compila la aplicación FitSweet para dispositivos o emuladores de desarrollo.
