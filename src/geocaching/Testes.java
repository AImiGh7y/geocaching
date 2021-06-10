package geocaching;

import java.util.ArrayList;

/**
 * teste das caches e visitas (as caches)
 */
public class Testes {
    public static void testarCache() {
        Cache cache1 = new CacheBasic("cache1", "Norte", 0.5, 2.8);
        Cache cache2 = new CachePremium("cache2", "Norte", 7.2, 3.2);
        Cache.caches_por_id.put("cache1", cache1);
        Cache.caches_por_id.put("cache2", cache2);
        /*
        writeFile("temp.txt");
        caches = new RedBlackBST<>();
        readFile("temp.txt");
        System.out.println("Test tamanho cache: " + caches.size() + " tem que ser 2");
         */
        System.out.println("Cache 1: " + (Cache.caches_por_id.get("cache1") != null) + " tem que ser true");
        System.out.println("Cache 3: " + (Cache.caches_por_id.get("cache3") != null) + " tem que ser false");
    }

    public static void testarVisitas() {
        IO.readFile("input.txt");
        User joana = User.utilizadores_por_nome.get("Joana");
        Cache geocache1 = Cache.caches_por_id.get("geocache1");
        geocache1.visitadaPor(joana, "abc");
        // lista de caches visitadas pela joana
        ArrayList<Cache> caches_visitadas_por_joana = new ArrayList<>();
        for(String id: Cache.caches_por_id.keys()) {
            Cache cache = Cache.caches_por_id.get(id);
            if(cache.foiVisitadaPor(joana))
                caches_visitadas_por_joana.add(cache);
        }
        System.out.println("caches visitadas por joana (tem que ser 1): " + caches_visitadas_por_joana.size());
        System.out.println("primeira cache visitada por joana (tem que ser geocache1): " + caches_visitadas_por_joana.get(0));
    }

    public static void main(String[] args) {
        testarCache();
        testarVisitas();
    }

}
