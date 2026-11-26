public class Main {
    public static void main(String[] args){
        Book book = new Book("B1", "Introduction to Algorithms", "Cormen");
        Magazine magazine = new Magazine("M1", "Resonance", "Oct 2026");
        ReferenceBook reference = new ReferenceBook("R1", "Mahabharata", "Vyasa");

        System.out.println(book.getType() + " fine " + book.getFinePerDay());
        System.out.println(magazine.getType() + " fine " + magazine.getFinePerDay());
        System.out.println(reference.getType() + " cannot be borrowed");

        Student student =  new Student("S1", "Ananya Rao");
        Staff staff = new Staff("T1", "Ravi Kumar");
        Faculty faculty = new Faculty("F1", "Meera Iyer");
        
        System.out.println(student.getName() + " limit " + student.getMaxItems() + " period " + student.getLoanPeriod());
        System.out.println(staff.getName() + " limit " + staff.getMaxItems() + " period " + staff.getLoanPeriod());
        System.out.println(faculty.getName() + " limit " + faculty.getMaxItems() + " period " + faculty.getLoanPeriod());
        
    }
}
