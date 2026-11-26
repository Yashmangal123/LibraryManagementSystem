public abstract class LibraryItem {
    private final String itemCode;
    private final String itemName;

    public LibraryItem(String itemCode, String itemName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
    }

    public String getId() {
        return itemCode;
    }

    public String getTitle() {
        return itemName;
    }

    public abstract String getType();
}
