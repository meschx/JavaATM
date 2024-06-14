import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class Receipt {

    private static final String RECEIPT_DIRECTORY = "./receipts/";

    private static String generateConfirmationNumber() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public static void generateWithdrawalReceipt(Card card, double amount, String currency) {
        String confirmationNumber = generateConfirmationNumber();
        String content = "Numer potwierdzenia: " + confirmationNumber +
                "\nData: " + LocalDateTime.now() +
                "\nTyp transakcji: Wypłata" +
                "\nKwota: " + amount + " " + currency +
                "\nNumer karty: **** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4);
        writeToFile(confirmationNumber + "_withdrawal_receipt.txt", content);
    }

    public static void generateDepositReceipt(Card card, double amount, String currency) {
        String confirmationNumber = generateConfirmationNumber();
        String content = "Numer potwierdzenia: " + confirmationNumber +
                "\nData: " + LocalDateTime.now() +
                "\nTyp transakcji: Wpłata" +
                "\nKwota: " + amount + " " + currency +
                "\nNumer karty: **** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4);
        writeToFile(confirmationNumber + "_deposit_receipt.txt", content);
    }

    public static void generatePinChangeReceipt(Card card) {
        String confirmationNumber = generateConfirmationNumber();
        String content = "Numer potwierdzenia: " + confirmationNumber +
                "\nData: " + LocalDateTime.now() +
                "\nTyp transakcji: Zmiana PIN" +
                "\nNumer karty: **** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4);
        writeToFile(confirmationNumber + "_pin_change_receipt.txt", content);
    }

    public static void generateCurrencyExchangeReceipt(Card card, double amount, String sourceCurrency, double convertedAmount, String targetCurrency) {
        String confirmationNumber = generateConfirmationNumber();
        String content = "Numer potwierdzenia: " + confirmationNumber +
                "\nData: " + LocalDateTime.now() +
                "\nTyp transakcji: Przewalutowanie" +
                "\nKwota: " + amount + " " + sourceCurrency +
                "\nPrzeliczona kwota: " + convertedAmount + " " + targetCurrency +
                "\nNumer karty: **** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4);
        writeToFile(confirmationNumber + "_currency_exchange_receipt.txt", content);
    }

    private static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECEIPT_DIRECTORY + fileName, true))) {
            writer.write(content);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}