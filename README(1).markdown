# Fitness Logger

A Java console-based workout tracker called **Fitness Logger**, designed to log strength, cardio, and endurance exercises, view workout history, and track progress. This project, authored by "GymRat," features an ASCII-art UI, robust input parsing, and persistent storage in `log.txt`. It was updated to rename `RunningEntry` to `EnduranceEntry` (line 126) for clarity, supporting exercises like running or swimming. This `README.md` is converted from a detailed code walkthrough PDF, covering every class, method, and feature for a student presentation.

## Overview of FitnessLogger Class

- **Purpose**: The main public class containing the entire application, defining data structures, user interaction methods, and the main method.
- **Features**:
  - Add exercises (strength, cardio, endurance).
  - View workout logs.
  - Track progress (e.g., reps, duration, speed).
  - Display ASCII-art menus.
  - Store data in `log.txt`.
- **Structure**: Includes:
  - `LogEntry` interface for exercise entries.
  - Inner static classes: `StrengthEntry`, `CardioEntry`, `EnduranceEntry`.
  - Utility methods for parsing, display, and file I/O.

## LogEntry Interface

```java
interface LogEntry {
    String toCSV();
    String toString();
    LocalDate getDate();
}
```

- **Purpose**: Ensures all exercise entries implement common methods for CSV storage, display, and date access.
- **Methods**:
  - `toCSV()`: Returns a semicolon-separated string for file storage.
  - `toString()`: Returns a readable string for user display.
  - `getDate()`: Returns the entry’s `LocalDate` for sorting/comparison.
- **Role**: Allows uniform handling of entries when saving, loading, or sorting.

## StrengthEntry Class

```java
static class StrengthEntry implements LogEntry {
    String name;
    int sets, reps;
    LocalDate date;
    // Constructor and methods below...
}
```

- **Represents**: Strength exercises (e.g., Push-ups, Squats).
- **Fields**:
  - `name` (String): Exercise name (e.g., "Push-ups").
  - `sets` (int): Number of sets.
  - `reps` (int): Reps per set.
  - `date` (LocalDate): Exercise date.
- **Constructor**: `StrengthEntry(String name, int sets, int reps, LocalDate date)`.

### StrengthEntry Methods

- **int totalReps()**:
  - Calculates total reps: `return sets * reps;`.
  - Example: 3 sets of 10 reps = 30 total reps.
- **String formattedDate()**:
  - Formats date as "dd/MM/yyyy" using `DateTimeFormatter.ofPattern("dd/MM/yyyy")`.
  - Example: 2025-05-18 → "18/05/2025".
- **public String toString()**:
  - Returns: `formattedDate() + " - Strength: " + name + ": " + sets + " sets of " + reps + " reps"`.
  - Example: "18/05/2025 - Strength: Push-ups: 3 sets of 10 reps".
- **public String toCSV()**:
  - Returns: `"strength;" + name + ";" + sets + ";" + reps + ";" + date`.
  - Example: "strength;Push-ups;3;10;2025-05-18".
- **public LocalDate getDate()**:
  - Returns the `date` field.
- **static StrengthEntry fromCSV(String[] parts)**:
  - Parses CSV array `["strength", name, sets, reps, date]` into a `StrengthEntry`.
  - Example: `return new StrengthEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));`.

## CardioEntry Class

```java
static class CardioEntry implements LogEntry {
    String name;
    int duration, sets;
    LocalDate date;
    // Constructor and methods below...
}
```

- **Represents**: Cardio exercises (e.g., Plank).
- **Fields**:
  - `name` (String): Exercise name (e.g., "Plank").
  - `duration` (int): Duration in seconds (e.g., 90).
  - `sets` (int): Number of sets.
  - `date` (LocalDate): Exercise date.
- **Constructor**: `CardioEntry(String name, int duration, int sets, LocalDate date)`.

### CardioEntry Methods

- **String formattedDate()**:
  - Same as `StrengthEntry`, returns "dd/MM/yyyy".
