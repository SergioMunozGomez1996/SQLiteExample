package com.example.sqliteexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class GestionUsuariosActivity extends AppCompatActivity {

    private Spinner usuariosSpinner;
    private Button actualizarButton;
    private Button borrarButton;
    private Button nuevoButton;
    private Button volverButton;
    private String usuarioID;

    private UsuariosSQLiteHelper usuariosSQLiteHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);

        usuariosSpinner = findViewById(R.id.usuariosSpinner);
        actualizarButton = findViewById(R.id.actualizarUsuariobButton);
        borrarButton = findViewById(R.id.eliminarUsuarioButton);
        nuevoButton = findViewById(R.id.actualizarUsuarioButton);
        volverButton = findViewById(R.id.volverButton);



        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(GestionUsuariosActivity.this, ActualizarUsuarioActivity.class);
                intent.putExtra("ID", usuarioID);
                startActivity(intent);
            }
        });

        borrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete("Usuarios", "ID="+usuarioID, null);
                finish();
                startActivity(getIntent());
            }
        });

        nuevoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(GestionUsuariosActivity.this, NuevoUsuarioAcitivty.class);
                startActivity(intent);
            }
        });

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> usuarios = new ArrayList<>();
        final ArrayList<String> usuariosID = new ArrayList<>();

        //Abrimos la base de datos 'DBUsuarios' en modo ecritura
        usuariosSQLiteHelper = new UsuariosSQLiteHelper(this,"DBUsuarios", null, 1);
        database = usuariosSQLiteHelper.getWritableDatabase();

        Cursor c = database.rawQuery("SELECT nombre_completo, ID FROM Usuarios", null);
        if(c.moveToFirst()){
            do{
                usuarios.add(c.getString(0));
                usuariosID.add(c.getString(1));
            }while (c.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        usuariosSpinner.setAdapter(adapter);

        usuariosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usuarioID = usuariosID.get(position);
                actualizarButton.setEnabled(true);
                borrarButton.setEnabled(true);
                nuevoButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //cerramos la base de datos
        database.close();
    }
}
