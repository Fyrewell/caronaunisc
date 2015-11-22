package br.unisc.pdm.caronauniscapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 05/10/2015.
 */
public class UsuarioDAO {    Context context;
    private UsuarioDbHelper mdb;
    private SQLiteDatabase database;
    private String[] colunas = {
            UsuarioContract.Usuario._ID,
            UsuarioContract.Usuario.NOME,
            UsuarioContract.Usuario.ENDERECO,
            UsuarioContract.Usuario.EMAIL,
            UsuarioContract.Usuario.SEXO,
    };

    public UsuarioDAO (Context context){
        this.mdb = new UsuarioDbHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = mdb.getWritableDatabase();
    }

    public void close() {
        mdb.close();
    }

    public Usuario insertUsuario(Usuario p){

        ContentValues dadosUsuario = new ContentValues();
        dadosUsuario.put(UsuarioContract.Usuario.NOME,p.getNome());
        dadosUsuario.put(UsuarioContract.Usuario.ENDERECO,p.getEndereco());
        dadosUsuario.put(UsuarioContract.Usuario.EMAIL,p.getEmail());
        dadosUsuario.put(UsuarioContract.Usuario.SEXO, p.getSexo());

        try {
            long newUsuarioId = database.insert(
                    UsuarioContract.Usuario.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    dadosUsuario);

            Toast.makeText(this.context, "Usuario inserida com sucesso: " + newUsuarioId, Toast.LENGTH_SHORT).show();
            return p;
        }catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void deleteUsuario(Usuario Usuario) {
        long id = Usuario.getId();
        database.delete(UsuarioContract.Usuario.TABLE_NAME, UsuarioContract.Usuario._ID + " = " + id, null);
        Toast.makeText(this.context, "Usuario deletada com sucesso: " + id, Toast.LENGTH_SHORT).show();
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> Usuarios = new ArrayList<Usuario>();

        Cursor cursor = database.query(UsuarioContract.Usuario.TABLE_NAME,
                colunas, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Usuario Usuario = cursorToUsuario(cursor);
            Usuarios.add(Usuario);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Usuarios;
    }

    private Usuario cursorToUsuario(Cursor cursor) {
        Usuario Usuario = new Usuario();
        Usuario.setId(cursor.getInt(0));
        Usuario.setNome(cursor.getString(1));
        Usuario.setEndereco(cursor.getString(2));
        Usuario.setEmail(cursor.getString(3));
        Usuario.setSexo(cursor.getString(4));
        return Usuario;
    }

    public Usuario getUsuarioById(int id) {


        //Colunas para filtrar no WHERE da query
        String selection = UsuarioContract.Usuario._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        Cursor c = database.query(
                UsuarioContract.Usuario.TABLE_NAME,      // The table to query
                colunas,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        if(c.getCount() > 0){
            c.moveToFirst();
            Usuario p = cursorToUsuario(c);
            return p;
        }else
            return null;

    }

    public void updateUsuario(Usuario p) {
        ContentValues dadosUsuario = new ContentValues();
        dadosUsuario.put(UsuarioContract.Usuario.NOME,p.getNome());
        dadosUsuario.put(UsuarioContract.Usuario.ENDERECO,p.getEndereco());
        dadosUsuario.put(UsuarioContract.Usuario.EMAIL,p.getEmail());
        dadosUsuario.put(UsuarioContract.Usuario.SEXO, p.getSexo());

        // Which row to update, based on the ID
        String selection = UsuarioContract.Usuario._ID + " = ?";
        String[] selectionArgs = { String.valueOf(p.getId()) };

        try {
            int count = database.update(
                    UsuarioContract.Usuario.TABLE_NAME,
                    dadosUsuario,
                    selection,
                    selectionArgs);


            Toast.makeText(this.context, "Usuario atualizada com sucesso: " + count, Toast.LENGTH_SHORT).show();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
