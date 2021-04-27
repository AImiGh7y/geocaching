package geocaching;

public class Log {
    private Utilizador utilizador;
    private String mensagem;

    public Log(Utilizador utilizador, String mensagem){
        this.utilizador = utilizador;
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}