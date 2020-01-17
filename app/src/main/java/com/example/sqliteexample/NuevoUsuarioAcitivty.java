package com.example.sqliteexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NuevoUsuarioAcitivty extends AppCompatActivity {

    private EditText nombreUsuarioEditText;
    private EditText passwordEditText;
    private EditText nombreEditText;
    private EditText emailEditText;
    private Button crearUsuarioButton;
    private Button volverButton;

    private UsuariosSQLiteHelper usuariosSQLiteHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario_acitivty);

        nombreUsuarioEditText = findViewById(R.id.nombreUsuarioEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nombreEditText = findViewById(R.id.nombreEditText);
        emailEditText = findViewById(R.id.emailEditText);
        crearUsuarioButton = findViewById(R.id.nuevoUsuarioButton);
        volverButton = findViewById(R.id.volverButton);

        crearUsuarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrimos la base de datos 'DBUsuarios' en modo ecritura
                usuariosSQLiteHelper = new UsuariosSQLiteHelper(getApplicationContext(),"DBUsuarios", null, 1);
                database = usuariosSQLiteHelper.getWritableDatabase();

                //Establecemos los campos-valores a actualizar
                ContentValues valores = new ContentValues();
                valores.put("nombre_usuario", nombreUsuarioEditText.getText().toString());
                valores.put("password", passwordEditText.getText().toString());
                valores.put("nombre_completo", nombreEditText.getText().toString());
                valores.put("email", emailEditText.getText().toString());

                //Actualizamos el registro en la base de datos
                database.insert("Usuarios", null, valores);
                finish();
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
    protected void onPause() {
        super.onPause();
        database.close();
    }
}
