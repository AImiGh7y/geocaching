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
            System.out.print("Qual o nome da cache? ");
            String id = scanner.next();
            if (Cache.caches_por_id.contains(id))
                cache = Cache.caches_por_id.get(id);
            else
                System.out.println("Cache nao existe");
        } while (cache == null);
        return cache;
    }

    public static void menuUtilizador(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
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
                        for (String id: Cache.caches_por_id.keys()) {
                            Cache cache = Cache.caches_por_id.get(id);
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

    public static void menuPesquisas(Scanner scanner) {
        int opcao;
        do {
            System.out.println();
            System.out.println("== Menu Pesquisas ==");
            System.out.println("1: Todas as caches visitadas por um utilizador. Global por região");
            System.out.println("0: Voltar atras");
            opcao = scanner.nextInt();
            switch(opcao) {
                case 1: {  // Todas as caches visitadas por um utilizador
                    User utilizador = pedirUtilizador(scanner);
                    for(String id: Cache.caches_por_id.keys()) {
                        Cache cache = Cache.caches_por_id.get(id);
                        if(cache.foiVisitadaPor(utilizador))
                            System.out.println("- " + cache);
                    }
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
            System.out.println();
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
                        Cache.caches_por_id.put(id, cache);
                        if (Cache.caches_por_regiao.contains(regiao)) {
                            Cache.caches_por_regiao.get(regiao).add(cache);
                        } else {
                            ArrayList lista = new ArrayList<Cache>();
                            lista.add(cache);
                            Cache.caches_por_regiao.put(regiao, lista);
                        }
                    }
                    else
                        System.out.println("Utilizador " + nome + " nao existe");
                    break;
                }
                case 2: {  // listar caches
                    System.out.println("Lista de caches");
                    for (String id: Cache.caches_por_id.keys()) {
                        Cache cache = Cache.caches_por_id.get(id);
                        ArrayList<Log> logs = cache.getLogs();
                        ArrayList<Item> items = cache.getItems();
                        System.out.println("- " + cache);
                        if(logs.size() > 0)
                            System.out.println("  Logs:");
                        for (Log log: logs) {
                            System.out.println("  - " + log);
                        }
                        if(items.size() > 0)
                            System.out.println("  Items:");
                        for (Item item: items) {
                            System.out.println("  - " + item);
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
            System.out.println();
            System.out.println("== MENU ==");
            System.out.println("1: menu utilizador");
            System.out.println("2: menu cache");
            System.out.println("3: visitar cache");
            System.out.println("4: pesquisas");
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

                    System.out.println("Quer deixar travelbug - sim?");
                    String sim = scanner.next();
                    if(sim.equals("sim")) {
                        TravelBug bug_cache, bug_utilizador;
                        bug_cache = cache.getTravelBug();
                        bug_utilizador = utilizador.getTravelBug();
                        if (bug_utilizador == null) {
                            System.out.println("Nome do travel bug?");
                            String nome = scanner.next();
                            bug_utilizador = new TravelBug(nome, utilizador, cache, cache);
                        }
                        if(bug_cache != null) {
                            utilizador.setTravelBug(bug_cache);
                            cache.removeItem(bug_cache);
                        }
                        cache.addItem(bug_utilizador);
                    }
                    break;
                }
                case 4:  // abre menu pesquisas
                    menuPesquisas(scanner);
                    break;
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
