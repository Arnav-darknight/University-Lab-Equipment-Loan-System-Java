/**
 * Base class for a university member who can borrow equipment.
 */
public abstract class UniversityMember {
    private String name;
    private int memberId;
    private String contact;

    public UniversityMember(String name, int memberId, String contact) {
        if (memberId < 0) {
            throw new IllegalArgumentException("Member ID cannot be negative.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("Contact cannot be empty.");
        }

        this.name = name;
        this.memberId = memberId;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getContact() {
        return contact;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public void setContact(String contact) {
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("Contact cannot be empty.");
        }
        this.contact = contact;
    }

    public abstract String getMemberType();

    @Override
    public String toString() {
        return getMemberType() + " - Name: " + name + ", ID: " + memberId + ", Contact: " + contact;
    }
}