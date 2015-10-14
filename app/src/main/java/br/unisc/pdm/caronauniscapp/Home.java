package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        if (extras!=null) {
            Log.d("testReceb", extras.getString("matricula"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_list) {
//            startActivity(new Intent(this,ListViewLoader.class));
//        }
        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        String mat = "";
        if (extras!=null) {
            mat = extras.getString("matricula");
        }
        if (id == R.id.action_cadastro) {
            Intent formAct = new Intent(this,FormUsuario.class);
            formAct.putExtra("matricula",mat);
            startActivity(formAct);
        }

        if (id == R.id.action_map) {
            Intent mapAct = new Intent(this,MapsSearchActivity.class);
            mapAct.putExtra("matricula",mat);
            startActivity(mapAct);
        }

        if (id == R.id.action_agenda) {
            Intent agAct = new Intent(this,Agenda.class);
            agAct.putExtra("matricula",mat);
            startActivity(agAct);
        }

        return super.onOptionsItemSelected(item);
    }
}
