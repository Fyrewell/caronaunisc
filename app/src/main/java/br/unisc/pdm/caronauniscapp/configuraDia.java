package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import br.unisc.pdm.caronauniscapp.webservice.AgendaTela;
import br.unisc.pdm.caronauniscapp.webservice.AgendaWebDao;

/**
 * Usuario eh presentado para a marcacao da agenda de caronas, onde podra selecionar os dias
 * para a atual semana para quando podera disponibilizar carona ou para quando precisa de de carona,
 * informando o dia e turno para cada situacao.
 *
 * Usuarios que irao disponibilizar carona devem informar tbm o numero de lugares disponiveis.
 *
 * Usuario tem tambem a opcao de copiar a agenda da semana anteerior para repetir as mesmas
 * marcacoes ja feitas anteriormente.
 *
 * Created by Diego, Gabriel, Rafael
 */
public class configuraDia extends ActionBarActivity implements AgendaTela {

    private AgendaWebDao dao = new AgendaWebDao(this);
    private String mat = "";
    private String dia_click = "";
    private int usuariotipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configura_dia);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            mat = extras.getString("matricula");
            dia_click = extras.getString("VALUE_DIA_SEMANA");
            usuariotipo = extras.getInt("usuariotipo");
            labelarDia();
        }

        RadioButton rbrec = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rbdar = (RadioButton) findViewById(R.id.radio_dar);
        if (usuariotipo == 1){
            rbrec.setVisibility(View.INVISIBLE);
        }else if (usuariotipo == 2){
            RadioButton qtd1 = (RadioButton) findViewById(R.id.radio_carona_qtd1);
            RadioButton qtd2 = (RadioButton) findViewById(R.id.radio_carona_qtd2);
            RadioButton qtd3 = (RadioButton) findViewById(R.id.radio_carona_qtd3);
            RadioButton qtd4 = (RadioButton) findViewById(R.id.radio_carona_qtd4);
            TextView txtqtd = (TextView) findViewById(R.id.txt_qtd_passageiros);
            rbdar.setVisibility(View.INVISIBLE);
            qtd1.setVisibility(View.INVISIBLE);
            qtd2.setVisibility(View.INVISIBLE);
            qtd3.setVisibility(View.INVISIBLE);
            qtd4.setVisibility(View.INVISIBLE);
            txtqtd.setVisibility(View.INVISIBLE);
            txtqtd.setText("");
        }

        rbrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton qtd1 = (RadioButton) findViewById(R.id.radio_carona_qtd1);
                RadioButton qtd2 = (RadioButton) findViewById(R.id.radio_carona_qtd2);
                RadioButton qtd3 = (RadioButton) findViewById(R.id.radio_carona_qtd3);
                RadioButton qtd4 = (RadioButton) findViewById(R.id.radio_carona_qtd4);
                TextView txtqtd = (TextView) findViewById(R.id.txt_qtd_passageiros);
                qtd1.setVisibility(View.INVISIBLE);
                qtd2.setVisibility(View.INVISIBLE);
                qtd3.setVisibility(View.INVISIBLE);
                qtd4.setVisibility(View.INVISIBLE);
                txtqtd.setVisibility(View.INVISIBLE);
                qtd1.setChecked(false);
                qtd2.setChecked(false);
                qtd3.setChecked(false);
                qtd4.setChecked(false);
            }
        });

        rbdar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtqtd = (TextView) findViewById(R.id.txt_qtd_passageiros);
                RadioButton qtd1 = (RadioButton) findViewById(R.id.radio_carona_qtd1);
                RadioButton qtd2 = (RadioButton) findViewById(R.id.radio_carona_qtd2);
                RadioButton qtd3 = (RadioButton) findViewById(R.id.radio_carona_qtd3);
                RadioButton qtd4 = (RadioButton) findViewById(R.id.radio_carona_qtd4);
                qtd1.setVisibility(View.VISIBLE);
                qtd2.setVisibility(View.VISIBLE);
                qtd3.setVisibility(View.VISIBLE);
                qtd4.setVisibility(View.VISIBLE);
                txtqtd.setVisibility(View.VISIBLE);
            }
        });

    }

    public void labelarDia(){
        TextView et = (TextView) findViewById(R.id.cfgdia_data_titulo);
        String lbl = "";
        switch (Integer.parseInt(dia_click)){
            case 1: lbl = "Domingo"; break;
            case 2: lbl = "Segunda-feira"; break;
            case 3: lbl = "Terca-feira"; break;
            case 4: lbl = "Quarta-feira"; break;
            case 5: lbl = "Quinta-feira"; break;
            case 6: lbl = "Sexta-feira"; break;
            case 7: lbl = "Sabado"; break;
        }
        et.setText(lbl);
        dao.getAgendaByMat(mat,dia_click);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configura_dia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_accept_dia){
            gravarDiaAgenda();
            return true;
        }else if (id == R.id.action_exclui_dia) {
            dao.clrAgendaDia(mat, dia_click);
            return true;
        }else if (id == android.R.id.home){
            Intent intent = new Intent();
            intent.putExtra("matricula",mat);
            intent.putExtra("diaatualiza",dia_click);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gravarDiaAgenda(){
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_manha = (RadioButton) findViewById(R.id.radio_manha);
        RadioButton rb_tarde = (RadioButton) findViewById(R.id.radio_tarde);
        RadioButton rb_noite = (RadioButton) findViewById(R.id.radio_noite);
        RadioButton rb_qtd1 = (RadioButton) findViewById(R.id.radio_carona_qtd1);
        RadioButton rb_qtd2 = (RadioButton) findViewById(R.id.radio_carona_qtd2);
        RadioButton rb_qtd3 = (RadioButton) findViewById(R.id.radio_carona_qtd3);
        RadioButton rb_qtd4 = (RadioButton) findViewById(R.id.radio_carona_qtd4);
        int ctipo = 0;
        if(rb_dar.isChecked()) ctipo = 1;
        if(rb_receber.isChecked()) ctipo = 2;
        String turno = "";
        if(rb_manha.isChecked()) turno = "M";
        if(rb_tarde.isChecked()) turno = "T";
        if(rb_noite.isChecked()) turno = "N";
        int qtd = 0;
        if(rb_qtd1.isChecked()) qtd = 1;
        if(rb_qtd2.isChecked()) qtd = 2;
        if(rb_qtd3.isChecked()) qtd = 3;
        if(rb_qtd4.isChecked()) qtd = 4;

        if (ctipo==0||(ctipo==1&&(turno.equals("")||ctipo==0||qtd==0))||(ctipo==2&&(turno.equals("")||ctipo==0))){
            Toast.makeText(this, "Entrar com todos os dados!", Toast.LENGTH_SHORT).show();
            return;
        }

        dao.setAgendaDia(mat, dia_click, ctipo, turno, qtd);
    }

    public void agendaAllItens(int tipo, JSONArray agItens){
    }

    public void agendaItem(String dataF, int tipo, String turno, int qtd){
        TextView et = (TextView) findViewById(R.id.cfgdia_data_titulo);
        et.setText(et.getText()+" - "+dataF);
        RadioButton rb_dar = (RadioButton) findViewById(R.id.radio_dar);
        RadioButton rb_receber = (RadioButton) findViewById(R.id.radio_receber);
        RadioButton rb_manha = (RadioButton) findViewById(R.id.radio_manha);
        RadioButton rb_tarde = (RadioButton) findViewById(R.id.radio_tarde);
        RadioButton rb_noite = (RadioButton) findViewById(R.id.radio_noite);
        RadioButton rb_qtd1 = (RadioButton) findViewById(R.id.radio_carona_qtd1);
        RadioButton rb_qtd2 = (RadioButton) findViewById(R.id.radio_carona_qtd2);
        RadioButton rb_qtd3 = (RadioButton) findViewById(R.id.radio_carona_qtd3);
        RadioButton rb_qtd4 = (RadioButton) findViewById(R.id.radio_carona_qtd4);
        if(turno.equals("M"))
            rb_manha.setChecked(true);
        if(turno.equals("T"))
            rb_tarde.setChecked(true);
        if(turno.equals("N"))
            rb_noite.setChecked(true);
        if(tipo==1)
            rb_dar.setChecked(true);
        if(tipo==2)
            rb_receber.setChecked(true);
        if(qtd==1)
            rb_qtd1.setChecked(true);
        if(qtd==2)
            rb_qtd2.setChecked(true);
        if(qtd==3)
            rb_qtd3.setChecked(true);
        if(qtd==4)
            rb_qtd4.setChecked(true);
    }

    public void setAgendaDia_callback(){
        Intent intent = new Intent();
        intent.putExtra("matricula",mat);
        intent.putExtra("diaatualiza",dia_click);
        intent.putExtra("usuariotipo",usuariotipo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