- **String formatDuration()**:
  - Converts seconds to "h/m/s" format, omitting leading zeros.
  - Code:
    ```java
    int h = duration / 3600;
    int m = (duration % 3600) / 60;
    int s = duration % 60;
    if (h > 0) return String.format("%dh%dm%ds", h, m, s);
    if (m > 0) return String.format("%dm%ds", m, s);
    return s + "s";
    ```
  - Examples: 3600 → "1h0m0s", 120 → "2m0s", 45 → "45s".
- **public String toString()**:
  - Returns: `formattedDate() + " - Cardio: " + name + ": " + sets + " sets of " + formatDuration()`.
  - Example: "18/05/2025 - Cardio: Plank: 2 sets of 1m0s".
- **public String toCSV()**:
  - Returns: `"cardio;" + name + ";" + duration + ";" + sets + ";" + date`.
  - Example: "cardio;Plank;60;2;2025-05-18".
- **public LocalDate getDate()**:
  - Returns the `date` field.
- **static CardioEntry fromCSV(String[] parts)**:
  - Parses `["cardio", name, duration, sets, date]` into a `CardioEntry`.

## EnduranceEntry Class

```java
static class EnduranceEntry implements LogEntry {
    String name;
    int distance, duration;
    LocalDate date;
    // Constructor and methods below...
}
```

- **Represents**: Endurance exercises (e.g., Running, Swimming).
- **Fields**:
  - `name` (String): Exercise name (e.g., "Swimming").
  - `distance` (int): Distance in meters (e.g., 5000 for 5km).
  - `duration` (int): Duration in seconds.
  - `date` (LocalDate): Exercise date.
- **Constructor**: `EnduranceEntry(String name, int distance, int duration, LocalDate date)`.

### EnduranceEntry Methods

- **String formattedDate()**:
  - Returns "dd/MM/yyyy".
- **String formatDistance()**:
  - Converts meters to "km" or "m":
    ```java
    return distance >= 1000 ? (distance / 1000.0) + "km" : distance + "m";
    ```
  - Examples: 1500 → "1.5km", 800 → "800m".
- **String formatDuration()**:
  - Same as `CardioEntry`, e.g., 1200 → "20m0s".
- **public String toString()**:
  - Returns: `formattedDate() + " - Endurance: " + name + ": " + formatDistance() + " in " + formatDuration()`.
  - Example: "18/05/2025 - Endurance: Swimming: 1.0km in 20m0s".
- **public String toCSV()**:
  - Returns: `"endurance;" + name + ";" + distance + ";" + duration + ";" + date`.
  - Example: "endurance;Running;5000;1800;2025-05-18".
- **public LocalDate getDate()**:
  - Returns the `date` field.
- **static EnduranceEntry fromCSV(String[] parts)**:
  - Parses `["endurance", name, distance, duration, date]` into an `EnduranceEntry`.

## Log Data Storage (log List and log.txt)

```java
static List<LogEntry> log = new ArrayList<>();
static final String LOG_FILE = "log.txt";
```

- **log List**: A static `List<LogEntry>` holding all exercise entries in memory.
- **LOG_FILE**: The file `log.txt` for persistent storage.

### loadLog() Method

```java
static void loadLog() {
    File file = new File(LOG_FILE);
    if (!file.exists()) return;
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(";");
                switch (parts[0]) {
                    case "strength": log.add(StrengthEntry.fromCSV(parts)); break;
                    case "cardio": log.add(CardioEntry.fromCSV(parts)); break;
                    case "endurance": log.add(EnduranceEntry.fromCSV(parts)); break;
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading log: " + e.getMessage());
    }
}
```

- **Purpose**: Reads `log.txt` and populates the `log` list.
- **Behavior**:
  - Checks if `log.txt` exists; if not, returns.
  - Reads lines, splits by semicolon, and creates entries based on type (`strength`, `cardio`, `endurance`).
  - Handles I/O errors with a message.

### saveLog() Method

```java
static void saveLog() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
        for (LogEntry e : log) {
            writer.write(e.toCSV());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving log: " + e.getMessage());
    }
}
```

- **Purpose**: Writes `log` entries to `log.txt`, overwriting previous content.
- **Behavior**:
  - Writes each entry’s `toCSV()` output as a line.
  - Handles I/O errors.

## Display and UI Utility Methods

### clearScreen()

```java
static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
}
```

