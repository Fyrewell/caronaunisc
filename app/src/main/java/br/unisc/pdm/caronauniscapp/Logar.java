package br.unisc.pdm.caronauniscapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import br.unisc.pdm.caronauniscapp.database.Usuario;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioTela;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioWebDao;

/**
 * Recebe login (matricula) e senha de usuario ja cadastrado e realiza o login no aplicativo.
 */
public class Logar extends Activity implements UsuarioTela {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void logar(View v){
        EditText mat = (EditText) findViewById(R.id.editText2);
        EditText sen = (EditText) findViewById(R.id.editText);

        UsuarioWebDao wb = new UsuarioWebDao(this);

        wb.loginUsuario(mat.getText().toString(),sen.getText().toString());
    }

    public void popularView(List<Usuario> values) {
    }
}
