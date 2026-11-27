import java.util.*;

public class Library {

    private final Map<String, LibraryItem> items = new TreeMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, Loan> activeLoans = new HashMap<>();
    private final Map<String, Queue<String>> reservations = new HashMap<>();
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

    public Loan borrow(String memberId, String itemId)
            throws MemberNotFoundException, ItemNotFoundException,
            ItemNotAvailableException, BorrowLimitExceededException {
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
        removeReservation(memberId, itemId);
        return loan;
    }


    public ReturnResult returnItem(String itemId)
            throws ItemNotFoundException, ItemNotOnLoanException {
        findItem(itemId);
        Loan returnedLoan = activeLoans.remove(itemId);
        if (returnedLoan == null) {
            throw new ItemNotOnLoanException(itemId);
        }

        int fine = returnedLoan.calculateFine(currentDay);
        Loan issuedLoan = issueToNextReservedMember(itemId, (Borrowable) returnedLoan.getItem());
        return new ReturnResult(fine, issuedLoan);
    }

    public void reserve(String memberId, String itemId)
            throws MemberNotFoundException, ItemNotFoundException, ItemNotAvailableException {
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        if (!(item instanceof Borrowable)) {
            throw new ItemNotAvailableException(itemId + " cannot be borrowed");
        }

        Loan currentLoan = activeLoans.get(itemId);
        if (currentLoan == null) {
            throw new ItemNotAvailableException(itemId + " is available");
        }
        if (currentLoan.getBorrower().getId().equals(memberId)) {
            throw new ItemNotAvailableException(memberId + " already has " + itemId);
        }

        Queue<String> queue = reservations.computeIfAbsent(itemId, key -> new ArrayDeque<>());
        if (queue.contains(memberId)) {
            throw new ItemNotAvailableException(memberId + " already reserved " + itemId);
        }
        queue.add(member.getId());
    }

    private Loan issueToNextReservedMember(String itemId, Borrowable item) {
        Queue<String> queue = reservations.get(itemId);
        if (queue == null || queue.isEmpty()) {
            return null;
        }

        for (String memberId : queue) {
            Member member = members.get(memberId);
            if (member != null && member.hasCapacity(countLoans(memberId))) {
                queue.remove(memberId);
                Loan newLoan = new Loan(member, item, currentDay);
                activeLoans.put(itemId, newLoan);
                if (queue.isEmpty()) {
                    reservations.remove(itemId);
                }
                return newLoan;
            }
        }
        return null;
    }

    private void removeReservation(String memberId, String itemId) {
        Queue<String> queue = reservations.get(itemId);
        if (queue == null) {
            return;
        }
        queue.remove(memberId);
        if (queue.isEmpty()) {
            reservations.remove(itemId);
        }
    }

    public static class ReturnResult {
        private final int fine;
        private final Loan issuedLoan;

        public ReturnResult(int fine, Loan issuedLoan) {
            this.fine = fine;
            this.issuedLoan = issuedLoan;
        }

        public int getFine() {
            return fine;
        }

        public Loan getIssuedLoan() {
            return issuedLoan;
        }
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

    public List<LibraryItem> search(String key) {
        String wanted = key.toLowerCase(Locale.ROOT);
        List<LibraryItem> found = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.getTitle().toLowerCase(Locale.ROOT).contains(wanted)) {
                found.add(item);
            }
        }
        found.sort(Comparator.comparing(LibraryItem::getTitle)
                .thenComparing(LibraryItem::getId));
        return found;
    }

    public List<Loan> getMemberLoans(String memberId) throws MemberNotFoundException {
        findMember(memberId);
        List<Loan> result = new ArrayList<>();
        for (Loan loan : activeLoans.values()) {
            if (loan.getBorrower().getId().equals(memberId)) {
                result.add(loan);
            }
        }
        Comparator<Loan> byDueDay = Comparator.comparingInt(Loan::getDueDay);
        result.sort(byDueDay.thenComparing(loan -> loan.getItem().getId()));
        return result;
    }

    public List<Loan> getOverdue() {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : activeLoans.values()) {
            if (loan.isOverdue(currentDay)) {
                result.add(loan);
            }
        }
        Comparator<Loan> byDaysLate =
                Comparator.comparingInt(loan -> loan.getDaysLate(currentDay));
        result.sort(byDaysLate.reversed()
                .thenComparing(loan -> loan.getItem().getId()));
        return result;
    }

    public List<LibraryItem> getInventory() {
        return new ArrayList<>(items.values());
    }
}
