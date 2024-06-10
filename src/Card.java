import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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

    // Gettery
    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
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

    public double getBalancePLN() {
        return balancePLN;
    }

    public double getBalanceUSD() {
        return balanceUSD;
    }

    public double getBalanceEUR() {
        return balanceEUR;
    }

    public double getBalanceCZK() {
        return balanceCZK;
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

    public static void main(String[] args) {
//        Card card = new Card();
//        card.cardNumber = "1234567890123456";
//        card.pin = "1234";
//        card.cardHolderName = "Jan";
//        card.cardHolderSurname = "Kowalski";
//        card.cardType = true;
//        card.cardExpiryDate = "06/24";
//        card.balancePLN = 1000.0;
//        card.balanceUSD = 0;
//        card.balanceEUR = 0;
//        card.balanceCZK = 0;
//
//        LocalDate expiryDate = card.getCardExpiryDateAsDate();
//        System.out.println("Data ważności karty: " + expiryDate);
//
//        try {
//            card.saveCard();
//        } catch (IOException e) {
//            System.out.println("Wystąpił błąd podczas zapisywania karty: " + e.getMessage());
//        }

        Card card = new Card();
        Card card2 = new Card();
        try {

            card = card.readCard("1234567890123456");
            System.out.println("Wczytano kartę: " + card.getCardHolderName() + " " + card.getCardHolderSurname());
            System.out.println("Saldo w PLN: " + card.getBalancePLN());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Wystąpił błąd podczas wczytywania karty: " + e.getMessage());
        }
    }
}
