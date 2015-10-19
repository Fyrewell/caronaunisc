package br.unisc.pdm.caronauniscapp.database;

/**
 * Usuario cadastrado no aplicativo.
 *
 * Tipo do usuario pode ser:
 * 1 - usuario pode dar carona
 * 2 - usuario precisa receber carona
 * 3 - usuario pode tanto dar carona, como pode disponibilizar carona
 *      (diferente agenda para diferentes dias)
 *
 * Created by Diego on 05/10/2015.
 */
public class Usuario {
    public static final String STORE_MODE = "WEB";  //opcoes: DB ou WEB
    public static final String BASE_URL = "http://caronaunisc.herokuapp.com/api";

    public Usuario(){
        this.sexo = "indefinido";
    }
    private int id;
    private int matricula;
    private String senha;
    private String nome;
    private String email;
    private String endereco;
    private String numero;
    private String bairro;
    private String cep;
    private String fone;
    private String sexo;
    private int usuario_tipo;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getCadastroTipo() {
        return usuario_tipo;
    }
    public void setCadastroTipo(int usuario_tipo) {
        this.usuario_tipo = usuario_tipo;
    }
    @Override
    public String toString(){
        return id + " | " + nome + " - " + email;
    }
}
