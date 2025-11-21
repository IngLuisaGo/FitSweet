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
    private static final int DB_VERSION = 2;
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String TABLE_USUARIOS = "usuarios";

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
                "password_hash TEXT)";
        db.execSQL(createUsuariosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
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
        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    // Validar credenciales
    public boolean validarCredenciales(String correo, String passwordHash) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{"id"},
                "correo = ? AND password_hash = ?",
                new String[]{correo, passwordHash},
                null,
                null,
                null
        );

        boolean credencialesValidas = cursor.moveToFirst();
        cursor.close();
        db.close();
        return credencialesValidas;
    }
}
