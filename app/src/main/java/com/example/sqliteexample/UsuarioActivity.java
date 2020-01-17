package com.example.sqliteexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UsuarioActivity extends AppCompatActivity {

    private TextView bienvenidoTextView;
    private TextView usuarioTextView;
    private TextView emailTextView;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        bienvenidoTextView = findViewById(R.id.bienVenidoTextView);
        usuarioTextView = findViewById(R.id.usuarioTextView);
        emailTextView = findViewById(R.id.emailTextView);

        this.preferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        int id = this.preferences.getInt("ID", -1);

        //Abrimos la base de datos 'DBUsuarios' en modo ecritura
        UsuariosSQLiteHelper usuariosSQLiteHelper = new UsuariosSQLiteHelper(this,"DBUsuarios", null, 1);
        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        String[] campos = new String[] {"nombre_usuario", "email", "nombre_completo"};
        String[] args = new String[] {String.valueOf(id)};

        Cursor c = database.query("Usuarios", campos,"ID=?", args, null,null, null);
        if(c.moveToFirst()){
            bienvenidoTextView.append(c.getString(2));
            usuarioTextView.append(c.getString(0));
            emailTextView.append(c.getString(1));
            //cerramos la base de datos
            database.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.buckup:


            case R.id.restaurar:


            case R.id.usuarios:
                Intent intent = new Intent(UsuarioActivity.this,GestionUsuariosActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
