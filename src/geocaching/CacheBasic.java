package geocaching;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SequentialSearchST;

import java.util.ArrayList;

public class CacheBasic extends Cache {
    public CacheBasic(String id, String regiao, double lat, double lon) {
        super(id, regiao, lat, lon);
    }

    public String toString() {
        return getId() + " - " + getRegiao() + " - " + getLat() + "," + getLon() + " [basic]";
    }

    public String getTipo() { return "Basic"; }
}

