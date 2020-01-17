package com.example.sqliteexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActualizarUsuarioActivity extends AppCompatActivity {

    private EditText nombreUsuarioEditText;
    private EditText passwordEditText;
    private EditText nombreEditText;
    private Button actualizarUsuarioButton;
    private Button volverButton;

    private UsuariosSQLiteHelper usuariosSQLiteHelper;
    private SQLiteDatabase database;

    private String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usuario);

        nombreUsuarioEditText = findViewById(R.id.nombreUsuarioEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nombreEditText = findViewById(R.id.nombreEditText);
        actualizarUsuarioButton = findViewById(R.id.actualizarUsuarioButton);
        volverButton = findViewById(R.id.volverButton);

        Intent intent = getIntent();
        usuarioID = intent.getStringExtra("ID");

        //Abrimos la base de datos 'DBUsuarios' en modo ecritura
        usuariosSQLiteHelper = new UsuariosSQLiteHelper(this,"DBUsuarios", null, 1);
        database = usuariosSQLiteHelper.getWritableDatabase();

        Cursor c = database.rawQuery("SELECT nombre_usuario, password, nombre_completo FROM Usuarios WHERE ID='"+usuarioID+"'" , null);
        if(c.moveToFirst()){
            do{
                nombreUsuarioEditText.setText(c.getString(0));
                passwordEditText.setText(c.getString(1));
                nombreEditText.setText(c.getString(2));
            }while (c.moveToNext());
        }

        actualizarUsuarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Establecemos los campos-valores a actualizar
                ContentValues valores = new ContentValues();
                valores.put("nombre_usuario", nombreUsuarioEditText.getText().toString());
                valores.put("password", passwordEditText.getText().toString());
                valores.put("nombre_completo", nombreEditText.getText().toString());

                //Actualizamos el registro en la base de datos
                database.update("Usuarios", valores, "ID="+usuarioID,null);
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
