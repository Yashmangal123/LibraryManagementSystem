import java.util.*;

public class Library {

    private final Map<String, LibraryItem> items = new TreeMap<>();
    private final Map<String, Member> members = new HashMap<>();

    private final Map<String, Loan> activeLoans = new HashMap<>();
    private int currentDay = 0;

    public void addBook(Book book) {
        items.put(book.getId(), book);
    }

    public void addMagazine(Magazine magazine) {
        items.put(magazine.getId(), magazine);
    }

    public void addReference(ReferenceBook reference) {
        items.put(reference.getId(), reference);
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    public Loan borrow(String memberId, String itemId) {
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("ERROR member " + memberId + " not found");
            return null;
        }
        LibraryItem item = items.get(itemId);
        if (item == null) {
            System.out.println("ERROR item " + itemId + " not found");
            return null;
        }
        if (!(item instanceof Borrowable)) {
            System.out.println("ERROR " + itemId + " cannot be borrowed");
            return null;
        }
        if (activeLoans.containsKey(itemId)) {
            System.out.println("ERROR " + itemId + " is on loan");
            return null;
        }
        if (!member.hasCapacity(countLoans(memberId))) {
            System.out.println("ERROR " + memberId + " has reached the limit of "
                    + member.getMaxItems());
            return null;
        }
        Loan loan = new Loan(member, (Borrowable) item, currentDay);
        activeLoans.put(itemId, loan);
        return loan;
    }

    public int returnItem(String itemId) {
        if (!items.containsKey(itemId)) {
            System.out.println("ERROR item " + itemId + " not found");
            return -1;
        }
        Loan loan = activeLoans.remove(itemId);
        if (loan == null) {
            System.out.println("ERROR " + itemId + " is not on loan");
            return -1;
        }
        return loan.calculateFine(currentDay);
    }

    public void advanceDay(int days) {
        currentDay += days;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public Loan getLoanFor(String itemId) {
        return activeLoans.get(itemId);
    }

    private int countLoans(String memberId) {
        int count = 0;
        for (Loan loan : activeLoans.values()) {
            if (loan.getBorrower().getId().equals(memberId)) {
                count++;
            }
        }
        return count;
    }

    public List<LibraryItem> getInventory() {
        return new ArrayList<>(items.values());
    }
}
