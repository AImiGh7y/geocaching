package geocaching;

public class UserPremium extends User {
    public UserPremium(int id, String nome) {
        super(id, nome);
    }

    public UserPremium(String nome) {
        super(nome);
    }

    public String toString() {
        return getNome() + " [premium]";
    }

    public String getTipo() { return "premium"; }
}
