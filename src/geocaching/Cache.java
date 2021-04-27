package geocaching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import edu.princeton.cs.algs4.*;

public class Cache {
    public static RedBlackBST<Point2D, Cache> caches = new RedBlackBST<>();
    public Point2D gps;
    public RedBlackBST<String, Log> logs;
    public ArrayList<Item> items;

    public Cache(double lat, double lon) {
        gps = new Point2D(lat, lon);
        logs = new RedBlackBST<>();
        items = new ArrayList<Item>();
    }

    public void visitadaPor(Utilizador utilizador, String mensagem) {
        Log log = new Log(utilizador, mensagem);
        logs.put(utilizador.getNome(), log);
        utilizador.addCacheVisitada(this);
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
                System.out.println("ignora linha");
                sc.nextLine();  // ignora linha
                while(true) {
                    String utilizador = sc.nextLine();
                    System.out.println("leu utilizador");
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
                for(String utilizador: cache.logs.keys()) {
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
}