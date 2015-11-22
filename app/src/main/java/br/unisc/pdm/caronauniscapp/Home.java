package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import br.unisc.pdm.caronauniscapp.chat.SearchActivity;

/**
 * Activity inicial apos feito o login no aplicativo.
 * Acesso a todas as funcionalidades atraves de shortcuts para perfil, agenda ou rota.
 */
public class Home extends AppCompatActivity{

    String mat = "";
    String sexo = "";
    String nome = "";
    int usuario_tipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        if (extras!=null) {
            mat = extras.getString("matricula");
            sexo = extras.getString("sexo");
            nome = extras.getString("nome");
            usuario_tipo = extras.getInt("usuario_tipo");
            if (sexo.equals("Feminino")) {
                ImageButton btn = (ImageButton)findViewById(R.id.btn_open_voce);
                btn.setImageResource(R.drawable.icon_open_vocef);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cadastro) {
            openVoce(findViewById(R.id.action_cadastro));
        }
        if (id == R.id.action_map) {
            openRota(findViewById(R.id.action_map));
        }
        if (id == R.id.action_agenda) {
            openAgenda(findViewById(R.id.action_agenda));
        }
        if (id == R.id.action_caronas) {
            openCaronas(findViewById(R.id.action_caronas));
        }
        if (id == R.id.action_chat) {
            openChat(findViewById(R.id.action_chat));
        }

        return super.onOptionsItemSelected(item);
    }

    public void openVoce(View v){
        Intent formAct = new Intent(this,FormUsuario.class);
        formAct.putExtra("matricula",mat);
        startActivity(formAct);
    }
    public void openRota(View v){
        Intent mapAct = new Intent(this,MapsSearchActivity.class);
        mapAct.putExtra("matricula",mat);
        startActivity(mapAct);
    }
    public void openAgenda(View v){
        Intent agAct = new Intent(this,Agenda.class);
        agAct.putExtra("matricula",mat);
        this.startActivityForResult(agAct, 1);
    }
    public void openCaronas(View v){
        Intent carAct = new Intent(this,CaronasActivity.class);
        carAct.putExtra("matricula",mat);
        carAct.putExtra("usuario_tipo",usuario_tipo);
        startActivity(carAct);
    }
    public void openChat(View v){
        Intent chatAct = new Intent(this,SearchActivity.class);
        chatAct.putExtra("matricula",mat);
        chatAct.putExtra("nome",nome);
        startActivity(chatAct);
    }
    public void deslogar(View v){
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mat = data.getStringExtra("matricula");
            }
        }
    }

}
