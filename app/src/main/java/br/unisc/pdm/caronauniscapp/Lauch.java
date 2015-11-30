package br.unisc.pdm.caronauniscapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity inicial, responsavel por chamar tela de Login caso usuario ja tenha se cadastrado
 * ou Tela de input de dados do usuario para cadastro.
 *
 * Diego, Gabriel, Rafael
 *
 */
public class Lauch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lauch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void logar(View v){
        startActivity(new Intent(this,Logar.class));
    }

    public void cadastrar(View v){
        startActivity(new Intent(this,FormUsuario.class));
    }

}