- **Purpose**: Clears the console using ANSI escape codes.
- **Note**: May not work in all terminals.

### centerText(String text, int width)

```java
static String centerText(String text, int width) {
    return " ".repeat(Math.max(0, (width - text.length()) / 2)) + text;
}
```

- **Purpose**: Centers text by adding leading spaces.
- **Example**: `centerText("Hello", 11)` → `"   Hello"`.

### printAsciiArt()

```java
static void printAsciiArt() {
    String[] art = {
        // ASCII art lines (incomplete in PDF)
    };
    // Includes centered slogan
    System.out.println(centerText("Track your progress like a beast!", 100));
    for (String line : art) System.out.println(line);
}
```

- **Purpose**: Prints an ASCII-art header and slogan.
- **Note**: Art array is incomplete; add your banner (e.g., “FITNESS LOGGER”) manually.

### displayWindow(String title)

```java
static void displayWindow(String title) {
    clearScreen();
    printAsciiArt();
    System.out.println("\n" + centerText(title, 100) + "\n");
}
```

- **Purpose**: Shows a window with ASCII art and a centered title.

### displayMainMenu()

```java
static void displayMainMenu() {
    try {
        Thread.sleep(2000); // 2-second delay
    } catch (InterruptedException e) {
        System.out.println("Error during delay: " + e.getMessage());
    }
    clearScreen();
    printAsciiArt();
    System.out.println("\n" + centerText("1. Add exercise", 100));
    System.out.println("\n" + centerText("2. View log", 100));
    System.out.println(centerText("3. Show progress", 100));
    System.out.println(centerText("Type 'exit' to quit", 100));
    System.out.println("\n" + centerText("Select an option: ", 100));
}
```

- **Purpose**: Displays the main menu with options 1-3 and “exit”.

## Input and Parsing Utility Methods

### getInput(String prompt, Scanner scanner)

```java
static String getInput(String prompt, Scanner scanner) {
    System.out.print(prompt);
    String input = scanner.nextLine().trim().toLowerCase();
    if (input.equals("back")) return null;
    return input;
}
```

- **Purpose**: Reads user input, returns `null` for “back” or the input string.

### parseDuration(String input)

```java
static Integer parseDuration(String input) {
    input = input.trim().toLowerCase();
    if (input.isEmpty()) return null;
    try {
        int seconds = 0;
        String currentNum = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                currentNum += c;
            } else if (c == 'h' || c == 'm' || c == 's') {
                if (currentNum.isEmpty()) return null;
                int num = Integer.parseInt(currentNum);
                if (c == 'h') seconds += num * 3600;
                else if (c == 'm') seconds += num * 60;
                else if (c == 's') seconds += num;
                currentNum = "";
            } else {
                return null;
            }
        }
        if (!currentNum.isEmpty()) return null;
        return seconds > 0 ? seconds : null;
    } catch (NumberFormatException e) {
        return null;
    }
}
```

- **Purpose**: Parses duration (e.g., “2h30m”) to seconds.
- **Examples**:
  - "2h30m" → 9000.
  - "15m24s" → 924.
  - "48s" → 48.
  - Invalid → `null`.

### parseDistance(String input)

```java
static Integer parseDistance(String input) {
    input = input.trim().toLowerCase();
    try {
        if (input.endsWith("km")) return (int) (Double.parseDouble(input.replace("km", "")) * 1000);
        if (input.endsWith("m")) return Integer.parseInt(input.replace("m", ""));
        return Integer.parseInt(input);
    } catch (Exception e) {
        return null;
    }
}
```

- **Purpose**: Parses distance (e.g., “2km”) to meters.
- **Examples**:
  - "2km" → 2000.
  - "500m" → 500.
  - "3" → 3.
  - Invalid → `null`.

### parseDate(String input)

```java
static LocalDate parseDate(String input) {
    if (input.trim().isEmpty()) return LocalDate.now();
    try {
        return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (Exception e) {
        return null;
    }
}
```

- **Purpose**: Parses “dd/MM/yyyy” or returns today if empty.
- **Examples**:
  - "18/05/2025" → 2025-05-18.
  - "" → today’s date.
  - Invalid → `null`.

