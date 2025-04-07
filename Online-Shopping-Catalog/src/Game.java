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

        products.add(new Product(101, "Laptop", 1200.00));
        products.add(new Product(102, "Smartphone", 800.00));
        products.add(new Product(103, "Headphones", 150.00));
        products.add(new Product(104, "Smartwatch", 200.00));
        products.add(new Product(105, "Tablet", 600.00));
        products.add(new Product(106, "Bluetooth Speaker", 90.00));
        products.add(new Product(107, "Gaming Mouse", 45.00));
        products.add(new Product(108, "Mechanical Keyboard", 100.00));
        products.add(new Product(109, "HD Monitor", 250.0));
        products.add(new Product(110, "External Hard Drive", 110.00));
        products.add(new Product(111, "Flash Drive", 20.00));
        products.add(new Product(112, "Graphics Card", 500.00));
        products.add(new Product(113, "Power Bank", 40.00));
        products.add(new Product(114, "Webcam", 75.00));
        products.add(new Product(115, "WiFi Router", 60.00));
        products.add(new Product(116, "Projector", 300.00));
        products.add(new Product(117, "Portable SSD", 150.00));
        products.add(new Product(118, "Action Camera", 220.00));
        products.add(new Product(119, "DSLR Camera", 900.00));
        products.add(new Product(120, "Microwave Oven", 180.00));
        products.add(new Product(121, "Air Fryer", 120.00));
        products.add(new Product(122, "Blender", 80.00));
        products.add(new Product(123, "Coffee Maker", 95.00));
        products.add(new Product(124, "Electric Kettle", 40.00));
        products.add(new Product(125, "Rice Cooker", 70.00));
        products.add(new Product(126, "Smart TV", 1000.00));
        products.add(new Product(127, "Washing Machine", 750.00));
        products.add(new Product(128, "Refrigerator", 900.00));
        products.add(new Product(129, "Air Conditioner", 1100.00));
        products.add(new Product(130, "Electric Fan", 50.00));
        products.add(new Product(131, "Desk Lamp", 25.00));
        products.add(new Product(132, "Office Chair", 160.00));
        products.add(new Product(133, "Standing Desk", 300.00));
        products.add(new Product(134, "Bookshelf", 90.00));
        products.add(new Product(135, "Printer", 130.00));
        products.add(new Product(136, "Scanner", 140.00));
        products.add(new Product(137, "Vacuum Cleaner", 180.00));
        products.add(new Product(138, "Iron", 40.00));
        products.add(new Product(139, "Toaster", 35.00));
        products.add(new Product(140, "Water Dispenser", 160.00));
        products.add(new Product(141, "Tablet Pen", 45.00));
        products.add(new Product(142, "Noise Cancelling Headphones", 250.00));
        products.add(new Product(143, "Smart Light Bulb", 30.00));
        products.add(new Product(144, "Thermostat", 200.00));
        products.add(new Product(145, "Fitness Tracker", 120.00));
        products.add(new Product(146, "VR Headset", 600.0));
        products.add(new Product(147, "Electric Scooter", 400.00));
        products.add(new Product(148, "Drone", 550.00));
        products.add(new Product(149, "Smart Lock", 180.00));
        products.add(new Product(150, "Security Camera", 150.00));
        products.add(new Product(151, "Smartphone Case", 25.00));
        products.add(new Product(152, "Wireless Charger", 30.00));
        products.add(new Product(153, "USB-C Hub", 40.00));
        products.add(new Product(154, "Gaming Headset", 80.00));
        products.add(new Product(155, "Portable Bluetooth Speaker", 60.00));
        products.add(new Product(156, "Smartwatch Charger", 20.00));
        products.add(new Product(157, "Laptop Stand", 35.00));
        products.add(new Product(158, "HDMI Cable", 15.00));
        products.add(new Product(159, "Ethernet Cable", 10.00));
        products.add(new Product(160, "Wireless Mouse", 25.00));
        products.add(new Product(161, "Wireless Keyboard", 50.00));
        products.add(new Product(162, "Laptop Sleeve", 30.00));
        products.add(new Product(163, "Screen Protector", 15.00));
        products.add(new Product(164, "Phone Stand", 20.00));
        products.add(new Product(165, "Car Phone Mount", 25.00));
        products.add(new Product(166, "Smart Light Strip", 40.00));
        products.add(new Product(167, "Smart Thermostat", 150.00));
        products.add(new Product(168, "Smart Plug", 30.00));
        products.add(new Product(169, "Home Security System", 200.00));
        products.add(new Product(170, "Smart Doorbell", 120.00));
        products.add(new Product(171, "Smart Smoke Detector", 50.00));
        products.add(new Product(172, "Smart Water Leak Detector", 70.00));
        products.add(new Product(173, "Smart Garage Door Opener", 100.00));
        products.add(new Product(174, "Smart Home Hub", 80.00));
        products.add(new Product(175, "Smart Air Purifier", 150.00));
        products.add(new Product(176, "Smart Coffee Maker", 120.00));
        products.add(new Product(177, "Smart Refrigerator", 2000.00));
        products.add(new Product(178, "Smart Oven", 800.00));
        products.add(new Product(179, "Smart Dishwasher", 700.00));
        products.add(new Product(180, "Smart Washing Machine", 900.00));
        products.add(new Product(181, "Smart Dryer", 800.00));
        products.add(new Product(182, "Electric Toothbrush", 100.00));
        products.add(new Product(183, "Electric Shaver", 80.00));
        products.add(new Product(184, "Hair Dryer", 50.0));
        products.add(new Product(185, "Hair Straightener", 60.00));
        products.add(new Product(186, "Electric Razor", 70.00));
        products.add(new Product(187, "Electric Massager", 90.00));
        products.add(new Product(188, "Electric Blanket", 100.00));
        products.add(new Product(189, "Air Purifier", 150.00));
        products.add(new Product(190, "Humidifier", 50.00));
        products.add(new Product(191, "Dehumidifier", 80.00));
        products.add(new Product(192, "Electric Kettle", 40.00));
        products.add(new Product(193, "Coffee Grinder", 30.00));
        products.add(new Product(194, "Electric Skillet", 60.00));
        products.add(new Product(195, "Slow Cooker", 70.00));
        products.add(new Product(196, "Pressure Cooker", 90.00));
        products.add(new Product(197, "Rice Cooker", 50.00));
        products.add(new Product(198, "Food Processor", 120.00));
        products.add(new Product(199, "Blender", 80.00));
        products.add(new Product(200, "Juicer", 100.00));
        products.add(new Product(201, "Electric Grill", 150.00));
        products.add(new Product(202, "Electric Fryer", 120.00));
        products.add(new Product(203, "Toaster Oven", 70.00));
        products.add(new Product(204, "Induction Cooktop", 200.00));
        products.add(new Product(205, "Electric Pressure Washer", 300.0));
        products.add(new Product(206, "Cordless Vacuum Cleaner", 150.00));
        products.add(new Product(207, "Robot Vacuum", 250.00));
        products.add(new Product(208, "Handheld Vacuum", 80.00));
        products.add(new Product(209, "Steam Mop", 100.00));
        products.add(new Product(210, "Car Vacuum Cleaner", 40.00));
        products.add(new Product(211, "Electric Lawn Mower", 400.00));
        products.add(new Product(212, "Electric Hedge Trimmer", 150.00));
        products.add(new Product(213, "Electric Chainsaw", 200.00));
        products.add(new Product(214, "Electric Leaf Blower", 100.00));
        products.add(new Product(215, "Electric Pressure Washer", 300.00));
        products.add(new Product(216, "Electric Snow Shovel", 200.00));
        products.add(new Product(217, "Electric Grill", 150.00));
        products.add(new Product(218, "Electric Fryer", 120.00));
        products.add(new Product(219, "Electric Skillet", 60.00));
        products.add(new Product(220, "Electric Wok", 80.00));
        products.add(new Product(221, "Electric Fondue Pot", 70.00));
        products.add(new Product(222, "Electric Popcorn Maker", 40.00));
        products.add(new Product(223, "Electric Ice Cream Maker", 100.00));
        products.add(new Product(224, "Electric Yogurt Maker", 50.00));
        products.add(new Product(225, "Electric Food Dehydrator", 80.00));
        products.add(new Product(226, "Electric Egg Cooker", 30.00));
        products.add(new Product(227, "Electric Rice Cooker", 50.00));
        products.add(new Product(228, "Electric Pressure Cooker", 90.00));
        products.add(new Product(229, "Electric Slow Cooker", 70.00));
        products.add(new Product(230, "Electric Food Steamer", 60.00));
        products.add(new Product(231, "Electric Griddle", 80.00));
        products.add(new Product(232, "Electric Sandwich Maker", 40.00));
        products.add(new Product(233, "Electric Panini Press", 50.00));
        products.add(new Product(234, "Electric Waffle Maker", 30.00));
        products.add(new Product(235, "Electric Crepe Maker", 40.00));
        products.add(new Product(236, "Electric Pizza Maker", 60.00));
        products.add(new Product(237, "Electric Hot Dog Maker", 30.00));
        products.add(new Product(238, "Electric Popcorn Maker", 40.00));
        products.add(new Product(239, "Electric Candy Floss Maker", 50.00));
        products.add(new Product(240, "Electric Chocolate Fountain", 70.00));
        products.add(new Product(241, "Electric Food Processor", 120.00));
        products.add(new Product(242, "Electric Blender", 80.00));
        products.add(new Product(243, "Electric Juicer", 100.00));
        products.add(new Product(244, "Electric Mixer", 60.99));
        products.add(new Product(245, "Electric Food Chopper", 40.99));
        products.add(new Product(246, "Electric Meat Grinder", 150.99));
        products.add(new Product(247, "Electric Pasta Maker", 100.99));
        products.add(new Product(248, "Electric Ice Maker", 200.99));
        products.add(new Product(249, "Electric Water Dispenser", 150.99));
        products.add(new Product(250, "Electric Water Heater", 100.0));
        products.add(new Product(251, "Smartphone Wallet Case", 29.99));
        products.add(new Product(252, "Wireless Earbuds", 79.99));
        products.add(new Product(253, "Portable Power Bank", 39.99));
        products.add(new Product(254, "Bluetooth Headphones", 89.99));
        products.add(new Product(255, "Smartphone Tripod", 24.99));
        products.add(new Product(256, "USB Wall Charger", 19.99));
        products.add(new Product(257, "Screen Cleaning Kit", 14.99));
        products.add(new Product(258, "Laptop Cooling Pad", 34.99));
        products.add(new Product(259, "Wireless Charging Pad", 29.99));
        products.add(new Product(260, "Smartphone Gimbal Stabilizer", 99.99));
        products.add(new Product(261, "VR Headset", 299.99));
        products.add(new Product(262, "Smart Home Security Camera", 149.99));
        products.add(new Product(263, "Smart Light Bulb Starter Kit", 49.99));
        products.add(new Product(264, "Smart Door Lock", 199.99));
        products.add(new Product(265, "Smart Smoke Alarm", 39.99));
        products.add(new Product(266, "Smart Water Sensor", 29.99));
        products.add(new Product(267, "Smart Thermostat", 129.99));
        products.add(new Product(268, "Smart Plug with Energy Monitoring", 34.99));
        products.add(new Product(269, "Smart Home Hub", 79.99));
        products.add(new Product(270, "Smart Light Switch", 49.99));
        products.add(new Product(271, "Smart Garage Door Controller", 99.99));
        products.add(new Product(272, "Smart Irrigation Controller", 129.99));
        products.add(new Product(273, "Smart Pet Feeder", 89.99));
        products.add(new Product(274, "Smart Air Quality Monitor", 59.99));
        products.add(new Product(275, "Smart Coffee Maker", 149.99));
        products.add(new Product(276, "Smart Refrigerator with Touchscreen", 2499.99));
        products.add(new Product(277, "Smart Oven with Wi-Fi", 899.99));
        products.add(new Product(278, "Smart Dishwasher with App Control", 799.99));
        products.add(new Product(279, "Smart Washing Machine with Wi-Fi", 699.99));
        products.add(new Product(280, "Smart Dryer with App Control", 699.99));
        products.add(new Product(281, "Electric Toothbrush with App", 99.99));
        products.add(new Product(282, "Electric Shaver with Smart Features", 79.99));
        products.add(new Product(283, "Hair Dryer with Smart Heat Control", 59.99));
        products.add(new Product(284, "Hair Straightener with App", 89.99));
        products.add(new Product(285, "Electric Massager with Heat", 69.99));
        products.add(new Product(286, "Electric Blanket with Timer", 49.99));
        products.add(new Product(287, "Air Purifier with Smart Features", 149.99));
        products.add(new Product(288, "Humidifier with App Control", 59.99));
        products.add(new Product(289, "Dehumidifier with Smart Features", 89.99));
        products.add(new Product(290, "Electric Kettle with Temperature Control", 39.99));
        products.add(new Product(291, "Coffee Grinder with App", 49.99));
        products.add(new Product(292, "Electric Skillet with Temperature Control", 59.99));
        products.add(new Product(293, "Pressure Cooker with Smart Features", 99.99));
        products.add(new Product(294, "Rice Cooker with App Control", 49.99));
        products.add(new Product(295, "Food Processor with Smart Features", 129.99));
        products.add(new Product(296, "Blender with Smart Features", 79.99));
        products.add(new Product(297, "Juicer with Smart Features", 99.99));
        products.add(new Product(298, "Electric Grill with Temperature Control", 89.99));
        products.add(new Product(299, "Electric Fryer with Smart Features", 99.99));
        products.add(new Product(300, "Electric Wok with Temperature Control", 69.99));
        products.add(new Product(301, "Electric Fondue Pot with Temperature Control", 49.99));
        products.add(new Product(302, "Electric Popcorn Maker with Smart Features", 39.99));
        products.add(new Product(303, "Electric Ice Cream Maker with App", 99.99));
        products.add(new Product(304, "Electric Yogurt Maker with App", 49.99));
        products.add(new Product(305, "Electric Food Dehydrator with App", 79.99));
        products.add(new Product(306, "Electric Egg Cooker with Timer", 29.99));
        products.add(new Product(307, "Electric Rice Cooker with Smart Features", 59.99));
        products.add(new Product(308, "Electric Pressure Cooker with App", 99.99));
        products.add(new Product(309, "Electric Slow Cooker with Smart Features", 69.99));
        products.add(new Product(310, "Electric Food Steamer with App", 49.99));
        products.add(new Product(311, "Electric Griddle with Temperature Control", 59.99));
        products.add(new Product(312, "Electric Sandwich Maker with Smart Features", 39.99));
        products.add(new Product(313, "Electric Panini Press with Temperature Control", 49.99));
        products.add(new Product(314, "Electric Waffle Maker with Smart Features", 39.99));
        products.add(new Product(315, "Electric Crepe Maker with Temperature Control", 49.99));
        products.add(new Product(316, "Electric Pizza Maker with Smart Features", 59.99));
        products.add(new Product(317, "Electric Hot Dog Maker with Timer", 29.99));
        products.add(new Product(318, "Electric Candy Floss Maker with App", 39.99));
        products.add(new Product(319, "Electric Chocolate Fountain with Smart Features", 49.99));
        products.add(new Product(320, "Electric Food Processor with Smart Features", 129.99));
        products.add(new Product(321, "Electric Blender with Smart Features", 79.99));
        products.add(new Product(322, "Electric Juicer with Smart Features", 99.99));
        products.add(new Product(323, "Electric Mixer with Smart Features", 49.99));
        products.add(new Product(324, "Electric Food Chopper with Smart Features", 39.99));
        products.add(new Product(325, "Electric Meat Grinder with Smart Features", 149.99));
        products.add(new Product(326, "Electric Pasta Maker with Smart Features", 99.99));
        products.add(new Product(327, "Electric Ice Maker with Smart Features", 199.99));
        products.add(new Product(328, "Electric Water Dispenser with Smart Features", 149.99));
        products.add(new Product(329, "Electric Water Heater with Smart Features", 99.99));
        products.add(new Product(330, "Smart Home Starter Kit", 199.99));
        products.add(new Product(331, "Smart Light Bulb", 19.99));
        products.add(new Product(332, "Smart Plug", 24.99));
        products.add(new Product(333, "Smart Doorbell", 99.99));
        products.add(new Product(334, "Smart Security Camera", 129.99));
        products.add(new Product(335, "Smart Thermostat", 89.99));
        products.add(new Product(336, "Smart Smoke Detector", 39.99));
        products.add(new Product(337, "Smart Water Leak Detector", 29.99));
        products.add(new Product(338, "Smart Garage Door Opener", 79.99));
        products.add(new Product(339, "Smart Home Hub", 49.99));
        products.add(new Product(340, "Smart Air Quality Monitor", 59.99));
        products.add(new Product(341, "Smart Pet Feeder", 99.99));
        products.add(new Product(342, "Smart Coffee Maker", 149.99));
        products.add(new Product(343, "Smart Refrigerator", 1999.99));
        products.add(new Product(344, "Smart Oven", 899.99));
        products.add(new Product(345, "Smart Dishwasher", 799.99));
        products.add(new Product(346, "Smart Washing Machine", 699.99));
        products.add(new Product(347, "Smart Dryer", 699.99));
        products.add(new Product(348, "Smart Air Purifier", 149.99));
        products.add(new Product(349, "Smart Humidifier", 59.99));
        products.add(new Product(350, "Smart Dehumidifier", 89.99));
        
        User loggedIn = getUser (loggedInUser );

        frame = new JFrame("Online Shopping Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));

        // Editable fields for user profile
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
            // Update user information
            loggedIn.username = usernameField.getText();
            loggedIn.gender = genderField.getText();
            loggedIn.country = countryField.getText();
            loggedIn.age = Integer.parseInt(ageField.getText());
            loggedIn.phoneNumber = phoneField.getText();

            JOptionPane.showMessageDialog(frame, "Profile updated successfully!");

            // Shrink the profile panel
            profilePanel.setPreferredSize(new Dimension(300, 200)); // Set a smaller preferred size
            profilePanel.revalidate(); // Revalidate the panel to apply the new size
            profilePanel.repaint(); // Repaint the panel to reflect changes
        });

        profilePanel.add(saveButton);
        tabbedPane.addTab("Profile", profilePanel);

        // Address Panel
        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("Shipping Address: (To be updated soon)"));
        tabbedPane.addTab("Address", addressPanel);

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
