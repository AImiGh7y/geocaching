package geocaching;

public class CachePremium extends Cache {
    public CachePremium(String id, String regiao, double lat, double lon) {
        super(id, regiao, lat, lon);
    }

    public String toString() {
        return getId() + " - " + getRegiao() + " - " + getLat() + "," + getLon() + " [premium]";
    }
}