## Adding Exercises

### addStrengthExercise(Scanner scanner)

```java
static String addStrengthExercise(Scanner scanner) {
    displayWindow("Add Strength Exercise");
    String name = getInput(centerText("Enter exercise name: ", 100), scanner);
    if (name == null) return null;
    if (name.equals("exit")) return "exit";
    String setsInput = getInput(centerText("Enter number of sets: ", 100), scanner);
    if (setsInput == null) return null;
    if (setsInput.equals("exit")) return "exit";
    int sets;
    try {
        sets = Integer.parseInt(setsInput);
    } catch (NumberFormatException e) {
        System.out.println(centerText("Invalid sets! Returning...", 100));
        return null;
    }
    String repsInput = getInput(centerText("Enter reps per set: ", 100), scanner);
    if (repsInput == null) return null;
    if (repsInput.equals("exit")) return "exit";
    int reps;
    try {
        reps = Integer.parseInt(repsInput);
    } catch (NumberFormatException e) {
        System.out.println(centerText("Invalid reps! Returning...", 100));
        return null;
    }
    String dateInput = getInput(centerText("Enter date (dd/MM/yyyy, empty for today): ", 100), scanner);
    if (dateInput == null) return null;
    if (dateInput.equals("exit")) return "exit";
    LocalDate date = parseDate(dateInput);
    if (date == null) {
        System.out.println(centerText("Invalid date! Returning...", 100));
        return null;
    }
    log.add(new StrengthEntry(name, sets, reps, date));
    saveLog();
    System.out.println(centerText("Exercise added!", 100));
    return null;
}
```

- **Purpose**: Adds a strength exercise, prompting for name, sets, reps, and date.
- **Behavior**:
  - Validates inputs, returns `null` for “back” or invalid inputs, “exit” to quit.
  - Saves to `log` and `log.txt`.

### addCardioExercise(Scanner scanner)

```java
static String addCardioExercise(Scanner scanner) {
    displayWindow("Add Cardio Exercise");
    String name = getInput(centerText("Enter exercise name: ", 100), scanner);
    if (name == null) return null;
    if (name.equals("exit")) return "exit";
    String durationInput = getInput(centerText("Enter duration (e.g., 2h56m45s, 15m24s, 48s): ", 100), scanner);
    if (durationInput == null) return null;
    if (durationInput.equals("exit")) return "exit";
    Integer duration = parseDuration(durationInput);
    if (duration == null) {
        System.out.println(centerText("Invalid duration! Use formats like 15m, 2h, 2h30m, 48s.", 100));
        return null;
    }
    String setsInput = getInput(centerText("Enter number of sets: ", 100), scanner);
    if (setsInput == null) return null;
    if (setsInput.equals("exit")) return "exit";
    int sets;
    try {
        sets = Integer.parseInt(setsInput);
    } catch (NumberFormatException e) {
        System.out.println(centerText("Invalid sets! Returning...", 100));
        return null;
    }
    String dateInput = getInput(centerText("Enter date (dd/MM/yyyy, empty for today): ", 100), scanner);
    if (dateInput == null) return null;
    if (dateInput.equals("exit")) return "exit";
    LocalDate date = parseDate(dateInput);
    if (date == null) {
        System.out.println(centerText("Invalid date! Returning...", 100));
        return null;
    }
    log.add(new CardioEntry(name, duration, sets, date));
    saveLog();
    System.out.println(centerText("Exercise added!", 100));
    return null;
}
```

- **Purpose**: Adds a cardio exercise, prompting for name, duration, sets, and date.

### addEnduranceExercise(Scanner scanner)

