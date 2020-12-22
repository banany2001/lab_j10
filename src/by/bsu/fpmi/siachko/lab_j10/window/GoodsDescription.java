package by.bsu.fpmi.siachko.lab_j10.window;

public class GoodsDescription {

    private String goodsName;
    private String importingCountry;
    private int volume;

    public GoodsDescription(String goodsName, String importingCountry, int volume) {
        this.goodsName = goodsName;
        this.importingCountry = importingCountry;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return goodsName + "{" +
                "Importing country:" + importingCountry +
                ", Volume:" + volume +
                "}";
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getImportingCountry() {
        return importingCountry;
    }

    public int getVolume() {
        return volume;
    }
}
