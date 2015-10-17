package br.unisc.pdm.caronauniscapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import br.unisc.pdm.caronauniscapp.database.Usuario;
import br.unisc.pdm.caronauniscapp.database.UsuarioDAO;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioTela;
import br.unisc.pdm.caronauniscapp.webservice.UsuarioWebDao;

/**
 * Created by Diego on 05/10/2015.
 */
public class FormUsuario extends ActionBarActivity implements UsuarioTela {
    private UsuarioDAO dao;
    private String matricula = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pessoa);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            matricula = extras.getString("matricula");
        }

        Log.d("MAT", "buscou o matricula " + matricula);

        dao = new UsuarioDAO(this);
        dao.open();


        if (!matricula.equals("")) {
            UsuarioWebDao webservice = new UsuarioWebDao(this);
            webservice.getUsuarioByMat(matricula);
            Toast.makeText(this, "Deve buscar na web!", Toast.LENGTH_SHORT).show();
        }


    }

    public void populaTela(Usuario p){
        EditText edit_matricula = (EditText) findViewById(R.id.edit_matricula);
        EditText edit_nome = (EditText) findViewById(R.id.edit_nome);
        EditText edit_senha = (EditText) findViewById(R.id.edit_senha);
        RadioButton rb_masc = (RadioButton) findViewById(R.id.radio_masculino);
        RadioButton rb_fem = (RadioButton) findViewById(R.id.radio_feminino);
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_ambos = (RadioButton) findViewById(R.id.radio_ambos);
        edit_matricula.setText(String.valueOf(p.getMatricula()));
        edit_nome.setText(p.getNome());
        edit_senha.setText(p.getSenha());
        if(p.getSexo().equals("Masculino"))
            rb_masc.setChecked(true);
        if(p.getSexo().equals("Feminino"))
            rb_fem.setChecked(true);
        if(p.getCadastroTipo()==1)
            rb_dar.setChecked(true);
        if(p.getCadastroTipo()==2)
            rb_receber.setChecked(true);
        if(p.getCadastroTipo()==3)
            rb_ambos.setChecked(true);

    }

    public void insertOrEditPerson(){
        //Buscando dados de entrada digitados pelo usuario
        EditText edit_id = (EditText) findViewById(R.id.edit_id);
        EditText edit_matricula = (EditText) findViewById(R.id.edit_matricula);
        EditText edit_nome = (EditText) findViewById(R.id.edit_nome);
        EditText edit_senha = (EditText) findViewById(R.id.edit_senha);
        RadioButton rb_masc = (RadioButton) findViewById(R.id.radio_masculino);
        RadioButton rb_fem = (RadioButton) findViewById(R.id.radio_feminino);
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_ambos = (RadioButton) findViewById(R.id.radio_ambos);
        String sexo = null;
        if(rb_masc.isChecked()) sexo = "Masculino";
        if(rb_fem.isChecked()) sexo = "Feminino";
        int ctipo = 0;
        if(rb_dar.isChecked()) ctipo = 1;
        if(rb_receber.isChecked()) ctipo = 2;
        if(rb_ambos.isChecked()) ctipo = 3;

        if (edit_id.equals("")||edit_matricula.equals("")||edit_nome.equals("")||edit_senha.equals("")||rb_masc.equals("")||ctipo==0){
            Toast.makeText(this,"Entrar com todos os dados!",Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario person = new Usuario();

        if(edit_id.getText().toString().length() > 0)
            person.setId(Integer.parseInt(edit_id.getText().toString()));

        person.setNome(edit_nome.getText().toString());
        person.setMatricula(Integer.parseInt(edit_matricula.getText().toString()));
        person.setSenha(edit_senha.getText().toString());
        person.setSexo(sexo);
        person.setCadastroTipo(ctipo);

        Toast.makeText(this,"Deve gravar na web!",Toast.LENGTH_SHORT).show();
        UsuarioWebDao webservice = new UsuarioWebDao(this);
        if(!matricula.equals("")){
            webservice.editUsuario(person);
        }else{
            webservice.insertUsuario(person);
        }

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_accept_pessoa){
            insertOrEditPerson();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void popularView(List<Usuario> values) {
        populaTela(values.get(0));
        Log.d("WBS", values.toString());
        Toast.makeText(this,"Voltou.. populando!",Toast.LENGTH_SHORT).show();
    }

    public void cadastrar(View v)
    {
        insertOrEditPerson();
    }
}