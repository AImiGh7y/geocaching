package geocaching;

import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static User pedirUtilizador(Scanner scanner) {
        User utilizador = null;
        do {
            System.out.print("Quem vistou? ");
            String nome = scanner.next();
            if (User.utilizadores_por_nome.contains(nome))
                utilizador = User.utilizadores_por_nome.get(nome);
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
            if (Cache.caches_por_gps.contains(pt))
                cache = Cache.caches_por_gps.get(pt);
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
                    String tipo;
                    do {
                        System.out.print("Qual o tipo de utilizador (basic/premium/admin)? ");
                        tipo = scanner.next();
                    } while(!tipo.equals("basic") && !tipo.equals("premium") && !tipo.equals("admin"));
                    User user;
                    if(tipo.equals("basic"))
                        user = new UserBasic(nome);
                    else if(tipo.equals("premium"))
                        user = new UserPremium(nome);
                    else
                        user = new UserAdmin(nome);
                    User.utilizadores_por_nome.put(nome, user);
                    User.utilizadores_por_id.put(user.getId(), user);
                    break;
                }
                case 2: {  // listar utilizadores
                    System.out.println("Lista de utilizadores");
                    for (int id: User.utilizadores_por_id.keys())
                        System.out.println("- " + User.utilizadores_por_id.get(id));
                    System.out.println();
                    break;
                }
                case 3: {
                    System.out.print("Que utilizador quer remover? ");
                    String nome = scanner.next();
                    User utilizador = User.utilizadores_por_nome.get(nome);
                    if (utilizador != null) {
                        User.utilizadores_por_nome.delete(nome);
                        User.utilizadores_por_id.delete(utilizador.getId());
                        for (Point2D pt : Cache.caches_por_gps.keys()) {
                            Cache cache = Cache.caches_por_gps.get(pt);
                            cache.removerVisita(utilizador);
                        }
                    } else
                        System.out.println("Utilizador " + nome + " nao existe");
                    break;
                }
                case 4: {
                    System.out.print("Que utilizador quer editar? ");
                    String nome = scanner.next();
                    User utilizador = User.utilizadores_por_nome.get(nome);
                    if (utilizador != null) {
                        System.out.print("Novo nome? ");
                        String nome2 = scanner.next();
                        utilizador.setNome(nome2);
                        // para manter consistencia, renomear a chave da ST
                        User.utilizadores_por_nome.delete(nome);
                        User.utilizadores_por_nome.put(nome2, utilizador);
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
                    System.out.print("Qual o utilizador? ");
                    String nome = scanner.next();
                    if(User.utilizadores_por_nome.contains(nome)) {
                        System.out.print("Qual o id? ");
                        String id = scanner.next();
                        System.out.print("Qual a regiao? ");
                        String regiao = scanner.next();
                        double lat, lon;
                        System.out.print("Qual a latitude? ");
                        lat = scanner.nextDouble();
                        System.out.print("Qual a longitude? ");
                        lon = scanner.nextDouble();
                        User user = User.utilizadores_por_nome.get(nome);
                        Cache cache;
                        if(user instanceof UserBasic)
                            cache = new CacheBasic(id, regiao, lat, lon);
                        else
                            cache = new CachePremium(id, regiao, lat, lon);
                        Cache.caches_por_gps.put(new Point2D(lat, lon), cache);
                        Cache.caches_por_regiao.put(regiao, cache);
                    }
                    else
                        System.out.println("Utilizador " + nome + " nao existe");
                    break;
                }
                case 2: {  // listar caches
                    System.out.println("Lista de caches");
                    for (Point2D pt : Cache.caches_por_gps.keys()) {
                        Cache cache = Cache.caches_por_gps.get(pt);
                        ArrayList<Log> logs = Cache.caches_por_gps.get(pt).getLogs();
                        System.out.println("- " + cache);
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
                    User utilizador = pedirUtilizador(scanner);
                    Cache cache = pedirCache(scanner);

                    System.out.println("Qual a mensagem que quer deixar?");
                    String mensagem = scanner.next();

                    cache.visitadaPor(utilizador, mensagem);
                    break;
                }
                case 8: {  // ler ficheiro
                    IO.readFile("input.txt");
                    break;
                }
                case 9: {  // gravar ficheiro
                    IO.writeFile("output.txt");
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
