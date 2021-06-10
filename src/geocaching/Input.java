package geocaching;

import java.util.Scanner;

public class Input {
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
}
