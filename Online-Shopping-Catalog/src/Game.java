import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OnlineShoppingGUI {
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private DefaultListModel<String> cartModel;
    private List<Product> products;
    private List<Product> cart;
    private String loggedInUser;
    private List<User> users = new ArrayList<>();

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
                loggedInUser = username;
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

            for (User user : users) {
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
        for (User user : users) {
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

        public Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
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

        public void setPrice(double price) {
            this.price = price;
        }
    }

    private void showMainScreen() {
        products = new ArrayList<>();
        cart = new ArrayList<>();

        products.add(new Product(101, "Laptop", 1200.0));
        products.add(new Product(102, "Smartphone", 800.0));
        products.add(new Product(103, "Headphones", 150.0));
        products.add(new Product(104, "Smartwatch", 200.0));
        products.add(new Product(105, "Tablet", 600.0));
        products.add(new Product(106, "Bluetooth Speaker", 90.0));
        products.add(new Product(107, "Gaming Mouse", 45.0));
        products.add(new Product(108, "Mechanical Keyboard", 100.0));
        products.add(new Product(109, "HD Monitor", 250.0));
        products.add(new Product(110, "External Hard Drive", 110.0));
        products.add(new Product(111, "Flash Drive", 20.0));
        products.add(new Product(112, "Graphics Card", 500.0));
        products.add(new Product(113, "Power Bank", 40.0));
        products.add(new Product(114, "Webcam", 75.0));
        products.add(new Product(115, "WiFi Router", 60.0));
        products.add(new Product(116, "Projector", 300.0));
        products.add(new Product(117, "Portable SSD", 150.0));
        products.add(new Product(118, "Action Camera", 220.0));
        products.add(new Product(119, "DSLR Camera", 900.0));
        products.add(new Product(120, "Microwave Oven", 180.0));
        products.add(new Product(121, "Air Fryer", 120.0));
        products.add(new Product(122, "Blender", 80.0));
        products.add(new Product(123, "Coffee Maker", 95.0));
        products.add(new Product(124, "Electric Kettle", 40.0));
        products.add(new Product(125, "Rice Cooker", 70.0));
        products.add(new Product(126, "Smart TV", 1000.0));
        products.add(new Product(127, "Washing Machine", 750.0));
        products.add(new Product(128, "Refrigerator", 900.0));
        products.add(new Product(129, "Air Conditioner", 1100.0));
        products.add(new Product(130, "Electric Fan", 50.0));
        products.add(new Product(131, "Desk Lamp", 25.0));
        products.add(new Product(132, "Office Chair", 160.0));
        products.add(new Product(133, "Standing Desk", 300.0));
        products.add(new Product(134, "Bookshelf", 90.0));
        products.add(new Product(135, "Printer", 130.0));
        products.add(new Product(136, "Scanner", 140.0));
        products.add(new Product(137, "Vacuum Cleaner", 180.0));
        products.add(new Product(138, "Iron", 40.0));
        products.add(new Product(139, "Toaster", 35.0));
        products.add(new Product(140, "Water Dispenser", 160.0));
        products.add(new Product(141, "Tablet Pen", 45.0));
        products.add(new Product(142, "Noise Cancelling Headphones", 250.0));
        products.add(new Product(143, "Smart Light Bulb", 30.0));
        products.add(new Product(144, "Thermostat", 200.0));
        products.add(new Product(145, "Fitness Tracker", 120.0));
        products.add(new Product(146, "VR Headset", 600.0));
        products.add(new Product(147, "Electric Scooter", 400.0));
        products.add(new Product(148, "Drone", 550.0));
        products.add(new Product(149, "Smart Lock", 180.0));
        products.add(new Product(150, "Security Camera", 150.0));

        User loggedIn = getUser(loggedInUser);

        frame = new JFrame("Online Shopping Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.add(new JLabel("Username: " + loggedIn.username));
        profilePanel.add(new JLabel("Gender: " + loggedIn.gender));
        profilePanel.add(new JLabel("Country: " + loggedIn.country));
        profilePanel.add(new JLabel("Age: " + loggedIn.age));
        profilePanel.add(new JLabel("Phone Number: " + loggedIn.phoneNumber));

        JButton toggleButton = new JButton("Show More Info");
        JPanel extraInfoPanel = new JPanel();
        extraInfoPanel.setLayout(new BoxLayout(extraInfoPanel, BoxLayout.Y_AXIS));
        extraInfoPanel.add(new JLabel("This is additional profile info"));

        toggleButton.addActionListener(e -> {
            if (extraInfoPanel.isVisible()) {
                extraInfoPanel.setVisible(false);
                toggleButton.setText("Show More Info");
            } else {
                extraInfoPanel.setVisible(true);
                toggleButton.setText("Hide Info");
            }
        });

        profilePanel.add(toggleButton);
        profilePanel.add(extraInfoPanel);
        extraInfoPanel.setVisible(false);
        tabbedPane.addTab("Profile", profilePanel);

        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("Shipping Address: (To be updated soon)"));
        tabbedPane.addTab("Address", addressPanel);

        JPanel signOutPanel = new JPanel();
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setBackground(new Color(220, 20, 60));
        signOutButton.setForeground(Color.WHITE);
        signOutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
        signOutPanel.add(signOutButton);
        tabbedPane.addTab("Sign Out", signOutPanel);

        // Product Table
        String[] columns = {"ID", "Name", "Price"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        loadProducts();

        cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        JButton addToCartButton = new JButton("Add to Cart");
        JButton checkoutButton = new JButton("Checkout");

        addToCartButton.addActionListener(e -> addToCart());
        checkoutButton.addActionListener(e -> checkout());

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(addToCartButton);
        buttonPanel.add(checkoutButton);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 🔍 Search Panel
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
                    productTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice()});
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
        frame.add(tabbedPane, BorderLayout.NORTH);
        frame.add(new JScrollPane(productTable), BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);
        frame.add(searchPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
}


    // Show Add Product Screen for sellers
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
        JButton addButton = new JButton("Add Product");

        addButton.addActionListener(e -> {
            String name = productNameField.getText();
            double price = Double.parseDouble(productPriceField.getText());

            products.add(new Product(products.size() + 1, name, price));
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
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        addProductFrame.add(panel, BorderLayout.CENTER);
        addProductFrame.setVisible(true);
    }

    // Show Modify Price Screen for sellers
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

    // Helper methods to add products to the table and cart
    private void loadProducts() {
        productTableModel.setRowCount(0);  // Clear existing table rows
        for (Product product : products) {
            productTableModel.addRow(new Object[]{product.getId(), product.getName(), product.getPrice()});
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product selectedProduct = products.get(selectedRow);
            cart.add(selectedProduct);
            cartModel.addElement(selectedProduct.getName());
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a product to add to the cart", "Error", JOptionPane.ERROR_MESSAGE);
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

        // Payment options
        String[] paymentOptions = {"E-cash payment", "Cash on Delivery", "Debit/Credit"};
        String selectedPayment = (String) JOptionPane.showInputDialog(frame, "Select Payment Method:",
                "Payment", JOptionPane.QUESTION_MESSAGE, null, paymentOptions, paymentOptions[0]);

        if (selectedPayment != null) {
            receipt.append("\nPayment Method: ").append(selectedPayment);
            JOptionPane.showMessageDialog(frame, receipt.toString(), "Checkout", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Payment method selection was cancelled.", "Checkout", JOptionPane.WARNING_MESSAGE);
        }
    }
}

    private User getUser(String username) {
        for (User user : users) {
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
