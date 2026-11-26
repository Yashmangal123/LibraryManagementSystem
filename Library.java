import java.util.*;

public class Library {
    private final Map<String, LibraryItem> items = new TreeMap<>();
    private final Map<String, Member> members = new HashMap<>();

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

    public List<LibraryItem> getInventory() {
        return new ArrayList<>(items.values());
    }
}
