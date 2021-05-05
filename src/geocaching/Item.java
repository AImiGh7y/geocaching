package geocaching;

public class Item {
    private String descricao;

    public Item(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String toString() {
        return "item " + descricao;
    }
}