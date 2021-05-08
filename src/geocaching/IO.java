package geocaching;

import edu.princeton.cs.algs4.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Le e escreve dum ficheiro.
 */
public class IO {
    /**
     * Carrega ficheiro.
     * @param nome_ficheiro nome do ficheiro para ler
     */
    public static void readFile(String nome_ficheiro){
        // user
        Scanner sc = null;
        try {
            sc = new Scanner(new File(nome_ficheiro));
            int n = sc.nextInt();
            sc.nextLine();  // passa para proxima linha
            for(int i = 0; i < n; i++) {
                String linha = sc.nextLine();
                String campos[] = linha.split(", ");
                int id = Integer.valueOf(campos[0]);
                String nome = campos[1];
                String tipo = campos[2];

                User user;
                if(tipo.equals("basic"))
                    user = new UserBasic(id, nome);
                else if(tipo.equals("premium"))
                    user = new UserPremium(id, nome);
                else
                    user = new UserAdmin(id, nome);

                User.utilizadores_por_id.put(id, user);
                User.utilizadores_por_nome.put(nome, user);
            }

            // ler regioes
            int n_regioes = sc.nextInt();
            sc.nextLine();  // passa para proxima linha

            for(int i = 0; i < n_regioes; i++) {
                // norte, 7
                String linha = sc.nextLine();
                String[] campos = linha.split(", ");
                String nome_regiao = campos[0];
                int n_caches = Integer.valueOf(campos[1]);
                for(int j = 0; j < n_caches; j++) {
                    // geocache1, basic, 41.1720859, -8.6148178, 3, canudo, livro, oculos
                    linha = sc.nextLine();
                    campos = linha.split(", ");
                    Cache cache;
                    if (campos[1].equals("basic"))
                        cache = new CacheBasic(campos[0], nome_regiao, Double.valueOf(campos[2]), Double.valueOf(campos[3]));
                    else
                        cache = new CachePremium(campos[0], nome_regiao, Double.valueOf(campos[2]), Double.valueOf(campos[3]));
                    int n_items = Integer.valueOf(campos[4]);
                    for (int k = 0; k < n_items; k++) {
                        cache.addItem(new Item(campos[5 + k]));
                    }
                    Cache.caches_por_id.put(campos[0], cache);
                    if (Cache.caches_por_regiao.contains(nome_regiao)) {
                        Cache.caches_por_regiao.get(nome_regiao).add(cache);
                    } else {
                        ArrayList lista = new ArrayList<Cache>();
                        lista.add(cache);
                        Cache.caches_por_regiao.put(nome_regiao, lista);
                    }
                }
            }

            // ler distancias e tempos
            int n_distancias = sc.nextInt();
            sc.nextLine();  // passa para proxima linha
            for(int i = 0; i < n_distancias; i++) {
                String linha = sc.nextLine();
                String[] campos = linha.split(", ");
                String cache1 = campos[0];
                String cache2 = campos[1];
                double distancia = Double.valueOf(campos[2]);
                int tempo = Integer.valueOf(campos[3]);
                Cache c1 = Cache.caches_por_id.get(cache1);
                Cache c2 = Cache.caches_por_id.get(cache2);
                c1.distancia_para_cache.put(c2, distancia);
                //c2.distancia_para_cache.put(c1, distancia);
                c1.tempo_para_cache.put(c2, tempo);
                //c2.tempo_para_cache.put(c1, tempo);
            }

            // ler travel bugs

            //3
            //travelbug1, Maria, geocache1, geocache15

            int n_bugs = sc.nextInt();
            sc.nextLine();  // passa para proxima linha

            for(int i = 0; i < n_bugs; i++) {
                String linha = sc.nextLine();
                String[] campos = linha.split(", ");
                String id = campos[0];
                User utilizador = User.utilizadores_por_nome.get(campos[1]);
                Cache cache1 = Cache.caches_por_id.get(campos[2]);
                Cache cache2 = Cache.caches_por_id.get(campos[3]);
                TravelBug bug = new TravelBug(id, utilizador, cache1, cache2);
                cache2.addItem(bug);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String nome_ficheiro){
        try {
            FileWriter file = new FileWriter(nome_ficheiro);
            // utilizadores
            file.write(User.utilizadores_por_id.size() + "\n");
            for(int id: User.utilizadores_por_id.keys()){
                User user = User.utilizadores_por_id.get(id);
                if(user instanceof UserBasic)
                    file.write(id + ", " + user.getNome() + ", basic\n");
                else if(user instanceof UserPremium)
                    file.write(id + ", " + user.getNome() + ", premium\n");
                else
                    file.write(id + ", " + user.getNome() + ", admin\n");
            }

            // caches
            file.write(Cache.caches_por_regiao.size() + "\n");
            for(String regiao: Cache.caches_por_regiao.keys()) {
                ArrayList<Cache> caches = Cache.caches_por_regiao.get(regiao);
                file.write(regiao + ", " + caches.size() + "\n");
                for(Cache cache: caches) {
                    // geocache1, basic, 41.1720859, -8.6148178, 3, canudo, livro, oculos
                    if(cache instanceof CacheBasic)
                        file.write(cache.getId() + ", basic, " + cache.getLat() + ", " + cache.getLon() + ", ");
                    else
                        file.write(cache.getId() + ", premium, " + cache.getLat() + ", " + cache.getLon() + ", ");
                    ArrayList<Item> items = cache.getItems();
                    file.write("" + items.size());
                    for(Item item: items) {
                        file.write(", " + item.getDescricao());
                    }
                    file.write("\n");
                }
            }

            // distancias
            int n = 0;
            for(String id1: Cache.caches_por_id.keys()) {
                Cache cache1 = Cache.caches_por_id.get(id1);
                for(String id2: Cache.caches_por_id.keys()) {
                    Cache cache2 = Cache.caches_por_id.get(id2);
                    if(cache1.distancia_para_cache.contains(cache2))
                        n += 1;
                }
            }
            file.write(n + "\n");
            for(String id1: Cache.caches_por_id.keys()) {
                Cache cache1 = Cache.caches_por_id.get(id1);
                for(String id2: Cache.caches_por_id.keys()) {
                    Cache cache2 = Cache.caches_por_id.get(id2);
                    if(cache1.distancia_para_cache.contains(cache2)) {
                        file.write(id1 + ", " + id2 + ", " + cache1.distancia_para_cache.get(cache2) + ", " + cache1.tempo_para_cache.get(cache2) + "\n");
                    }
                }
            }

            ArrayList<TravelBug> bugs = new ArrayList<>();
            for(String id: Cache.caches_por_id.keys()) {
                Cache cache = Cache.caches_por_id.get(id);
                ArrayList<Item> items = cache.getItems();
                for(Item item: items)
                    if(item instanceof TravelBug)
                        bugs.add((TravelBug) item);
            }
            file.write(bugs.size() + "\n");
            for(TravelBug bug: bugs) {
                file.write(bug.getDescricao() + ", " + bug.getCriador().getNome() + ", " + bug.getOrigem().getId() + ", " + bug.getAtual().getId() + "\n");
            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        // cache
        try {
            FileWriter file = new FileWriter(ficheiro);
            for(Point2D pt: caches.keys()) {
                Cache cache = caches.get(pt);
                // String s = String.valueOf(5);
                file.write(String.valueOf(cache.gps.x()) + "\n");
                file.write(String.valueOf(cache.gps.y()) + "\n");
                file.write("\n");
                for(User utilizador: cache.logs.keys()) {
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
        }*/
    }
}
