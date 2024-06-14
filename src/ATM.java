import javax.swing.JOptionPane;

public class ATM {
    private Card card;

    public ATM(Card card) {
        this.card = card;
    }

    public void withdrawCash(double amount) {
        if (card.getBalance("PLN") >= amount && amount % 10 == 0 && amount <= 4000) {
            card.setBalance("PLN", card.getBalance("PLN") - amount);
            JOptionPane.showMessageDialog(null, "Wypłacono " + amount + " PLN.");
            Receipt.generateWithdrawalReceipt(card, amount, "PLN");
        } else if (amount % 10 != 0) {
            JOptionPane.showMessageDialog(null, "Kwota musi być wielokrotnością 10.");
        } else if (amount > 4000) {
            JOptionPane.showMessageDialog(null, "Maksymalna kwota wypłaty to 4000 PLN.");
        } else if (amount < 0) {
            JOptionPane.showMessageDialog(null, "Kwota musi być większa od zera.");
        } else {
            JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
        }
    }

    public void depositCash(double amount) {
        if (amount % 10 == 0 && amount <= 20000) {
            card.setBalance("PLN", card.getBalance("PLN") + amount);
            JOptionPane.showMessageDialog(null, "Wpłacono " + amount + " PLN.");
            Receipt.generateDepositReceipt(card, amount, "PLN");
        } else if (amount > 20000) {
            JOptionPane.showMessageDialog(null, "Maksymalna kwota wpłaty to 20000 PLN.");
        } else if (amount < 0) {
            JOptionPane.showMessageDialog(null, "Kwota musi być większa od zera.");
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
            Receipt.generatePinChangeReceipt(card);
        }
    }

    public void exchangeCurrency(double amount, String sourceCurrency, String targetCurrency) throws Exception {
        double exchangeRate;
        //przewalutowanie z PLN
        if (sourceCurrency.equals("PLN")) {
            exchangeRate = CurrencyConverter.getExchangeRate(targetCurrency);
            double convertedAmount = Math.round((amount / exchangeRate) * 100.0) / 100.0;
            if (card.getBalance(sourceCurrency) >= amount) {
                card.setBalance(sourceCurrency, Math.round((card.getBalance(sourceCurrency) - amount) * 100.0) / 100.0);
                card.setBalance(targetCurrency, Math.round((card.getBalance(targetCurrency) + convertedAmount) * 100.0) / 100.0);
                JOptionPane.showMessageDialog(null, "Przewalutowano " + amount + " " + sourceCurrency + " na " + convertedAmount + " " + targetCurrency + ".");
                Receipt.generateCurrencyExchangeReceipt(card, amount, sourceCurrency, convertedAmount, targetCurrency);
            } else {
                JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
            }
        } else if (targetCurrency.equals("PLN")) {
            exchangeRate = CurrencyConverter.getExchangeRate(sourceCurrency);
            double convertedAmount = Math.round((amount * exchangeRate) * 100.0) / 100.0;
            if (card.getBalance(sourceCurrency) >= amount) {
                card.setBalance(sourceCurrency, Math.round((card.getBalance(sourceCurrency) - amount) * 100.0) / 100.0);
                card.setBalance("PLN", Math.round((card.getBalance("PLN") + convertedAmount) * 100.0) / 100.0);
                JOptionPane.showMessageDialog(null, "Przewalutowano " + amount + " " + sourceCurrency + " na " + convertedAmount + " PLN.");
                Receipt.generateCurrencyExchangeReceipt(card, amount, sourceCurrency, convertedAmount, "PLN");
            } else {
                JOptionPane.showMessageDialog(null, "Brak wystarczających środków na koncie.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Przewalutowanie między walutami obcymi nie jest obsługiwane.");
        }
    }
}