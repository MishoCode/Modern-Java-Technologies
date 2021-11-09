package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Gold extends BaseAsset {
    public Gold(String id, String name) {
        super(id, name);
    }

    @Override
    public AssetType getType() {
        return AssetType.GOLD;
    }
}
