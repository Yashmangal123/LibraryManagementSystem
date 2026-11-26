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

    public Loan borrow(String memberId, String itemId) throws MemberNotFoundException, ItemNotFoundException, ItemNotAvailableException, BorrowLimitExceededException {
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        if (!(item instanceof Borrowable)) {
            throw new ItemNotAvailableException(itemId + " cannot be borrowed");
        }
        if (activeLoans.containsKey(itemId)) {
            throw new ItemNotAvailableException(itemId + " is on loan");
        }
        if (!member.hasCapacity(countLoans(memberId))) {
            throw new BorrowLimitExceededException(memberId, member.getMaxItems());
        }
        Loan loan = new Loan(member, (Borrowable) item, currentDay);
        activeLoans.put(itemId, loan);
        return loan;
    }

    public int returnItem(String itemId) throws ItemNotFoundException, ItemNotOnLoanException {
        findItem(itemId);
        Loan loan = activeLoans.remove(itemId);
        if (loan == null) {
            throw new ItemNotOnLoanException(itemId);
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

    private Member findMember(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }
        return member;
    }

    private LibraryItem findItem(String itemId) throws ItemNotFoundException {
        LibraryItem item = items.get(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        return item;
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
