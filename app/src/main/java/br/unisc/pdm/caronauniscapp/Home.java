package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class Home extends ActionBarActivity {
    String mat = "";
    String sexo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f1e79")));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        if (extras!=null) {
            Log.d("testReceb", extras.getString("matricula"));
            Log.d("testReceb", extras.getString("sexo"));
            mat = extras.getString("matricula");
            sexo = extras.getString("sexo");
            if (sexo.equals("Feminino")) {
                ImageButton btn = (ImageButton)findViewById(R.id.btn_open_voce);
                btn.setImageResource(R.drawable.icon_open_vocef);
            }
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

        if (id == R.id.action_cadastro) {
            openVoce(findViewById(R.id.action_cadastro));
        }

        if (id == R.id.action_map) {
            openRota(findViewById(R.id.action_map));
        }

        if (id == R.id.action_agenda) {
            openAgenda(findViewById(R.id.action_agenda));
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
