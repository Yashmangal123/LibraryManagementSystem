public class Loan {
    private final Member member;
    private final Borrowable borrowedItem;
    private final int borrowDay;
    private final int dueDay;

    public Loan(Member member, Borrowable borrowedItem, int borrowDay) {
        this.member = member;
        this.borrowedItem = borrowedItem;
        this.borrowDay = borrowDay;
        this.dueDay = borrowDay + member.getLoanPeriod();
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

    /** 0 if returned on or before the due day. */
    public int getDaysLate(int today) {
        return Math.max(0, today - dueDay);
    }

    public boolean isOverdue(int currentDay) {
        return currentDay > dueDay;
    }

    public int calculateFine(int today) {
        return getDaysLate(today) * borrowedItem.getFinePerDay();
    }
}
