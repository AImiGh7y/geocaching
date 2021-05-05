package geocaching;

public class TravelBug extends Item {
    private User criador;
    private Cache origem, atual;

    public TravelBug(String descricao, User criador, Cache origem, Cache destino) {
        super(descricao);
        this.criador = criador;
        this.origem = origem;
        this.atual = destino;
    }

    public void setCache(Cache cache) {
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

    public String toString() {
        return "item (travelbug) " + getDescricao() + " de " + criador.getNome() + " em " + atual.getId();
    }
}
