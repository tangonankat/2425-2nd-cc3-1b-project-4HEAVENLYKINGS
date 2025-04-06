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

    public OnlineShoppingGUI() {
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

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Customer", "Admin"};
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

        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(loginFrame, "Registration feature coming soon!"));

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

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(roleLabel, gbc);

        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }

    private boolean authenticate(String username, String password, String role) {
        return ("admin".equals(username) && "password".equals(password)) ||
               ("customer".equals(username) && "1234".equals(password));
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
    }

    private void showMainScreen() {
        products = new ArrayList<>();
        cart = new ArrayList<>();

        products.add(new Product(101, "Laptop", 1200.0));
        products.add(new Product(102, "Smartphone", 800.0));
        products.add(new Product(103, "Headphones", 150.0));
        products.add(new Product(104, "Smartwatch", 200.0));
        products.add(new Product(105, "Tablet", 600.0));

        frame = new JFrame("Online Shopping Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // === Dashboard Tabs ===
        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile Tab
        JPanel profilePanel = new JPanel();
        profilePanel.add(new JLabel("Username: " + loggedInUser));
        tabbedPane.addTab("Profile", profilePanel);

        // Address Tab
        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("Shipping Address: (To be updated soon)"));
        tabbedPane.addTab("Address", addressPanel);

        // Sign Out Tab
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

        // === Product Table ===
        String[] columns = {"ID", "Name", "Price"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        loadProducts();

        // === Cart Panel ===
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

        frame.add(tabbedPane, BorderLayout.NORTH); // 👈 Tabs on top
        frame.add(new JScrollPane(productTable), BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product product = products.get(selectedRow);
            cart.add(product);
            cartModel.addElement(product.getName() + " - $" + product.getPrice());
        }
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            return;
        }
        String[] options = {"Cash on Delivery", "Online Payment (Debit/Credit Card)"};
        int choice = JOptionPane.showOptionDialog(frame, "Select Payment Method:", "Payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice != -1) {
            JOptionPane.showMessageDialog(frame, "Checkout successful!");
            cart.clear();
            cartModel.clear();
        }
    }

    private void loadProducts() {
        productTableModel.setRowCount(0);
        for (Product product : products) {
            productTableModel.addRow(new Object[]{product.getId(), product.getName(), product.getPrice()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlineShoppingGUI::new);
    }
}
