package geocaching;

public class Log {
    public Utilizador utilizador;
    public String mensagem;

    public Log(Utilizador utilizador, String mensagem){
        this.utilizador = utilizador;
        this.mensagem = mensagem;
    }

    public String getMensagem(String nome) {
        return mensagem;
    }
}