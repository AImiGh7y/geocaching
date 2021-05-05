package geocaching;

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
        System.out.println("Cache tem ponto 0.5,2.8: " + (Cache.caches_por_id.get("cache1") != null) + " tem que ser true");
        System.out.println("Cache tem ponto 6.2,3.8: " + (Cache.caches_por_id.get("cache3") != null) + " tem que ser false");
    }

    public static void main(String[] args) {
        testarCache();
    }

}
