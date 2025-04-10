import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class OnlineShoppingGUI {
    // UI styling properties
    private boolean darkMode = false;
    
    // Light mode colors
    private static final Color LIGHT_BG = new Color(248, 249, 250);
    private static final Color LIGHT_TEXT = new Color(33, 37, 41);
    private static final Color LIGHT_ACCENT = new Color(13, 110, 253);
    private static final Color LIGHT_SECONDARY = new Color(108, 117, 125);
    private static final Color LIGHT_BORDER = new Color(222, 226, 230);
    private static final Color LIGHT_PANEL_BG = new Color(255, 255, 255);
    private static final Color LIGHT_SUCCESS = new Color(25, 135, 84);
    private static final Color LIGHT_WARNING = new Color(255, 193, 7);
    private static final Color LIGHT_DANGER = new Color(220, 53, 69);
    private static final Color LIGHT_HOVER = new Color(10, 88, 202);
    
    // UI Helper Methods
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
        return button;
    }
    
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10), 
            title
        ));
        panel.setLayout(new GridBagLayout());
        return panel;
    }
    
    // Dark mode colors
    private static final Color DARK_BG = new Color(33, 37, 41);
    private static final Color DARK_TEXT = new Color(248, 249, 250);
    private static final Color DARK_ACCENT = new Color(13, 110, 253);
    private static final Color DARK_SECONDARY = new Color(173, 181, 189);
    private static final Color DARK_BORDER = new Color(73, 80, 87);
    private static final Color DARK_PANEL_BG = new Color(52, 58, 64);
    private static final Color DARK_SUCCESS = new Color(25, 135, 84);
    private static final Color DARK_WARNING = new Color(255, 193, 7);
    private static final Color DARK_DANGER = new Color(220, 53, 69);
    
    // Fonts
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private DefaultListModel<String> cartModel;
    private List<Product> products;
    private List<Product> cart;
    private String loggedInUser;
    private List<User> users = new ArrayList<>();
    private JSpinner quantitySpinner; // Spinner for quantity input
    private JTextField couponField; // Field for entering coupon code
    private Map<String, Double> validCoupons; // Map to store valid coupons
    private List<Purchase> purchases = new ArrayList<>(); // List to track all purchases
    private JScrollPane historyScroll; // Scroll pane for purchase history
    private JTable historyTable; // Table for purchase history
    private DefaultTableModel historyModel; // Model for purchase history
    private JPanel cartPanel; // Panel for shopping cart
    private JComboBox<String> productCategoryCombo; // Combo box for product categories
    private JTextField newCategoryField; // Field for adding new categories

    class Purchase {
        String customer;
        List<Product> items;
        double total;
        long timestamp;
        String paymentMethod;
        String status;

        public Purchase(String customer, List<Product> items, double total, String paymentMethod) {
            this.customer = customer;
            this.items = new ArrayList<>(items);
            this.total = total;
            this.timestamp = System.currentTimeMillis();
            this.paymentMethod = paymentMethod;
            this.status = "Completed";
        }

        public String getFormattedDate() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timestamp));
        }

        public String getItemsSummary() {
            StringBuilder summary = new StringBuilder();
            for (Product item : items) {
                summary.append(item.getName()).append(" ($").append(item.getPrice()).append("), ");
            }
            return summary.length() > 0 ? summary.substring(0, summary.length() - 2) : "";
        }
    }

    class User {
        String username;
        String password;
        String role;
        String gender;
        String country;
        int age;
        String phoneNumber;
        boolean restricted;
        String restrictionType; // "PERMANENT" or "TEMPORARY"
        long restrictionDate; // timestamp when restricted
        int restrictionDuration; // days for temporary ban
        String address;
        boolean darkMode;
    
        public User(String username, String password, String role, String gender, 
                   String country, int age, String phoneNumber) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.gender = gender;
            this.country = country;
            this.age = age;
            this.phoneNumber = phoneNumber;
            this.restricted = false;
            this.address = "Not Set";  // Add this line
        }

        public boolean isRestrictionExpired() {
            if (!restricted || restrictionType.equals("PERMANENT")) {
                return false;
            }
            long currentTime = System.currentTimeMillis();
            long restrictionEndTime = restrictionDate + (restrictionDuration * 24L * 60 * 60 * 1000);
            return currentTime > restrictionEndTime;
        }

        public String getRestrictionMessage() {
            if (!restricted) return "";
            
            if (restrictionType.equals("PERMANENT")) {
                return "Your account has been permanently restricted.";
            } else {
                long currentTime = System.currentTimeMillis();
                long restrictionEndTime = restrictionDate + (restrictionDuration * 24L * 60 * 60 * 1000);
                long remainingDays = (restrictionEndTime - currentTime) / (24 * 60 * 60 * 1000);
                
                if (remainingDays <= 0) {
                    return "Your temporary restriction has expired. Please try logging in again.";
                } else {
                    return "Your account is temporarily restricted for " + restrictionDuration + 
                           " days. " + remainingDays + " days remaining.";
                }
            }
        }
    }

    public OnlineShoppingGUI() {
        users.add(new User("admin", "password", "Admin", "Male", "USA", 30, "123-456-7890"));
        users.add(new User("customer", "1234", "Customer", "Female", "Canada", 25, "987-654-3210"));
        users.add(new User("seller", "5678", "Seller", "Male", "UK", 28, "555-555-5555"));
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
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passField.setEchoChar((char) 0);  // Show password
                } else {
                    passField.setEchoChar('*');  // Hide password
                }
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                if (authenticate(username, password, role)) {
                    loggedInUser = username;
                    loginFrame.dispose();
                    // Create and show loading screen
                    JFrame loadingFrame = new JFrame("Loading");
                    loadingFrame.setSize(300, 100);
                    loadingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    loadingFrame.setLayout(new BorderLayout());
                    loadingFrame.add(new JLabel("Loading, please wait...", JLabel.CENTER), BorderLayout.CENTER);
                    loadingFrame.setLocationRelativeTo(null);
                    loadingFrame.setVisible(true);
                    
                    // Use a timer to simulate loading and then show main screen
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ((Timer)evt.getSource()).stop();
                            loadingFrame.dispose(); // Dispose loading frame
                            showMainScreen();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid login credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterScreen();
            }
        });

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
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passField.setEchoChar((char) 0);  // Show password
                } else {
                    passField.setEchoChar('*');  // Hide password
                }
            }
        });

        JButton submitButton = new JButton("Register");
        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.WHITE);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
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

    private void deleteUser(String username) {
        if (username.equals(loggedInUser)) {
            JOptionPane.showMessageDialog(frame, "Cannot delete currently logged in user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        for (User user : users) {
            if (user.username.equals(username)) {
                int confirm = JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to delete user: " + username + "?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    users.remove(user);
                    JOptionPane.showMessageDialog(frame, "User deleted successfully");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void restrictUser(String username, String restrictionType, int durationDays) {
        if (username.equals(loggedInUser)) {
            JOptionPane.showMessageDialog(frame, "Cannot restrict currently logged in user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        for (User user : users) {
            if (user.username.equals(username)) {
                if (user.restricted) {
                    JOptionPane.showMessageDialog(frame, "User is already restricted", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    user.restricted = true;
                    user.restrictionType = restrictionType;
                    user.restrictionDate = System.currentTimeMillis();
                    user.restrictionDuration = durationDays;
                    String message = "User restricted successfully (" + restrictionType;
                    if (restrictionType.equals("TEMPORARY")) {
                        message += " for " + durationDays + " days";
                    }
                    message += ")";
                    JOptionPane.showMessageDialog(frame, message);
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void unrestrictUser(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                if (!user.restricted) {
                    JOptionPane.showMessageDialog(frame, "User is not restricted", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    user.restricted = false;
                    JOptionPane.showMessageDialog(frame, "User unrestricted successfully");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showAdminOptions() {
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
    
        JButton deleteUserButton = new JButton("Delete User");
        JButton restrictUserButton = new JButton("Restrict User");
        JButton unrestrictUserButton = new JButton("Unrestrict User");
    
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteUserScreen();
            }
        });
        restrictUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRestrictUserScreen();
            }
        });
        unrestrictUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUnrestrictUserScreen();
            }
        });
    
        adminPanel.add(deleteUserButton);
        adminPanel.add(restrictUserButton);
        adminPanel.add(unrestrictUserButton);
    
        frame.add(adminPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }
    
    private void showDeleteUserScreen() {
        String[] options = users.stream().map(u -> u.username).toArray(String[]::new);
        String selectedUser = (String) JOptionPane.showInputDialog(frame, 
            "Select User to Delete", "Delete User", 
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selectedUser != null) {
            deleteUser(selectedUser);
        }
    }
    
    private void showRestrictUserScreen() {
        String[] options = users.stream()
            .filter(u -> !u.username.equals(loggedInUser) && !u.restricted)
            .map(u -> u.username)
            .toArray(String[]::new);
        
        if (options.length == 0) {
            JOptionPane.showMessageDialog(frame, "No users available to restrict", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        String selectedUser = (String) JOptionPane.showInputDialog(frame, 
            "Select User to Restrict", "Restrict User", 
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selectedUser != null) {
            // Create panel for restriction options
            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.add(new JLabel("Restriction Type:"));
            
            ButtonGroup typeGroup = new ButtonGroup();
            JRadioButton permanentBtn = new JRadioButton("Permanent");
            JRadioButton temporaryBtn = new JRadioButton("Temporary");
            typeGroup.add(permanentBtn);
            typeGroup.add(temporaryBtn);
            panel.add(permanentBtn);
            panel.add(temporaryBtn);
            permanentBtn.setSelected(true);
            
            JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 365, 1));
            panel.add(new JLabel("Duration (days):"));
            panel.add(durationSpinner);
            durationSpinner.setVisible(false);
            
        temporaryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                durationSpinner.setVisible(true);
            }
        });
        permanentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                durationSpinner.setVisible(false);
            }
        });
            
            int result = JOptionPane.showConfirmDialog(frame, panel, 
                "Restriction Options", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String restrictionType = permanentBtn.isSelected() ? "PERMANENT" : "TEMPORARY";
                int duration = permanentBtn.isSelected() ? 0 : (int)durationSpinner.getValue();
                restrictUser(selectedUser, restrictionType, duration);
            }
        }
    }
    
    private void showUnrestrictUserScreen() {
        String[] options = users.stream()
            .filter(u -> u.restricted)
            .map(u -> u.username)
            .toArray(String[]::new);
        
        if (options.length == 0) {
            JOptionPane.showMessageDialog(frame, "No restricted users found", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        String selectedUser = (String) JOptionPane.showInputDialog(frame, 
            "Select User to Unrestrict", "Unrestrict User", 
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selectedUser != null) {
            unrestrictUser(selectedUser);
        }
    }

    private void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadProductsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("products.dat"))) {
            products = (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If file doesn't exist or error occurs, start with default products
            products = new ArrayList<>();
            products.add(new Product(101, "Laptop", 1200.00, 5));
            products.add(new Product(102, "Smartphone", 800.00, 5));
        }
    }

    private void applyDarkMode() {
        User currentUser = getUser(loggedInUser);
        if (currentUser != null) {
            darkMode = currentUser.darkMode;
            
            Color bgColor = darkMode ? DARK_BG : LIGHT_BG;
            Color textColor = darkMode ? DARK_TEXT : LIGHT_TEXT;
            Color accentColor = darkMode ? DARK_ACCENT : LIGHT_ACCENT;
            Color panelBgColor = darkMode ? DARK_PANEL_BG : LIGHT_PANEL_BG;
            
            // Apply to main frame and all components recursively
            applyDarkModeToComponent(frame.getContentPane(), bgColor, textColor, accentColor, panelBgColor);
            
            // Special handling for tables
            productTable.setBackground(bgColor);
            productTable.setForeground(textColor);
            productTable.setGridColor(darkMode ? DARK_BORDER : Color.LIGHT_GRAY);
            
            historyTable.setBackground(bgColor);
            historyTable.setForeground(textColor);
            historyTable.setGridColor(darkMode ? DARK_BORDER : Color.LIGHT_GRAY);
            
            // Force repaint of all components
            frame.revalidate();
            frame.repaint();
        }
    }
    
    private void applyDarkModeToComponent(Component component, Color bgColor, Color textColor, Color accentColor, Color panelBgColor) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(panelBgColor);
            panel.setBorder(BorderFactory.createLineBorder(darkMode ? DARK_BORDER : Color.LIGHT_GRAY));
            
            for (Component child : panel.getComponents()) {
                applyDarkModeToComponent(child, bgColor, textColor, accentColor, panelBgColor);
            }
        } else if (component instanceof JLabel) {
            ((JLabel)component).setForeground(textColor);
        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            if (darkMode) {
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(LIGHT_ACCENT);
                button.setForeground(Color.WHITE);
            }
            button.setBorder(BorderFactory.createLineBorder(darkMode ? DARK_BORDER : LIGHT_ACCENT, 2));
        } else if (component instanceof JTextField || component instanceof JSpinner) {
            component.setBackground(panelBgColor);
            component.setForeground(textColor);
        } else if (component instanceof JList) {
            ((JList<?>)component).setBackground(panelBgColor);
            ((JList<?>)component).setForeground(textColor);
        } else if (component instanceof JScrollPane) {
            component.setBackground(bgColor);
            ((JScrollPane)component).getViewport().setBackground(panelBgColor);
        }
    }

    private void showMainScreen() {
        loadProductsFromFile();
        cart = new ArrayList<>();
        validCoupons = new HashMap<>();
        
        // Initialize category components
        productCategoryCombo = new JComboBox<>(new String[]{"Electronics", "Clothing", "Home", "Books", "Other"});
        newCategoryField = new JTextField(15);

        // Initialize valid coupons with correct discount percentages
        validCoupons.put("D5", 5.0); // 5% discount
        validCoupons.put("D10", 10.0); // 10% discount
        validCoupons.put("D15", 15.0); // 15% discount
        validCoupons.put("D20", 20.0); // 20% discount
        validCoupons.put("D25", 25.0); // 25% discount
        validCoupons.put("D30", 30.0); // 30% discount
        validCoupons.put("D35", 35.0); // 35% discount
        validCoupons.put("D40", 40.0); // 40% discount
        validCoupons.put("D45", 45.0); // 45% discount
        validCoupons.put("D50", 50.0); // 50% discount
        // Add more coupons as needed

        // Products are loaded from file in loadProductsFromFile()
        // Only add new products here if needed

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
        
        // Add dark mode toggle
        JCheckBox darkModeToggle = new JCheckBox("Dark Mode");
        darkModeToggle.setSelected(darkMode);
        darkModeToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User currentUser = getUser(loggedInUser);
                if (currentUser != null) {
                    currentUser.darkMode = darkModeToggle.isSelected();
                    applyDarkMode();
                }
            }
        });

        JTextField usernameField = new JTextField(loggedIn.username);
        JTextField genderField = new JTextField(loggedIn.gender);
        JTextField countryField = new JTextField(loggedIn.country);
        JTextField ageField = new JTextField(String.valueOf(loggedIn.age));
        JTextField phoneField = new JTextField(loggedIn.phoneNumber);
        JTextField addressField = new JTextField(loggedIn.address);

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
        profilePanel.add(new JLabel("Address:"));
        profilePanel.add(addressField);
        profilePanel.add(darkModeToggle);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedIn.username = usernameField.getText();
                loggedIn.gender = genderField.getText();
                loggedIn.country = countryField.getText();
                loggedIn.age = Integer.parseInt(ageField.getText());
                loggedIn.phoneNumber = phoneField.getText();
                loggedIn.address = addressField.getText();
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
                // Update address display
                JPanel addressPanel = findAddressPanel(dashboardContent);
                if (addressPanel != null) {
                    addressPanel.removeAll();
                    addressPanel.add(new JLabel("Current Shipping Address: " + loggedIn.address));
                    addressPanel.revalidate();
                    addressPanel.repaint();
                }
            }
        });

        JButton darkModeButton = new JButton("Toggle Dark Mode");
        darkModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darkMode = !darkMode;
                applyDarkMode();
            }
        });

        profilePanel.add(saveButton);
        profilePanel.add(darkModeButton);
        dashboardContent.add(profilePanel);

        // Address Panel (updated)
        JPanel addressPanel = new JPanel();
        addressPanel.setName("addressPanel");
        addressPanel.add(new JLabel("Current Shipping Address: " + loggedIn.address));
        dashboardContent.add(addressPanel);
        
        // Address update button
        JButton updateAddressButton = new JButton("Update Address");
        updateAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newAddress = JOptionPane.showInputDialog(frame, "Enter new shipping address:");
                if (newAddress != null && !newAddress.trim().isEmpty()) {
                    loggedIn.address = newAddress;
                    addressPanel.removeAll();
                    addressPanel.add(new JLabel("Current Shipping Address: " + loggedIn.address));
                    addressPanel.revalidate();
                    addressPanel.repaint();
                    JOptionPane.showMessageDialog(frame, "Address updated successfully!");
                }
            }
        });
        dashboardContent.add(updateAddressButton);
        
        // Sign Out Panel
        JPanel signOutPanel = new JPanel();
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setBackground(new Color(220, 20, 60));
        signOutButton.setForeground(Color.WHITE);
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showLoginScreen();
            }
        });
        signOutPanel.add(signOutButton);
        dashboardContent.add(signOutPanel);

        // Toggle Button Action
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardContent.setVisible(!dashboardContent.isVisible());
                toggleButton.setText(dashboardContent.isVisible() ? "Hide Dashboard" : "Show Dashboard");
                dashboardPanel.revalidate();
                dashboardPanel.repaint();
            }
        });

        dashboardPanel.add(toggleButton);
        dashboardPanel.add(dashboardContent);

        // Product Table
        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Rating"};
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

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
        removeFromCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCart();
            }
        });
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });

        // Initialize cartPanel (will be empty for sellers)
        cartPanel = new JPanel(new BorderLayout());
        
        if (!loggedIn.role.equals("Seller")) {
            cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
            buttonPanel.add(new JLabel("Quantity:"));
            buttonPanel.add(quantitySpinner);
            buttonPanel.add(addToCartButton);
            buttonPanel.add(removeFromCartButton);
            buttonPanel.add(checkoutButton);
            cartPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // Coupon Panel
        JPanel couponPanel = new JPanel();
        couponField = new JTextField(15);
        JButton applyCouponButton = new JButton("Apply Coupon");
        applyCouponButton.addActionListener((ActionEvent e) -> applyCoupon());
        couponPanel.add(new JLabel("Coupon Code:"));
        couponPanel.add(couponField);
        couponPanel.add(applyCouponButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        JTextField nameField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"All", "Electronics", "Clothing", "Home", "Books", "Other"});
        JTextField minPriceField = new JTextField();
        JTextField maxPriceField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        JButton saleButton = new JButton("Sale Items");

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Products"));
        searchPanel.add(new JLabel("Name:"));
        searchPanel.add(nameField);
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryCombo);
        searchPanel.add(new JLabel("Min Price:"));
        searchPanel.add(minPriceField);
        searchPanel.add(new JLabel("Max Price:"));
        searchPanel.add(maxPriceField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        searchPanel.add(saleButton);

        searchButton.addActionListener((ActionEvent e) -> {
            String nameText = nameField.getText().trim().toLowerCase();
            String categoryText = (String)categoryCombo.getSelectedItem();
            String minText = minPriceField.getText().trim();
            String maxText = maxPriceField.getText().trim();

            double min = minText.isEmpty() ? 0 : Double.parseDouble(minText);
            double max = maxText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxText);

            productTableModel.setRowCount(0); // clear table
            for (Product p : products) {
                boolean categoryMatch = categoryText.equals("All") || p.getCategory().equalsIgnoreCase(categoryText);
                boolean nameMatch = p.getName().toLowerCase().contains(nameText);
                boolean priceMatch = p.getPrice() >= min && p.getPrice() <= max;
                
                if (categoryMatch && nameMatch && priceMatch) {
                    String formattedPrice = String.format("$%.2f", p.getPrice());
                    String stockDisplay = p.needsRestock() ? 
                        "<html><font color='red'>" + p.getStock() + " (Low!)</font></html>" : 
                        String.valueOf(p.getStock());
                    String ratingDisplay = p.getReviewCount() > 0 ? 
                        String.format("%.1f (%d)", p.getAverageRating(), p.getReviewCount()) : 
                        "No reviews";
                        
                    productTableModel.addRow(new Object[]{
                        p.getId(), 
                        p.getName(), 
                        p.getCategory(),
                        formattedPrice, 
                        stockDisplay,
                        ratingDisplay
                    });
                }
            }
        });

        saleButton.addActionListener((ActionEvent e) -> {
            productTableModel.setRowCount(0);
            for (Product p : products) {
                if (p.getPrice() < p.getOriginalPrice()) {
                    String formattedPrice = String.format("$%.2f (was $%.2f)", p.getPrice(), p.getOriginalPrice());
                    productTableModel.addRow(new Object[]{
                        p.getId(), 
                        p.getName(), 
                        p.getCategory(),
                        formattedPrice, 
                        p.getStock(),
                        String.format("%.1f (%d)", p.getAverageRating(), p.getReviewCount())
                    });
                }
            }
        });

        resetButton.addActionListener((ActionEvent e) -> {
            nameField.setText("");
            minPriceField.setText("");
            maxPriceField.setText("");
            productTableModel.setRowCount(0);
            loadProducts();
        });

        // Purchase History Panel
        historyModel = new DefaultTableModel(new String[]{"Date", "Items", "Total", "Payment"}, 0);
        historyTable = new JTable(historyModel);
        historyScroll = new JScrollPane(historyTable);
        historyScroll.setPreferredSize(new Dimension(400, 200));
        
        // Toggle for Purchase History
        JButton historyToggleButton = new JButton("Hide History");
        historyToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyScroll.setVisible(!historyScroll.isVisible());
                historyToggleButton.setText(historyScroll.isVisible() ? "Hide History" : "Show History");
                frame.revalidate();
                frame.repaint();
            }
        });
        
        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // Add dashboard to top of left panel
        leftPanel.add(dashboardPanel, BorderLayout.NORTH);
        
        // Add history panel below dashboard
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.add(historyToggleButton, BorderLayout.NORTH);
        historyPanel.add(historyScroll, BorderLayout.CENTER);
        leftPanel.add(historyPanel, BorderLayout.CENTER);
        
        // Add product table to center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);
        mainPanel.add(couponPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Seller functionality
        if (loggedIn.role.equals("Seller")) {
            showSellerOptions();
        }
        // Admin functionality
        if (loggedIn.role.equals("Admin")) {
            showAdminOptions();
        }
    }

    private void showSellerOptions() {
        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(new BoxLayout(sellerPanel, BoxLayout.Y_AXIS));

        JButton addProductButton = new JButton("Add Product");
        JButton removeProductButton = new JButton("Remove Product");
        JButton modifyPriceButton = new JButton("Modify Product Price");
        JButton categoryStatsButton = new JButton("Category Statistics");

        addProductButton.addActionListener((ActionEvent e) -> showAddProductScreen());
        removeProductButton.addActionListener((ActionEvent e) -> showRemoveProductScreen());
        modifyPriceButton.addActionListener((ActionEvent e) -> showModifyPriceScreen());

        sellerPanel.add(addProductButton);
        sellerPanel.add(removeProductButton);
        sellerPanel.add(modifyPriceButton);
        sellerPanel.add(categoryStatsButton);
        
        categoryStatsButton.addActionListener(e -> showCategoryStats());

        frame.add(sellerPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    private void showCategoryStats() {
        Map<String, Integer> categoryCounts = new HashMap<>();
        Map<String, Double> categoryTotals = new HashMap<>();
        
        for (Product product : products) {
            String category = product.getCategory();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + product.getPrice());
        }
        
        StringBuilder stats = new StringBuilder("Category Statistics:\n");
        for (String category : categoryCounts.keySet()) {
            stats.append(String.format("%s: %d products, Total value: $%.2f\n", 
                category, categoryCounts.get(category), categoryTotals.get(category)));
        }
        
        JOptionPane.showMessageDialog(frame, stats.toString(), "Category Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddProductScreen() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(350, 300);
        addProductFrame.setLayout(new BorderLayout());
        addProductFrame.getContentPane().setBackground(new Color(255, 255, 224));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Product Name
        JLabel productNameLabel = new JLabel("Product Name:");
        JTextField productNameField = new JTextField(15);

        // Category Selection
        JLabel categoryLabel = new JLabel("Category:");
        productCategoryCombo = new JComboBox<>(new String[]{"Electronics", "Clothing", "Home", "Books", "Other", "New Category..."});
        newCategoryField = new JTextField(15);
        newCategoryField.setVisible(false);
        
        productCategoryCombo.addActionListener(e -> {
            if ("New Category...".equals(productCategoryCombo.getSelectedItem())) {
                newCategoryField.setVisible(true);
                newCategoryField.setText("");
            } else {
                newCategoryField.setVisible(false);
            }
        });

        // Price and Stock
        JLabel productPriceLabel = new JLabel("Price:");
        JTextField productPriceField = new JTextField(15);
        JLabel productStockLabel = new JLabel("Stock:");
        JTextField productStockField = new JTextField(15);

        // Buttons
        JButton addButton = new JButton("Add Product");
        JButton statsButton = new JButton("Category Stats");
        statsButton.addActionListener(e -> showCategoryStats());
        
        addButton.addActionListener(event -> {
            String category;
            if ("New Category...".equals(productCategoryCombo.getSelectedItem())) {
                category = newCategoryField.getText().trim();
                if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(addProductFrame, "Please enter a new category name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validate new category name
                if (category.length() < 3 || category.length() > 20) {
                    JOptionPane.showMessageDialog(addProductFrame, "Category name must be between 3-20 characters", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                category = (String)productCategoryCombo.getSelectedItem();
            }
            try {
                String name = productNameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(addProductFrame, "Product name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double price = Double.parseDouble(productPriceField.getText());
                int stock = Integer.parseInt(productStockField.getText());
                
                if (price <= 0) {
                    JOptionPane.showMessageDialog(addProductFrame, "Price must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (stock < 0) {
                    JOptionPane.showMessageDialog(addProductFrame, "Stock cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create product with category
                Product newProduct = new Product(products.size() + 1, name, price, stock);
                newProduct.setCategory(category); // Set the category
                products.add(newProduct);
                
                loadProducts();
                refreshAllProductViews();
                JOptionPane.showMessageDialog(addProductFrame, 
                    "Product added successfully!\n" +
                    "Name: " + name + "\n" +
                    "Category: " + category + "\n" +
                    "Price: $" + price + "\n" +
                    "Stock: " + stock);
                addProductFrame.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(addProductFrame, "Invalid number format for price or stock", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(productNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        panel.add(productCategoryCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("New Category:"), gbc);
        gbc.gridx = 1;
        panel.add(newCategoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(productPriceLabel, gbc);
        gbc.gridx = 1;
        panel.add(productPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(productStockLabel, gbc);
        gbc.gridx = 1;
        panel.add(productStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(addButton, gbc);
        
        gbc.gridx = 1;
        panel.add(statsButton, gbc);

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
            refreshAllProductViews();
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
                    refreshAllProductViews();
                    JOptionPane.showMessageDialog(frame, "Price updated successfully!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }
    }

    private void updatePurchaseHistory() {
        historyModel.setRowCount(0);
        
        for (Purchase purchase : purchases) {
            if (purchase.customer.equals(loggedInUser)) {
                historyModel.addRow(new Object[]{
                    purchase.getFormattedDate(),
                    purchase.getItemsSummary(),
                    String.format("$%.2f", purchase.total),
                    purchase.paymentMethod
                });
            }
        }
    }

    private void refreshAllProductViews() {
        // This would notify all active sessions to refresh their product views
        // In a real application, this would use a server push or websocket
        // For this demo, we'll just reload products for the current user
        loadProducts();
    }

    // Helper method to find the address panel in dashboard content
    private JPanel findAddressPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals("addressPanel")) {
                return (JPanel)comp;
            }
            if (comp instanceof Container) {
                JPanel found = findAddressPanel((Container)comp);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void loadProducts() {
        productTableModel.setRowCount(0);  // Clear existing table rows
        for (Product product : products) {
            // Format the price with a dollar sign
            String formattedPrice = String.format("$%.2f", product.getPrice());
            String stockDisplay = product.needsRestock() ? 
                "<html><font color='red'>" + product.getStock() + " (Low!)</font></html>" : 
                String.valueOf(product.getStock());
                
            String ratingDisplay = product.getReviewCount() > 0 ? 
                String.format("%.1f (%d)", product.getAverageRating(), product.getReviewCount()) : 
                "No reviews";
                
            Object[] row = {
                product.getId(), 
                product.getName(), 
                product.getCategory(),
                formattedPrice, 
                stockDisplay,
                ratingDisplay
            };
            productTableModel.addRow(row);
        }
        // Force full table refresh including column resizing
        productTableModel.fireTableDataChanged();
        productTable.setModel(productTableModel);
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
                // Stock was already reduced when adding to cart
                // No need to reduce again during checkout
                receipt.append("\nPayment Method: ").append(selectedPayment);
                JOptionPane.showMessageDialog(frame, receipt.toString(), "Checkout", JOptionPane.INFORMATION_MESSAGE);
                
                // Record the purchase
                purchases.add(new Purchase(loggedInUser, new ArrayList<>(cart), total, selectedPayment));
                updatePurchaseHistory();
                
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
