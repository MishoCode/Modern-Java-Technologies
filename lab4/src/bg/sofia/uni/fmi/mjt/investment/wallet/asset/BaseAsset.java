package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

import java.util.Objects;

public abstract class BaseAsset implements Asset {

    private final String id;
    private final String name;

    public BaseAsset(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAsset baseAsset = (BaseAsset) o;
        return getId().equals(baseAsset.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public abstract AssetType getType();
}