```java
static String addEnduranceExercise(Scanner scanner) {
    displayWindow("Add Endurance Exercise");
    String name = getInput(centerText("Enter exercise name (e.g., Running, Swimming): ", 100), scanner);
    if (name == null) return null;
    if (name.equals("exit")) return "exit";
    String distanceInput = getInput(centerText("Enter distance (e.g., 100m, 2km): ", 100), scanner);
    if (distanceInput == null) return null;
    if (distanceInput.equals("exit")) return "exit";
    Integer distance = parseDistance(distanceInput);
    if (distance == null) {
        System.out.println(centerText("Invalid distance! Returning...", 100));
        return null;
    }
    String durationInput = getInput(centerText("Enter duration (e.g., 2h56m45s, 15m24s, 48s): ", 100), scanner);
    if (durationInput == null) return null;
    if (durationInput.equals("exit")) return "exit";
    Integer duration = parseDuration(durationInput);
    if (duration == null) {
        System.out.println(centerText("Invalid duration! Use formats like 15m, 2h, 2h30m, 48s.", 100));
        return null;
    }
    String dateInput = getInput(centerText("Enter date (dd/MM/yyyy, empty for today): ", 100), scanner);
    if (dateInput == null) return null;
    if (dateInput.equals("exit")) return "exit";
    LocalDate date = parseDate(dateInput);
    if (date == null) {
        System.out.println(centerText("Invalid date! Returning...", 100));
        return null;
    }
    log.add(new EnduranceEntry(name, distance, duration, date));
    saveLog();
    System.out.println(centerText("Exercise added!", 100));
    return null;
}
```

- **Purpose**: Adds an endurance exercise, prompting for name, distance, duration, and date.
- **Note**: Uses `EnduranceEntry` (line 126), updated for clarity.

### addExercise(Scanner scanner)

```java
static String addExercise(Scanner scanner) {
    while (true) {
        displayWindow("Add Exercise");
        System.out.println(centerText("1. Strength (e.g., Push-ups)", 100));
        System.out.println(centerText("2. Cardio (e.g., Plank)", 100));
        System.out.println(centerText("3. Endurance (e.g., Running, Swimming)", 100));
        System.out.println(centerText("Type 'back' to return or 'exit' to quit", 100));
        System.out.print("\n" + centerText("Select type: ", 100));
        String choice = getInput("", scanner);
        if (choice == null) return null;
        if (choice.equals("exit")) return "exit";
        switch (choice) {
            case "1": return addStrengthExercise(scanner);
            case "2": return addCardioExercise(scanner);
            case "3": return addEnduranceExercise(scanner);
            default: System.out.println(centerText("Invalid option.", 100));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error during delay: " + e.getMessage());
        }
    }
}
```

- **Purpose**: Menu to choose exercise type, looping until valid input.

## Clearing and Viewing the Log

### clearLog()

```java
static void clearLog() {
    log.clear();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
    } catch (IOException e) {
        System.out.println("Error clearing log: " + e.getMessage());
    }
    System.out.println("Log cleared!");
}
```

- **Purpose**: Clears the `log` list and truncates `log.txt`.

### viewLog(Scanner scanner)

```java
static String viewLog(Scanner scanner) {
    displayWindow("View Log");
    if (log.isEmpty()) {
        System.out.println(centerText("No exercises logged.", 100));
    } else {
        System.out.println(centerText("Exercise Log:", 100));
        for (LogEntry entry : log) {
            System.out.println(centerText(" - " + entry, 100));
        }
    }
    System.out.println("\n" + centerText("Type 'clear' to clear, 'back' to return, 'exit' to quit:", 100));
    while (true) {
        String input = getInput(centerText("Command: ", 100), scanner);
        if (input == null) return null;
        if (input.equals("exit")) return "exit";
        if (input.equals("clear")) {
            clearLog();
            return null;
        }
        System.out.println(centerText("Invalid command.", 100));
    }
}
```

- **Purpose**: Displays logs and allows clearing.

## Showing Progress

