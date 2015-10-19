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
 * Metodos webservice para gravar uma nova rota ou obter uma rota ja gravado por um usuario.
 *
 * Created by Diego on 12/10/2015.
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
        Log.d("WBS", "URL: " + url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", response.toString());
                        double lat, lng;
                        String locDest;
                        try {
                            lat = response.getDouble("lat");
                            lng = response.getDouble("lng");
                            locDest = response.getString("locnome");
                            if (locDest.length() > 0) {
                                tela.getPositionWs(lat, lng, locDest);
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


    public void setRotaUsuario(String mat,double lat,double lng, String locDest){
        String url = baseurl+"/rota";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("matricula",mat);
            jsonBody.put("lat",lat);
            jsonBody.put("lng",lng);
            jsonBody.put("locnome",locDest);
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
