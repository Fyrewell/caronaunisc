package br.unisc.pdm.caronauniscapp.chat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import br.unisc.pdm.caronauniscapp.R;
import br.unisc.pdm.caronauniscapp.database.Usuario;

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    private String mat = "";
    private String nome = "";

    ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_persons);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        this.listView = (ListView) findViewById(R.id.search_pessoas_list);

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            mat = extras.getString("matricula");
            nome = extras.getString("nome");
        }
        pgBar = (ProgressBar) findViewById(R.id.progressBar);
        pgBar.setVisibility(View.GONE);
    }

    public void buscaPessoa() {

        //pegando nome digitado para buscar
        EditText edit_busca_pessoa = (EditText) findViewById(R.id.inp_nome_search);
        String nome_buscar = edit_busca_pessoa.getText().toString();

        buscaWs(nome_buscar);
        pgBar.setVisibility(View.VISIBLE);

    }

    public void buscaWs(String nome) {
        try {
            nome = URLEncoder.encode(nome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getBaseContext(), "Erro ao parsear url", Toast.LENGTH_SHORT).show();
        }
        String url = "http://caronaunisc.herokuapp.com/api/usuario/search/" + nome;

        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Usuario> pessoas = new ArrayList<Usuario>();

                        //Fazendo PARSE do JSON
                        String valores = "";
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonKeyValue = response.getJSONObject(i);
                                Usuario pessoa = jsobjToUsuario(jsonKeyValue);
                                pessoas.add(pessoa);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        popularView(pessoas);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getBaseContext(), "Problema ao executar sua solicitacao", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        queue.add(jsArrRequest);

    }

    private Usuario jsobjToUsuario(JSONObject json) {
        Usuario p = new Usuario();
        try {
            p.setId(json.getInt("id"));
            p.setMatricula(json.getInt("matricula"));
            p.setNome(json.getString("nome"));
            p.setSenha(json.getString("senha"));
            p.setSexo(json.getString("sexo"));
            p.setCadastroTipo(json.getInt("usuario_tipo"));
            p.setFoto(json.getString("foto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void popularView(List<Usuario> values) {

        ArrayList<String> listNome = new ArrayList<String>();
        ArrayList<String> listImages = new ArrayList<String>();
        ArrayList<Integer> listMats = new ArrayList<Integer>();
        for (Usuario u : values) {
            listNome.add(u.getNome());
            listImages.add(u.getFoto());
            listMats.add(u.getMatricula());
        }

        ListPessoas adapter = new ListPessoas(listImages, listNome, listMats, mat, nome, this);
        listView.setAdapter(adapter);
        pgBar.setVisibility(View.GONE);
    }

    public void buscaPessoa(View v) {
        this.buscaPessoa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}