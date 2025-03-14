classDiagram
    %% User hierarchy
    class User {
        - int id
        - String name
        - String email
        - String password
        + register(): void
        + login(): boolean
        + logout(): void
    }
    
    class Customer {
        - String address
        - Cart cart
        + placeOrder(o: Order): void
        + viewOrderHistory(): List<Order>
        + addToCart(p: Product): void
    }
    
    class Admin {
        - String adminCode
        + manageProducts(): void
        + viewReports(): void
    }

    class Seller {
        - List<Product> products
        + addProduct(p: Product): void
        + updateProduct(p: Product, price: double, description: String, image: String): void
        + removeProduct(p: Product): void
    }
    
    %% Product hierarchy
    class Product {
        - int id
        - String name
        - double price
        - String description
        - String image
        + getDetails(): String
        + updateStock(): void
    }

    class PhysicalProduct {
        - double weight
        - String dimensions
        + calculateShippingCost(): double
    }

    class DigitalProduct {
        - double fileSize
        - String downloadLink
        + generateDownloadLink(): String
    }

    %% Cart System
    class Cart {
        - List<Product> items
        + addItem(p: Product): void
        + removeItem(p: Product): void
        + clearCart(): void
        + getTotal(): double
    }

    %% Order and Payment
    class Order {
        - int id
        - Customer customer
        - List<Product> items
        - double totalPrice
        - PaymentMethod paymentMethod
        + placeOrder(): void
        + cancelOrder(): void
    }

    class Payment {
        - int id
        - Order order
        - PaymentMethod paymentMethod
        - String status
        + processPayment(): boolean
    }

    %% Payment Method hierarchy
    class PaymentMethod {
        + processPayment(): boolean
    }

    class OnlinePayment {
        - String transactionId
        - String paymentGateway
        + validateTransaction(): boolean
    }

    class CashOnDelivery {
        - boolean isPaidOnDelivery
        + confirmPayment(): void
    }

    %% Relationships (Inheritance)
    Customer <|-- User
    Seller <|-- User
    Admin <|-- User
    PhysicalProduct <|-- Product
    DigitalProduct <|-- Product
    OnlinePayment <|-- PaymentMethod
    CashOnDelivery <|-- PaymentMethod

    %% Relationships (Associations)
    Order  -->  Product 
    Customer  -->  Order 
    Payment  <--  Order
    Order  -->  PaymentMethod 
    Customer  -->  Cart 
    Cart  <|--  Product
    Seller  -->  Product
