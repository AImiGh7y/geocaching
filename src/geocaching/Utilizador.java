package geocaching;

import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;

public class Utilizador {
    public static RedBlackBST<String, Utilizador> utilizadores = new RedBlackBST<>();
    private String nome;
    private ArrayList<Cache> visitadas;

    public Utilizador(String nome) {
        this.nome = nome;
        visitadas = new ArrayList<>();
    }

    public void addCacheVisitada(Cache cache) {
        visitadas.add(cache);
    }

    public String getNome() {
        return nome;
    }

    /*
    ricardo
    nuno
    eduardo
    */


}