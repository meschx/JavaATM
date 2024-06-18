import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
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
                Image backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/background3.png")));
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
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    } // buttony z menu glownego

    private JButton addButton2(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        return button;
    } //buttony z operacji (wstecz itp) i menu glownego

    private JLabel addLabel (String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    } //labelki (przy formularzu itp)

    private JTextField addTextField () {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setMaximumSize(new Dimension(200, 50));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setFont(new Font("Courier New", Font.PLAIN, 20));
        return textField;
    } //pole tekstowe (do wpisywania pinu itp)

    private JPasswordField addPinField () {
        JPasswordField pinField = new JPasswordField();
        pinField.setPreferredSize(new Dimension(200, 50));
        pinField.setMaximumSize(new Dimension(200, 50));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinField.setFont(new Font("Courier New", Font.PLAIN, 20));
        return pinField;
    } //pole tekstowe (do wpisywania pinu itp)

    private JCheckBox addCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 20));
        checkBox.setForeground(Color.WHITE);
        checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBox.setOpaque(false);
        return checkBox;
    } //checkboxy (do wyboru karty itp)

    private JComboBox addComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        comboBox.setMaximumSize(new Dimension(200, 50));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        return comboBox;
    } //comboboxy (do wyboru waluty itp)

    private JPanel panelSetup() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    } //panel

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

    private JPanel buttonPanelSetup() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return buttonPanel;
    }

    private JPanel containerSetup(JPanel panel) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(panel, new GridBagConstraints());
        return container;
    }

    //=====================================

    private JPanel startScreen() {
        JPanel startScreen = panelSetup();

        startScreen.add(Box.createVerticalGlue());

        JButton insertCardButton = addButton("Włóż kartę");
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

        JButton createCardButton = addButton("Stwórz kartę");
        createCardButton.addActionListener(_ -> {
            cards.add(cardScreen(), CREATE_CARD_SCREEN);
            cardLayout.show(cards, "createCard");
        });
        startScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        startScreen.add(createCardButton);

        startScreen.add(Box.createVerticalGlue());
        return startScreen;
    }

    private JPanel cardScreen() {
        JPanel createCard = panelSetup();

        createCard.add(Box.createVerticalGlue());

        JLabel nameLabel = addLabel("Imię:");
        createCard.add(nameLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField cardHolderNameField = addTextField();
        createCard.add(cardHolderNameField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel surnameLabel = addLabel("Nazwisko:");
        createCard.add(surnameLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField cardHolderSurnameField = addTextField();
        createCard.add(cardHolderSurnameField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel pinLabel = addLabel("PIN:");
        createCard.add(pinLabel);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField pinField = addPinField();
        createCard.add(pinField);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JCheckBox cardTypeCheckBox = addCheckBox("Karta własna");
        cardTypeCheckBox.addActionListener(_ -> {
            card.setCardType(cardTypeCheckBox.isSelected());
            System.out.println(cardTypeCheckBox.isSelected());
        });
        createCard.add(cardTypeCheckBox);
        createCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = buttonPanelSetup();

        JButton backButton = addButton2("Wstecz");
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "startScreen");
        });
        buttonPanel.add(backButton);

        JButton createCardButton2 = addButton2("Stwórz kartę");
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
                    cardLayout.show(cards, "startScreen");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(createCardButton2);

        createCard.add(buttonPanel);
        createCard.add(Box.createVerticalGlue());

        return containerSetup(createCard);
    }

    private JPanel pinScreen() {
        JPanel pinScreen = panelSetup();

        pinScreen.add(Box.createVerticalGlue());

        JLabel pinLabel2 = addLabel("Wprowadź kod PIN:");
        pinScreen.add(pinLabel2);
        pinScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        JPasswordField pinField2 = addPinField();
        pinScreen.add(pinField2);
        pinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel2 = buttonPanelSetup();

        JButton backButton2 = addButton2("Wstecz");
        backButton2.addActionListener(_ -> {
            cardLayout.show(cards, "startScreen");
        });
        JButton acceptButton = addButton2("Zatwierdź");
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
        pinScreen.add(buttonPanel2);
        pinScreen.add(Box.createVerticalGlue());

        return containerSetup(pinScreen);
    }

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
        JButton withdrawButton = addButton2("Wypłata");
        JButton depositButton = addButton2("Wpłata");
        JButton checkBalanceButton = addButton2("Sprawdź saldo");
        JButton exchangeRatesButton = addButton2("Kursy walut");
        leftPanel.add(withdrawButton);
        leftPanel.add(depositButton);
        leftPanel.add(checkBalanceButton);
        leftPanel.add(exchangeRatesButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());
        JLabel helloLabel = addLabel("Witaj, " + card.getCardHolderName() + " " + card.getCardHolderSurname() + "!");
        JLabel cardNumberLabel = addLabel("Numer karty: " + card.getCardNumber());
        JLabel expiryDateLabel = addLabel("Data ważności: " + card.getCardExpiryDate());
        if (!card.getCardType()) {
            expiryDateLabel.setText("Karta obca - niektóre funkcje mogą byc niedostępne");
        }
        JLabel chooseLabel = addLabel("Wybierz jedną z opcji dostępnych na ekranie");
        centerPanel.add(helloLabel);
        centerPanel.add(cardNumberLabel);
        centerPanel.add(expiryDateLabel);
        centerPanel.add(chooseLabel);
        centerPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new GridLayout(4, 1));
        rightPanel.setOpaque(false);
        JButton convertCurrencyButton = addButton2("Przewalutuj");
        JButton changePinButton = addButton2("Zmień PIN");
        JButton currentTimeButton = addButton2(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        currentTimeButton.setEnabled(false);
        JButton backButton = addButton2("Wyjmij kartę");
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
        JPanel currencyRatesScreen = panelSetup();

        currencyRatesScreen.add(Box.createVerticalGlue());

        JLabel currencyRatesLabel = addLabel("Kursy walut:");
        currencyRatesScreen.add(currencyRatesLabel);
        currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        try {
            double usdRate = CurrencyConverter.getExchangeRate("USD");
            JLabel usdRateLabel = addLabel("USD: " + usdRate);
            currencyRatesScreen.add(usdRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

            double eurRate = CurrencyConverter.getExchangeRate("EUR");
            JLabel eurRateLabel = addLabel("EUR: " + eurRate);
            currencyRatesScreen.add(eurRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));

            double czkRate = CurrencyConverter.getExchangeRate("CZK");
            JLabel czkRateLabel = addLabel("CZK: " + czkRate);
            currencyRatesScreen.add(czkRateLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        } catch (Exception e) {
            JLabel errorLabel = addLabel("Wystąpił błąd podczas pobierania kursów walut: " + e.getMessage());
            currencyRatesScreen.add(errorLabel);
            currencyRatesScreen.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        JPanel buttonPanel = buttonPanelSetup();

        JButton backButton = new JButton("Wstecz");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        currencyRatesScreen.add(buttonPanel);
        currencyRatesScreen.add(Box.createVerticalGlue());

        return containerSetup(currencyRatesScreen);
    }

    private JPanel withdrawScreen() {
        JPanel withdrawScreen = panelSetup();

        withdrawScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = addLabel("Kwota:");
        withdrawScreen.add(amountLabel);
        withdrawScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = addTextField();
        withdrawScreen.add(amountField);
        withdrawScreen.add(Box.createRigidArea(new Dimension(0, 20)));

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

        return containerSetup(withdrawScreen);
    }

    private JPanel depositScreen() {
        JPanel depositScreen = panelSetup();

        depositScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = addLabel("Kwota:");
        depositScreen.add(amountLabel);
        depositScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = addTextField();
        depositScreen.add(amountField);
        depositScreen.add(Box.createRigidArea(new Dimension(0, 20)));

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

        return containerSetup(depositScreen);
    }

    private JPanel checkBalanceScreen() {
        JPanel checkBalanceScreen = panelSetup();

        checkBalanceScreen.add(Box.createVerticalGlue());

        JLabel balanceLabel = addLabel("Saldo:");
        checkBalanceScreen.add(balanceLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balancePLNLabel = addLabel("PLN: " + card.getBalance("PLN"));
        checkBalanceScreen.add(balancePLNLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceUSDLabel = addLabel("USD: " + card.getBalance("USD"));
        checkBalanceScreen.add(balanceUSDLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceEURLabel = addLabel("EUR: " + card.getBalance("EUR"));
        checkBalanceScreen.add(balanceEURLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel balanceCZKLabel = addLabel("CZK: " + card.getBalance("CZK"));
        checkBalanceScreen.add(balanceCZKLabel);
        checkBalanceScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = panelSetup();

        JButton backButton = addButton2("Wstecz");
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
            cardLayout.removeLayoutComponent(checkBalanceScreen);
        });
        buttonPanel.add(backButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkBalanceScreen.add(buttonPanel);
        checkBalanceScreen.add(Box.createVerticalGlue());

        return containerSetup(checkBalanceScreen);
    }

    private JPanel changePinScreen() {
        JPanel changePinScreen = panelSetup();

        changePinScreen.add(Box.createVerticalGlue());

        JLabel currentPinLabel = addLabel("Aktualny PIN:");
        changePinScreen.add(currentPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField currentPinField = addPinField();
        changePinScreen.add(currentPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel newPinLabel = addLabel("Nowy PIN:");
        changePinScreen.add(newPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField newPinField = addPinField();
        changePinScreen.add(newPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel confirmPinLabel = addLabel("Potwierdź nowy PIN:");
        changePinScreen.add(confirmPinLabel);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPasswordField confirmPinField = addPinField();
        changePinScreen.add(confirmPinField);
        changePinScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dla przycisków
        JPanel buttonPanel = buttonPanelSetup();

        JButton backButton = addButton2("Wstecz");
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton changePinButton = addButton2("Zmień PIN");
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

        changePinScreen.add(buttonPanel);
        changePinScreen.add(Box.createVerticalGlue());

        return containerSetup(changePinScreen);
    }

    private JPanel currencyExchangeScreen() {
        JPanel currencyExchangeScreen = panelSetup();

        currencyExchangeScreen.add(Box.createVerticalGlue());

        JLabel amountLabel = addLabel("Kwota:");
        currencyExchangeScreen.add(amountLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField amountField = addTextField();
        currencyExchangeScreen.add(amountField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel sourceCurrencyLabel = addLabel("Waluta źródłowa:");
        currencyExchangeScreen.add(sourceCurrencyLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] currencies = { "PLN", "USD", "EUR", "CZK" };

        JComboBox sourceCurrencyField = addComboBox(currencies);
        currencyExchangeScreen.add(sourceCurrencyField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel targetCurrencyLabel = addLabel("Waluta docelowa:");
        currencyExchangeScreen.add(targetCurrencyLabel);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JComboBox targetCurrencyField = addComboBox(currencies);
        currencyExchangeScreen.add(targetCurrencyField);
        currencyExchangeScreen.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = buttonPanelSetup();

        JButton backButton = addButton2("Wstecz");
        backButton.addActionListener(_ -> {
            cardLayout.show(cards, "optionsScreen");
        });
        buttonPanel.add(backButton);

        JButton exchangeButton = addButton2("Przewalutuj");
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

        currencyExchangeScreen.add(buttonPanel);
        currencyExchangeScreen.add(Box.createVerticalGlue());

        return containerSetup(currencyExchangeScreen);
    }

    public ATMGUI() {
        //card.setCardType(true);
        setupFrame();
        add(cards);
        cards.add(startScreen(), START_SCREEN);
        setVisible(true);
    }
}