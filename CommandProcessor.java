import java.util.*;

public class CommandProcessor {
    private final Library library;

    public CommandProcessor(Library library) {
        this.library = library;
    }

    public void runCommands(Scanner input) {
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (!line.trim().isEmpty()) {
                processCommand(line);
            }
        }
    }

    public void processCommand(String line) {
        String[] parts = line.split("\\|", -1);

        switch (parts[0]) {
            case "ADD_BOOK":
                library.addBook(new Book(parts[1], parts[2], parts[3]));
                System.out.println("OK");
                break;
            case "ADD_MAGAZINE":
                library.addMagazine(new Magazine(parts[1], parts[2], parts[3]));
                System.out.println("OK");
                break;
            case "ADD_REFERENCE":
                library.addReference(new ReferenceBook(parts[1], parts[2], parts[3]));
                System.out.println("OK");
                break;
            case "ADD_MEMBER":
                library.addMember(createMember(parts[1], parts[2], parts[3]));
                System.out.println("OK");
                break;
            case "BORROW":
                Loan loan = library.borrow(parts[1], parts[2]);
                if (loan != null) {
                    System.out.println("OK due on day " + loan.getDueDay());
                }
                break;
            case "RETURN":
                int fine = library.returnItem(parts[1]);
                if (fine >= 0) {
                    System.out.println("OK fine " + fine);
                }
                break;
            case "ADVANCE":
                library.advanceDay(Integer.parseInt(parts[1]));
                System.out.println("OK today is day " + library.getCurrentDay());
                break;
            case "INVENTORY":
                printInventory();
                break;
            default:
                break;
        }
    }

    private Member createMember(String id, String name, String type) {
        switch (type) {
            case "STUDENT":
                return new Student(id, name);
            case "STAFF":
                return new Staff(id, name);
            case "FACULTY":
                return new Faculty(id, name);
            default:
                throw new IllegalArgumentException("unknown member type " + type);
        }
    }

    private void printInventory() {
        List<LibraryItem> inventory = library.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("NONE");
            return;
        }
        for (LibraryItem item : inventory) {
            System.out.println(item.getId() + "|" + item.getType() + "|" + statusOf(item));
        }
    }

    private String statusOf(LibraryItem item) {
        if (!(item instanceof Borrowable)) {
            return "NOT_BORROWABLE";
        }
        Loan loan = library.getLoanFor(item.getId());
        if (loan == null) {
            return "AVAILABLE";
        }
        return "ON_LOAN|" + loan.getBorrower().getId();
    }
}
