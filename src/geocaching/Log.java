package geocaching;

public class Log {
    private User utilizador;
    private String mensagem;

    public Log(User utilizador, String mensagem){
        this.utilizador = utilizador;
        this.mensagem = mensagem;
    }

    public User getUtilizador() { return utilizador; }

    public String getMensagem() {
        return mensagem;
    }

    public String toString() {
        return utilizador.getNome() + " - " + mensagem;
    }
}