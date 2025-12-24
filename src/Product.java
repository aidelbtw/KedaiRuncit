public class Product {
    private String model;
    private double price;
    private int[] stockLevels; // Array to store stock for each outlet

    public Product(String model, double price, int[] stockLevels) {
        this.model = model;
        this.price = price;
        this.stockLevels = stockLevels;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public int getStockByOutletIndex(int outletIndex) {
        if (outletIndex >= 0 && outletIndex < stockLevels.length) {
            return stockLevels[outletIndex];
        }
        return 0;
    }

    public void setStockByOutletIndex(int outletIndex, int quantity) {
        if (outletIndex >= 0 && outletIndex < stockLevels.length) {
            stockLevels[outletIndex] = quantity;
        }
    }

    public int getStockByOutletCode(String outletCode, DataManager dm) {
        int outletIndex = getOutletIndex(outletCode, dm);
        if (outletIndex >= 0 && outletIndex < stockLevels.length) {
            return stockLevels[outletIndex];
        }
        return 0;
    }

    public void setStockByOutletCode(String outletCode, DataManager dm, int quantity) {
        int outletIndex = getOutletIndex(outletCode, dm);
        if (outletIndex >= 0 && outletIndex < stockLevels.length) {
            stockLevels[outletIndex] = quantity;
        }
    }

    private int getOutletIndex(String outletCode, DataManager dm) {
        for (int i = 0; i < dm.getOutletCodes().size(); i++) {
            if (dm.getOutletCodes().get(i).equals(outletCode)) {
                return i;
            }
        }
        return -1;
    }
}