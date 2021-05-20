package geocaching;

import edu.princeton.cs.algs4.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
            System.out.println("1: Todas as caches visitadas por um utilizador. Global e por região");
            System.out.println("2: Todas as caches não visitadas por um utilizador. Global e por região");
            System.out.println("3: Todos os utilizadores que já visitaram uma dada cache");
            System.out.println("4: Todas as caches premium que têm pelo menos um objecto");
            System.out.println("5: Top-5 de utilizadores que visitaram maior nº de caches num dado período temporal");
            System.out.println("6: Travel bugs com maior número de localizações percorridas no seu histórico");
            System.out.println("0: Voltar atras");
            opcao = scanner.nextInt();
            switch(opcao) {
                case 1: {  // Todas as caches visitadas por um utilizador
                    User utilizador = pedirUtilizador(scanner);
                    int nvisitas = 0;
                    // global
                    System.out.println("* Visitadas globais:");
                    for(String id: Cache.caches_por_id.keys()) {
                        Cache cache = Cache.caches_por_id.get(id);
                        if(cache.foiVisitadaPor(utilizador)) {
                            System.out.println("- " + cache);
                            nvisitas++;
                        }
                    }
                    // por regiao
                    if(nvisitas == 0)
                        System.out.println("Nao ha caches visitadas");
                    else {
                        System.out.println("* Visitadas por regiao:");
                        for(String regiao: Cache.caches_por_regiao.keys()) {
                            System.out.println(regiao);
                            ArrayList<Cache> caches = Cache.caches_por_regiao.get(regiao);
                            for (Cache cache : caches) {
                                if (cache.foiVisitadaPor(utilizador)) {
                                    System.out.println("- " + cache);
                                    nvisitas++;
                                }
                            }
                        }
                    }
                    break;
                }
                case 2: {  // Todas as caches visitadas por um utilizador
                    User utilizador = pedirUtilizador(scanner);
                    int nvisitas = 0;
                    // global
                    System.out.println("* Caches nao-visitadas globais:");
                    for (String id : Cache.caches_por_id.keys()) {
                        Cache cache = Cache.caches_por_id.get(id);
                        if (!cache.foiVisitadaPor(utilizador)) {
                            System.out.println("- " + cache);
                            nvisitas++;
                        }
                    }
                    // por regiao
                    if (nvisitas == 0)
                        System.out.println("Nao ha caches nao-visitadas");
                    else {
                        System.out.println("* Caches nao-visitadas por regiao:");
                        for (String regiao : Cache.caches_por_regiao.keys()) {
                            System.out.println(regiao);
                            ArrayList<Cache> caches = Cache.caches_por_regiao.get(regiao);
                            for (Cache cache : caches) {
                                if (!cache.foiVisitadaPor(utilizador)) {
                                    System.out.println("- " + cache);
                                    nvisitas++;
                                }
                            }
                        }
                    }
                    break;
                }
                case 3: {  // Todas as caches visitadas por um utilizador
                    Cache cache = pedirCache(scanner);
                    int nutilizadores = 0;
                    for(int id: User.utilizadores_por_id.keys()){
                        User utilizador = User.utilizadores_por_id.get(id);
                        if(cache.foiVisitadaPor(utilizador)){
                            System.out.println(utilizador);
                            nutilizadores++;
                        }
                    }
                    if(nutilizadores == 0){
                        System.out.println("Nenhum utilizador visitou a cache");
                    }
                    break;
                }
                case 4: {
                    for(String id: Cache.caches_por_id.keys()){
                        Cache cache = Cache.caches_por_id.get(id);
                        if(cache instanceof CachePremium){
                            if(cache.getItems().size() >= 1){
                                System.out.println(cache + " tem " + cache.getItems().size() + " items");
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    // pedir intervalo temporal (o dia)
                    System.out.println("Qual o dia da visita?");
                    int dia = scanner.nextInt();
                    if(dia < 1 || dia > 31) {
                        System.out.println("Dia invalido");
                        continue;
                    }
                    // symbol table que associa nvisitas -> lista de utilizadores
                    RedBlackBST<Integer, ArrayList<User>> visitas = new RedBlackBST<>();
                    for(int user_id: User.utilizadores_por_id.keys()){
                        // quantas caches, o utilizador visitou
                        User user = User.utilizadores_por_id.get(user_id);
                        int nvisitas = 0;
                        for(String cache_id : Cache.caches_por_id.keys()){
                            Cache cache = Cache.caches_por_id.get(cache_id);
                            if(cache.foiVisitadaPor(user)){
                                boolean visitou_tempo = false;
                                for(Log log: cache.getLogs()) {
                                    if (log.getDate().getDayOfMonth() == dia) {
                                        visitou_tempo = true;
                                        break;
                                    }
                                }
                                if(visitou_tempo)
                                    nvisitas++;
                            }
                        }
                        // adicionar utilizador ao symbol table
                        if(nvisitas > 0) {
                            if (visitas.contains(nvisitas)) {
                                visitas.get(nvisitas).add(user);
                            } else {
                                ArrayList<User> users = new ArrayList<>();
                                users.add(user);
                                visitas.put(nvisitas, users);
                            }
                        }
                    }
                    // imprimir top-5
                    ArrayList<Integer> visitas_ordenadas = new ArrayList<>();
                    for(Integer nvisitas: visitas.keys())
                        visitas_ordenadas.add(0, nvisitas);
                    int i = 0;
                    for(Integer nvisitas: visitas_ordenadas) {
                        for(User user: visitas.get(nvisitas)) {
                            System.out.println(nvisitas + " de " + user);
                            i++;
                        }
                        // mostrar apenas 5 maiores
                        if(i >= 5)
                            break;
                    }
                    break;
                }
                case 6: {
                    int maior_historico = 0;
                    TravelBug maior_bug = null;
                    for(String id: Cache.caches_por_id.keys()) {
                        Cache cache = Cache.caches_por_id.get(id);
                        TravelBug bug = cache.getTravelBug();
                        if(bug != null && bug.getHistorico().size() > maior_historico) {
                            maior_historico = bug.getHistorico().size();
                            maior_bug = bug;
                        }
                    }
                    if(maior_bug != null) {
                        System.out.println("Travel bug em mais localizacoes: " + maior_bug);
                        for(Cache cache: maior_bug.getHistorico())
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
            System.out.println("3: caminho minimo");  // R14
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
                case 3: {  // caminho mais curto
                    System.out.print("De que cache? ");
                    String id1 = scanner.next();
                    System.out.print("Para que cache? ");
                    String id2 = scanner.next();
                    if(Cache.caches_por_id.contains(id1) && Cache.caches_por_id.contains(id2)) {
                        System.out.println("Qual o atributo a usar?");
                        System.out.println("1: tempo");
                        System.out.println("2: distancia");
                        System.out.println("Outra tecla para sair");
                        int opcao_grafo = scanner.nextInt();
                        SymbolDigraphLP grafo = null;
                        if(opcao_grafo == 1)
                            grafo = Cache.grafo_tempos;
                        else if(opcao_grafo == 2)
                            grafo = Cache.grafo_distancias;
                        if(grafo != null) {
                            int i1 = grafo.indexOf(id1);
                            int i2 = grafo.indexOf(id2);
                            DijkstraSP dijkstra = new DijkstraSP(grafo.digraph(), i1);
                            if(dijkstra.hasPathTo(i2)) {
                                double custoMin = dijkstra.distTo(i2);
                                System.out.println("Custo minimo = " + custoMin);
                                System.out.print(id1);
                                for(DirectedEdge e: dijkstra.pathTo(i2)) {
                                    System.out.print(" -> " + grafo.nameOf(e.to()));
                                }
                                System.out.println();
                            }
                            else
                                System.out.println("Erro: nao existe caminho entre os nos");
                        }
                    }
                    else {
                        System.out.println("Error: cache does not exist.");
                    }
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
            System.out.println("4: menu pesquisas");
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

                    // "O Manuel sendo um Basic user, não devia ter acesso à geocache16."
                    if(utilizador instanceof UserBasic && cache instanceof CachePremium) {
                        System.out.println("Erro: " + utilizador.getNome() + " (utilizador basico) nao pode visitar uma cache premium");
                        continue;
                    }

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
                        bug_utilizador.setCache(cache);
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
