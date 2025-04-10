import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private double price;
    private double originalPrice; // For sale/discount tracking
    private int stock;
    private int minStockThreshold = 5; // Reorder point
    private String imagePath;
    private String category;
    private double averageRating;
    private int reviewCount;

    public Product(int id, String name, double price, int stock) {
        this(id, name, price, stock, "images/default.png");
    }

    public Product(int id, String name, double price, int stock, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = "No description available";
        this.price = price;
        this.originalPrice = price;
        this.stock = stock;
        this.imagePath = imagePath;
        this.category = "Uncategorized";
        this.averageRating = 0;
        this.reviewCount = 0;
    }

    // Getters and setters
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSalePrice(double salePrice) {
        this.originalPrice = this.price;
        this.price = salePrice;
    }

    public void endSale() {
        this.price = this.originalPrice;
    }

    public boolean needsRestock() {
        return stock <= minStockThreshold;
    }

    public void addReview(int rating) {
        double totalRating = averageRating * reviewCount;
        reviewCount++;
        averageRating = (totalRating + rating) / reviewCount;
    }
    
    @Override
    public String toString() {
        String priceStr = (price < originalPrice) ? 
            String.format("$%.2f (was $%.2f)", price, originalPrice) : 
            String.format("$%.2f", price);
        return String.format("%s (%s) - %s", name, category, priceStr);
    }
}
