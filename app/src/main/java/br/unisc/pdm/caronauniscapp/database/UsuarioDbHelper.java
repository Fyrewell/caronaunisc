package br.unisc.pdm.caronauniscapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Diego on 05/10/2015.
 */
public class UsuarioDbHelper  extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String NUMERIC_TYPE = " NUMERIC";
    private static final String COMMA_SEP = ",";


    private static final String SQL_CREATE_TABLE_USUARIO =
            "CREATE TABLE " + UsuarioContract.Usuario.TABLE_NAME + " (" +
                    UsuarioContract.Usuario._ID + " INTEGER PRIMARY KEY, " +
                    UsuarioContract.Usuario.NOME + TEXT_TYPE + COMMA_SEP +
                    UsuarioContract.Usuario.ENDERECO + TEXT_TYPE + COMMA_SEP +
                    UsuarioContract.Usuario.EMAIL + TEXT_TYPE + COMMA_SEP +
                    UsuarioContract.Usuario.SEXO + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_TABLE_USUARIO =
            "DROP TABLE IF EXISTS " + UsuarioContract.Usuario.TABLE_NAME;

    // Se voce modificar o schema do banco, voce deve incrementar a versao do software.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PESSOA.db";

    public UsuarioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_USUARIO);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
