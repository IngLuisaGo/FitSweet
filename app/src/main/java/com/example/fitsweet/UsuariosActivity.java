package com.example.fitsweet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsweet.adapters.UsuarioAdapter;
import com.example.fitsweet.database.DBHelper;
import com.example.fitsweet.models.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerUsuarios;
    private UsuarioAdapter usuarioAdapter;
    private DBHelper dbHelper;
    private ArrayList<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        listaUsuarios = dbHelper.obtenerUsuarios();
        usuarioAdapter = new UsuarioAdapter(listaUsuarios, new UsuarioAdapter.OnUsuarioActionListener() {
            @Override
            public void onEditar(Usuario usuario) {
                mostrarDialogoEditar(usuario);
            }

            @Override
            public void onEliminar(Usuario usuario) {
                if ("Admin".equalsIgnoreCase(usuario.getCorreo())) {
                    Toast.makeText(UsuariosActivity.this, "No se puede eliminar el usuario administrador", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(UsuariosActivity.this)
                        .setTitle("Eliminar usuario")
                        .setMessage("¿Deseas eliminar a " + usuario.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            dbHelper.eliminarUsuario(usuario.getId());
                            cargarUsuarios();
                            Toast.makeText(UsuariosActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
        recyclerUsuarios.setAdapter(usuarioAdapter);
    }

    private void mostrarDialogoEditar(Usuario usuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_editar_usuario, null);
        builder.setView(vista);

        EditText etNombre = vista.findViewById(R.id.etNombreUsuario);
        EditText etCorreo = vista.findViewById(R.id.etCorreoUsuario);
        Spinner spinnerRol = vista.findViewById(R.id.spinnerRolUsuario);

        etNombre.setText(usuario.getNombre());
        etCorreo.setText(usuario.getCorreo());

        List<String> roles = Arrays.asList("usuario", "admin");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);
        int rolPosition = roles.indexOf(usuario.getRol());
        if (rolPosition >= 0) {
            spinnerRol.setSelection(rolPosition);
        }

        builder.setTitle("Editar usuario");
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String rolSeleccionado = spinnerRol.getSelectedItem().toString();

            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("Admin".equalsIgnoreCase(usuario.getCorreo()) && !"admin".equalsIgnoreCase(rolSeleccionado)) {
                Toast.makeText(this, "El administrador debe mantener el rol de admin", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.actualizarUsuario(usuario.getId(), nombre, correo, rolSeleccionado);
            cargarUsuarios();
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
