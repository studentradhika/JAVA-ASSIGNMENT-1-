import java.util.Scanner;


public class BankingApp {

    
    static class Account {
        private final int accountNumber;      
        private String accountHolderName;
        private double balance;
        private String email;
        private String phoneNumber;

        public Account(int accountNumber, String accountHolderName, double initialDeposit,
                       String email, String phoneNumber) {
            this.accountNumber = accountNumber;
            this.accountHolderName = accountHolderName;
            this.balance = initialDeposit;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public int getAccountNumber() { return accountNumber; }
        public String getAccountHolderName() { return accountHolderName; }
        public double getBalance() { return balance; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }

        public void setAccountHolderName(String name) { this.accountHolderName = name; }

        
        public boolean deposit(double amount) {
            if (amount <= 0) return false;
            balance += amount;
            return true;
        }

        
        public boolean withdraw(double amount) {
            if (amount <= 0) return false;
            if (amount > balance) return false;
            balance -= amount;
            return true;
        }

        public void updateContactDetails(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        }

        
        public void displayAccountDetails() {
            System.out.println("------------------------------");
            System.out.println("Account Number : " + accountNumber);
            System.out.println("Account Holder : " + accountHolderName);
            System.out.printf ("Balance        : %.2f\n", balance);
            System.out.println("Email          : " + email);
            System.out.println("Phone          : " + phoneNumber);
            System.out.println("------------------------------");
        }
    }

    
    static class UserInterface {
        private static final int MAX_ACCOUNTS = 100; 
        private final Account[] accounts = new Account[MAX_ACCOUNTS];
        private int count = 0; 

        private final Scanner sc = new Scanner(System.in);
        private int nextAccountNumber = 1001; 

        public void mainMenu() {
            System.out.println("Welcome to the Banking Application!\n");
            int choice;
            do {
                printMenu();
                choice = safeReadInt("Enter your choice: ");
                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> performDeposit();
                    case 3 -> performWithdrawal();
                    case 4 -> showAccountDetails();
                    case 5 -> updateContact();
                    case 6 -> System.out.println("Thank you for using the Banking Application. Goodbye!");
                    default -> System.out.println("Invalid choice. Please try again.\n");
                }
            } while (choice != 6);
        }

        private void printMenu() {
            System.out.println("==============================");
            System.out.println("1. Create a new account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. View account details");
            System.out.println("5. Update contact details");
            System.out.println("6. Exit");
            System.out.println("==============================");
        }

        public void createAccount() {
            if (count >= accounts.length) {
                System.out.println("Bank is at full capacity. Cannot create more accounts.\n");
                return;
            }

            System.out.print("Enter account holder name: ");
            String name = safeReadNonEmptyLine();

            double initialDeposit = safeReadDouble(
                    "Enter initial deposit amount: ",
                    (val) -> val >= 0,
                    "Amount must be non-negative. Try again."
            );

            String email = safeReadEmail("Enter email address: ");
            String phone = safeReadPhone("Enter phone number: ");

            int accNo = nextAccountNumber++;
            Account acc = new Account(accNo, name, initialDeposit, email, phone);
            accounts[count++] = acc;

            System.out.println("Account created successfully with Account Number: " + accNo + "\n");
        }

        public void performDeposit() {
            int accNo = safeReadInt("Enter account number: ");
            Account acc = findAccountByNumber(accNo);
            if (acc == null) {
                System.out.println("Account not found.\n");
                return;
            }

            double amount = safeReadDouble(
                    "Enter amount to deposit: ",
                    (val) -> val > 0,
                    "Amount must be positive. Try again."
            );

            if (acc.deposit(amount)) {
                System.out.printf("Deposit successful. New balance: %.2f\n\n", acc.getBalance());
            } else {
                System.out.println("Deposit failed due to invalid amount.\n");
            }
        }

       
        public void performWithdrawal() {
            int accNo = safeReadInt("Enter account number: ");
            Account acc = findAccountByNumber(accNo);
            if (acc == null) {
                System.out.println("Account not found.\n");
                return;
            }

            double amount = safeReadDouble(
                    "Enter amount to withdraw: ",
                    (val) -> val > 0,
                    "Amount must be positive. Try again."
            );

            if (amount > acc.getBalance()) {
                System.out.printf("Insufficient balance. Current balance: %.2f\n\n", acc.getBalance());
                return;
            }

            if (acc.withdraw(amount)) {
                System.out.printf("Withdrawal successful. New balance: %.2f\n\n", acc.getBalance());
            } else {
                System.out.println("Withdrawal failed due to invalid amount.\n");
            }
        }

        
        public void showAccountDetails() {
            int accNo = safeReadInt("Enter account number: ");
            Account acc = findAccountByNumber(accNo);
            if (acc == null) {
                System.out.println("Account not found.\n");
                return;
            }
            acc.displayAccountDetails();
            System.out.println();
        }

       
        public void updateContact() {
            int accNo = safeReadInt("Enter account number: ");
            Account acc = findAccountByNumber(accNo);
            if (acc == null) {
                System.out.println("Account not found.\n");
                return;
            }

            String newEmail = safeReadEmail("Enter new email address: ");
            String newPhone = safeReadPhone("Enter new phone number: ");
            acc.updateContactDetails(newEmail, newPhone);
            System.out.println("Contact details updated successfully.\n");
        }

     
        private Account findAccountByNumber(int accNo) {
            for (int i = 0; i < count; i++) {
                if (accounts[i].getAccountNumber() == accNo) return accounts[i];
            }
            return null;
        }

        private int safeReadInt(String prompt) {
            while (true) {
                System.out.print(prompt);
                try {
                    int val = Integer.parseInt(sc.nextLine().trim());
                    return val;
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid integer. Please try again.");
                }
            }
        }

        interface DoubleValidator { boolean isValid(double val); }

        private double safeReadDouble(String prompt, DoubleValidator validator, String invalidMsg) {
            while (true) {
                System.out.print(prompt);
                String line = sc.nextLine().trim();
                try {
                    double val = Double.parseDouble(line);
                    if (validator == null || validator.isValid(val)) return val;
                    System.out.println(invalidMsg);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid number. Please try again.");
                }
            }
        }

        private String safeReadNonEmptyLine() {
            while (true) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) return line;
                System.out.print("Input cannot be empty. Please re-enter: ");
            }
        }

        private String safeReadEmail(String prompt) {
            while (true) {
                System.out.print(prompt);
                String email = sc.nextLine().trim();
                
                if (email.matches("^[\\w.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}$")) return email;
                System.out.println("Invalid email format. Try again.");
            }
        }

        private String safeReadPhone(String prompt) {
            while (true) {
                System.out.print(prompt);
                String phone = sc.nextLine().trim();
                
                if (phone.matches("^\\d{10,15}$")) return phone;
                System.out.println("Invalid phone. Enter 10-15 digits without spaces or symbols.");
            }
        }
    }

        public static void main(String[] args) {
        new UserInterface().mainMenu();
    }
}