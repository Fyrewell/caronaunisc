package br.unisc.pdm.caronauniscapp.webservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Diego on 17/10/2015.
 */
public class AgendaWebDao {
    public Context context;
    public AgendaTela tela;
    public String baseurl = "http://caronaunisc.herokuapp.com/api";

    public AgendaWebDao (AgendaTela t){
        this.context = (Context) t;
        this.tela = t;
    }

    public void getAgendaByMat(String mat, String dia){
        String url = baseurl+"/agenda/diasemana/"+dia;
        Log.d("WBS", "URL: " + url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", response.toString());
                        String dataT;
                        try {
                            dataT = response.getString("dataT");
                            tela.agendaItem(dataT);
                        }catch(JSONException e){
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(context, "Problema ao executar sua solicitacao", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }


    public void setAgendaDia(String mat, String dia, int tipo, String turno, int qtd){
        String url = baseurl+"/agenda/diasemana/"+dia;
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("matricula",mat);
            jsonBody.put("ag_tipo",tipo);
            jsonBody.put("ag_turno",turno);
            jsonBody.put("ag_qtd",qtd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("WBS",jsonBody.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", "Retornou do request!");
                        Log.d("WBS", response.toString());
                        try {
                            Log.d("WBS", response.getString("result"));
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("WBS","caiu no onErrorResponse");
                        Log.d("WBS", error.toString());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }
}
