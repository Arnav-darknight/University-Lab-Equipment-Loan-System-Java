public class Staff extends UniversityMember {
    public Staff(String name, int memberId, String contact) {
        super(name, memberId, contact);
    }

    @Override
    public String getMemberType() {
        return "Staff";
    }
}