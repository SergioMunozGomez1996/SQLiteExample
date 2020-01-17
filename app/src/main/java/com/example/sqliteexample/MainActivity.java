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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usuarioEditText;
    private EditText passwordEditText;
    private Button accederButton;
    private Button cerrarButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioEditText = findViewById(R.id.usuarioEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        accederButton = findViewById(R.id.accederButton);
        cerrarButton = findViewById(R.id.cerrarButton);

        this.preferences = PreferenceManager.getDefaultSharedPreferences((Context)this);

        //Abrimos la base de datos 'DBUsuarios' en modo ecritura
        UsuariosSQLiteHelper usuariosSQLiteHelper = new UsuariosSQLiteHelper(this,"DBUsuarios", null, 1);
        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        Cursor c = database.rawQuery("SELECT ID FROM Usuarios", null);
        if(!c.moveToFirst()){
            database.execSQL("INSERT INTO Usuarios (nombre_usuario, password, nombre_completo, email) VALUES ('sergio', 1234, 'Sergio Munoz', 'email@email.com')");
            //cerramos la base de datos
            database.close();
        }


        accederButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usuarioEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                UsuariosSQLiteHelper usuariosSQLiteHelper = new UsuariosSQLiteHelper(getApplicationContext(),"DBUsuarios", null, 1);
                SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

                Cursor c = database.rawQuery("SELECT ID, nombre_usuario, password FROM Usuarios WHERE nombre_usuario = '" + usuario + "' and password = '"+ password +"'", null);
                if(c.moveToFirst()){
                    SharedPreferences.Editor editor = MainActivity.this.preferences.edit();
                    editor.putInt("ID", c.getInt(0));
                    editor.apply();

                    database.close();

                    Intent intent =  new Intent(MainActivity.this, UsuarioActivity.class);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                                    "Error usuario/password incorrectos", Toast.LENGTH_SHORT);

                    toast.show();
                }
            }
        });

        cerrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
