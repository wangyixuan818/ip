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
        boolean[] taskDone = new boolean[100];
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
                    String status = taskDone[i] ? "X" : " ";
                    System.out.println(" " + (i + 1) + ".[" + status + "] " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                String[] parts = command.split("\\s+");

                if (parts.length != 2) {
                    System.out.println(" Please provide a task number to mark as done.");
                    System.out.println("____________________________________________________________");
                    continue;
                }

                try {
                    int taskNumber = Integer.parseInt(parts[1]);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        taskDone[taskIndex] = true;
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number.");
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
