import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final ArrayList<Equipment> equipmentList = new ArrayList<>();
    private static final ArrayList<UniversityMember> memberList = new ArrayList<>();
    private static final ArrayList<Loan> loanList = new ArrayList<>();

    private static int nextLoanId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadSampleData();
        int choice;

        do {
            printMenu();
            choice = readInt(scanner, "Choice: ");

            try {
                switch (choice) {
                    case 1 -> addEquipment(scanner);
                    case 2 -> editEquipment(scanner);
                    case 3 -> deleteEquipment(scanner);
                    case 4 -> registerMember(scanner);
                    case 5 -> editMember(scanner);
                    case 6 -> deleteMember(scanner);
                    case 7 -> borrowEquipment(scanner);
                    case 8 -> returnEquipment(scanner);
                    case 9 -> viewAllEquipment();
                    case 10 -> viewAvailableEquipment();
                    case 11 -> viewMembers();
                    case 12 -> viewLoans();
                    case 13 -> showReminderForLoan(scanner);
                    case 0 -> System.out.println("Goodbye.");
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== University Lab Equipment Loan System ===");
        System.out.println("1. Add Equipment");
        System.out.println("2. Edit Equipment");
        System.out.println("3. Delete Equipment");
        System.out.println("4. Register Member");
        System.out.println("5. Edit Member");
        System.out.println("6. Delete Member");
        System.out.println("7. Borrow Equipment");
        System.out.println("8. Return Equipment");
        System.out.println("9. View All Equipment");
        System.out.println("10. View Available Equipment");
        System.out.println("11. View Members");
        System.out.println("12. View Loans");
        System.out.println("13. Generate Late Reminder");
        System.out.println("0. Exit");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            System.out.println("Please enter a valid whole number.");
            scanner.nextLine();
        }
    }

    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private static Equipment findEquipmentById(int id) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getEquipmentId() == id) {
                return equipment;
            }
        }
        return null;
    }

    private static UniversityMember findMemberById(int id) {
        for (UniversityMember member : memberList) {
            if (member.getMemberId() == id) {
                return member;
            }
        }
        return null;
    }

    private static Loan findLoanById(int id) {
        for (Loan loan : loanList) {
            if (loan.getLoanId() == id) {
                return loan;
            }
        }
        return null;
    }

    private static boolean hasReachedLoanLimit(UniversityMember member) {
        int activeLoans = 0;

        for (Loan loan : loanList) {
            if (loan.getBorrower().getMemberId() == member.getMemberId() && !loan.isReturned()) {
                activeLoans++;
            }
        }

        return activeLoans >= 3;
    }

    private static void addEquipment(Scanner scanner) {
        int id = readInt(scanner, "Equipment ID: ");

        if (findEquipmentById(id) != null) {
            System.out.println("That equipment ID is already in use.");
            return;
        }

        String name = readNonEmptyString(scanner, "Equipment name: ");
        String manufacturer = readNonEmptyString(scanner, "Manufacturer: ");
        String model = readNonEmptyString(scanner, "Model: ");
        LocalDate purchaseDate = readDate(scanner, "Purchase date");
        String category = readNonEmptyString(scanner, "Category: ");

        Equipment equipment = new Equipment(id, name, manufacturer, model, purchaseDate, category);
        equipmentList.add(equipment);

        System.out.println("Equipment added successfully.");
    }

    private static void editEquipment(Scanner scanner) {
        viewAllEquipment();
        int id = readInt(scanner, "Enter equipment ID to edit: ");
        Equipment equipment = findEquipmentById(id);

        if (equipment == null) {
            System.out.println("Equipment not found.");
            return;
        }

        if (!equipment.isAvailable()) {
            System.out.println("Cannot edit equipment while it is on loan.");
            return;
        }

        String newName = readNonEmptyString(scanner, "New name: ");
        String newManufacturer = readNonEmptyString(scanner, "New manufacturer: ");
        String newModel = readNonEmptyString(scanner, "New model: ");
        LocalDate newPurchaseDate = readDate(scanner, "New purchase date");
        String newCategory = readNonEmptyString(scanner, "New category: ");

        equipment.setName(newName);
        equipment.setManufacturer(newManufacturer);
        equipment.setModel(newModel);
        equipment.setPurchaseDate(newPurchaseDate);
        equipment.setCategory(newCategory);

        System.out.println("Equipment updated successfully.");
    }

    private static void deleteEquipment(Scanner scanner) {
        viewAllEquipment();
        int id = readInt(scanner, "Enter equipment ID to delete: ");

        Equipment equipment = findEquipmentById(id);
        if (equipment == null) {
            System.out.println("Equipment not found.");
            return;
        }

        if (!equipment.isAvailable()) {
            System.out.println("Cannot delete equipment that is currently on loan.");
            return;
        }

        equipmentList.remove(equipment);
        System.out.println("Equipment deleted.");
    }

    private static void registerMember(Scanner scanner) {
        System.out.println("1. Student");
        System.out.println("2. Staff");
        int memberTypeChoice = readInt(scanner, "Select member type: ");

        String name = readNonEmptyString(scanner, "Name: ");
        int id = readInt(scanner, "University ID: ");

        if (findMemberById(id) != null) {
            System.out.println("That university ID is already in use.");
            return;
        }

        String contact = readNonEmptyString(scanner, "Contact: ");

        UniversityMember member;
        if (memberTypeChoice == 1) {
            member = new Student(name, id, contact);
        } else if (memberTypeChoice == 2) {
            member = new Staff(name, id, contact);
        } else {
            System.out.println("Invalid member type.");
            return;
        }

        memberList.add(member);
        System.out.println("Member registered.");
    }

    private static void editMember(Scanner scanner) {
        viewMembers();
        int id = readInt(scanner, "Enter member ID to edit: ");
        UniversityMember member = findMemberById(id);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        String newName = readNonEmptyString(scanner, "New name: ");
        String newContact = readNonEmptyString(scanner, "New contact: ");

        member.setName(newName);
        member.setContact(newContact);

        System.out.println("Member updated successfully.");
    }

    private static void deleteMember(Scanner scanner) {
        viewMembers();
        int id = readInt(scanner, "Enter member ID to delete: ");

        UniversityMember member = findMemberById(id);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        for (Loan loan : loanList) {
            if (loan.getBorrower().getMemberId() == id && !loan.isReturned()) {
                System.out.println("Cannot delete member because they still have active loans.");
                return;
            }
        }

        memberList.remove(member);
        System.out.println("Member deleted.");
    }

    private static void borrowEquipment(Scanner scanner) {
        viewAvailableEquipment();

        int equipmentId = readInt(scanner, "Enter equipment ID to borrow: ");
        Equipment equipment = findEquipmentById(equipmentId);

        if (equipment == null) {
            System.out.println("Equipment not found.");
            return;
        }

        if (!equipment.isAvailable()) {
            System.out.println("That equipment is currently unavailable.");
            return;
        }

        viewMembers();
        int memberId = readInt(scanner, "Enter member ID: ");
        UniversityMember member = findMemberById(memberId);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (hasReachedLoanLimit(member)) {
            System.out.println("This member already has the maximum of 3 active loans.");
            return;
        }

        LocalDate checkoutDate = readDate(scanner, "Enter checkout date");
        LocalDate dueDate = readDate(scanner, "Enter due date");

        if (dueDate.isBefore(checkoutDate)) {
            System.out.println("Due date cannot be before checkout date.");
            return;
        }

        Loan loan = new Loan(nextLoanId, equipment, member, checkoutDate, dueDate);
        loanList.add(loan);

        System.out.println("Loan created. Loan ID: " + nextLoanId);
        nextLoanId++;
    }

    private static void returnEquipment(Scanner scanner) {
        viewLoans();

        int loanId = readInt(scanner, "Enter loan ID to return: ");
        Loan loan = findLoanById(loanId);

        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        if (loan.isReturned()) {
            System.out.println("This item has already been returned.");
            return;
        }

        LocalDate returnDate = readDate(scanner, "Enter return date");
        loan.returnItem(returnDate);

        if (loan.isOverdue()) {
            System.out.println("Item returned late.");
            System.out.println("Overdue days: " + loan.getOverdueDays());
            System.out.println("Fee due: £" + String.format("%.2f", loan.calculateOverdueFee()));
        } else {
            System.out.println("Item returned on time.");
        }
    }

    private static void viewAllEquipment() {
        if (equipmentList.isEmpty()) {
            System.out.println("No equipment found.");
            return;
        }

        System.out.println("\n--- Equipment List ---");
        for (Equipment equipment : equipmentList) {
            System.out.println(equipment);
        }
    }

    private static void viewAvailableEquipment() {
        boolean found = false;

        System.out.println("\n--- Available Equipment ---");
        for (Equipment equipment : equipmentList) {
            if (equipment.isAvailable()) {
                System.out.println(equipment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No equipment is currently available.");
        }
    }

    private static void viewMembers() {
        if (memberList.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }

        System.out.println("\n--- Member List ---");
        for (UniversityMember member : memberList) {
            System.out.println(member);
        }
    }

    private static void viewLoans() {
        if (loanList.isEmpty()) {
            System.out.println("No loans recorded.");
            return;
        }

        int activeCount = 0;

        System.out.println("\n--- Loan Records ---");
        for (Loan loan : loanList) {
            System.out.println(loan);
            if (!loan.isReturned()) {
                activeCount++;
            }
        }

        System.out.println("Active loans: " + activeCount);
    }

    private static void showReminderForLoan(Scanner scanner) {
        viewLoans();

        int loanId = readInt(scanner, "Enter loan ID: ");
        Loan loan = findLoanById(loanId);

        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        System.out.println(loan.generateReminder());
    }
    private static void loadSampleData() {
        equipmentList.add(new Equipment(101, "Laptop", "HP", "HP010A",
                LocalDate.of(2022, 5, 10), "Electronics"));

        equipmentList.add(new Equipment(102, "Laptop", "Apple", "AP01A",
                LocalDate.of(2021, 9, 15), "Electronics"));

        equipmentList.add(new Equipment(103, "Laptop", "Lenovo", "LN01A",
                LocalDate.of(2023, 1, 20), "Electronics"));

        memberList.add(new Student("Noah", 2001, "noah@uni.ac.uk"));
        memberList.add(new Staff("Arnav", 3001, "arnav@uni.ac.uk"));

        System.out.println("Sample data loaded.");
    }
}




