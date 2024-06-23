import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Card implements Serializable {

    private String cardNumber;
    private String pin;
    private String cardHolderName;
    private String cardHolderSurname;
    private Boolean cardType; //true - karta "własna", false - karta "obca"
    private String cardExpiryDate;
    private double balancePLN; // saldo w PLN
    private double balanceUSD; // saldo w USD
    private double balanceEUR; // saldo w EUR
    private double balanceCZK; // saldo w CZK

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // Generate a random digit between 0 and 9
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setCardHolderSurname(String cardHolderSurname) {
        this.cardHolderSurname = cardHolderSurname;
    }

    public void setCardType(Boolean cardType) {
        this.cardType = cardType;
    }

    public void setCardExpiryDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusYears(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        this.cardExpiryDate = expiryDate.format(formatter);
    }

    public void setBalance(String currency, double amount) {
        switch (currency) {
            case "PLN":
                this.balancePLN = amount;
                break;
            case "USD":
                this.balanceUSD = amount;
                break;
            case "EUR":
                this.balanceEUR = amount;
                break;
            case "CZK":
                this.balanceCZK = amount;
                break;
            default:
                break;
        }
    }

    // Gettery
    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardHolderSurname() {
        return cardHolderSurname;
    }

    public Boolean getCardType() {
        return cardType;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public double getBalance(String currency) {
        return switch (currency) {
            case "PLN" -> this.balancePLN;
            case "USD" -> this.balanceUSD;
            case "EUR" -> this.balanceEUR;
            case "CZK" -> this.balanceCZK;
            default -> 0;
        };
    }

    public LocalDate getCardExpiryDateAsDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth yearMonth = YearMonth.parse(cardExpiryDate, formatter);
        return yearMonth.atEndOfMonth();
    }

    public void saveCard() throws IOException {
        String filename = this.cardNumber + ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public Card readCard(String cardNumber) throws IOException, ClassNotFoundException {
        String filename = cardNumber + ".ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Card readCard = (Card) ois.readObject();
            this.cardNumber = readCard.cardNumber;
            this.pin = readCard.pin;
            this.cardHolderName = readCard.cardHolderName;
            this.cardHolderSurname = readCard.cardHolderSurname;
            this.cardType = readCard.cardType;
            this.cardExpiryDate = readCard.cardExpiryDate;
            this.balancePLN = readCard.balancePLN;
            this.balanceUSD = readCard.balanceUSD;
            this.balanceEUR = readCard.balanceEUR;
            this.balanceCZK = readCard.balanceCZK;
            return this;
        }
    }
}
