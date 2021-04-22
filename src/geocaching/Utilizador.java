package geocaching;

import java.util.ArrayList;

public class Utilizador {
    public String nome;
    public ArrayList<Cache> visitadas;

    public Utilizador(String nome) {
        this.nome = nome;
        visitadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }
}