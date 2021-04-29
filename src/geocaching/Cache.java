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

public class Cache {
    public static RedBlackBST<Point2D, Cache> caches = new RedBlackBST<>();
    public Point2D gps;
    public SequentialSearchST<Utilizador, Log> logs;
    public ArrayList<Item> items;

    public Cache(double lat, double lon) {
        gps = new Point2D(lat, lon);
        logs = new SequentialSearchST<>();
        items = new ArrayList<Item>();
    }

    /**
     * Esta funcao espefici...
     * @param utilizador Utilizador que visitou a cache
     * @param mensagem Mensagem deixada pelo utiulizador
     */
    public void visitadaPor(Utilizador utilizador, String mensagem) {
        System.out.println("cache visitada por " + utilizador.getNome() + " - " + mensagem);
        Log log = new Log(utilizador, mensagem);
        logs.put(utilizador, log);
        utilizador.addCacheVisitada(this);
    }

    public void removerVisita(Utilizador utilizador) {
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
        for(Utilizador utilizador: logs.keys()){
            Log log = logs.get(utilizador);
            ret.add(log);
        }
        return ret;
    }

    public static void readFile(String ficheiro){
        // caches
        Scanner sc = null;
        try {
            sc = new Scanner(new File(ficheiro));
            sc.useLocale(Locale.ENGLISH);
            while (sc.hasNext()) {
                System.out.println("ler gps");
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                Cache cache = new Cache(x, y);

                // ler os logs na cache
                // ignorar linhas
                sc.nextLine(); sc.nextLine();
                while(true) {
                    String utilizador = sc.nextLine();
                    System.out.println("leu utilizador: " + utilizador);
                    if(utilizador.isEmpty())
                        break;
                    String mensagem = sc.nextLine();
                    Utilizador u = Utilizador.utilizadores.get(utilizador);
                    cache.visitadaPor(u, mensagem);
                }

                // ler os items na cache
                while(true) {
                    String descricao = sc.nextLine();
                    System.out.println("descricao: " + descricao);
                    if(descricao.isEmpty())
                        break;
                    cache.items.add(new Item(descricao));
                }
                System.out.println("acabou de ler cache");
                caches.put(cache.gps, cache);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String ficheiro) {
        // caches
/*
        10.6
        8.5

        utilizador1
        mensagem1
        utilizador2
        mensagem2

        descricao_item1
        descricao_item2

        11.7
        1.2
*/

        try {
            FileWriter file = new FileWriter(ficheiro);
            for(Point2D pt: caches.keys()) {
                Cache cache = caches.get(pt);
                // String s = String.valueOf(5);
                file.write(String.valueOf(cache.gps.x()) + "\n");
                file.write(String.valueOf(cache.gps.y()) + "\n");
                file.write("\n");
                for(Utilizador utilizador: cache.logs.keys()) {
                    Log log = cache.logs.get(utilizador);
                    file.write(utilizador + "\n");
                    file.write(log.getMensagem() + "\n");
                }
                file.write("\n");
                for(Item item: cache.items){
                    file.write(item.getDescricao() + "\n");
                }
                file.write("\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Cache cache1 = new Cache(0.5, 2.8);
        Cache cache2 = new Cache(7.2, 3.2);
        caches.put(cache1.gps, cache1);
        caches.put(cache2.gps, cache2);
        writeFile("temp.txt");
        caches = new RedBlackBST<>();
        readFile("temp.txt");
        System.out.println("Test tamanho cache: " + caches.size() + " tem que ser 2");
        System.out.println("Cache tem ponto 0.5,2.8: " + (caches.get(new Point2D(0.5, 2.8)) != null) + " tem que ser true");
        System.out.println("Cache tem ponto 6.2,3.8: " + (caches.get(new Point2D(6.2, 3.8)) != null) + " tem que ser false");
    }
}