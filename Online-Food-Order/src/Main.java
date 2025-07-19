import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name :: ");
        System.out.flush();  // <-- Flush output to force printing
        String name = scanner.nextLine();

        System.out.print("Enter email :: ");
        System.out.flush();  // <-- Flush again
        String email = scanner.nextLine();

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}