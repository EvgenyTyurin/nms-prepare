package evgenyt.nms;

import java.util.Objects;

public class NMSShip {

    private final String name;
    private final String url;

    public NMSShip(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NMSShip nmsShip = (NMSShip) o;
        return name.equals(nmsShip.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
