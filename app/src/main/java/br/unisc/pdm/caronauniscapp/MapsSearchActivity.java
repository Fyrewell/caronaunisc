package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import br.unisc.pdm.caronauniscapp.webservice.RotaTela;
import br.unisc.pdm.caronauniscapp.webservice.RotaWebDao;

/**
 * Metodos usados para a manipulacao do mapa e da rota, setando o mapa,
 * buscando o ponto que o usuario marcar e salvando a rota marcada.
 */
public class MapsSearchActivity extends FragmentActivity implements RotaTela{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int qtdMarkers = 0;
    private List<LatLng> markers;
    String origem = "";
    private Marker markerDest;
    private List<String> polylinesAll;
    private List<LatLng> positionsAll;
    private LatLng cordDest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps_search);
        setUpMapIfNeeded();
        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        String mat = "";
        if (extras!=null) {
            mat = extras.getString("matricula");

            String target_usr_mat = extras.getString("target_usr_mat");
            if (target_usr_mat!=null){
                EditText et1 = (EditText) findViewById(R.id.inpDestSearch);
                TextView tv1 = (TextView) findViewById(R.id.textView2);
                Button b1 = (Button) findViewById(R.id.button);
                Button b2 = (Button) findViewById(R.id.button2);
                Button b3 = (Button) findViewById(R.id.button3);
                Button b4 = (Button) findViewById(R.id.button4);
                et1.setVisibility(View.GONE); tv1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE); b2.setVisibility(View.GONE);
                b3.setVisibility(View.GONE); b4.setText("Voltar");
                new RotaWebDao(this).getRotaByMat(target_usr_mat);
            }
        }
        new RotaWebDao(this).getRotaByMat(mat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        addUnisc();
        centerUnisc();
        markers = new ArrayList<>();
        polylinesAll = new ArrayList<>();
        positionsAll = new ArrayList<>();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (qtdMarkers < 8) {
                    if (!origem.equals("")) {
                        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                        qtdMarkers++;
                        markers.add(latLng);
                        getDirections();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Limite de caminhos na rota atingido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addUnisc(){
        mMap.addMarker(new MarkerOptions().position(new LatLng(-29.697666, -52.438677)).title("UNISC"));
    }
    public void centerUnisc(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-29.697666, -52.438677), 14));
    }
    public void addDest(){
        mMap.addMarker(new MarkerOptions().position(cordDest).title(origem));
    }
    public void addWaypoints(){
        for (int i = 0; i < qtdMarkers; i++) {
            mMap.addMarker(new MarkerOptions().position(markers.get(i)));
        }
    }
    public void reqDestino(View v){
        qtdMarkers = 0;
        markers = new ArrayList<>();
        polylinesAll = new ArrayList<>();
        positionsAll = new ArrayList<>();
        EditText et = (EditText) findViewById(R.id.inpDestSearch);
        getLatLangByLocationString(et.getText().toString());
    }

    public void voltaWaypoints(View v){
        if (qtdMarkers>0) {
            qtdMarkers--;
            markers.remove(qtdMarkers);
            getDirections();
        }
    }

    /**
     * Obtem o ponto no plano cartesiano (lat,long) que representa o local marcado pelo usuario.
     *
     * PS: se marcado ponto em SCS, basta colocar o nome da rua, para localizacoes em outras cidades
     * precisa-se colocar endereco completo para marcar corretamente.
     *
     * @param local
     * @return
     */
    public boolean getDirections(){

        String wp = "";
        String origStr = "";
        try {
            origStr = URLEncoder.encode(origem, "UTF-8");
        }catch(UnsupportedEncodingException e){
            Toast.makeText(getBaseContext(),"Erro ao parsear url",Toast.LENGTH_SHORT).show();
        }

        if (qtdMarkers>0) {
            int i;
            wp = "&waypoints=optimize:true";
            for (i = 0; i < qtdMarkers; i++) {
                wp += "|" + markers.get(i).latitude + "," + markers.get(i).longitude;
            }
        }
        String url = "https://maps.googleapis.com/maps/api/directions/json?destination=-29.697666,-52.438677&origin="+origStr+wp+"&key=AIzaSyC7uds535yTKywUfGkBgrvV2LT4MA7_2Pk";
        Log.d("URLDIRECTIONS",url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Log.d("retornoDirections", response.toString());
                            Log.d("retornoDirectionsConv", response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").toString());
                            JSONArray ar = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                            JSONArray stp;
                            String pline = "",poslat = "",poslng = "";
                            List<LatLng> decodedPath;
                            mMap.clear();
                            addUnisc();
                            addDest();
                            polylinesAll = new ArrayList<>();
                            for (int j=0;j<ar.length();j++){
                                stp = ar.getJSONObject(j).getJSONArray("steps");
                                for (int k=0;k<stp.length();k++){
                                    pline = stp.getJSONObject(k).getJSONObject("polyline").getString("points");
                                    poslat = stp.getJSONObject(k).getJSONObject("start_location").getString("lat");
                                    poslng = stp.getJSONObject(k).getJSONObject("start_location").getString("lng");
                                    polylinesAll.add(pline);
                                    positionsAll.add((new LatLng(Double.parseDouble(poslat),Double.parseDouble(poslng))));
                                    decodedPath = PolyUtil.decode(pline);
                                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(4).color(Color.BLUE - 0x77000000));
                                }
                            }
                        }catch (JSONException j){

                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getBaseContext(),"Problema ao executar sua solicitacao",Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        queue.add(jsObjRequest);
        return true;
    }

    /**
     * Obtem o ponto no plano cartesiano (lat,long) que representa o local marcado pelo usuario.
     *
     * PS: se marcado ponto em SCS, basta colocar o nome da rua, para localizacoes em outras cidades
     * precisa-se colocar endereco completo para marcar corretamente.
     *
     * @param local
     * @return
     */
    public boolean getLatLangByLocationString(String local){
        String loc = "";
        try {
            loc = URLEncoder.encode(local+", Santa Cruz do Sul - RS, Brazil", "UTF-8");
        }catch(UnsupportedEncodingException e){
            Toast.makeText(getBaseContext(),"Erro ao parsear url",Toast.LENGTH_SHORT).show();
        }
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + loc + "&key=AIzaSyC7uds535yTKywUfGkBgrvV2LT4MA7_2Pk";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            JSONObject LatLongObj = response.getJSONArray("results")
                                    .getJSONObject(0)
                                    .getJSONObject("geometry")
                                    .getJSONObject("location");
                            String locSel = response.getJSONArray("results")
                                    .getJSONObject(0)
                                    .getString("formatted_address");
                            cordDest = new LatLng(LatLongObj.getDouble("lat"), LatLongObj.getDouble("lng"));
                            markerDest = mMap.addMarker(
                                    new MarkerOptions().position(cordDest)
                                            .title(locSel)
                                    //.draggable(true)
                                    //.snippet("Segure e arraste para ajustar.")
                            );
                            origem = locSel;
                            getDirections();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLongObj.getDouble("lat"), LatLongObj.getDouble("lng")), 14));
                        }catch (JSONException E){
                            Toast.makeText(getBaseContext(),"Erro parsear retorno json obj",Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getBaseContext(),"Problema ao executar sua solicitacao",Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        queue.add(jsObjRequest);
        return true;
    }

    private void traceExistingRoute(){

    }

    /**
     * Grava ponto de partida para a rota ate a UNISC marcada pelo usuario
     * @param v
     */
    public void gravarDestino(View v){
        LatLng latlng = markerDest.getPosition();
        String locDest = markerDest.getTitle();
        //request para salvar rota

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        String mat = "";
        if (extras!=null) {
            mat = extras.getString("matricula");
        }
        String latlags = "";
        for (LatLng ltl : markers) {
            latlags+= ltl.latitude+","+ltl.longitude+"|";
        }
        String pol = "";
        for (String p : polylinesAll) {
            pol+= p+", ";
        }
        String pos = "";
        for (LatLng ltl : positionsAll) {
            pos+= ltl.latitude+","+ltl.longitude+"|";
        }
        new RotaWebDao(this).setRotaUsuario(mat,latlng.latitude,latlng.longitude,locDest,latlags,pol,pos);
        finish();
    }


    public void getPositionWs(Double lat, Double lng, String locDest, String wps, String lines) {
        Log.d("WS-rota", "cheguei");
        markerDest = mMap.addMarker(
                new MarkerOptions().position(new LatLng(lat, lng))
                        .title(locDest)
                //.draggable(true)
                //.snippet("Segure e arraste para ajustar.")
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 13));

        cordDest = new LatLng(lat, lng);
        origem = locDest;

        if (wps.length()>5) {
            String arrltlngk[] = wps.split("\\|");
            qtdMarkers = arrltlngk.length;
            for (int k = 0; k < arrltlngk.length; k++) {
                markers.add(new LatLng(Double.parseDouble(arrltlngk[k].split(",")[0]), Double.parseDouble(arrltlngk[k].split(",")[1])));
                Log.d("HAHA", markers.get(k).toString());
            }
        }

        if (lines.length()>5) {
            String a[] = lines.split(", ");

            String pline = "";
            List<LatLng> decodedPath;
            for (int k = 0; k < a.length; k++) {
                pline = a[k];
                polylinesAll.add(a[k]);
                decodedPath = PolyUtil.decode(pline);
                mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(4).color(Color.BLUE - 0x77000000));
            }
        }
        EditText et = (EditText) findViewById(R.id.inpDestSearch);
        et.setText(locDest);
    }


    public void dispose(View v){
        finish();
    }

    public void caronasReceber_callback(JSONObject e) {
    }
    public void caronasDar_callback(JSONObject e) {
    }

}
