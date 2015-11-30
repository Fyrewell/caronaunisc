package br.unisc.pdm.caronauniscapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import br.unisc.pdm.caronauniscapp.database.Usuario;
import br.unisc.pdm.caronauniscapp.webservice.RotaTela;
import br.unisc.pdm.caronauniscapp.webservice.RotaWebDao;

/**
 * Diego, Gabriel, Rafael
 */
public class CaronasActivity extends TabActivity implements RotaTela {

    ListView listView;
    ListView listView1;
    private String mat = "";
    private String nome = "";
    private int usuario_tipo = 0;

    //repopular na mudanca tab
    ArrayList<Integer> ListDist = new ArrayList<Integer>();
    ArrayList<Integer> ListAgFornece = new ArrayList<Integer>();
    ArrayList<Integer> ListAgRecebe = new ArrayList<Integer>();
    ArrayList<Integer> ListStatus = new ArrayList<Integer>();
    ArrayList<String> ListDias = new ArrayList<String>();
    private int tipo = 1;
    ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caronas);

        this.listView = (ListView) findViewById(R.id.tab0);
        this.listView1 = (ListView) findViewById(R.id.tab1);

        pgBar = (ProgressBar) findViewById(R.id.progressBar);

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

        getTabHost().getTabWidget().getChildAt(0).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDist = new ArrayList<Integer>();
                        ListAgFornece = new ArrayList<Integer>();
                        ListAgRecebe = new ArrayList<Integer>();
                        ListStatus = new ArrayList<Integer>();
                        ListDias = new ArrayList<String>();
                        tipo = 1;

                        new RotaWebDao((CaronasActivity)v.getContext()).caronasReceber(mat);
                        getTabHost().setCurrentTab(0);
                        pgBar.setVisibility(View.VISIBLE);
                    }
                }
        );
        getTabHost().getTabWidget().getChildAt(1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDist = new ArrayList<Integer>();
                        ListAgFornece = new ArrayList<Integer>();
                        ListAgRecebe = new ArrayList<Integer>();
                        ListStatus = new ArrayList<Integer>();
                        ListDias = new ArrayList<String>();
                        tipo = 2;

                        new RotaWebDao((CaronasActivity)v.getContext()).caronasDar(mat);
                        getTabHost().setCurrentTab(1);
                        pgBar.setVisibility(View.VISIBLE);
                    }
                }
        );

        getTabHost().setCurrentTab(0);

        new RotaWebDao(this).caronasReceber(mat);
        listView.setAdapter(new ListCaronas());
        listView1.setAdapter(new ListCaronas());
    }

    public void buscaWs(String nome) {
        try {
            nome = URLEncoder.encode(nome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getBaseContext(), "Erro ao parsear url", Toast.LENGTH_SHORT).show();
        }
        String url = "http://caronaunisc.herokuapp.com/api/usuario/" + nome;

        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Usuario> pessoas = new ArrayList<Usuario>();

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

        ArrayList<Integer> locListDist = ListDist;
        ArrayList<Integer> locListAgFornece = ListAgFornece;
        ArrayList<Integer> locListAgRecebe = ListAgRecebe;
        ArrayList<Integer> locListStatus = ListStatus;
        ArrayList<String> locListDias = ListDias;

        ListCaronas adapter = new ListCaronas(listImages, listNome, listMats, locListDist, locListAgRecebe, locListAgFornece, locListStatus, locListDias, tipo, mat, nome, this);
        if (tipo==1) {
            listView.setAdapter(adapter);
        }else{
            listView1.setAdapter(adapter);
        }

        pgBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void getPositionWs(Double a,Double b,String c,String d,String e){

    }
    public void caronasReceber_callback(JSONObject e){
        JSONObject dias = null;
        JSONObject diax = null;
        String matricStr ="";

        for(int i=0;i<8;i++) {
            try{
                dias = e.getJSONObject("match_req");
                for (int x=0;x<dias.getJSONArray(String.valueOf(i)).length();x++){
                    diax = dias.getJSONArray(String.valueOf(i)).getJSONObject(x);
                    matricStr += String.valueOf(diax.getInt("mat")) + ",";
                    ListDist.add(diax.getInt("dist"));
                    ListAgFornece.add(diax.getInt("ag_id_fornece"));
                    ListAgRecebe.add(diax.getInt("ag_id_recebe"));
                    ListStatus.add(diax.getInt("status"));
                    ListDias.add(diax.getString("dia"));
                }
            }catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        if (matricStr.length()>0) {
            matricStr = matricStr.substring(0, matricStr.length() - 1);

            buscaWs(matricStr);
        }
    }

    public void caronasDar_callback(JSONObject e){
        JSONObject dias = null;
        JSONObject diax = null;
        String matricStr ="";

        for(int i=0;i<8;i++) {
            try{
                dias = e.getJSONObject("match_resp");
                for (int x=0;x<dias.getJSONArray(String.valueOf(i)).length();x++){
                    diax = dias.getJSONArray(String.valueOf(i)).getJSONObject(x);
                    matricStr += String.valueOf(diax.getInt("mat")) + ",";
                    ListDist.add(diax.getInt("dist"));
                    ListAgFornece.add(diax.getInt("ag_id_fornece"));
                    ListAgRecebe.add(diax.getInt("ag_id_recebe"));
                    ListStatus.add(diax.getInt("status"));
                    ListDias.add(diax.getString("dia"));
                }
            }catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        if (matricStr.length()>0) {
            matricStr = matricStr.substring(0, matricStr.length() - 1);

            buscaWs(matricStr);
        }
    }

}