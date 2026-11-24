public class Student extends UniversityMember {
    public Student(String name, int memberId, String contact) {
        super(name, memberId, contact);
    }

    @Override
    public String getMemberType() {
        return "Student";
    }
}