```java
static String showProgress(Scanner scanner) {
    displayWindow("Show Progress");
    Map<String, List<StrengthEntry>> strength = new HashMap<>();
    Map<String, List<CardioEntry>> cardio = new HashMap<>();
    Map<String, List<EnduranceEntry>> endurance = new HashMap<>();
    for (LogEntry e : log) {
        if (e instanceof StrengthEntry se)
            strength.computeIfAbsent(se.name.toLowerCase(), k -> new ArrayList<>()).add(se);
        else if (e instanceof CardioEntry ce)
            cardio.computeIfAbsent(ce.name.toLowerCase(), k -> new ArrayList<>()).add(ce);
        else if (e instanceof EnduranceEntry re)
            endurance.computeIfAbsent(re.name.toLowerCase(), k -> new ArrayList<>()).add(re);
    }
    System.out.println(centerText("Progress Summary:", 100));
    boolean hasProgress = false;

    // Strength progress
    for (var entry : strength.entrySet()) {
        List<StrengthEntry> entries = entry.getValue();
        if (entries.size() < 2) continue;
        entries.sort(Comparator.comparing(LogEntry::getDate));
        StrengthEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
        int diff = latest.totalReps() - oldest.totalReps();
        if (diff <= 0) continue;
        hasProgress = true;
        System.out.println(centerText("Strength: " + latest.name, 100));
        System.out.println(centerText(" First: " + oldest.sets + "x" + oldest.reps + " on " + oldest.formattedDate(), 100));
        System.out.println(centerText(" Last: " + latest.sets + "x" + latest.reps + " on " + latest.formattedDate(), 100));
        System.out.println(centerText(" Progress: " + diff + " reps", 100));
        System.out.println();
    }

    // Cardio progress
    for (var entry : cardio.entrySet()) {
        List<CardioEntry> entries = entry.getValue();
        if (entries.size() < 2) continue;
        entries.sort(Comparator.comparing(LogEntry::getDate));
        CardioEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
        int diff = latest.duration * latest.sets - oldest.duration * oldest.sets;
        if (diff <= 0) continue;
        hasProgress = true;
        System.out.println(centerText("Cardio: " + latest.name, 100));
        System.out.println(centerText(" First: " + oldest.sets + " sets of " + oldest.formatDuration() + " on " + oldest.formattedDate(), 100));
        System.out.println(centerText(" Last: " + latest.sets + " sets of " + latest.formatDuration() + " on " + latest.formattedDate(), 100));
        System.out.println(centerText(" Progress: " + (diff / 60) + " min", 100));
        System.out.println();
    }

    // Endurance progress
    for (var entry : endurance.entrySet()) {
        List<EnduranceEntry> entries = entry.getValue();
        if (entries.size() < 2) continue;
        entries.sort(Comparator.comparing(LogEntry::getDate));
        EnduranceEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
        double oldestSpeed = (double) oldest.distance / (oldest.duration / 60.0);
        double latestSpeed = (double) latest.distance / (latest.duration / 60.0);
        double speedDiff = latestSpeed - oldestSpeed;
        if (speedDiff <= 0) continue;
        hasProgress = true;
        System.out.println(centerText("Endurance: " + latest.name, 100));
        System.out.println(centerText(" First: " + oldest.formatDistance() + " in " + oldest.formatDuration() + " on " + oldest.formattedDate(), 100));
        System.out.println(centerText(" Last: " + latest.formatDistance() + " in " + latest.formatDuration() + " on " + latest.formattedDate(), 100));
        if (speedDiff >= 1000) {
            System.out.println(centerText(" Progress: " + String.format("%.2f", speedDiff / 1000) + " km/min", 100));
        } else {
            System.out.println(centerText(" Progress: " + String.format("%.2f", speedDiff) + " m/min", 100));
        }
        System.out.println();
    }

    if (!hasProgress) System.out.println(centerText("No progress yet. Keep training!", 100));
    System.out.println("\n" + centerText("Type 'back' to return, 'exit' to quit:", 100));
    while (true) {
        String input = getInput(centerText("Command: ", 100), scanner);
        if (input == null) return null;
        if (input.equals("exit")) return "exit";
        System.out.println(centerText("Invalid. Type 'back' or 'exit'.", 100));
    }
}
```

- **Purpose**: Compares first and last entries for each exercise, showing improvements (reps, duration, speed).
- **Key Lines**: 581 (method start), 628-637 (endurance speed calculation).

## main() Method and Program Flow

```java
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    loadLog();
    displayMainMenu();
    while (true) {
        String choice = getInput("", scanner);
        if (choice == null) {
            displayMainMenu();
            continue;
        }
        if (choice.equals("exit")) {
            clearScreen();
            System.out.println(centerText("Stay strong, GymRat!", 100));
            scanner.close();
            return;
        }
        String result = null;
        switch (choice) {
            case "1": result = addExercise(scanner); break;
            case "2": result = viewLog(scanner); break;
            case "3": result = showProgress(scanner); break;
            default: System.out.println(centerText("Invalid option.", 100));
        }
        if (result != null && result.equals("exit")) {
            clearScreen();
            System.out.println(centerText("Stay strong, GymRat!", 100));
            scanner.close();
            return;
        }
        displayMainMenu();
    }
}
```

