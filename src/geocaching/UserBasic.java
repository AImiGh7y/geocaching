package geocaching;

public class UserBasic extends User {
    public UserBasic(int id, String nome) {
        super(id, nome);
    }

    public UserBasic(String nome) {
        super(nome);
    }

    public String toString() {
        return getNome() + " [basic]";
    }

    public String getTipo() { return "basic"; }
}
