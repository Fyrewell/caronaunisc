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
        String url = baseurl+"/agenda/diasemana/"+mat+"/"+dia;
        Log.d("WBS", "URL: " + url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", response.toString());
                        String dataT;
                        int tipo=0; String turno=""; int qtd=0;
                        try {
                            dataT = response.getString("dataT");
                            if (!response.isNull("dados")) {
                                tipo = response.getJSONObject("dados").getInt("ag_tipo");
                                turno = response.getJSONObject("dados").getString("ag_turno");
                                qtd = response.getJSONObject("dados").getInt("ag_qtd");
                            }
                            tela.agendaItem(dataT,tipo,turno,qtd);
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

    public void getAgendaAllItensByMat(String mat){
        String url = baseurl+"/agenda/diasemana/"+mat;
        Log.d("WBS", "URL: " + url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", response.toString());
                        int cad_tipo=0;
                        try {
                            cad_tipo = response.getInt("usuario_tipo");
                            tela.agendaAllItens(cad_tipo, response.getJSONArray("dados"));
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
                            tela.setAgendaDia_callback();
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

    public void copiarAgendaSemanaAnterior(String mat){
        String url = baseurl+"/agenda/copiar_anterior";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("matricula",mat);
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
                            tela.setAgendaDia_callback();
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

    public void clrAgendaDia(String mat, String dia){
        String url = baseurl+"/agenda/diasemana/"+mat+"/"+dia;
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("matricula",mat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("WBS",jsonBody.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", "Retornou do request!");
                        Log.d("WBS", response.toString());
                        try {
                            Log.d("WBS", response.getString("result"));
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                            tela.setAgendaDia_callback();
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
