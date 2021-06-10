package geocaching;

import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;

/**
 * Utilizadores
 */

public abstract class User {
    public static RedBlackBST<String, User> utilizadores_por_nome = new RedBlackBST<>();
    public static RedBlackBST<Integer, User> utilizadores_por_id = new RedBlackBST<>();
    private int id;
    private String nome;
    private ArrayList<Cache> visitadas;
    private TravelBug travelbug_atual;

    public User(int id, String nome) {
        this.id = id;
        this.nome = nome;
        visitadas = new ArrayList<>();
    }

    public User(String nome) {
        this.id = proximoId();
        this.nome = nome;
        visitadas = new ArrayList<>();
    }

    public static int proximoId() {
        int max_id = 0;
        for(int id: utilizadores_por_id.keys())
            if(id > max_id)
                max_id = id;
        return max_id+1;
    }

    public void addCacheVisitada(Cache cache) {
        visitadas.add(cache);
    }

    public int getId() { return id; }

    public String getIdstr() { return String.valueOf(id); }

    public String getNome() {
        return nome;
    }

    public abstract String getTipo();

    public void setNome(String nome){
        this.nome = nome;
    }

    public TravelBug getTravelBug() {
        return travelbug_atual;
    }

    public void setTravelBug(TravelBug bug) {
        travelbug_atual = bug;
    }
}