package geocaching;

import java.util.ArrayList;

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
    private String id, regiao;
    private Point2D gps;
    private SequentialSearchST<User, Log> logs;
    private ArrayList<Item> items;

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
     * @param utilizador Utilizador que visitou a cache
     * @param mensagem Mensagem deixada pelo utilizador
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
    public abstract String getTipo();


    /**
     * Caches percorridas de acordo com o problema do caixeiro viajante.
     * Retorna caminho mais comprido da origem ate voltar a origem, sem repeticoes.
     * @param grafo Grafo a percorrer
     * @param origem Indice do no de origem
     * @param atual Indice do no atual
     * @param visitados Lista dos visitados anteriormente
     * @param tempoMax Tempo desejado para o metodo
     * @param start_time Tempo antes de executar a funcao
     * @return Lista dos nos percorridos
     */

    public static ArrayList<Integer> caixeiro(SymbolDigraphLP grafo, int origem, int atual, ArrayList<Integer> visitados, int tempoMax, long start_time) {
        // Retorna caminho mais comprido da origem ate voltar a origem, sem repeticoes.
/*        System.out.print("atual: " + grafo.nameOf(atual) + " visitados:");
        for (Integer i : visitados)
            System.out.print(" -> " + grafo.nameOf(i));
        System.out.println();*/

        if(atual == origem && visitados.size() > 0) {
            // chegamos ao destino, caminho mais longo = proprio
            ArrayList<Integer> lista = new ArrayList<>();
            lista.add(atual);
            //System.out.println("voltou a origem");
            return lista;
        }
        if(visitados.contains(atual)) {
            // nao permite visitar duas vezes
            //System.out.println("ja visitou");
            return null;
        }

        long dt = (System.currentTimeMillis() - start_time) / 1000;
        if(dt >= tempoMax)  // condicao de paragem de tempo
            return null;

        ArrayList<Integer> visitados2 = (ArrayList<Integer>)visitados.clone();
        visitados2.add(atual);

        ArrayList<Integer> maiorPercurso = null;
        for(DirectedEdge e: grafo.digraph().adj(atual)) {
            int prox = e.to();
            ArrayList<Integer> percurso = caixeiro(grafo, origem, prox, visitados2, tempoMax, start_time);
            if(percurso != null) {
                percurso.add(0, atual);
                if(maiorPercurso == null || percurso.size() > maiorPercurso.size())
                    maiorPercurso = percurso;
            }
        }
/*        if(maiorPercurso != null) {
            System.out.print("atual: " + grafo.nameOf(atual) + " encontrou maior percurso:");
            for (Integer i : maiorPercurso)
                System.out.print(" -> " + grafo.nameOf(i));
            System.out.println();
        }*/
        return maiorPercurso;
    }
}