package geocaching;

import java.time.LocalDate;

public class Log {
    private User utilizador;
    private String mensagem;
    private LocalDate date;

    public Log(User utilizador, String mensagem){
        this.utilizador = utilizador;
        this.mensagem = mensagem;
        this.date = LocalDate.now();
    }

    public User getUtilizador() { return utilizador; }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDate getDate() { return date; }

    public String toString() {
        return utilizador.getNome() + " - " + mensagem;
    }
}