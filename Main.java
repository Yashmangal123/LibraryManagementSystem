import java.util.*;

public class Main {
    public static void main(String[] args){
        CommandProcessor processor = new CommandProcessor(new Library());
        processor.runCommands(new Scanner(System.in));
    }
}
