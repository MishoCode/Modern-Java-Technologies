package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public abstract class BaseAsset implements Asset {

    private String id;
    private String name;

    public BaseAsset(String id, String name) {
        this.id = id;
        this.name = name;
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
