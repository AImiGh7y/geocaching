package geocaching;

import java.util.ArrayList;
import edu.princeton.cs.algs4.*;

public class Cache {
    public Point2D gps;
    public SequentialSearchST<String, Log> logs;
    public ArrayList<Item> items;

    public Cache(double lat, double lon) {
        gps = new Point2D(lat, lon);
        logs = new SequentialSearchST<>();
        items = new ArrayList<Item>();
    }

    public void visitadaPor(Utilizador utilizador, String mensagem) {
        Log log = new Log(utilizador, mensagem);
        logs.put(utilizador.getNome(), log);
    }

    public String listaUtilizadores() {
        String lista = "";
        for(String nome: logs.keys())
            lista += nome + " ";
        return lista;
    }

    public Log getLog(String nome) {
        return null;
    }
}