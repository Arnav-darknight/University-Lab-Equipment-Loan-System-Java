import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//-----------------------Represents a loan record for a piece of equipment-----------------------//
public class Loan {
    private static final double DAILY_OVERDUE_FEE = 2.00;

    private int loanId;
    private Equipment equipment;
    private UniversityMember borrower;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(int loanId, Equipment equipment, UniversityMember borrower,
                LocalDate checkoutDate, LocalDate dueDate) {
        if (loanId < 0) {
            throw new IllegalArgumentException("Loan ID cannot be negative.");
        }
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment is required.");
        }
        if (borrower == null) {
            throw new IllegalArgumentException("Borrower is required.");
        }
        if (checkoutDate == null || dueDate == null) {
            throw new IllegalArgumentException("Checkout date and due date are required.");
        }
        if (dueDate.isBefore(checkoutDate)) {
            throw new IllegalArgumentException("Due date cannot be before checkout date.");
        }
        if (!equipment.isAvailable()) {
            throw new IllegalArgumentException("Equipment is not available.");
        }

        this.loanId = loanId;
        this.equipment = equipment;
        this.borrower = borrower;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = null;

        equipment.markasBorrowed();
    }

    public int getLoanId() {
        return loanId;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public UniversityMember getBorrower() {
        return borrower;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public void returnItem(LocalDate dateReturned) {
        if (isReturned()) {
            throw new IllegalStateException("This item has already been returned.");
        }
        if (dateReturned == null) {
            throw new IllegalArgumentException("Return date is required.");
        }
        if (dateReturned.isBefore(checkoutDate)) {
            throw new IllegalArgumentException("Return date cannot be before checkout date.");
        }

        this.returnDate = dateReturned;
        equipment.markasReturned();
    }

    public boolean isOverdue() {
        if (isReturned()) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        LocalDate comparisonDate = isReturned() ? returnDate : LocalDate.now();

        if (!comparisonDate.isAfter(dueDate)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(dueDate, comparisonDate);
    }

    public double calculateOverdueFee() {
        return getOverdueDays() * DAILY_OVERDUE_FEE;
    }

    public String generateReminder() {
        if (!isOverdue()) {
            return "No reminder needed. This loan is not overdue.";
        }

        return "Reminder: " + borrower.getName() + " has overdue equipment (" + equipment.getName() + "). Overdue by " + getOverdueDays() + " day(s). Current fee: £" + String.format("%.2f", calculateOverdueFee());
    }

    @Override
    public String toString() {
        return "Loan ID: " + loanId + ", Equipment: " + equipment.getName() + ", Borrower: " + borrower.getName() + ", Checked out: " + checkoutDate + ", Due: " + dueDate + ", Returned: " + (returnDate == null ? "Not returned" : returnDate) + ", Status: " + (isReturned() ? "Closed" : "Active") + (isOverdue() ? ", Overdue Fee: £" + String.format("%.2f", calculateOverdueFee()) : "");
    }
}