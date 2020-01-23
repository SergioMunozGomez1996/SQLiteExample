package com.example.sqliteexample;

import android.provider.BaseColumns;

public class Usuarios implements BaseColumns {
     private Usuarios(){}

     //Nombre de las columnas
     public static final String COL_NOMBRE_USUARIO = "nombre_usuario";
     public static final String COL_PASSWORD = "password";
     public static final String COL_NOMBRE_COMPLETO = "nombre_completo";
     public static final String COL_EMAIL = "email";
}
