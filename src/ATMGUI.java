import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.imageio.ImageIO;

public class ATMGUI extends JFrame {

    private static final String START_SCREEN = "startScreen";
    private static final String CREATE_CARD_SCREEN = "createCard";
    private static final String PIN_SCREEN = "pinScreen";
    private static final String OPTIONS_SCREEN = "optionsScreen";
    private static final String CURRENCY_RATES_SCREEN = "currencyRatesScreen";
    private static final String WITHDRAW_SCREEN = "withdrawScreen";
    private static final String DEPOSIT_SCREEN = "depositScreen";
    private static final String CHECK_BALANCE_SCREEN = "checkBalanceScreen";
    private static final String CHANGE_PIN_SCREEN = "changePinScreen";
    private static final String CURRENCY_EXCHANGE_SCREEN = "currencyExchangeScreen";

    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                Image backgroundImage = ImageIO.read(getClass().getResource("/background3.png"));
                int x = (getWidth() - backgroundImage.getWidth(null)) / 2;
                int y = (getHeight() - backgroundImage.getHeight(null)) / 2;
                g.drawImage(backgroundImage, x, y, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Card card = new Card();
    private ATM atm = new ATM(card);

    private JButton addButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void setupFrame() {
        setTitle("JavaATM");
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        setIconImage(icon);
        setSize(1280, 720);
        setMinimumSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel startScreen() {
        JPanel startScreen = new JPanel();
        startScreen.setLayout(new BoxLayout(startScreen, BoxLayout.Y_AXIS));
        startScreen.setOpaque(false);

        startScreen.add(Box.createVerticalGlue());

        JButton insertCardButton = new JButton("Włóż kartę");
        insertCardButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        insertCardButton.setPreferredSize(new Dimension(200, 50)); // Increase button size
        insertCardButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        insertCardButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    card = card.readCard(selectedFile.getName().replace(".ser", ""));
                    if (card.getCardExpiryDateAsDate().isAfter(LocalDate.now())) {
                        cards.add(optionsScreen(), OPTIONS_SCREEN);
                        cards.add(pinScreen(), PIN_SCREEN);
                        cardLayout.show(cards, "pinScreen");
                        System.out.println("Wczytano kartę: " + card.getCardHolderName() + " " + card.getCardHolderSurname());
                        System.out.println("Typ karty: " + card.getCardType());
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        startScreen.add(insertCardButton);

        JButton createCardButton = new JButton("Stwórz kartę");
        createCardButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase font size
        createCardButton.setPreferredSize(new Dimension(200, 50)); // Increase button size
        createCardButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        createCardButton.addActionListener(_ -> {
            cards.add(createCardScreen(), CREATE_CARD_SCREEN);
            cardLayout.show(cards, "createCard");
        });
        startScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        startScreen.add(createCardButton);

        startScreen.add(Box.createVerticalGlue());
        return startScreen;
    } //OK

    private JPanel createCardScreen() {
        JPanel createCard = new JPanel();
        createCard.setLayout(new BoxLayout(createCard, BoxLayout.Y_AXIS));
        createCard.setOpaque(false);

        createCard.add(Box.createVerticalGlue());

        JLabel nameLabel = new JLabel("Imię:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(nameLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField cardHolderNameField = new JTextField();
        cardHolderNameField.setPreferredSize(new Dimension(200, 50));
        cardHolderNameField.setMaximumSize(new Dimension(200, 50));
        cardHolderNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(cardHolderNameField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel surnameLabel = new JLabel("Nazwisko:");
        surnameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        surnameLabel.setForeground(Color.WHITE);
        surnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(surnameLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField cardHolderSurnameField = new JTextField();
        cardHolderSurnameField.setPreferredSize(new Dimension(200, 50));
        cardHolderSurnameField.setMaximumSize(new Dimension(200, 50));
        cardHolderSurnameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(cardHolderSurnameField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        pinLabel.setForeground(Color.WHITE);
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(pinLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField pinField = new JPasswordField();
        pinField.setPreferredSize(new Dimension(200, 50));
        pinField.setMaximumSize(new Dimension(200, 50));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(pinField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JCheckBox cardTypeCheckBox = new JCheckBox("Karta własna");
        cardTypeCheckBox.setFont(new Font("Arial", Font.PLAIN, 20));
        cardTypeCheckBox.setForeground(Color.WHITE);
        cardTypeCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardTypeCheckBox.setOpaque(false);
        cardTypeCheckBox.addActionListener(_ -> {
            card.setCardType(cardTypeCheckBox.isSelected());
            System.out.println(cardTypeCheckBox.isSelected());
        });
        createCard.add(cardTypeCheckBox);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "startScreen");
        });
        buttonPanel.add(backButton);

        JButton createCardButton2 = new JButton("Stwórz kartę");
        createCardButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        createCardButton2.addActionListener(_ -> {
            String name = cardHolderNameField.getText();
            String surname = cardHolderSurnameField.getText();
            String pin = new String(pinField.getPassword());

            if (name.isEmpty() || surname.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Wszystkie pola muszą być wypełnione!");
            } else {
                card.setCardNumber(card.generateCardNumber());
                card.setPin(pin);
                card.setCardHolderName(name);
                card.setCardHolderSurname(surname);
                card.setCardType(cardTypeCheckBox.isSelected());
                card.setCardExpiryDate();
                try {
                    card.saveCard();
                    cardLayout.show(cards, "cardNumber");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(createCardButton2);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCard.add(buttonPanel);
        createCard.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(createCard, new GridBagConstraints());
        return container;
    } //OK

    private JPanel pinScreen() {
        JPanel pinScreen = new JPanel();
        pinScreen.setLayout(new BoxLayout(pinScreen, BoxLayout.Y_AXIS));
        pinScreen.setOpaque(false);

        pinScreen.add(Box.createVerticalGlue());

        JLabel pinLabel2 = new JLabel("Wprowadź kod PIN:");
        pinLabel2.setFont(new Font("Arial", Font.PLAIN, 20));
        pinLabel2.setForeground(Color.WHITE);
        pinLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinScreen.add(pinLabel2);
        pinScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        JPasswordField pinField2 = new JPasswordField();
        pinField2.setPreferredSize(new Dimension(200, 50));
        pinField2.setMaximumSize(new Dimension(200, 50));
        pinField2.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinScreen.add(pinField2);
        pinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setOpaque(false);

        JButton backButton2 = new JButton("Wstecz");
        backButton2.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton2.addActionListener(_ -> {
            cardLayout.show(cards, "startScreen");
        });
        JButton acceptButton = new JButton("Zatwierdź");
        acceptButton.setFont(new Font("Arial", Font.PLAIN, 20));
        acceptButton.addActionListener(_ -> {
            String pin = new String(pinField2.getPassword());
            if (card.getPin().equals(pin)) {
                cardLayout.show(cards, "optionsScreen");
            } else {
                JOptionPane.showMessageDialog(null, "Nieprawidłowy kod PIN!");
            }
            pinField2.setText("");
        });
        buttonPanel2.add(backButton2);
        buttonPanel2.add(acceptButton);
        buttonPanel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinScreen.add(buttonPanel2);
        pinScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(pinScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel optionsScreen() {
        JPanel optionsScreen = new JPanel();
        optionsScreen.setLayout(new BorderLayout());
        optionsScreen.setOpaque(false);

        cards.add(withdrawScreen(), WITHDRAW_SCREEN);
        cards.add(depositScreen(), DEPOSIT_SCREEN);
        cards.add(currencyRatesScreen(), CURRENCY_RATES_SCREEN);
        cards.add(currencyExchangeScreen(), CURRENCY_EXCHANGE_SCREEN);

        JPanel leftPanel = new JPanel(new GridLayout(4, 1));
        leftPanel.setOpaque(false);
        JButton withdrawButton = new JButton("Wypłata");
        withdrawButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton depositButton = new JButton("Wpłata");
        depositButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton checkBalanceButton = new JButton("Sprawdź saldo");
        checkBalanceButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton exchangeRatesButton = new JButton("Kursy walut");
        exchangeRatesButton.setFont(new Font("Arial", Font.PLAIN, 20));
        leftPanel.add(withdrawButton);
        leftPanel.add(depositButton);
        leftPanel.add(checkBalanceButton);
        leftPanel.add(exchangeRatesButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());
        JLabel helloLabel = new JLabel("Witaj, " + card.getCardHolderName() + " " + card.getCardHolderSurname());
        helloLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        helloLabel.setForeground(Color.WHITE);
        helloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel cardNumberLabel = new JLabel("Numer karty: " + card.getCardNumber());
        cardNumberLabel.setForeground(Color.WHITE);
        cardNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel expiryDateLabel = new JLabel("Data ważności: " + card.getCardExpiryDate());
        System.out.println("Data ważności: " + card.getCardExpiryDate());
        expiryDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        expiryDateLabel.setForeground(Color.WHITE);
        expiryDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (!card.getCardType()) {
            expiryDateLabel.setText("Karta obca - niektóre funkcje mogą byc niedostępne");
        }
        JLabel chooseLabel = new JLabel("Wybierz jedną z opcji dostępnych na ekranie");
        chooseLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        chooseLabel.setForeground(Color.WHITE);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(helloLabel);
        centerPanel.add(cardNumberLabel);
        centerPanel.add(expiryDateLabel);
        centerPanel.add(chooseLabel);
        centerPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new GridLayout(4, 1));
        rightPanel.setOpaque(false);
        JButton convertCurrencyButton = new JButton("Przewalutuj");
        convertCurrencyButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton changePinButton = new JButton("Zmień PIN");
        changePinButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton currentTimeButton = new JButton(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        currentTimeButton.setEnabled(false);
        currentTimeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        JButton backButton = new JButton("Wyjmij kartę");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        rightPanel.add(convertCurrencyButton);
        rightPanel.add(changePinButton);
        rightPanel.add(currentTimeButton);
        rightPanel.add(backButton);

        if (!card.getCardType()) {
            exchangeRatesButton.setEnabled(false);
            convertCurrencyButton.setEnabled(false);
            changePinButton.setEnabled(false);
        }

        optionsScreen.add(leftPanel, BorderLayout.WEST);
        optionsScreen.add(rightPanel, BorderLayout.EAST);
        optionsScreen.add(centerPanel, BorderLayout.CENTER);

        withdrawButton.addActionListener(_ -> {
            cardLayout.show(cards, "withdrawScreen");
        });

        depositButton.addActionListener(_ -> {
            cardLayout.show(cards, "depositScreen");
        });

        checkBalanceButton.addActionListener(_ -> {
            cards.add(checkBalanceScreen(), CHECK_BALANCE_SCREEN);
            cardLayout.show(cards, "checkBalanceScreen");
        });

        exchangeRatesButton.addActionListener(_ -> {
            cardLayout.show(cards, "currencyRatesScreen");
        });

        convertCurrencyButton.addActionListener(_ -> {
            cardLayout.show(cards, "currencyExchangeScreen");
        });

        changePinButton.addActionListener(_ -> {
            cards.add(changePinScreen(), CHANGE_PIN_SCREEN);
            cardLayout.show(cards, "changePinScreen");
        });

        backButton.addActionListener(_ -> {
            // Zapisanie karty po przeprowadzonych transakcjach
            try {
                card.saveCard();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cardLayout.show(cards, "startScreen");
        });

        return optionsScreen;
    }

    private JPanel currencyRatesScreen() {
        JPanel currencyRatesScreen = new JPanel();
        currencyRatesScreen.setLayout(new BoxLayout(currencyRatesScreen, BoxLayout.Y_AXIS));
        currencyRatesScreen.setOpaque(false);

        currencyRatesScreen.add(Box.createVerticalGlue());

        JLabel currencyRatesLabel = new JLabel("Kursy walut");
        currencyRatesLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        currencyRatesLabel.setForeground(Color.WHITE);
        currencyRatesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyRatesScreen.add(currencyRatesLabel);
        currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        try {
            double usdRate = CurrencyConverter.getExchangeRate("USD");
            JLabel usdRateLabel = new JLabel("USD: " + usdRate);
            usdRateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            usdRateLabel.setForeground(Color.WHITE);
            usdRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currencyRatesScreen.add(usdRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

            double eurRate = CurrencyConverter.getExchangeRate("EUR");
            JLabel eurRateLabel = new JLabel("EUR: " + eurRate);
            eurRateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            eurRateLabel.setForeground(Color.WHITE);
            eurRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currencyRatesScreen.add(eurRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

            double czkRate = CurrencyConverter.getExchangeRate("CZK");
            JLabel czkRateLabel = new JLabel("CZK: " + czkRate);
            czkRateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            czkRateLabel.setForeground(Color.WHITE);
            czkRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currencyRatesScreen.add(czkRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Wystąpił błąd podczas pobierania kursów walut: " + e.getMessage());
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            errorLabel.setForeground(Color.WHITE);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currencyRatesScreen.add(errorLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyRatesScreen.add(buttonPanel);
        currencyRatesScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(currencyRatesScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel withdrawScreen() {
        // Ekran wypłaty
        JPanel withdrawScreen = new JPanel();
        withdrawScreen.setLayout(new BoxLayout(withdrawScreen, BoxLayout.Y_AXIS));
        withdrawScreen.setOpaque(false);

        withdrawScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = new JLabel("Kwota:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawScreen.add(amountLabel);
        withdrawScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 50));
        amountField.setMaximumSize(new Dimension(200, 50));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawScreen.add(amountField);
        withdrawScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton withdrawButton = new JButton("Wypłać");
        withdrawButton.setFont(new Font("Arial", Font.PLAIN, 20));
        withdrawButton.addActionListener(_ -> {
            double amount = Double.parseDouble(amountField.getText());
            boolean flag = atm.withdrawCash(amount);
            if (flag) {
                cardLayout.show(cards, "optionsScreen");
            }
            amountField.setText("");
        });
        buttonPanel.add(withdrawButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawScreen.add(buttonPanel);
        withdrawScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(withdrawScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel depositScreen() {
        // Ekran wpłaty
        JPanel depositScreen = new JPanel();
        depositScreen.setLayout(new BoxLayout(depositScreen, BoxLayout.Y_AXIS));
        depositScreen.setOpaque(false);

        depositScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = new JLabel("Kwota:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositScreen.add(amountLabel);
        depositScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 50));
        amountField.setMaximumSize(new Dimension(200, 50));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositScreen.add(amountField);
        depositScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton depositButton = new JButton("Wpłać");
        depositButton.setFont(new Font("Arial", Font.PLAIN, 20));
        depositButton.addActionListener(_ -> {
            double amount = Double.parseDouble(amountField.getText());
            boolean flag = atm.depositCash(amount);
            if (flag) {
                cardLayout.show(cards, "optionsScreen");
            }
            amountField.setText("");
        });
        buttonPanel.add(depositButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositScreen.add(buttonPanel);
        depositScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(depositScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel checkBalanceScreen() {
        JPanel checkBalanceScreen = new JPanel();
        checkBalanceScreen.setLayout(new BoxLayout(checkBalanceScreen, BoxLayout.Y_AXIS));
        checkBalanceScreen.setOpaque(false);

        checkBalanceScreen.add(Box.createVerticalGlue());

        JLabel balanceLabel = new JLabel("Saldo:");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(balanceLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balancePLNLabel = new JLabel("PLN: " + String.valueOf(card.getBalance("PLN")));
        balancePLNLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balancePLNLabel.setForeground(Color.WHITE);
        balancePLNLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(balancePLNLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceUSDLabel = new JLabel("USD: " + card.getBalance("USD"));
        balanceUSDLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balanceUSDLabel.setForeground(Color.WHITE);
        balanceUSDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(balanceUSDLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceEURLabel = new JLabel("EUR: " + card.getBalance("EUR"));
        balanceEURLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balanceEURLabel.setForeground(Color.WHITE);
        balanceEURLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(balanceEURLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceCZKLabel = new JLabel("CZK: " + card.getBalance("CZK"));
        balanceCZKLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        balanceCZKLabel.setForeground(Color.WHITE);
        balanceCZKLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(balanceCZKLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
            cardLayout.removeLayoutComponent(checkBalanceScreen);
        });
        buttonPanel.add(backButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(buttonPanel);
        checkBalanceScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(checkBalanceScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel changePinScreen() {
        JPanel changePinScreen = new JPanel();
        changePinScreen.setLayout(new BoxLayout(changePinScreen, BoxLayout.Y_AXIS));
        changePinScreen.setOpaque(false);

        changePinScreen.add(Box.createVerticalGlue());

        JLabel currentPinLabel = new JLabel("Aktualny PIN:");
        currentPinLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        currentPinLabel.setForeground(Color.WHITE);
        currentPinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(currentPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField currentPinField = new JPasswordField();
        currentPinField.setPreferredSize(new Dimension(200, 50));
        currentPinField.setMaximumSize(new Dimension(200, 50));
        currentPinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(currentPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel newPinLabel = new JLabel("Nowy PIN:");
        newPinLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        newPinLabel.setForeground(Color.WHITE);
        newPinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(newPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField newPinField = new JPasswordField();
        newPinField.setPreferredSize(new Dimension(200, 50));
        newPinField.setMaximumSize(new Dimension(200, 50));
        newPinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(newPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel confirmPinLabel = new JLabel("Potwierdź nowy PIN:");
        confirmPinLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        confirmPinLabel.setForeground(Color.WHITE);
        confirmPinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(confirmPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField confirmPinField = new JPasswordField();
        confirmPinField.setPreferredSize(new Dimension(200, 50));
        confirmPinField.setMaximumSize(new Dimension(200, 50));
        confirmPinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(confirmPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton changePinButton = new JButton("Zmień PIN");
        changePinButton.setFont(new Font("Arial", Font.PLAIN, 20));
        changePinButton.addActionListener(_ -> {
            String currentPin = new String(currentPinField.getPassword());
            String newPin = new String(newPinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());
            boolean flag = atm.changePin(currentPin, newPin, confirmPin);
            if (flag) {
                cardLayout.show(cards, "optionsScreen");
            }
        });
        buttonPanel.add(changePinButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePinScreen.add(buttonPanel);
        changePinScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(changePinScreen, new GridBagConstraints());
        return container;
    } //OK

    private JPanel currencyExchangeScreen() {
        JPanel currencyExchangeScreen = new JPanel();
        currencyExchangeScreen.setLayout(new BoxLayout(currencyExchangeScreen, BoxLayout.Y_AXIS));
        currencyExchangeScreen.setOpaque(false);

        currencyExchangeScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = new JLabel("Kwota:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(amountLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 50));
        amountField.setMaximumSize(new Dimension(200, 50));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(amountField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel sourceCurrencyLabel = new JLabel("Waluta źródłowa:");
        sourceCurrencyLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        sourceCurrencyLabel.setForeground(Color.WHITE);
        sourceCurrencyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(sourceCurrencyLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] currencies = { "PLN", "USD", "EUR", "CZK" };
        JComboBox<String> sourceCurrencyField = new JComboBox<>(currencies);
        sourceCurrencyField.setMaximumSize(new Dimension(200, 50));
        sourceCurrencyField.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(sourceCurrencyField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel targetCurrencyLabel = new JLabel("Waluta docelowa:");
        targetCurrencyLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        targetCurrencyLabel.setForeground(Color.WHITE);
        targetCurrencyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(targetCurrencyLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JComboBox<String> targetCurrencyField = new JComboBox<>(currencies);
        targetCurrencyField.setMaximumSize(new Dimension(200, 50));
        targetCurrencyField.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(targetCurrencyField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton exchangeButton = new JButton("Przewalutuj");
        exchangeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exchangeButton.addActionListener(_ -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String sourceCurrency = (String) sourceCurrencyField.getSelectedItem();
                String targetCurrency = (String) targetCurrencyField.getSelectedItem();
                boolean flag = atm.exchangeCurrency(amount, sourceCurrency, targetCurrency);
                if (flag) {
                    cardLayout.show(cards, "optionsScreen");
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas przewalutowania.");
            }
        });
        buttonPanel.add(exchangeButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currencyExchangeScreen.add(buttonPanel);
        currencyExchangeScreen.add(Box.createVerticalGlue());

        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(currencyExchangeScreen, new GridBagConstraints());
        return container;
    } //OK+ATM OK

    public ATMGUI() {
        //card.setCardType(true);
        setupFrame();
        add(cards);
        cards.add(startScreen(), START_SCREEN);
        setVisible(true);
    }
}