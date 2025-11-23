import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single task item in the Persistent Task Manager.
 * nipun 24mim10186
 * Implements Serializable so its data can be written to and read from a file.
 */
public class Task implements Serializable {

    // Unique ID for serialization compatibility
    private static final long serialVersionUID = 1L; 

    private String title;
    private String description;
    private int priority; // 1 (High) to 3 (Low)
    private LocalDate dueDate;
    private boolean isCompleted;

    // Formatter for display purposes
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Task(String title, String description, int priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = false; // Default status is incomplete
    }

    // --- Getters ---
    public String getTitle() { return title; }
    public String getDescription() { return description;}
    public int getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return isCompleted; }

    // --- Setters / Actions ---
    public void markCompleted() {
        this.isCompleted = true;
    }

    // --- Custom Display Method ---
    @Override
    public String toString() {
        String status = isCompleted ? "[COMPLETED]" : "[PENDING]";
        String priorityLevel = switch (priority) {
            case 1 -> "HIGH";
            case 2 -> "MEDIUM";
            case 3 -> "LOW";
            default -> "N/A";
        };

        return String.format(
            "%s %s (Due: %s) - Priority: %s\n   Description: %s",
            status, title, dueDate.format(DATE_FORMAT), priorityLevel, description
        );
    }
}