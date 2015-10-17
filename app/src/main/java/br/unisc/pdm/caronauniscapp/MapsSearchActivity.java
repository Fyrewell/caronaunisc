package br.unisc.pdm.caronauniscapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import br.unisc.pdm.caronauniscapp.webservice.RotaTela;
import br.unisc.pdm.caronauniscapp.webservice.RotaWebDao;

public class MapsSearchActivity extends FragmentActivity implements RotaTela{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker markerDest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps_search);

        setUpMapIfNeeded();
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
        Log.d("AA", "mark");
        mMap.addMarker(new MarkerOptions().position(new LatLng(-29.697666, -52.438677)).title("UNISC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-29.697666, -52.438677), 12));

        Intent rcv = getIntent();
        Bundle extras = rcv.getExtras();
        String mat = "";
        if (extras!=null) {
            mat = extras.getString("matricula");
        }
        new RotaWebDao(this).getRotaByMat(mat);
    }


    public void reqDestino(View v){
        EditText et = (EditText) findViewById(R.id.inpDestSearch);

        getLatLangByLocationString(et.getText().toString());

    }

    public boolean getLatLangByLocationString(String local){
        String loc = "";
        try {
            loc = URLEncoder.encode(local+", Santa Cruz do Sul - RS, Brazil", "UTF-8");
        }catch(UnsupportedEncodingException e){
            Toast.makeText(getBaseContext(),"Erro ao parsear url",Toast.LENGTH_SHORT).show();
        }
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + loc + "&key=AIzaSyC7uds535yTKywUfGkBgrvV2LT4MA7_2Pk";
        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=Rua%20Bahia,%2095,%20Ana%20Nery,%20Santa%20Cruz%20do%20Sul&key=AIzaSyC7uds535yTKywUfGkBgrvV2LT4MA7_2Pk";

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
                            if (markerDest==null) {
                                markerDest = mMap.addMarker(
                                        new MarkerOptions().position(new LatLng(LatLongObj.getDouble("lat"), LatLongObj.getDouble("lng")))
                                                .title(locSel)
                                                //.draggable(true)
                                                //.snippet("Segure e arraste para ajustar.")
                                );
                            }else{
                                markerDest.setPosition(new LatLng(LatLongObj.getDouble("lat"), LatLongObj.getDouble("lng")));
                                markerDest.setTitle(locSel);
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLongObj.getDouble("lat"), LatLongObj.getDouble("lng")), 12));
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
        new RotaWebDao(this).setRotaUsuario(mat,latlng.latitude,latlng.longitude,locDest);
        finish();
    }


    public void getPositionWs(Double lat, Double lng, String locDest) {
        Log.d("WS-rota", "cheguei");
        markerDest = mMap.addMarker(
                new MarkerOptions().position(new LatLng(lat, lng))
                        .title(locDest)
                        //.draggable(true)
                        //.snippet("Segure e arraste para ajustar.")
                        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));

        EditText et = (EditText) findViewById(R.id.inpDestSearch);
        et.setText(locDest);
    }


    public void dispose(View v){
        finish();
    }

}
