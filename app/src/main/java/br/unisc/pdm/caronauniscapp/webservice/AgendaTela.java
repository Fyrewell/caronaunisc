package br.unisc.pdm.caronauniscapp.webservice;

import org.json.JSONArray;

/**
 * Created by Diego, Gabriele Rafael on 17/10/2015.
 */
public interface AgendaTela {
    public void agendaAllItens(int tipo, JSONArray agItens);
    public void agendaItem(String dataBaseLivre, int tipo, String turno, int qtd);
    public void setAgendaDia_callback();
}
