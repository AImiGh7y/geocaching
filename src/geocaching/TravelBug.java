package geocaching;

import java.util.ArrayList;

public class TravelBug extends Item {
    private User criador;
    private Cache origem, atual;
    private ArrayList<Cache> historico;

    public TravelBug(String descricao, User criador, Cache origem, Cache destino) {
        super(descricao);
        this.criador = criador;
        this.origem = origem;
        this.atual = destino;
        historico = new ArrayList<>();
        historico.add(origem);
        historico.add(atual);
    }

    public void setCache(Cache cache) {
        historico.add(cache);
        atual = cache;
    }

    public User getCriador() {
        return criador;
    }

    public Cache getOrigem() {
        return origem;
    }

    public Cache getAtual() {
        return atual;
    }

    public ArrayList<Cache> getHistorico() {
        return historico;
    }

    public String toString() {
        return "item (travelbug) " + getDescricao() + " de " + criador.getNome() + " em " + atual.getId();
    }
}
