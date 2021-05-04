package geocaching;

import edu.princeton.cs.algs4.RedBlackBST;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class User {
    public static RedBlackBST<String, User> utilizadores_por_nome = new RedBlackBST<>();
    public static RedBlackBST<Integer, User> utilizadores_por_id = new RedBlackBST<>();

    private int id;
    private String nome;
    private ArrayList<Cache> visitadas;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
}