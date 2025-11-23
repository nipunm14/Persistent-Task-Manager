# Persistent Task Manager (PTM)

The Persistent Task Manager (PTM) is a simple, console-based Java application designed to help users manage their to-do lists effectively. It features persistence through file serialization (`.dat`) for program data and generates a human-readable CSV file (`.csv`) for data inspection.

##  Key Features

* **Task Management (CRUD):** Add, View, Mark Complete, and Delete tasks.
* **Persistence:** Tasks are automatically saved and loaded using Java Serialization (`tasks.dat`).
* **Data Inspection:** A human-readable backup file (`tasks.csv`) is automatically generated on every save operation.
* **Priority System:** Tasks can be assigned High (1), Medium (2), or Low (3) priority.
* **Sorting:** View tasks sorted by **Priority** or **Due Date**.
* **User Interface:** Simple, menu-driven console interface for easy interaction.

##  Technology Stack

* **Language:** Java
* **Persistence:**
    * Primary: Java Serialization (`ObjectOutputStream`/`ObjectInputStream`)
    * Secondary (Inspection): CSV File I/O (`PrintWriter`/`FileWriter`)
* **Date/Time Handling:** `java.time.LocalDate`

##  Project Structure

The project is composed of three core classes:

| File Name | Description |
| :--- | :--- |
| `Main.java` | Contains the `main` method and handles the console-based User Interface (UI), input validation, and menu navigation. |
| `TaskManager.java` | Manages the collection of `Task` objects. Implements the business logic for persistence (loading/saving to `.dat` and `.csv`) and task operations (add, delete, sort). |
| `Task.java` | The model class representing a single task. It implements `Serializable` for persistence and handles the task's properties (title, description, priority, due date, completion status). |

##  Getting Started

### Prerequisites

You need a Java Development Kit (JDK) installed on your system.

### Compiling and Running

1.  **Download the files :**
    ```bash
    Main.java, TaskManager.java, Task.java
    ```
2.  **Compile the Java files:**
    ```bash
    javac Main.java TaskManager.java Task.java
    ```
3.  **Run the application:**
    ```bash
    java Main
    ```

##  Usage

Here is a description of each menu option:

| Option | Description | Action Performed |
| :--- | :--- | :--- |
| **1. Add New Task** | Allows you to create a new task. You will be prompted to enter a **Title**, **Description**, **Priority** (1=High, 2=Medium, 3=Low), and **Due Date**. | Creates a `Task` object, adds it to the list, and automatically saves the updated list to both `tasks.dat` and `tasks.csv`. |
| **2. View All Tasks (Unsorted)** | Displays the complete list of tasks in the order they were added. | Calls `manager.getAllTasks()` and prints the list to the console, showing the index and details for each task. |
| **3. Mark Task as Complete** | Changes the status of an existing task to `[COMPLETED]`. | Prompts for the task number, updates the task object's completion status, and saves the changes. |
| **4. Delete Task** | Permanently removes a task from the list. | Prompts for the task number, removes the task from the list, and saves the updated list. |
| **5. Sort and View Tasks** | Provides a sub-menu to view tasks sorted by different criteria. | Prompts for sort choice (`1` for Priority, `2` for Date), uses `manager.getSortedTasks(sortBy)`, and displays the sorted list. |
| **6. Exit & Save** | Saves the current state of the task list and closes the application. | Calls `manager.saveTasks()`, ensuring the latest data is written to both `tasks.dat` and `tasks.csv`, then terminates the program. |


## Date and Priority Format 


* **Due Date:** Must be entered in the format **`dd-MM-yyyy`** (e.g., `25-11-2025`).
* **Priority:** Must be a number from **1** (High), **2** (Medium), or **3** (Low).

## Data Files

The application will create the following files in the project directory:

| File | Purpose |
| :--- | :--- |
| `tasks.dat` | Primary data storage. This is a binary file used by the program to load and save the object structure. |
| `tasks.csv` | Secondary data storage. This human-readable file is generated for inspection/debugging purposes. |

##  Code Highlights

### Dual Persistence in `TaskManager.java`

The `TaskManager` class ensures robust data saving by utilizing two methods:

1.  **Serialization (`.dat`):** Ensures all object state is perfectly preserved for the program.
2.  **CSV Export (`.csv`):** Provides a simple, cross-platform, human-readable view of the data.

```java
// Simplified persistence snippet from TaskManager.java
public void saveTasks() {
    // 1. Primary Save via Serialization
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DAT_FILE))) {
        oos.writeObject(tasks);
    } catch (IOException e) { /* ... */ }
    
    // 2. Secondary Save to CSV
    saveTasksToCsv();
}
```

## Author:
This project was developed by:

* **NIPUN MITTAL** 
* REG NO. 24MIM10186
