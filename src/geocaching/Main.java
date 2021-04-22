package geocaching;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SequentialSearchST;
import java.util.Scanner;

public class Main {
    // - se se modificar, preciso atualizar o UML?
    // - deixar estas duas variaveis no Main ou criar classe para os utilizadores e as caches
    static SequentialSearchST<String, Utilizador> utilizadores = new SequentialSearchST<>();
    static SequentialSearchST<Point2D, Cache> caches = new SequentialSearchST<>();

    public static Utilizador pedirUtilizador(Scanner scanner) {
        Utilizador utilizador = null;
        do {
            System.out.print("Quem vistou? ");
            String nome = scanner.next();
            if (utilizadores.contains(nome))
                utilizador = utilizadores.get(nome);
            else
                System.out.println("Utilizador nao existe");
        } while (utilizador == null);
        return utilizador;
    }

    public static Cache pedirCache(Scanner scanner) {
        Cache cache = null;
        do {
            System.out.print("Qual a latitude da cache? ");
            double lat = scanner.nextDouble();
            System.out.print("Qual a longitude da cache? ");
            double lon = scanner.nextDouble();
            Point2D pt = new Point2D(lat, lon);
            if (caches.contains(pt))
                cache = caches.get(pt);
            else
                System.out.println("Cache nao existe");
        } while (cache == null);
        return cache;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("== MENU ==");
            System.out.println("1: adicionar utilizador");
            System.out.println("2: listar utilizadores");
            System.out.println("3: adicionar cache");
            System.out.println("4: visitar caches");
            System.out.println("5: listar caches");
            System.out.println("0: sair");

            System.out.println();
            System.out.print("Opcao: ");
            opcao = scanner.nextInt();  // Stdin
            switch(opcao) {
                case 1: {  // adicionar utilizador
                    System.out.print("Qual o nome? ");
                    String nome = scanner.next();
                    utilizadores.put(nome, new Utilizador(nome));
                    break;
                }
                case 2: {  // listar utilizadores
                    System.out.println("Lista de utilizadores");
                    for (String nome : utilizadores.keys())
                        System.out.println("- " + nome);
                    System.out.println();
                    break;
                }
                case 3: {  // adicionar cache
                    double lat, lon;
                    System.out.print("Qual a latitude? ");
                    lat = scanner.nextDouble();
                    System.out.print("Qual a longitude? ");
                    lon = scanner.nextDouble();
                    caches.put(new Point2D(lat, lon), new Cache(lat, lon));
                    break;
                }
                case 4: {  // visitado por
                    Utilizador utilizador = pedirUtilizador(scanner);
                    Cache cache = pedirCache(scanner);

                    System.out.println("Qual a mensagem que quer deixar?");
                    String mensagem = scanner.next();

                    cache.visitadaPor(utilizador, mensagem);
                    break;
                }
                case 5: {  // listar caches
                    System.out.println("Lista de caches");
                    for (Point2D pt : caches.keys()) {
                        String lista_utilizadores = caches.get(pt).listaUtilizadores();
                        System.out.println("- " + pt.x() + ", " + pt.y() + " - vistada por: " + lista_utilizadores);
                    }
                    break;
                }
            }
        } while(opcao != 0);
    }
}
