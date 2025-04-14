import java.util.Scanner;
import java.util.regex.Pattern;

public class ChatApplication {
    private static User currentUser = null;
    private static User[] registeredUsers = new User[10];
    private static int userCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        registerUser(scanner);
                        break;
                    case 2:
                        loginUser(scanner);
                        break;
                    case 3:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } else {
                System.out.println("\n1. Send Message\n2. View Messages\n3. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        sendMessage(scanner);
                        break;
                    case 2:
                        viewMessages();
                        break;
                    case 3:
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        }
        scanner.close();
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("\n--- Registration ---");
        
        // Username input and validation
        System.out.print("Enter username (Ensure username contains an underscore and is no more than five characters long): ");
        String username = scanner.nextLine();
        boolean validUsername = checkUserName(username);
        
        if (!validUsername) {
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            return;
        }
        
        // Password input and validation
        System.out.print("Enter password ( Ensure password is at least eight characters long\n" +
"· Contain a capital letter\n" +
"· Contain a number\n" +
"· Contain a special character): ");
        String password = scanner.nextLine();
        boolean validPassword = checkPasswordComplexity(password);
        
        if (!validPassword) {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            return;
        }
        
        // Cell phone input and validation
        System.out.print("Enter cell phone number (with country code, +27): ");
        String cellPhone = scanner.nextLine();
        boolean validCellPhone = checkCellPhoneNumber(cellPhone);
        
        if (!validCellPhone) {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            return;
        }
        
        // Personal details
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        // Create and store user
        User newUser = new User(username, password, cellPhone, firstName, lastName);
        registeredUsers[userCount++] = newUser;
        
        System.out.println("Registration successful!");
        System.out.println("Username successfully captured.");
        System.out.println("Password successfully captured.");
        System.out.println("Cell phone number successfully added.");
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        for (int i = 0; i < userCount; i++) {
            if (registeredUsers[i].getUsername().equals(username) && 
                registeredUsers[i].getPassword().equals(password)) {
                currentUser = registeredUsers[i];
                System.out.println("Welcome " + currentUser.getFirstName() + ", " + 
                                 currentUser.getLastName() + " it is great to see you again.");
                return;
            }
        }
        
        System.out.println("Username or password incorrect, please try again.");
    }

    private static void sendMessage(Scanner scanner) {
        System.out.println("\n--- Send Message ---");
        System.out.print("Enter recipient's username: ");
        String recipient = scanner.nextLine();
        
        User recipientUser = null;
        for (int i = 0; i < userCount; i++) {
            if (registeredUsers[i].getUsername().equals(recipient)) {
                recipientUser = registeredUsers[i];
                break;
            }
        }
        
        if (recipientUser == null) {
            System.out.println("Recipient not found.");
            return;
        }
        
        System.out.print("Enter your message: ");
        String messageText = scanner.nextLine();
        
        Message message = new Message(currentUser.getUsername(), recipient, messageText);
        recipientUser.receiveMessage(message);
        
        System.out.println("Message sent successfully.");
    }

    private static void viewMessages() {
        System.out.println("\n--- Your Messages ---");
        if (currentUser.getMessages().length == 0) {
            System.out.println("No messages found.");
            return;
        }
        
        for (Message message : currentUser.getMessages()) {
            if (message != null) {
                System.out.println("From: " + message.getSender());
                System.out.println("Message: " + message.getPayload());
                System.out.println("Status: " + (message.isRead() ? "Read" : "Unread"));
                System.out.println("---------------------");
                message.markAsRead();
            }
        }
    }

    // Validation methods
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public static boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        if (password.length() < 8) return false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }

    // AI-generated regex for phone number validation (inspired by ChatGPT)
    // Pattern explanation: ^\+\d{1,3} - country code with + prefix
    // \d{10}$ - exactly 10 digits after country code
    public static boolean checkCellPhoneNumber(String cellPhone) {
        Pattern pattern = Pattern.compile("^((?:\\+27|27)|0)(\\d{9})$");
        return pattern.matcher(cellPhone).matches();
    }
}

class User {
    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;
    private Message[] messages;
    private int messageCount;
    
    public User(String username, String password, String cellPhone, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messages = new Message[100];
        this.messageCount = 0;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellPhone() { return cellPhone; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Message[] getMessages() { return messages; }
    
    public void receiveMessage(Message message) {
        if (messageCount < messages.length) {
            messages[messageCount++] = message;
            message.markAsReceived();
        }
    }
}

class Message {
    private String sender;
    private String recipient;
    private String payload;
    private boolean sent;
    private boolean received;
    private boolean read;
    
    public Message(String sender, String recipient, String payload) {
        this.sender = sender;
        this.recipient = recipient;
        this.payload = payload;
        this.sent = true;
        this.received = false;
        this.read = false;
    }
    
    // Getters
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getPayload() { return payload; }
    public boolean isSent() { return sent; }
    public boolean isReceived() { return received; }
    public boolean isRead() { return read; }
    
    public void markAsReceived() { this.received = true; }
    public void markAsRead() { this.read = true; }
}
