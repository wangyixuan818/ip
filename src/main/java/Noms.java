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
        String[] tasks = new String[100];
        int taskCount = 0;

        while (true) {
            String command = scanner.nextLine();

            System.out.println(" " + command);
            System.out.println("____________________________________________________________");

            if (command.equals("bye")) {
                System.out.println("Bye~ Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else {
                tasks[taskCount] = command;
                taskCount++;

                System.out.println(" added: " + command);
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
