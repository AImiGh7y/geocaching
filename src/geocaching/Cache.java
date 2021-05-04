package geocaching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.SequentialSearchST;

public abstract class Cache {
    public static RedBlackBST<Point2D, Cache> caches_por_gps = new RedBlackBST<>();
    public static RedBlackBST<String, Cache> caches_por_regiao = new RedBlackBST<>();
    String id, regiao;
    public Point2D gps;
    public SequentialSearchST<User, Log> logs;
    public ArrayList<Item> items;

    public Cache(String id, String regiao, double lat, double lon) {
        this.id = id;
        this.regiao = regiao;
        gps = new Point2D(lat, lon);
        logs = new SequentialSearchST<>();
        items = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Esta funcao espefici...
     * @param utilizador Utilizador que visitou a cache
     * @param mensagem Mensagem deixada pelo utiulizador
     */
    public void visitadaPor(User utilizador, String mensagem) {
        System.out.println("cache visitada por " + utilizador.getNome() + " - " + mensagem);
        Log log = new Log(utilizador, mensagem);
        logs.put(utilizador, log);
        utilizador.addCacheVisitada(this);
    }

    public void removerVisita(User utilizador) {
        logs.delete(utilizador);
    }

    /**
     * Retornar lista de logs.
     * @return lista de logs
     */
    public ArrayList<Log> getLogs() {
        // RedBlackBST<String, Log> logs => ArrayList<Log> ret
        ArrayList<Log> ret = new ArrayList<>();
        //for(Tipo elemento : lista)
        for(User utilizador: logs.keys()){
            Log log = logs.get(utilizador);
            ret.add(log);
        }
        return ret;
    }

    public double getLat() { return gps.x(); }
    public double getLon() { return gps.y(); }
    public String getId() { return id; }
    public String getRegiao() { return regiao; }

    public static void main(String[] args) {
        Cache cache1 = new CacheBasic("cache1", "Norte", 0.5, 2.8);
        Cache cache2 = new CachePremium("cache2", "Norte", 7.2, 3.2);
        caches_por_gps.put(cache1.gps, cache1);
        caches_por_gps.put(cache2.gps, cache2);
        /*
        writeFile("temp.txt");
        caches = new RedBlackBST<>();
        readFile("temp.txt");
        System.out.println("Test tamanho cache: " + caches.size() + " tem que ser 2");
         */
        System.out.println("Cache tem ponto 0.5,2.8: " + (caches_por_gps.get(new Point2D(0.5, 2.8)) != null) + " tem que ser true");
        System.out.println("Cache tem ponto 6.2,3.8: " + (caches_por_gps.get(new Point2D(6.2, 3.8)) != null) + " tem que ser false");
    }
}