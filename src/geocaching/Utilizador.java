package geocaching;

import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.RedBlackBST;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

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

    public void setNome(String nome){
        this.nome = nome;
    }
    public static void readFile(String nome_ficheiro){
        Scanner sc = null;
        try {
            sc = new Scanner(new File(nome_ficheiro));
            while(sc.hasNext()){
                while(sc.hasNextLine()) {
                    String nome_utilizador = sc.nextLine();
                    if(nome_utilizador.isEmpty())
                        break;
                    utilizadores.put(nome_utilizador, new Utilizador(nome_utilizador));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     ricardo
     nuno
     eduardo
     */

    public static void writeFile(String nome_ficheiro){
        try {
            FileWriter file = new FileWriter(nome_ficheiro);
            for(String nome_utilizador: utilizadores.keys()){
                Utilizador utilizador = utilizadores.get(nome_utilizador);
                file.write(nome_utilizador + "\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}