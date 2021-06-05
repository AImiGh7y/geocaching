package geocaching;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Point2D;

import java.io.*;
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
                Cache.grafo_distancias.addEdge(c1.getId(), c2.getId(), distancia);
                Cache.grafo_tempos.addEdge(c1.getId(), c2.getId(), tempo);
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
    /**
     * escreve para o ficheiro
     * @param nome_ficheiro nome do ficheiro para escrever
     */
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
            int n = Cache.grafo_distancias.getNedges();
            file.write(n + "\n");
            for(int i = 0; i < Cache.grafo_distancias.getNvertices(); i++) {
                for(DirectedEdge e: Cache.grafo_distancias.digraph().adj(i)) {
                    String id1 = Cache.grafo_distancias.nameOf(e.from());
                    String id2 = Cache.grafo_distancias.nameOf(e.to());
                    file.write(id1 + ", " + id2 + ", " + e.weight() + ", " + ((int)Cache.grafo_tempos.getWeightBetween(id1, id2)) + "\n");                }
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
    }

    /**
     * escreve para o ficheiro binario
     * @param nome_ficheiro nome do ficheiro para escrever
     */
    public static void readBinFile(String nome_ficheiro){
        try {
            FileInputStream file = new FileInputStream(nome_ficheiro);
            DataInputStream dos = new DataInputStream(new BufferedInputStream(file));
            // utilizadores
            int n = dos.readInt();
            for(int i = 0; i < n; i++) {
                String tipo = dos.readUTF();
                int id = dos.readInt();
                String nome = dos.readUTF();
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

            // caches
            int nregioes = dos.readInt();
            for(int i = 0; i < nregioes; i++) {
                String regiao = dos.readUTF();
                int ncaches = dos.readInt();
                for(int j = 0; j < ncaches; j++) {
                    String tipo = dos.readUTF();
                    String id = dos.readUTF();
                    double lat = dos.readDouble();
                    double lon = dos.readDouble();
                    System.out.println("cache tipo " + tipo + " id: " + id + " lat: " + lat + " lon: " + lon);
                    Cache cache;
                    if(tipo.equals("basic"))
                        cache = new CacheBasic(id, regiao, lat, lon);
                    else
                        cache = new CachePremium(id, regiao, lat, lon);
                    Cache.caches_por_id.put(id, cache);
                    if(Cache.caches_por_regiao.contains(regiao))
                        Cache.caches_por_regiao.get(regiao).add(cache);
                    else {
                        ArrayList<Cache> lista = new ArrayList<Cache>();
                        lista.add(cache);
                        Cache.caches_por_regiao.put(regiao, lista);
                    }

                    int n_items = dos.readInt();
                    for (int k = 0; k < n_items; k++) {
                        cache.addItem(new Item(dos.readUTF()));
                    }
                }
            }

            // distancias
            int n_distancias = dos.readInt();
            for(int i = 0; i < n_distancias; i++) {
                String cache1 = dos.readUTF();
                String cache2 = dos.readUTF();
                double distancia = dos.readDouble();
                int tempo = dos.readInt();
                Cache c1 = Cache.caches_por_id.get(cache1);
                Cache c2 = Cache.caches_por_id.get(cache2);
                Cache.grafo_distancias.addEdge(c1.getId(), c2.getId(), distancia);
                Cache.grafo_tempos.addEdge(c1.getId(), c2.getId(), tempo);
            }

            // ler travel bugs
            int n_bugs = dos.readInt();
            for(int i = 0; i < n_bugs; i++) {
                String descricao = dos.readUTF();
                String username = dos.readUTF();
                String cache1_id = dos.readUTF();
                String cache2_id = dos.readUTF();
                User utilizador = User.utilizadores_por_nome.get(username);
                Cache cache1 = Cache.caches_por_id.get(cache1_id);
                Cache cache2 = Cache.caches_por_id.get(cache2_id);
                TravelBug bug = new TravelBug(descricao, utilizador, cache1, cache2);
                cache2.addItem(bug);
            }

            dos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * escreve para o ficheiro binario
     * @param nome_ficheiro nome do ficheiro para escrever
     */
    public static void writeBinFile(String nome_ficheiro){
        try {
            FileOutputStream file = new FileOutputStream(nome_ficheiro);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(file));
            // utilizadores
            dos.writeInt(User.utilizadores_por_id.size());
            for(int id: User.utilizadores_por_id.keys()){
                User user = User.utilizadores_por_id.get(id);
                if(user instanceof UserBasic)
                    dos.writeUTF("basic");
                else if(user instanceof UserPremium)
                    dos.writeUTF("premium");
                else
                    dos.writeUTF("admin");
                dos.writeInt(id);
                dos.writeUTF(user.getNome());
            }

            // caches
            dos.writeInt(Cache.caches_por_regiao.size());
            for(String regiao: Cache.caches_por_regiao.keys()) {
                ArrayList<Cache> caches = Cache.caches_por_regiao.get(regiao);
                dos.writeUTF(regiao);
                dos.writeInt(caches.size());
                for(Cache cache: caches) {
                    // geocache1, basic, 41.1720859, -8.6148178, 3, canudo, livro, oculos
                    if(cache instanceof CacheBasic)
                        dos.writeUTF("basic");
                    else
                        dos.writeUTF("premium");
                    dos.writeUTF(cache.getId());
                    dos.writeDouble(cache.getLat());
                    dos.writeDouble(cache.getLon());
                    ArrayList<Item> items = cache.getItems();
                    dos.writeInt(items.size());
                    for(Item item: items)
                        dos.writeUTF(item.getDescricao());
                }
            }

            // distancias
            int n = Cache.grafo_distancias.getNedges();
            dos.writeInt(n);
            for(int i = 0; i < Cache.grafo_distancias.getNvertices(); i++) {
                for (DirectedEdge e : Cache.grafo_distancias.digraph().adj(i)) {
                    String id1 = Cache.grafo_distancias.nameOf(e.from());
                    String id2 = Cache.grafo_distancias.nameOf(e.to());
                    dos.writeUTF(id1);
                    dos.writeUTF(id2);
                    dos.writeDouble(e.weight());
                    dos.writeInt((int) Cache.grafo_tempos.getWeightBetween(id1, id2));
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
            dos.writeInt(bugs.size());
            for(TravelBug bug: bugs) {
                dos.writeUTF(bug.getDescricao());
                dos.writeUTF(bug.getCriador().getNome());
                dos.writeUTF(bug.getOrigem().getId());
                dos.writeUTF(bug.getAtual().getId());
            }

            dos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
