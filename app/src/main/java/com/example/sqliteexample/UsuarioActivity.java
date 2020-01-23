package com.example.sqliteexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

        this.preferences = PreferenceManager.getDefaultSharedPreferences((Context) this);
        int id = this.preferences.getInt("ID", -1);

        //Abrimos la base de datos 'DBUsuarios' en modo ecritura
        UsuariosSQLiteHelper usuariosSQLiteHelper = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);
        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        String[] campos = new String[]{"nombre_usuario", "email", "nombre_completo"};
        String[] args = new String[]{String.valueOf(id)};

        Cursor c = database.query("Usuarios", campos, "ID=?", args, null, null, null);
        if (c.moveToFirst()) {
            bienvenidoTextView.append(c.getString(2));
            usuarioTextView.append(c.getString(0));
            emailTextView.append(c.getString(1));
            //cerramos la base de datos
            database.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buckup:
                InputStream in = null;
                try {
                    in = new FileInputStream("/data/data/com.example.sqliteexample/databases/DBUsuarios");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String fichero = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/DBUsuarios.db";

                        FileOutputStream out = new FileOutputStream(fichero);
                        byte[] buff = new byte[1024];
                        int read = 0;

                        try {
                            while ((read = in.read(buff)) > 0) {
                                out.write(buff, 0, read);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                                out.close();
                                Toast toast = Toast.makeText(getApplicationContext(), "BuckUp creado en SD", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.restaurar:
                InputStream inSD = null;
                try {
                    inSD = new FileInputStream("/sdcard/Android/data/com.example.sqliteexample/files/Documents/DBUsuarios.db");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String fichero = "/data/data/com.example.sqliteexample/databases/DBUsuarios";

                        FileOutputStream out = new FileOutputStream(fichero);
                        byte[] buff = new byte[1024];
                        int read = 0;

                        try {
                            while ((read = inSD.read(buff)) > 0) {
                                out.write(buff, 0, read);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                inSD.close();
                                out.close();
                                Toast toast = Toast.makeText(getApplicationContext(), "BuckUp recuperado de SD", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "No hay buckup almacenado", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.usuarios:
                Intent intent = new Intent(UsuarioActivity.this, GestionUsuariosActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
