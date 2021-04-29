package geocaching;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SequentialSearchST;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;

public class Main {
    public static Utilizador pedirUtilizador(Scanner scanner) {
        Utilizador utilizador = null;
        do {
            System.out.print("Quem vistou? ");
            String nome = scanner.next();
            if (Utilizador.utilizadores.contains(nome))
                utilizador = Utilizador.utilizadores.get(nome);
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
            if (Cache.caches.contains(pt))
                cache = Cache.caches.get(pt);
            else
                System.out.println("Cache nao existe");
        } while (cache == null);
        return cache;
    }

    public static void menuUtilizador(Scanner scanner) {
        int opcao;
        do {
            System.out.println("== Menu Utilizador ==");
            System.out.println("1: adicionar utilizador");
            System.out.println("2: listar utilizadores");
            System.out.println("3: remover utilizador");
            System.out.println("4: editar utilizador");
            System.out.println("0: Voltar atras");
            opcao = scanner.nextInt();
            switch(opcao) {
                case 1: {  // adicionar utilizador
                    System.out.print("Qual o nome? ");
                    String nome = scanner.next();
                    Utilizador.utilizadores.put(nome, new Utilizador(nome));
                    break;
                }
                case 2: {  // listar utilizadores
                    System.out.println("Lista de utilizadores");
                    for (String nome : Utilizador.utilizadores.keys())
                        System.out.println("- " + nome);
                    System.out.println();
                    break;
                }
                case 3: {
                    System.out.print("Que utilizador quer remover? ");
                    String nome = scanner.next();
                    Utilizador utilizador = Utilizador.utilizadores.get(nome);
                    if (utilizador != null) {
                        Utilizador.utilizadores.delete(nome);
                        for (Point2D pt : Cache.caches.keys()) {
                            Cache cache = Cache.caches.get(pt);
                            cache.removerVisita(utilizador);
                        }
                    } else
                        System.out.println("Utilizador " + nome + " nao existe");
                    break;
                }
                case 4: {
                    System.out.print("Que utilizador quer editar? ");
                    String nome = scanner.next();
                    Utilizador utilizador = Utilizador.utilizadores.get(nome);
                    if (utilizador != null) {
                        System.out.print("Novo nome? ");
                        String nome2 = scanner.next();
                        utilizador.setNome(nome2);
                        // para manter consistencia, renomear a chave da ST
                        Utilizador.utilizadores.delete(nome);
                        Utilizador.utilizadores.put(nome2, utilizador);
                    } else
                        System.out.println("Utilizador " + nome + " nao existe");
                    break;
                }
                case 0: break;
                default:
                    System.out.println("Opcao " + opcao + " invalida");
                    break;
            }
        } while(opcao != 0);
    }

    public static void menuCache(Scanner scanner) {
        int opcao;
        do {
            System.out.println("== Menu Cache ==");
            System.out.println("1: adicionar cache");
            System.out.println("2: listar caches");
            System.out.println("0: Voltar atras");
            opcao = scanner.nextInt();
            switch(opcao) {
                case 1: {  // adicionar cache
                    double lat, lon;
                    System.out.print("Qual a latitude? ");
                    lat = scanner.nextDouble();
                    System.out.print("Qual a longitude? ");
                    lon = scanner.nextDouble();
                    Cache.caches.put(new Point2D(lat, lon), new Cache(lat, lon));
                    break;
                }
                case 2: {  // listar caches
                    System.out.println("Lista de caches");
                    for (Point2D pt : Cache.caches.keys()) {
                        ArrayList<Log> logs = Cache.caches.get(pt).getLogs();
                        System.out.println("- " + pt.x() + ", " + pt.y());
                        for (Log log : logs) {
                            System.out.println("\t- " + log.getUtilizador().getNome() + " - " + log.getMensagem());
                        }
                    }
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opcao " + opcao + " invalida");
                    break;
            }
        } while(opcao != 0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("== MENU ==");
            System.out.println("1: menu utilizador");
            System.out.println("2: menu cache");
            System.out.println("3: visitar cache");
            System.out.println("8: ler do ficheiro");
            System.out.println("9: gravar para ficheiro");
            System.out.println("0: sair");

            System.out.println();
            System.out.print("Opcao: ");
            opcao = scanner.nextInt();  // Stdin
            switch(opcao) {
                case 1:  // abre menu utilizadores
                    menuUtilizador(scanner);
                    break;
                case 2:  // abre menu utilizadores
                    menuCache(scanner);
                    break;
                case 3: {  // visitado por
                    Utilizador utilizador = pedirUtilizador(scanner);
                    Cache cache = pedirCache(scanner);

                    System.out.println("Qual a mensagem que quer deixar?");
                    String mensagem = scanner.next();

                    cache.visitadaPor(utilizador, mensagem);
                    break;
                }
                case 8: {  // ler ficheiro
                    Utilizador.readFile("utilizador.txt");
                    Cache.readFile("cache.txt");
                    break;
                }
                case 9: {  // gravar ficheiro
                    Utilizador.writeFile("utilizador.txt");
                    Cache.writeFile("cache.txt");
                    break;
                }
                case 0: break;
                default:
                    System.out.println("Opcao " + opcao + " invalida");
                    break;
            }
        } while(opcao != 0);
    }
}
