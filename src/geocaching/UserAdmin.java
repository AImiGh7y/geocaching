package geocaching;

import java.util.ArrayList;

public class UserAdmin extends User {
    public UserAdmin(int id, String nome) {
        super(id, nome);
    }

    public UserAdmin(String nome) {
        super(nome);
    }

    public String toString() {
        return getNome() + " [admin]";
    }

    public String getTipo() { return "admin"; }
}
