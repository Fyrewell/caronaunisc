package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import br.unisc.pdm.caronauniscapp.webservice.AgendaTela;
import br.unisc.pdm.caronauniscapp.webservice.AgendaWebDao;

public class Agenda extends ActionBarActivity implements AgendaTela {

    private String mat = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();

        if (extras!=null) {
            mat = extras.getString("matricula");
        }

        Log.d("MAT", "trouxe a matricula " + mat);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int diaClicado = (position % 7)+1;

                configurarDia(diaClicado);

                Toast.makeText(Agenda.this, "clicado" + position,
                        Toast.LENGTH_SHORT).show();
            }
    });

        gridview.setBackgroundColor(Color.WHITE);
        gridview.setVerticalSpacing(0);
        gridview.setHorizontalSpacing(0);
    }

    public void configurarDia(int dia){
        Intent cfgDia = new Intent(this, configuraDia.class);
        cfgDia.putExtra("VALUE_DIA_SEMANA",Integer.toString(dia));
        cfgDia.putExtra("matricula",this.mat);
        this.startActivity(cfgDia);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
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

    public void agendaAllItens(){
    }

    public void agendaItem(String dataF){
    }

}
