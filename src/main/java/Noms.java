import java.util.Scanner;

public class Noms {
    public static void main(String[] args) {
        String banner = "____________________________________________________________\n"
                + " _   _  ___  __  __  ____\n"
                + "| \\ | |/ _ \\|  \\/  |/ ___|\n"
                + "|  \\| | | | | |\\/| | \\___ \\\n"
                + "| |\\  | |_| | |  | |  ___) |\n"
                + "|_| \\_|\\___/|_|  |_| |____/\n"
                + "____________________________________________________________";
        System.out.println(banner);
        System.out.println("Hello! I'm Noms.");
        System.out.println("NomNom, have you eaten? What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();

            System.out.println(" " + command);
            System.out.println("____________________________________________________________");

            if (command.equals("bye")) {
                System.out.println("Bye~ Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }
        }

        scanner.close();
    }
}
