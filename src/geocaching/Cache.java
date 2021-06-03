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

/**
 * Cache que guarda logs e items.
 */
public abstract class Cache {
    public static RedBlackBST<String, Cache> caches_por_id = new RedBlackBST<>();
    public static RedBlackBST<String, ArrayList<Cache>> caches_por_regiao = new RedBlackBST<>();
    public static SymbolDigraphLP grafo_tempos = new SymbolDigraphLP();
    public static SymbolDigraphLP grafo_distancias = new SymbolDigraphLP();
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

    public void removeItem(Item item) {
        items.remove(item);
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

    /**
     * Verifica se utilizador visitou a cache.
     * @param user Utilizador que visitou cache.
     * @return Se o utilizador visitou a cache.
     */
    public boolean foiVisitadaPor(User user) {
        for(User u: logs.keys())
            if(u == user)
                return true;
        return false;
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

    public ArrayList<Item> getItems() { return items; }

    public TravelBug getTravelBug() {
        for(Item item: items) {
            if(item instanceof TravelBug)
                return (TravelBug)item;
        }
        return null;
    }

    public double getLat() { return gps.x(); }
    public double getLon() { return gps.y(); }
    public String getLatstr() { return String.valueOf(gps.x()); }
    public String getLonstr() { return String.valueOf(gps.y()); }
    public String getId() { return id; }
    public String getRegiao() { return regiao; }
}