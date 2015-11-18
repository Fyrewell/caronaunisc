package br.unisc.pdm.caronauniscapp;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.caronauniscapp.chat.ListPessoas;
import br.unisc.pdm.caronauniscapp.database.Usuario;
import br.unisc.pdm.caronauniscapp.webservice.RotaTela;
import br.unisc.pdm.caronauniscapp.webservice.RotaWebDao;

public class CaronasActivity extends TabActivity implements RotaTela {

    ListView listView;
    private String mat = "";
    private String nome = "";
    private int usuario_tipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        this.listView = (ListView) findViewById(R.id.tab0);

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            mat = extras.getString("matricula");
            nome = extras.getString("nome");
            usuario_tipo = extras.getInt("usuario_tipo");
        }

        TabHost.TabSpec descritor = getTabHost().newTabSpec("tag1");
        descritor.setContent(R.id.tab0);
        descritor.setIndicator("Receber Carona");
        getTabHost().addTab(descritor);
        descritor = getTabHost().newTabSpec("tag2");
        descritor.setContent(R.id.tab1);
        descritor.setIndicator("Dar Carona");
        getTabHost().addTab(descritor);

        switch (usuario_tipo){
            case 1:
                getTabHost().getTabWidget().getChildAt(0).setVisibility(View.GONE);
                break;
            case 2:
                getTabHost().getTabWidget().getChildAt(1).setVisibility(View.GONE);
                break;
        }

        getTabHost().setCurrentTab(0);

        new RotaWebDao(this).caronasReceber(mat);
    }

    public void buscaPessoa() {

        //pegando nome digitado para buscar
        EditText edit_busca_pessoa = (EditText) findViewById(R.id.inp_nome_search);
        String nome_buscar = edit_busca_pessoa.getText().toString();

        buscaWs(nome_buscar);

    }

    public void buscaWs(String nome) {
        try {
            nome = URLEncoder.encode(nome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getBaseContext(), "Erro ao parsear url", Toast.LENGTH_SHORT).show();
        }
        String url = "http://caronaunisc.herokuapp.com/api/usuario/" + nome;
        Log.d("WBS", "URL: " + url);

        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("WBS", response.toString());

                        List<Usuario> pessoas = new ArrayList<Usuario>();

                        //Fazendo PARSE do JSON
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonKeyValue = response.getJSONObject(i);
                                Usuario pessoa = jsobjToUsuario(jsonKeyValue);
                                pessoas.add(pessoa);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("WBS", pessoas.toString());
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
        Log.d("return", values.toString());

        ArrayList<String> listNome = new ArrayList<String>();
        ArrayList<String> listImages = new ArrayList<String>();
        ArrayList<Integer> listMats = new ArrayList<Integer>();
        for (Usuario u : values) {
            listNome.add(u.getNome());
            listImages.add(u.getFoto());
            listMats.add(u.getMatricula());
        }

        ListCaronas adapter = new ListCaronas(listImages, listNome, listMats, mat, nome, this);
        listView.setAdapter(adapter);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPositionWs(Double a,Double b,String c,String d,String e){

    }
    public void caronasReceber_callback(JSONObject e){
        int matric = 0;
        JSONObject dias = null;
        JSONObject diax = null;
        String matricStr ="";

        for(int i=0;i<8;i++) {
            try{
                diax = null;
                dias = e.getJSONObject("match_req");
                diax = dias.getJSONObject(String.valueOf(i));
                matricStr += String.valueOf(diax.getInt("mat")) + ",";
            }catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        matricStr = matricStr.substring(0,matricStr.length()-1);

        buscaWs(matricStr);
    }

}