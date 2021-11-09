package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Crypto extends BaseAsset {
    public Crypto(String id, String name) {
        super(id, name);
    }

    @Override
    public AssetType getType() {
        return AssetType.CRYPTO;
    }
}
