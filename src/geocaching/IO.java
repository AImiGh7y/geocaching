package geocaching;

import edu.princeton.cs.algs4.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Le e escreve dum ficheiro.
 */
public class IO {
/*
6
1, Manuel, basic
2, Pedro, basic
3, Fernando, admin
4, Joana, basic
5, Maria, premium
6, Filomena, admin
3
norte, 7
geocache1, basic, 41.1720859, -8.6148178, 3, canudo, livro, oculos
geocache2, basic, 41.1605747, -8.5855818, 3, bola, brinquedo, camisola
geocache3, basic, 41.1666825, -8.6751895, 3, pedra, flor, areia
geocache4, basic, 41.158881, -8.6337921, 1, flauta
geocache5, premium, 41.1492281, -8.6120275, 0
geocache6, basic, 41.1504982, -8.6654695, 0
geocache7, basic, 41.2421226, -8.6807401, 1, bilhetes_aviao
centro, 5
geocache8, basic, 40.2094433, -8.4208601, 2, canudo, capa
geocache9, basic, 40.2027321, -8.4333746, 0
geocache10, premium, 40.188891, -8.410378, borracha, chaves
geocache11, basic, 40.6394116, -8.6621375, 1, ovos_moles
geocache12, basic, 40.6597787, -7.9136921, 1, espada
sul, 6
geocache13, basic, 38.6942546, -9.2129457, 0
geocache14, basic, 38.7288464, -9.1481854, 0
geocache15, basic, 38.7756417, -9.1325313, 1, bilhetes_aviao
geocache16, premium, 38.7527152, -9.1869627, 0
geocache17, basic, 38.7612341, -9.1639843, 0
geocache18, basic, 38.9369329, -9.3290594, 0
50
geocache1, geocache2, 5.2, 60
geocache1, geocache3, 8.2, 102
geocache1, geocache4, 6.2, 70
geocache1, geocache5, 4.2, 40
geocache1, geocache6, 3.2, 35
geocache1, geocache7, 5.2, 66
geocache2, geocache3, 8.5, 102
geocache2, geocache4, 5.8, 65
geocache2, geocache5, 7.2, 72
geocache2, geocache6, 5.4, 53
geocache2, geocache7, 4.5, 51
geocache3, geocache4, 2.7, 30
geocache3, geocache5, 3.2, 35
geocache3, geocache6, 4.1, 43
geocache3, geocache7, 8.5, 89
geocache4, geocache5, 3.7, 41
geocache4, geocache6, 4.3, 49
geocache4, geocache7, 7.4, 72
geocache5, geocache6, 7.4, 71
geocache5, geocache7, 5.2, 53
geocache6, geocache7, 7.4, 73
geocache7, geocache8, 130, 1500
geocache8, geocache9, 7.2, 72
geocache8, geocache10, 4.7, 51
geocache8, geocache11, 3.4, 41
geocache8, geocache12, 5.3, 55
geocache9, geocache10, 2.2, 24
geocache9, geocache11, 1.7, 21
geocache9, geocache12, 6.4, 71
geocache10, geocache11, 4.7, 52
geocache10, geocache12, 3.5, 36
geocache10, geocache13, 6.2, 67
geocache11, geocache12, 2.8, 32
geocache12, geocache13, 197, 2460
geocache13, geocache14, 2.6, 31
geocache13, geocache15, 4.5, 51
geocache13, geocache16, 3.8, 45
geocache13, geocache17, 4.2, 51
geocache13, geocache18, 2.8, 32
geocache14, geocache15, 3.3, 41
geocache14, geocache16, 4.8, 58
geocache14, geocache17, 5.2, 62
geocache14, geocache18, 3.7, 41
geocache15, geocache16, 4.4, 53
geocache15, geocache17, 5.3, 64
geocache15, geocache18, 3.8, 43
geocache16, geocache17, 1.2, 22
geocache16, geocache18, 2.1, 35
geocache17, geocache18, 3.8, 44
geocache7, geocache15, 330, 45
3
travelbug1, Maria, geocache1, geocache15
travelbug2, Pedro, geocache2, geocache17
travelbug3, Filomena, geocache3, geocache12
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
                System.out.println(id  + " - " + nome + " - " + tipo);

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
            System.out.println("n? " + n_regioes);

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
                    if(campos[1].equals("basic"))
                        cache = new CacheBasic(campos[0], nome_regiao, Double.valueOf(campos[2]), Double.valueOf(campos[3]));
                    else
                        cache = new CachePremium(campos[0], nome_regiao, Double.valueOf(campos[2]), Double.valueOf(campos[3]));
                    int n_items = Integer.valueOf(campos[4]);
                    for(int k = 0; k < n_items; k++) {
                        cache.addItem(new Item(campos[5+k]));
                    }
                    Cache.caches_por_gps.put(new Point2D(Double.valueOf(campos[2]), Double.valueOf(campos[3])), cache);
                    Cache.caches_por_regiao.put(nome_regiao, cache);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String nome_ficheiro){
        // user

        try {
            FileWriter file = new FileWriter(nome_ficheiro);
            file.write(User.utilizadores_por_id.size() + "\n");

/*            for(String nome_utilizador: utilizadores.keys()){
                User utilizador = utilizadores.get(nome_utilizador);
                file.write(nome_utilizador + "\n");
            }
            file.close();*/
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
