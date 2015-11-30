package br.unisc.pdm.caronauniscapp.webservice;

import org.json.JSONObject;

/**
 * Created by Diego, Gabriel, Rafael on 12/10/2015.
 */
public interface RotaTela {
    void getPositionWs(Double Lat, Double Lng, String locDest, String wps, String lines);
    void caronasReceber_callback(JSONObject users);
    void caronasDar_callback(JSONObject users);
}
