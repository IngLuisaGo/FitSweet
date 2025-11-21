package com.example.fitsweet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fitsweet.models.Producto;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitsweet.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String TABLE_CARRITO = "carrito";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductosTable = "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "descripcion TEXT, " +
                "precio REAL)";
        db.execSQL(createProductosTable);

        String createUsuariosTable = "CREATE TABLE " + TABLE_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "correo TEXT UNIQUE, " +
                "nombre TEXT, " +
                "password_hash TEXT, " +
                "rol TEXT DEFAULT 'usuario')";
        db.execSQL(createUsuariosTable);

        String createCarritoTable = "CREATE TABLE " + TABLE_CARRITO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "producto_id INTEGER, " +
                "cantidad INTEGER, " +
                "FOREIGN KEY(producto_id) REFERENCES productos(id))";
        db.execSQL(createCarritoTable);

        insertarAdminPorDefecto(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARRITO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    private void insertarAdminPorDefecto(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{"id"},
                "correo = ?",
                new String[]{"Admin"},
                null,
                null,
                null
        );

        boolean existeAdmin = cursor.moveToFirst();
        cursor.close();

        if (!existeAdmin) {
            ContentValues values = new ContentValues();
            values.put("nombre", "Administrador");
            values.put("correo", "Admin");
            values.put("password_hash", "Admin");
            values.put("rol", "admin");
            db.insert(TABLE_USUARIOS, null, values);
        }
    }

    // Insertar producto
    public void insertarProducto(String nombre, String descripcion, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("precio", precio);
        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

    // Obtener todos los productos
    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Actualizar producto
    public void actualizarProducto(int id, String nombre, String descripcion, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("precio", precio);
        db.update(TABLE_PRODUCTOS, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Eliminar producto
    public void eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Insertar usuario
    public void insertarUsuario(String nombre, String correo, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("password_hash", passwordHash);
        values.put("rol", "usuario");
        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    // Obtener rol de usuario según credenciales
    public String obtenerRolPorCredenciales(String correo, String passwordHash) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{"rol"},
                "correo = ? AND password_hash = ?",
                new String[]{correo, passwordHash},
                null,
                null,
                null
        );

        String rol = null;
        if (cursor.moveToFirst()) {
            rol = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return rol;
    }

    // ---- Carrito ----
    public void agregarProductoAlCarrito(int productoId, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_CARRITO,
                new String[]{"id", "cantidad"},
                "producto_id = ?",
                new String[]{String.valueOf(productoId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int cantidadActual = cursor.getInt(1);
            actualizarCantidadCarrito(id, cantidadActual + cantidad);
        } else {
            ContentValues values = new ContentValues();
            values.put("producto_id", productoId);
            values.put("cantidad", cantidad);
            db.insert(TABLE_CARRITO, null, values);
        }
        cursor.close();
        db.close();
    }

    public ArrayList<com.example.fitsweet.models.CarritoItem> obtenerCarrito() {
        ArrayList<com.example.fitsweet.models.CarritoItem> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.id, c.producto_id, c.cantidad, p.nombre, p.descripcion, p.precio " +
                "FROM " + TABLE_CARRITO + " c " +
                "JOIN " + TABLE_PRODUCTOS + " p ON c.producto_id = p.id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                lista.add(new com.example.fitsweet.models.CarritoItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getInt(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void actualizarCantidadCarrito(int id, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (cantidad <= 0) {
            eliminarItemCarrito(id);
            db.close();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("cantidad", cantidad);
        db.update(TABLE_CARRITO, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void eliminarItemCarrito(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARRITO, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public double calcularTotalCarrito() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.cantidad, p.precio FROM " + TABLE_CARRITO + " c " +
                "JOIN " + TABLE_PRODUCTOS + " p ON c.producto_id = p.id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int cantidad = cursor.getInt(0);
                double precio = cursor.getDouble(1);
                total += cantidad * precio;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return total;
    }
}
