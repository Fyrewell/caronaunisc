package br.unisc.pdm.caronauniscapp.webservice;

import android.content.Context;
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
 * Metodos webservice para gravar uma nova rota ou obter uma rota ja gravado por um usuario.
 *
 * Created by Diego, Gabriel, Rafael on 12/10/2015.
 */
public class RotaWebDao {
    public Context context;
    public RotaTela tela;
    public String baseurl = "http://caronaunisc.herokuapp.com/api";

    public RotaWebDao (RotaTela t){
        this.context = (Context) t;
        this.tela = t;
    }

    public void getRotaByMat(String mat){
        String url = baseurl+"/rota/"+mat;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        double lat, lng;
                        String locDest,wps,lines;
                        try {
                            lat = response.getDouble("lat");
                            lng = response.getDouble("lng");
                            locDest = response.getString("locnome");
                            wps = response.getString("wps");
                            lines = response.getString("lines");
                            if (locDest.length() > 0) {
                                tela.getPositionWs(lat, lng, locDest,wps,lines);
                            }
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


    public void setRotaUsuario(String mat,double lat,double lng, String locDest, String wps,String linhas,String pos){
        String url = baseurl+"/rota";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("matricula",mat);
            jsonBody.put("lat",lat);
            jsonBody.put("lng",lng);
            jsonBody.put("locnome",locDest);
            jsonBody.put("wps",wps);
            jsonBody.put("linhas",linhas);
            jsonBody.put("positions",pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void caronasReceber(String mat){
        String url = baseurl+"/rotas/caronasReceber/"+mat;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        tela.caronasReceber_callback(response);
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


    public void caronasDar(String mat){
        String url = baseurl+"/rotas/caronasDar/"+mat;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        tela.caronasDar_callback(response);
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

    public void caronasEscolher(int ag_id_fornece,int ag_id_recebe,int dist){
        String url = baseurl+"/rota/caronasEscolher";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mch_ag_id_fornece",ag_id_fornece);
            jsonBody.put("mch_ag_id_recebe",ag_id_recebe);
            jsonBody.put("mch_dist",dist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void caronasAceitar(int ag_id_fornece,int ag_id_recebe,int dist){
        String url = baseurl+"/rota/caronasAceitar";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mch_ag_id_fornece",ag_id_fornece);
            jsonBody.put("mch_ag_id_recebe",ag_id_recebe);
            jsonBody.put("mch_dist",dist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void caronasRecusar(int ag_id_fornece,int ag_id_recebe,int dist){
        String url = baseurl+"/rota/caronasRecusar";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mch_ag_id_fornece",ag_id_fornece);
            jsonBody.put("mch_ag_id_recebe",ag_id_recebe);
            jsonBody.put("mch_dist",dist);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }
}