- **Purpose**: Runs the program, handling menu navigation (line 663).
- **Flow**:
  - Loads logs.
  - Shows menu.
  - Loops to process user choices (1, 2, 3, or exit).

## Example Usage (Command-Line)

1. **Starting**:
   ```bash
   $ java FitnessLogger
   [Clears screen, prints ASCII art]
   Track your progress like a beast!
   1. Add exercise
   2. View log
   3. Show progress
   Type 'exit' to quit
   Select an option:
   ```

2. **Adding Strength Exercise**:
   - Choose 1, then 1:
     ```
     Select option: 1
     1. Strength (e.g., Push-ups)
     2. Cardio (e.g., Plank)
     3. Endurance
     Type 'back' to return or 'exit' to quit
     Select type: 1
     Enter exercise name: Push-ups
     Enter number of sets: 3
     Enter reps per set: 10
     Enter date (dd/MM/yyyy, empty for today):
     Exercise added!
     ```

3. **Adding Cardio Exercise**:
   - Choose 1, then 2:
     ```
     Select option: 1
     Select type: 2
     Enter exercise name: Plank
     Enter duration (e.g., 2h, 15m): 2m
     Enter number of sets: 2
     Enter date (dd/MM/yyyy, empty for today): 18/05/2025
     Exercise added!
     ```

4. **Viewing Log**:
   - Choose 2:
     ```
     Select option: 2
     Exercise Log:
     - 18/05/2025 - Strength: Push-ups: 3 sets of 10 reps
     - 18/05/2025 - Cardio: Plank: 2 sets of 2m0s
     Type 'clear' to clear, 'back' to return, 'exit' to quit:
     Command:
     ```

5. **Showing Progress**:
   - Choose 3:
     ```
     Select option: 3
     Progress Summary:
     Strength: Push-ups
     First: 3x10 on 18/05/2025
     Last: 4x12 on 25/05/2025
     Progress: +18 reps
     Type 'back' to return, 'exit' to quit:
     Command:
     ```

## log.txt File Structure

- **Location**: `log.txt` in the program directory.
- **Format**:
  - Strength: `strength;Name;sets;reps;YYYY-MM-DD`
    - Example: `strength;Push-ups;3;10;2025-05-18`
  - Cardio: `cardio;Name;duration;sets;YYYY-MM-DD`
    - Example: `cardio;Plank;120;2;2025-05-18`
  - Endurance: `endurance;Name;distance;duration;YYYY-MM-DD`
    - Example: `endurance;Running;5000;1800;2025-05-18`
- **Example Content**:
  ```
  strength;Push-ups;3;10;2025-05-18
  cardio;Plank;120;2;2025-05-18
  ```

## Edge Cases and Error Handling

- **Missing log.txt**: `loadLog()` does nothing if file doesn’t exist.
- **Invalid Input**:
  - Non-numeric inputs (e.g., sets) trigger `NumberFormatException`, show “Invalid” message.
  - Invalid duration/distance/date return `null`, show error, and return to menu.
- **Empty Input**:
  - Empty date uses today’s date.
  - Other empty inputs cause parsing failure.
- **Special Commands**:
  - “back” returns `null`, navigates up.
  - “exit” quits gracefully.
- **No Progress**: Shows “No progress yet. Keep training!” if no improvements.
- **UI**: Delays and screen clearing enhance UX; logic works without ANSI support.

## For Presentation

- **Key Lines**:
  - Line 126: `EnduranceEntry` class, renamed for clarity.
  - Line 286: `parseDuration` for robust time parsing.
  - Line 581: `showProgress` for performance comparison.
  - Line 663: `main` for program flow.
- **Video**: Use this `README.md` to guide your 5-7 minute video, scrolling to lines 126, 286, 581, 663 in `FitnessLogger.java`.

💪 Track your progress like a beast!