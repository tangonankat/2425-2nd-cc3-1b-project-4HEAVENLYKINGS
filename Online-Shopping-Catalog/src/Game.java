import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class OnlineShoppingGUI {
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private DefaultListModel<String> cartModel;
    private List<Product> products;
    private List<Product> cart;
    private String loggedInUser  ;
    private List<User> users = new ArrayList<>();
    private JSpinner quantitySpinner; // Spinner for quantity input
    private JTextField couponField; // Field for entering coupon code
    private Map<String, Double> validCoupons; // Map to store valid coupons

    class User {
        String username;
        String password;
        String role;
        String gender;
        String country;
        int age;
        String phoneNumber;

        public User(String username, String password, String role, String gender, String country, int age, String phoneNumber) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.gender = gender;
            this.country = country;
            this.age = age;
            this.phoneNumber = phoneNumber;
        }
    }

    public OnlineShoppingGUI() {
        users.add(new User("admin", "password", "Admin", "Male", "USA", 30, "123-456-7890"));
        users.add(new User("customer", "1234", "Customer", "Female", "Canada", 25, "987-654-3210"));
        users.add(new User("seller", "5678", "Seller", "Male", "UK", 28, "555-555-5555")); // Added seller for testing
        showLoginScreen();
    }

    private void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(350, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.getContentPane().setBackground(new Color(173, 216, 230));

        JLabel titleLabel = new JLabel("Online Shopping Catalog", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.BLACK);
        JTextField userField = new JTextField(10);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.BLACK);
        JPasswordField passField = new JPasswordField(10);

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(new Color(173, 216, 230));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passField.setEchoChar((char) 0);  // Show password
            } else {
                passField.setEchoChar('*');  // Hide password
            }
        });

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Customer", "Admin", "Seller"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            if (authenticate(username, password, role)) {
                loggedInUser   = username;
                loginFrame.dispose();
                showMainScreen();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid login credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> showRegisterScreen());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(showPasswordCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(roleLabel, gbc);

        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }

    private void showRegisterScreen() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(350, 300);
        registerFrame.setLayout(new BorderLayout());
        registerFrame.getContentPane().setBackground(new Color(224, 255, 255));

        JLabel titleLabel = new JLabel("Register New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(224, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(10);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(10);
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Customer", "Admin", "Seller"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(new Color(224, 255, 255));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passField.setEchoChar((char) 0);  // Show password
            } else {
                passField.setEchoChar('*');  // Hide password
            }
        });

        JButton submitButton = new JButton("Register");
        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.WHITE);

        submitButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (User   user : users) {
                if (user.username.equals(username)) {
                    JOptionPane.showMessageDialog(registerFrame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            users.add(new User(username, password, role, "Not Set", "Not Set", 0, "Not Set"));
            JOptionPane.showMessageDialog(registerFrame, "Registration successful!");
            registerFrame.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(showPasswordCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(roleLabel, gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        registerFrame.add(panel, BorderLayout.CENTER);
        registerFrame.setVisible(true);
    }

    private boolean authenticate(String username, String password, String role) {
        for (User   user : users) {
            if (user.username.equals(username) && user.password.equals(password) && user.role.equals(role)) {
                return true;
            }
        }
        return false;
    }

    class Product {
        private int id;
        private String name;
        private double price;
        private int stock;

        public Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public void reduceStock(int quantity) {
            if (stock >= quantity) {
                stock -= quantity;
            }
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    private void showMainScreen() {
        products = new ArrayList<>();
        cart = new ArrayList<>();
        validCoupons = new HashMap<>();

        // Initialize valid coupons
        validCoupons.put("IND10", 10.0); // 10% discount
        validCoupons.put("IND20", 20.0); // 20% discount
        // Add more coupons as needed

        // Sample products with stock levels
        products.add(new Product(101, "Laptop", 1200.00, 5)); // 5 units in stock
        products.add(new Product(102, "Smartphone", 800.00, 0)); // Out of stock
        // Add more products as needed...

        User loggedIn = getUser (loggedInUser  );

        frame = new JFrame("Online Shopping Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));

        JButton toggleButton = new JButton("Show Dashboard");
        JPanel dashboardContent = new JPanel();
        dashboardContent.setLayout(new BoxLayout(dashboardContent, BoxLayout.Y_AXIS));
        dashboardContent.setVisible(false); // Initially hidden

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(loggedIn.username);
        JTextField genderField = new JTextField(loggedIn.gender);
        JTextField countryField = new JTextField(loggedIn.country);
        JTextField ageField = new JTextField(String.valueOf(loggedIn.age));
        JTextField phoneField = new JTextField(loggedIn.phoneNumber);

        profilePanel.add(new JLabel("Username:"));
        profilePanel.add(usernameField);
        profilePanel.add(new JLabel("Gender:"));
        profilePanel.add(genderField);
        profilePanel.add(new JLabel("Country:"));
        profilePanel.add(countryField);
        profilePanel.add(new JLabel("Age:"));
        profilePanel.add(ageField);
        profilePanel.add(new JLabel("Phone Number:"));
        profilePanel.add(phoneField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            loggedIn.username = usernameField.getText();
            loggedIn.gender = genderField.getText();
            loggedIn.country = countryField.getText();
            loggedIn.age = Integer.parseInt(ageField.getText());
            loggedIn.phoneNumber = phoneField.getText();
            JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
        });

        profilePanel.add(saveButton);
        dashboardContent.add(profilePanel);

        // Address Panel
        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("Shipping Address: (To be updated soon)"));
        dashboardContent.add(addressPanel);

        // Sign Out Panel
        JPanel signOutPanel = new JPanel();
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setBackground(new Color(220, 20, 60));
        signOutButton.setForeground(Color.WHITE);
        signOutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
        signOutPanel.add(signOutButton);
        dashboardContent.add(signOutPanel);

        // Toggle Button Action
        toggleButton.addActionListener(e -> {
            dashboardContent.setVisible(!dashboardContent.isVisible());
            toggleButton.setText(dashboardContent.isVisible() ? "Hide Dashboard" : "Show Dashboard");
            dashboardPanel.revalidate();
            dashboardPanel.repaint();
        });

        dashboardPanel.add(toggleButton);
        dashboardPanel.add(dashboardContent);

        // Product Table
        String[] columns = {"ID", "Name", "Price", "Stock"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        loadProducts();

        cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        
        // Quantity input
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Default quantity is 1, max 10
        JButton addToCartButton = new JButton("Add to Cart");
        JButton removeFromCartButton = new JButton("Remove from Cart");
        JButton checkoutButton = new JButton("Checkout");

        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        checkoutButton.addActionListener(e -> checkout());

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.add(new JLabel("Quantity:"));
        buttonPanel.add(quantitySpinner);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(removeFromCartButton);
        buttonPanel.add(checkoutButton);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Coupon Panel
        JPanel couponPanel = new JPanel();
        couponField = new JTextField(15);
        JButton applyCouponButton = new JButton("Apply Coupon");
        applyCouponButton.addActionListener(e -> applyCoupon());
        couponPanel.add(new JLabel("Coupon Code:"));
        couponPanel.add(couponField);
        couponPanel.add(applyCouponButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JTextField nameField = new JTextField();
        JTextField minPriceField = new JTextField();
        JTextField maxPriceField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Products"));
        searchPanel.add(new JLabel("Name:"));
        searchPanel.add(nameField);
        searchPanel.add(new JLabel("Min Price:"));
        searchPanel.add(minPriceField);
        searchPanel.add(new JLabel("Max Price:"));
        searchPanel.add(maxPriceField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        searchButton.addActionListener(e -> {
            String nameText = nameField.getText().trim().toLowerCase();
            String minText = minPriceField.getText().trim();
            String maxText = maxPriceField.getText().trim();

            double min = minText.isEmpty() ? 0 : Double.parseDouble(minText);
            double max = maxText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxText);

            productTableModel.setRowCount(0); // clear table
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(nameText)
                        && p.getPrice() >= min
                        && p.getPrice() <= max) {
                    productTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getStock()});
                }
            }
        });

        resetButton.addActionListener(e -> {
            nameField.setText("");
            minPriceField.setText("");
            maxPriceField.setText("");
            productTableModel.setRowCount(0);
            loadProducts();
        });

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(dashboardPanel, BorderLayout.WEST);
        mainPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);
        mainPanel.add(couponPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Seller functionality
        if (loggedIn.role.equals("Seller")) {
            showSellerOptions();
        }
    }

    private void showSellerOptions() {
        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(new BoxLayout(sellerPanel, BoxLayout.Y_AXIS));

        JButton addProductButton = new JButton("Add Product");
        JButton removeProductButton = new JButton("Remove Product");
        JButton modifyPriceButton = new JButton("Modify Product Price");

        addProductButton.addActionListener(e -> showAddProductScreen());
        removeProductButton.addActionListener(e -> showRemoveProductScreen());
        modifyPriceButton.addActionListener(e -> showModifyPriceScreen());

        sellerPanel.add(addProductButton);
        sellerPanel.add(removeProductButton);
        sellerPanel.add(modifyPriceButton);

        frame.add(sellerPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    private void showAddProductScreen() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(300, 200);
        addProductFrame.setLayout(new BorderLayout());
        addProductFrame.getContentPane().setBackground(new Color(255, 255, 224));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel productNameLabel = new JLabel("Product Name:");
        JTextField productNameField = new JTextField(15);
        JLabel productPriceLabel = new JLabel("Price:");
        JTextField productPriceField = new JTextField(15);
        JLabel productStockLabel = new JLabel("Stock:");
        JTextField productStockField = new JTextField(15);
        JButton addButton = new JButton("Add Product");

        addButton.addActionListener(e -> {
            String name = productNameField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int stock = Integer.parseInt(productStockField.getText());

            products.add(new Product(products.size() + 1, name, price, stock));
            loadProducts();
            JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!");
            addProductFrame.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(productNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(productPriceLabel, gbc);
        gbc.gridx = 1;
        panel.add(productPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(productStockLabel, gbc);
        gbc.gridx = 1;
        panel.add(productStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        addProductFrame.add(panel, BorderLayout.CENTER);
        addProductFrame.setVisible(true);
    }

    private void showRemoveProductScreen() {
        String[] options = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            options[i] = products.get(i).getName();
        }

        String selectedProduct = (String) JOptionPane.showInputDialog(frame, "Select Product to Remove",
                "Remove Product", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selectedProduct != null) {
            products.removeIf(product -> product.getName().equals(selectedProduct));
            loadProducts();
            JOptionPane.showMessageDialog(frame, "Product removed successfully!");
        }
    }

    private void showModifyPriceScreen() {
        String[] options = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            options[i] = products.get(i).getName();
        }

        String selectedProduct = (String) JOptionPane.showInputDialog(frame, "Select Product to Modify Price",
                "Modify Price", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selectedProduct != null) {
            for (Product product : products) {
                if (product.getName().equals(selectedProduct)) {
                    String newPrice = JOptionPane.showInputDialog(frame, "Enter new price for " + product.getName());
                    try {
                        product.setPrice(Double.parseDouble(newPrice));
                        loadProducts();
                        JOptionPane.showMessageDialog(frame, "Price updated successfully!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }
    }

    private void loadProducts() {
        productTableModel.setRowCount(0);  // Clear existing table rows
        for (Product product : products) {
            // Format the price with a dollar sign
            String formattedPrice = String.format("$%.2f", product.getPrice());
            productTableModel.addRow(new Object[]{product.getId(), product.getName(), formattedPrice, product.getStock()});
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product selectedProduct = products.get(selectedRow);
            int quantity = (int) quantitySpinner.getValue(); // Get the quantity from the spinner

            // Debugging statements
            System.out.println("Selected Product: " + selectedProduct.getName());
            System.out.println("Requested Quantity: " + quantity);
            System.out.println("Available Stock: " + selectedProduct.getStock());

            if (selectedProduct.getStock() >= quantity) {
                // Check if the product is already in the cart
                boolean productExists = false;
                for (int i = 0; i < cart.size(); i++) {
                    Product cartProduct = cart.get(i);
                    if (cartProduct.getId() == selectedProduct.getId()) {
                        // Update the quantity in the cart
                        cartModel.set(i, cartProduct.getName() + " x " + (quantity + 1)); // Update display
                        productExists = true;
                        break;
                    }
                }
                if (!productExists) {
                    cart.add(selectedProduct);
                    cartModel.addElement(selectedProduct.getName() + " x " + quantity); // Add new product with quantity
                }
                selectedProduct.reduceStock(quantity); // Reduce stock by the specified quantity
                JOptionPane.showMessageDialog(frame, quantity + " of " + selectedProduct.getName() + " added to cart.");
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient stock available.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a product to add to the cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart() {
        int selectedIndex = cartModel.getSize() > 0 ? cartModel.getSize() - 1 : -1; // Get the last selected index
        if (selectedIndex >= 0) {
            // Remove the selected item from the cart
            Product removedProduct = cart.remove(selectedIndex);
            cartModel.remove(selectedIndex);
            removedProduct.reduceStock(1); // Increase stock when removed from cart
            JOptionPane.showMessageDialog(frame, "Item removed from cart successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an item to remove from the cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyCoupon() {
        String couponCode = couponField.getText().trim();
        double discount = 0.0;

        // Check if the coupon code is valid
        if (validCoupons.containsKey(couponCode)) {
            discount = validCoupons.get(couponCode);
            JOptionPane.showMessageDialog(frame, "Coupon applied! Discount: " + discount + "%");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid coupon code!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            double total = 0;
            StringBuilder receipt = new StringBuilder("Receipt:\n");
            for (Product product : cart) {
                receipt.append(product.getName()).append(" - ").append(product.getPrice()).append("\n");
                total += product.getPrice();
            }
            receipt.append("\nTotal: ").append(total);

            // Apply coupon discount if available
            String couponCode = couponField.getText().trim();
            if (validCoupons.containsKey(couponCode)) {
                double discount = validCoupons.get(couponCode);
                total -= (total * discount / 100);
                receipt.append("\nDiscount: ").append(discount).append("%");
            }

            // Payment options
            String[] paymentOptions = {"E-cash payment", "Cash on Delivery", "Debit/Credit"};
            String selectedPayment = (String) JOptionPane.showInputDialog(frame, "Select Payment Method:",
                    "Payment", JOptionPane.QUESTION_MESSAGE, null, paymentOptions, paymentOptions[0]);

            if (selectedPayment != null) {
                // Deduct stock for each product in the cart
                for (Product product : cart) {
                    product.reduceStock(1); // Deduct stock after checkout
                }
                receipt.append("\nPayment Method: ").append(selectedPayment);
                JOptionPane.showMessageDialog(frame, receipt.toString(), "Checkout", JOptionPane.INFORMATION_MESSAGE);
                cart.clear(); // Clear the cart after checkout
                cartModel.clear(); // Clear the cart model
                loadProducts(); // Reload products to update stock display
            } else {
                JOptionPane.showMessageDialog(frame, "Payment method selection was cancelled.", "Checkout", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private User getUser (String username) {
        for (User   user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineShoppingGUI());
    }
}
