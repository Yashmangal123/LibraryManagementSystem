/** One item lent to one member. */
public class Loan {
    private final Member member;
    private final Borrowable borrowedItem;
    private final int borrowDay;
    private final int dueDay;

    public Loan(Member member, Borrowable borrowedItem, int borrowDay, int dueDay) {
        this.member = member;
        this.borrowedItem = borrowedItem;
        this.borrowDay = borrowDay;
        this.dueDay = dueDay;
    }

    public Member getBorrower() {
        return member;
    }

    public Borrowable getItem() {
        return borrowedItem;
    }

    public int getBorrowDay() {
        return borrowDay;
    }

    public int getDueDay() {
        return dueDay;
    }
}
