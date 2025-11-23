import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;  
import java.util.List;
import java.util.Scanner;

/**
 * The main class for the Persistent Task Manager (PTM).
 * Provides the console-based user interface and interaction logic.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager manager = new TaskManager();
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("|| Persistent Task Manager (PTM) Initialized ||");
        System.out.println("=============================================");

        boolean running = true;
        while (running) {
            displayMenu();
            try {
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (choice) {
                        case 1 -> addTask();
                        case 2 -> viewTasks(manager.getAllTasks());
                        case 3 -> markTaskCompleted();
                        case 4 -> deleteTask();
                        case 5 -> sortTasksMenu();
                        case 6 -> {
                            manager.saveTasks(); // Manual save for confirmation
                            System.out.println("Data saved. Exiting PTM. Goodbye!");
                            running = false;
                        }
                        default -> System.out.println("Invalid choice. Please enter a number from 1 to 6.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume the invalid input
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks (Unsorted)");
        System.out.println("3. Mark Task as Complete");
        System.out.println("4. Delete Task");
        System.out.println("5. Sort and View Tasks");
        System.out.println("6. Exit & Save");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        System.out.println("\n--- Add New Task ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        int priority = getPriorityInput();
        LocalDate dueDate = getDueDateInput();

        Task newTask = new Task(title, description, priority, dueDate);
        manager.addTask(newTask);
        System.out.println("\nSUCCESS: Task added and saved.");
    }

    private static int getPriorityInput() {
        while (true) {
            System.out.print("Priority (1=High, 2=Medium, 3=Low): ");
            try {
                int priority = Integer.parseInt(scanner.nextLine());
                if (priority >= 1 && priority <= 3) {
                    return priority; //nipun
                } else {
                    System.out.println("Priority must be 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static LocalDate getDueDateInput() {
        while (true) {
            System.out.printf("Due Date (%s): ", INPUT_DATE_FORMAT.toString());
            String dateStr = scanner.nextLine();
            try {
                return LocalDate.parse(dateStr, INPUT_DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use " + INPUT_DATE_FORMAT.toString() + " (e.g., 25-11-2025).");
            }
        }
    }

    private static void viewTasks(List<Task> tasks) {
        System.out.println("\n--- Task List ---");
        if (tasks.isEmpty()) {
            System.out.println("The task list is empty. Add a new task (Option 1).");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, tasks.get(i).toString());
        }
        System.out.println("-----------------");
    }
    
    private static void sortTasksMenu() {
        System.out.println("\n--- Sort Options ---");
        System.out.println("1. Sort by Priority (High to Low)");
        System.out.println("2. Sort by Due Date (Closest First)");
        System.out.print("Enter sort choice (1 or 2): ");
        
        try {
            int sortChoice = Integer.parseInt(scanner.nextLine());
            String sortBy = "";
            if (sortChoice == 1) {
                sortBy = "priority";
            } else if (sortChoice == 2) {
                sortBy = "date";
            } else {
                System.out.println("Invalid sort option. Returning to main menu.");
                return;
            }
            viewTasks(manager.getSortedTasks(sortBy));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private static void markTaskCompleted() {
        List<Task> currentTasks = manager.getAllTasks();
        if (currentTasks.isEmpty()) {
            System.out.println("No tasks to mark complete.");
            return;
        }
        viewTasks(currentTasks);
        System.out.print("Enter the number of the task to mark as complete: ");
        
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine());
            if (manager.markTaskCompleted(taskNumber)) {
                System.out.println("SUCCESS: Task " + taskNumber + " marked as completed and saved.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private static void deleteTask() {
        List<Task> currentTasks = manager.getAllTasks();
        if (currentTasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return;
        }
        viewTasks(currentTasks);
        System.out.print("Enter the number of the task to delete: ");

        try {
            int taskNumber = Integer.parseInt(scanner.nextLine());
            if (manager.deleteTask(taskNumber)) {
                System.out.println("SUCCESS: Task " + taskNumber + " deleted and saved.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}