package com.example.sqliteexample;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UsuariosProvider extends ContentProvider {
    //Definición del CONTENT_URI
    private static final String uri = "content://com.example.sqliteexample/usuarios";

    public static final Uri CONTENT_URI = Uri.parse(uri);

    //UriMatcher
    private static final int USUARIOS = 1;
    private static final int USUARIOS_ID = 2;
    private static final UriMatcher uriMatcher;

    private UsuariosSQLiteHelper usuariosSQLiteHelper;

    //Iniciaizamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.sqliteexample","usuarios", USUARIOS);
        uriMatcher.addURI("com.example.sqliteexample","usuarios/#", USUARIOS_ID);
    }

    @Override
    public boolean onCreate() {
        usuariosSQLiteHelper = new UsuariosSQLiteHelper(getContext(),"DBUsuarios", null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        Cursor c = database.query("Usuarios", projection, where, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match){
            case USUARIOS:
                return "vnd.android.cursor.dir/com.example.sqliteexample/usuarios";
            case USUARIOS_ID:
                return "vnd.android.cursor.item/com.exmaple.sqliteexample/usuarios#";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long regID = 1;

        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        regID = database.insert("Usuarios", null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regID);

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;

        if(uriMatcher.match(uri) == USUARIOS_ID){
            where = "ID=" + uri.getLastPathSegment();
        }

        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        cont = database.delete("Usuarios", where, selectionArgs);

        return cont;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int cont;

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == USUARIOS_ID){
            where = "ID=" + uri.getLastPathSegment();
        }
        SQLiteDatabase database = usuariosSQLiteHelper.getWritableDatabase();

        cont = database.update("Usuarios", values, where,selectionArgs);
        return cont;
    }
}
