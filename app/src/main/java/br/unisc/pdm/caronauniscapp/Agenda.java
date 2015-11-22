package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.unisc.pdm.caronauniscapp.webservice.AgendaTela;
import br.unisc.pdm.caronauniscapp.webservice.AgendaWebDao;

public class Agenda extends ActionBarActivity implements AgendaTela {

    private AgendaWebDao dao = new AgendaWebDao(this);
    private String mat = "";
    int diaClicado = 0;
    int usuariotipo = 0;
    private GridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras != null) {
            mat = extras.getString("matricula");
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int diaClicado = (position % 7) + 1;

                configurarDia(diaClicado);
            }
        });

        gridview.setBackgroundColor(Color.WHITE);
        gridview.setVerticalSpacing(0);
        gridview.setHorizontalSpacing(0);

        dao.getAgendaAllItensByMat(mat);
    }

    public void configurarDia(int dia) {
        Intent cfgDia = new Intent(this, configuraDia.class);
        cfgDia.putExtra("VALUE_DIA_SEMANA", Integer.toString(dia));
        cfgDia.putExtra("matricula", this.mat);
        cfgDia.putExtra("usuariotipo", this.usuariotipo);
        this.startActivityForResult(cfgDia, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent intent = new Intent();
            intent.putExtra("matricula",mat);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void agendaAllItens(int tipo, JSONArray agItens){
        int i;
        JSONObject ob;
        ImageAdapter imgAdp = (ImageAdapter) gridview.getAdapter();
        for (i = 1; i < 8; i++) {
            try {
                ob = agItens.getJSONObject(i - 1);
                imgAdp = getAgendaView(imgAdp, Integer.parseInt(ob.get("ag_diasemana").toString()), Integer.parseInt(ob.get("ag_tipo").toString()), ob.get("ag_turno").toString(), Integer.parseInt(ob.get("ag_qtd").toString()));
            }catch(JSONException e){
            }
        }
        usuariotipo = tipo;
        gridview.setAdapter(imgAdp);
    }

    public void agendaItem(String dataBaseLivre, int tipo, String turno, int qtd) {
        ImageAdapter imgAdp = (ImageAdapter) gridview.getAdapter();
        imgAdp = getAgendaView(imgAdp,diaClicado,tipo,turno,qtd);
        gridview.setAdapter(imgAdp);
    }

    public ImageAdapter getAgendaView(ImageAdapter imgAdp, int inpDia, int tipo, String turno, int qtd){

        switch (inpDia){
            case 1:
                imgAdp.mThumbIds[7] = getImgTipo(tipo);
                imgAdp.mThumbIds[14] = getImgTurno(turno);
                imgAdp.mThumbIds[21] = getImgQtd(qtd);
                imgAdp.getView(7,null,null);
                imgAdp.getView(14,null,null);
                imgAdp.getView(21,null,null);
                break;
            case 2:
                imgAdp.mThumbIds[8] = getImgTipo(tipo);
                imgAdp.mThumbIds[15] = getImgTurno(turno);
                imgAdp.mThumbIds[22] = getImgQtd(qtd);
                imgAdp.getView(8,null,null);
                imgAdp.getView(15,null,null);
                imgAdp.getView(22,null,null);
                break;
            case 3:
                imgAdp.mThumbIds[9] = getImgTipo(tipo);
                imgAdp.mThumbIds[16] = getImgTurno(turno);
                imgAdp.mThumbIds[23] = getImgQtd(qtd);
                imgAdp.getView(9,null,null);
                imgAdp.getView(16,null,null);
                imgAdp.getView(23,null,null);
                break;
            case 4:
                imgAdp.mThumbIds[10] = getImgTipo(tipo);
                imgAdp.mThumbIds[17] = getImgTurno(turno);
                imgAdp.mThumbIds[24] = getImgQtd(qtd);
                imgAdp.getView(10,null,null);
                imgAdp.getView(17,null,null);
                imgAdp.getView(24,null,null);
                break;
            case 5:
                imgAdp.mThumbIds[11] = getImgTipo(tipo);
                imgAdp.mThumbIds[18] = getImgTurno(turno);
                imgAdp.mThumbIds[25] = getImgQtd(qtd);
                imgAdp.getView(11,null,null);
                imgAdp.getView(18,null,null);
                imgAdp.getView(25,null,null);
                break;
            case 6:
                imgAdp.mThumbIds[12] = getImgTipo(tipo);
                imgAdp.mThumbIds[19] = getImgTurno(turno);
                imgAdp.mThumbIds[26] = getImgQtd(qtd);
                imgAdp.getView(12,null,null);
                imgAdp.getView(19,null,null);
                imgAdp.getView(26,null,null);
                break;
            case 7:
                imgAdp.mThumbIds[13] = getImgTipo(tipo);
                imgAdp.mThumbIds[20] = getImgTurno(turno);
                imgAdp.mThumbIds[27] = getImgQtd(qtd);
                imgAdp.getView(13,null,null);
                imgAdp.getView(20,null,null);
                imgAdp.getView(27,null,null);
                break;
        }
        return imgAdp;
    }

    public int getImgTipo(int tipo){
        switch(tipo){
            case 0: return R.drawable.tipo;
            case 1: return R.drawable.agdar;
            case 2: return R.drawable.agrec;
            default: return R.drawable.agblank;
        }
    }
    public int getImgTurno(String turno){
        if(turno.equals("")){
            return R.drawable.turno;
        }else if(turno.equals("M")) {
            return R.drawable.agm;
        }else if (turno.equals("T")){
            return R.drawable.agt;
        }else if (turno.equals("N")){
            return R.drawable.agn;
        }else{
            return R.drawable.agblank;
        }
    }

    public int getImgQtd(int qtd){
        switch(qtd){
            case 1: return R.drawable.ag1;
            case 2: return R.drawable.ag2;
            case 3: return R.drawable.ag3;
            case 4: return R.drawable.ag4;
            default: return R.drawable.agblank;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mat = data.getStringExtra("matricula");
                diaClicado = Integer.parseInt(data.getStringExtra("diaatualiza"));
                dao.getAgendaByMat(mat,String.valueOf(diaClicado));
            }
        }
    }

    public void setAgendaDia_callback(){
        dao.getAgendaAllItensByMat(mat);
    }

    public void copiarRoteiroSemanaPassada(View v) {
        dao.copiarAgendaSemanaAnterior(mat);
    }

}