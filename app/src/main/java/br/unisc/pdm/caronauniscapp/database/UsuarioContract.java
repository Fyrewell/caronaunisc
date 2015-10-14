package br.unisc.pdm.caronauniscapp.database;

import android.provider.BaseColumns;

/**
 * Created by Diego on 05/10/2015.
 */
public class UsuarioContract {

    public UsuarioContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Usuario implements BaseColumns {
        public final static String TABLE_NAME = "PESSOA";
        public final static String NOME = "nome";
        public final static String ENDERECO = "endereco";
        public final static String EMAIL = "email";
        public final static String SEXO = "sexo";
    }
}
