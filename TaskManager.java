import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Manages the collection of tasks, implementing dual persistence:
 * Primary (loading/saving) via .dat (Serialization) for the program,
 * Secondary (saving only) via .csv for human inspection.
 */
public class TaskManager {
    private List<Task> tasks;
    
    // Primary file for program loading/saving
    private static final String DAT_FILE = "tasks.dat"; 
    
    // Secondary file for human-readable inspection
    private static final String CSV_FILE = "tasks.csv"; 

    public TaskManager() {
        // Initialize the task list and immediately attempt to load existing data
        this.tasks = loadTasks(); // Loads from DAT_FILE
    }

    // --- Primary Persistence Logic (File I/O & Serialization: .dat) ---

    /**
     * Saves the current list of tasks to the primary .dat file using Serialization.
     * Also calls saveTasksToCsv() to create the backup/inspection file.
     */
    public void saveTasks() {
        // 1. Save to DAT (Primary format for the program)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DAT_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks to DAT: " + e.getMessage());
        }
        
        // 2. Save to CSV (Secondary format for checking/debugging)
        saveTasksToCsv();
    }

    /**
     * Loads the list of tasks from the primary .dat file.
     * @return The loaded list of tasks.
     */
    @SuppressWarnings("unchecked")
    private List<Task> loadTasks() {
        File file = new File(DAT_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // Start with an empty list if file not found
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DAT_FILE))) {
            return (List<Task>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Error handling for DAT file loading
            System.err.println("Task data file not found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks from DAT. Starting fresh: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    // --- Secondary Persistence Logic (CSV Generation: .csv) ---

    /**
     * Saves the current list of tasks to the secondary CSV file for human inspection.
     * This method is called automatically by saveTasks().
     */
    private void saveTasksToCsv() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write Header
            writer.println("Description,Priority,DueDate,IsCompleted"); 
            
            for (Task task : tasks) {
                // Format the Task object into a CSV line
                String csvLine = String.format("%s,%d,%s,%b",
                    task.getDescription(), 
                    task.getPriority(), 
                    task.getDueDate().toString(), // Use standard date format (YYYY-MM-DD)
                    task.isCompleted()
                );
                writer.println(csvLine);
            }
            // System.out.println("Tasks saved successfully to CSV for inspection.");
        } catch (IOException e) {
            System.err.println("Error saving tasks to CSV: " + e.getMessage());
        }
    }


    // --- CRUD Operations (NO CHANGE) ---

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks(); // This now saves both DAT and CSV
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public boolean markTaskCompleted(int index) {
        try {
            Task task = tasks.get(index - 1);
            task.markCompleted();
            saveTasks();
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid task number.");
            return false;
        }
    }

    public boolean deleteTask(int index) {
        try {
            tasks.remove(index - 1);
            saveTasks();
            return true;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid task number.");
            return false;
        }
    }

    // --- Algorithm: Sorting Logic (NO CHANGE) ---

    public List<Task> getSortedTasks(String sortBy) {
        List<Task> sortedList = new ArrayList<>(tasks);
        
        if ("priority".equalsIgnoreCase(sortBy)) {
            Collections.sort(sortedList, Comparator
                .comparingInt(Task::getPriority)
                .thenComparing(Task::isCompleted)
            );
        } else if ("date".equalsIgnoreCase(sortBy)) {
             Collections.sort(sortedList, Comparator
                .comparing(Task::getDueDate)
                .thenComparing(Task::isCompleted)
            );
        } else {
            return tasks;
        }
        return sortedList;
    }
}