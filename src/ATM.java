import javax.swing.JOptionPane;

public class ATM {
    private Card card;
    private CurrencyConverter currencyConverter;

    public ATM(Card card, CurrencyConverter currencyConverter) {
        this.card = card;
        this.currencyConverter = currencyConverter;
    }

    public void withdrawCash(double amount) {
        if (card.getBalance("PLN") >= amount && amount % 10 == 0 && amount <= 4000) {
            card.setBalance("PLN", card.getBalance("PLN") - amount);
            JOptionPane.showMessageDialog(null, "Wypłacono " + amount + " PLN.");
        } else if (amount % 10 != 0) {
            JOptionPane.showMessageDialog(null, "Kwota musi być wielokrotnością 10.");
        } else if (amount > 4000) {
            JOptionPane.showMessageDialog(null, "Maksymalna kwota wypłaty to 4000 PLN.");
        } else {
            JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
        }
    }

    public void depositCash(double amount) {
        if (amount % 10 == 0 && amount <= 20000) {
            card.setBalance("PLN", card.getBalance("PLN") + amount);
            JOptionPane.showMessageDialog(null, "Wpłacono " + amount + " PLN.");
        } else if (amount > 20000) {
            JOptionPane.showMessageDialog(null, "Maksymalna kwota wpłaty to 20000 PLN.");
        } else {
            JOptionPane.showMessageDialog(null, "Kwota musi być wielokrotnością 10.");
        }
    }

    public void changePin(String currentPin, String newPin, String confirmPin) {
        if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Wszystkie pola muszą być wypełnione!");
        } else if (!currentPin.equals(card.getPin())) {
            JOptionPane.showMessageDialog(null, "Nieprawidłowy kod PIN!");
        } else if (!newPin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(null, "Nowe kody PIN nie są takie same!");
        } else {
            card.setPin(newPin);
            JOptionPane.showMessageDialog(null, "PIN został zmieniony.");
        }
    }

    public void exchangeCurrency(double amount, String sourceCurrency, String targetCurrency) throws Exception {
        double exchangeRate;
        //przewalutowanie z PLN
        if (sourceCurrency.equals("PLN")) {
            exchangeRate = CurrencyConverter.getExchangeRate(targetCurrency);
            double convertedAmount = amount / exchangeRate;
            if (card.getBalance(sourceCurrency) >= amount) {
                card.setBalance(sourceCurrency, card.getBalance(sourceCurrency) - amount);
                card.setBalance(targetCurrency, card.getBalance(targetCurrency) + convertedAmount);
                JOptionPane.showMessageDialog(null, "Przewalutowano " + amount + " " + sourceCurrency + " na " + convertedAmount + " " + targetCurrency + ".");
            } else {
                JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
            }
        } else {
            exchangeRate = CurrencyConverter.getExchangeRate(sourceCurrency);
            double convertedAmount = amount * exchangeRate;
            if (card.getBalance(sourceCurrency) >= amount) {
                card.setBalance(sourceCurrency, card.getBalance(sourceCurrency) - amount);
                card.setBalance("PLN", card.getBalance("PLN") + convertedAmount);
                JOptionPane.showMessageDialog(null, "Przewalutowano " + amount + " " + sourceCurrency + " na " + convertedAmount + " PLN.");
            } else {
                JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
            }
        }
    }
}