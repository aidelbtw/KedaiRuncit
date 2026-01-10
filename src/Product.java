public class Product {
    private String model;
    private double price;
    private int[] stockLevels; 

    public Product(String model, double price, int[] stockLevels) {
        this.model = model;
        this.price = price;
        this.stockLevels = stockLevels;
    }

    public String getModel() { return model; }
    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getStockByOutletCode(String outletCode, DataManager dm) {
        int index = dm.getOutletIndex(outletCode);
        if (index >= 0 && index < stockLevels.length) return stockLevels[index];
        return 0;
    }

    public void setStockByOutletCode(String outletCode, DataManager dm, int quantity) {
        int index = dm.getOutletIndex(outletCode);
        if (index >= 0 && index < stockLevels.length) stockLevels[index] = quantity;
    }
    
    public int getStockByOutletIndex(int i) {
        if(i >= 0 && i < stockLevels.length) return stockLevels[i];
        return 0;
    }
}
