import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * FitnessLogger: A console-based workout tracker for strength, cardio, and endurance exercises.
 * Features: Add exercises, view logs, track progress, with ASCII-art UI and log file storage.
 * Author: GymRat
 */
public class FitnessLogger {

    /**
     * Interface for log entries to ensure consistent CSV, string representation, and date access.
     */
    interface LogEntry {
        String toCSV();
        String toString();
        LocalDate getDate();
    }

    /**
     * Represents a strength exercise (e.g., Push-ups) with sets, reps, and date.
     */
    static class StrengthEntry implements LogEntry {
        String name;
        int sets, reps;
        LocalDate date;

        StrengthEntry(String name, int sets, int reps, LocalDate date) {
            this.name = name;
            this.sets = sets;
            this.reps = reps;
            this.date = date;
        }

        /** Calculates total reps (sets × reps). */
        int totalReps() { return sets * reps; }

        /** Formats date as dd/MM/yyyy. */
        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        /** Returns string representation for display (e.g., "18/05/2025 - Strength: Push-ups: 3 sets of 10 reps"). */
        @Override
        public String toString() {
            return formattedDate() + " - Strength: " + name + ": " + sets + " sets of " + reps + " reps";
        }

        /** Returns CSV representation for log file (e.g., "strength;Push-ups;3;10;2025-05-18"). */
        @Override
        public String toCSV() {
            return "strength;" + name + ";" + sets + ";" + reps + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        /** Creates StrengthEntry from CSV parts. */
        static StrengthEntry fromCSV(String[] parts) {
            return new StrengthEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

    /**
     * Represents a cardio exercise (e.g., Plank) with duration, sets, and date.
     */
    static class CardioEntry implements LogEntry {
        String name;
        int duration, sets;
        LocalDate date;

        CardioEntry(String name, int duration, int sets, LocalDate date) {
            this.name = name;
            this.duration = duration;
            this.sets = sets;
            this.date = date;
        }

        /** Formats date as dd/MM/yyyy. */
        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        /** Formats duration as hours, minutes, seconds (e.g., "1m0s"). */
        String formatDuration() {
            int h = duration / 3600, m = (duration % 3600) / 60, s = duration % 60;
            if (h > 0) return String.format("%dh%dm%ds", h, m, s);
            if (m > 0) return String.format("%dm%ds", m, s);
            return s + "s";
        }

        /** Returns string representation (e.g., "18/05/2025 - Cardio: Plank: 2 sets of 1m0s"). */
        @Override
        public String toString() {
            return formattedDate() + " - Cardio: " + name + ": " + sets + " sets of " + formatDuration();
        }

        /** Returns CSV representation (e.g., "cardio;Plank;60;2;2025-05-18"). */
        @Override
        public String toCSV() {
            return "cardio;" + name + ";" + duration + ";" + sets + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        /** Creates CardioEntry from CSV parts. */
        static CardioEntry fromCSV(String[] parts) {
            return new CardioEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

    /**
     * Represents an endurance exercise (e.g., Swimming, Running) with distance, duration, and date.
     */
    static class EnduranceEntry implements LogEntry {
        String name;
        int distance, duration;
        LocalDate date;

        EnduranceEntry(String name, int distance, int duration, LocalDate date) {
            this.name = name;
            this.distance = distance;
            this.duration = duration;
            this.date = date;
        }

        /** Formats date as dd/MM/yyyy. */
        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        /** Formats distance as km or m (e.g., "1.0km" or "100m"). */
        String formatDistance() {
            return distance >= 1000 ? (distance / 1000.0) + "km" : distance + "m";
        }

        /** Formats duration as hours, minutes, seconds (e.g., "20m0s"). */
        String formatDuration() {
            int h = duration / 3600, m = (duration % 3600) / 60, s = duration % 60;
            if (h > 0) return String.format("%dh%dm%ds", h, m, s);
            if (m > 0) return String.format("%dm%ds", m, s);
            return s + "s";
        }

        /** Returns string representation (e.g., "18/05/2025 - Endurance: Swimming: 1.0km in 20m0s"). */
        @Override
        public String toString() {
            return formattedDate() + " - Endurance: " + name + ": " + formatDistance() + " in " + formatDuration();
        }

        /** Returns CSV representation (e.g., "endurance;Swimming;1000;1200;2025-05-18"). */
        @Override
        public String toCSV() {
            return "endurance;" + name + ";" + distance + ";" + duration + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        /** Creates EnduranceEntry from CSV parts. */
        static EnduranceEntry fromCSV(String[] parts) {
            return new EnduranceEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

    // List to store all exercise entries
    static List<LogEntry> log = new ArrayList<>();
    // File to store logs
    static final String LOG_FILE = "log.txt";

    /**
     * Loads exercise entries from log.txt into the log list.
     */
    static void loadLog() {
        File file = new File(LOG_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(";");
                    // Parse based on exercise type
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

    /**
     * Saves all log entries to log.txt.
     */
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

    /**
     * Clears the console screen for a clean UI.
     */
    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Centers text within a given width for neat display.
     * @param text Text to center
     * @param width Desired width
     * @return Centered text with padding
     */
    static String centerText(String text, int width) {
        return " ".repeat(Math.max(0, (width - text.length()) / 2)) + text;
    }

    /**
     * Prints ASCII-art header with slogan.
     */
    static void printAsciiArt() {
        String[] art = {
                "███████╗██╗████████╗███╗░░██╗███████╗░██████╗░██████╗  ██╗░░░░░░█████╗░░██████╗░░██████╗░███████╗██████╗░",
                "██╔════╝██║╚══██╔══╝████╗░██║██╔════╝██╔════╝██╔════╝  ██║░░░░░██╔══██╗██╔════╝░██╔════╝░██╔════╝██╔══██╗",
                "█████╗░░██║░░░██║░░░██╔██╗██║█████╗░░╚█████╗░╚█████╗░  ██║░░░░░██║░░██║██║░░██╗░██║░░██╗░█████╗░░██████╔╝",
                "██╔══╝░░██║░░░██║░░░██║╚████║██╔══╝░░░╚═══██╗░╚═══██╗  ██║░░░░░██║░░██║██║░░╚██╗██║░░╚██╗██╔══╝░░██╔══██╗",
                "██║░░░░░██║░░░██║░░░██║░╚███║███████╗██████╔╝██████╔╝  ███████╗╚█████╔╝╚██████╔╝╚██████╔╝███████╗██║░░██║",
                "╚═╝░░░░░╚═╝░░░╚═╝░░░╚═╝░░╚══╝╚══════╝╚═════╝░╚═════╝░  ╚══════╝░╚════╝░░╚═════╝░░╚═════╝░╚══════╝╚═╝░░╚═╝",
                "",
                centerText("🏋️ Track your progress like a beast! 🏋️", 100)
        };
        for (String line : art) System.out.println(line);
    }

    /**
     * Displays a window with ASCII-art and title.
     * @param title Window title
     */
    static void displayWindow(String title) {
        clearScreen();
        printAsciiArt();
        System.out.println("\n" + centerText(title, 100) + "\n");
    }

    /**
     * Displays the main menu with options.
     */
    static void displayMainMenu() {
        try {
            Thread.sleep(2000); // 2-second delay for smooth UX
        } catch (InterruptedException e) {
            System.out.println("Error during delay: " + e.getMessage());
        }
        clearScreen();
        printAsciiArt();
        System.out.println("\n" + centerText("1. Add exercise", 100));
        System.out.println(centerText("2. View log", 100));
        System.out.println(centerText("3. Show progress", 100));
        System.out.println(centerText("Type 'exit' to quit", 100));
        System.out.print("\n" + centerText("Select an option: ", 100));
    }

    /**
     * Gets user input, handles 'back' command.
     * @param prompt Prompt to display
     * @param scanner Scanner for input
     * @return Input string, or null if 'back'
     */
    static String getInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("back")) return null;
        return input;
    }

    /**
     * Parses duration input (e.g., "2h56m45s") into seconds.
     * @param input Duration string
     * @return Seconds, or null if invalid
     */
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

    /**
     * Parses distance input (e.g., "2km", "100m") into meters.
     * @param input Distance string
     * @return Meters, or null if invalid
     */
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

    /**
     * Parses date input (e.g., "18/05/2025") or returns today if empty.
     * @param input Date string
     * @return LocalDate, or null if invalid
     */
    static LocalDate parseDate(String input) {
        if (input.trim().isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds a strength exercise (e.g., Push-ups).
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return, or null on success
     */
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
        System.out.println(centerText("✅ Exercise added!", 100));
        return null;
    }

    /**
     * Adds a cardio exercise (e.g., Plank).
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return, or null on success
     */
    static String addCardioExercise(Scanner scanner) {
        displayWindow("Add Cardio Exercise");
        String name = getInput(centerText("Enter exercise name: ", 100), scanner);
        if (name == null) return null;
        if (name.equals("exit")) return "exit";

        String durationInput = getInput(centerText("Enter duration (e.g., 2h56m45s, 15m24s, 15m, 2h, 2h30m, 48s): ", 100), scanner);
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
        System.out.println(centerText("✅ Exercise added!", 100));
        return null;
    }

    /**
     * Adds an endurance exercise (e.g., Running, Swimming).
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return, or null on success
     */
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

        String durationInput = getInput(centerText("Enter duration (e.g., 2h56m45s, 15m24s, 15m, 2h, 2h30m, 48s): ", 100), scanner);
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
        System.out.println(centerText("✅ Exercise added!", 100));
        return null;
    }

    /**
     * Guides user to add an exercise by selecting type.
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return
     */
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
                Thread.sleep(1000); // 1-second delay for feedback
            } catch (InterruptedException e) {
                System.out.println("Error during delay: " + e.getMessage());
            }
        }
    }

    /**
     * Clears the log file and in-memory log.
     */
    static void clearLog() {
        log.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
        } catch (IOException e) {
            System.out.println("Error clearing log: " + e.getMessage());
        }
        System.out.println("🗑️ Log cleared!");
    }

    /**
     * Displays all logged exercises, allows clearing.
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return
     */
    static String viewLog(Scanner scanner) {
        displayWindow("View Log");
        if (log.isEmpty()) {
            System.out.println(centerText("No exercises logged.", 100));
        } else {
            System.out.println(centerText("📓 Exercise Log:", 100));
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

    /**
     * Shows progress by comparing first and last entries for each exercise.
     * @param scanner Scanner for input
     * @return "exit" to quit, null to return
     */
    static String showProgress(Scanner scanner) {
        displayWindow("Show Progress");
        // Group entries by exercise name (case-insensitive)
        Map<String, List<StrengthEntry>> strength = new HashMap<>();
        Map<String, List<CardioEntry>> cardio = new HashMap<>();
        Map<String, List<EnduranceEntry>> endurance = new HashMap<>();

        for (LogEntry e : log) {
            if (e instanceof StrengthEntry se) strength.computeIfAbsent(se.name.toLowerCase(), k -> new ArrayList<>()).add(se);
            else if (e instanceof CardioEntry ce) cardio.computeIfAbsent(ce.name.toLowerCase(), k -> new ArrayList<>()).add(ce);
            else if (e instanceof EnduranceEntry re) endurance.computeIfAbsent(re.name.toLowerCase(), k -> new ArrayList<>()).add(re);
        }

        System.out.println(centerText("📈 Progress Summary:", 100));
        boolean hasProgress = false;

        // Strength progress: compare total reps
        for (var entry : strength.entrySet()) {
            List<StrengthEntry> entries = entry.getValue();
            if (entries.size() < 2) continue;
            entries.sort(Comparator.comparing(LogEntry::getDate));
            StrengthEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
            int diff = latest.totalReps() - oldest.totalReps();
            if (diff <= 0) continue;
            hasProgress = true;
            System.out.println(centerText("Strength: " + latest.name, 100));
            System.out.println(centerText("  First: " + oldest.sets + "x" + oldest.reps + " on " + oldest.formattedDate(), 100));
            System.out.println(centerText("  Last: " + latest.sets + "x" + latest.reps + " on " + latest.formattedDate(), 100));
            System.out.println(centerText("  Progress: +" + diff + " reps", 100));
            System.out.println();
        }

        // Cardio progress: compare total duration
        for (var entry : cardio.entrySet()) {
            List<CardioEntry> entries = entry.getValue();
            if (entries.size() < 2) continue;
            entries.sort(Comparator.comparing(LogEntry::getDate));
            CardioEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
            int diff = latest.duration * latest.sets - oldest.duration * oldest.sets;
            if (diff <= 0) continue;
            hasProgress = true;
            System.out.println(centerText("Cardio: " + latest.name, 100));
            System.out.println(centerText("  First: " + oldest.sets + " sets of " + oldest.formatDuration() + " on " + oldest.formattedDate(), 100));
            System.out.println(centerText("  Last: " + latest.sets + " sets of " + latest.formatDuration() + " on " + latest.formattedDate(), 100));
            System.out.println(centerText("  Progress: +" + (diff / 60) + " min", 100));
            System.out.println();
        }

        // Endurance progress: compare speed (m/min)
        for (var entry : endurance.entrySet()) {
            List<EnduranceEntry> entries = entry.getValue();
            if (entries.size() < 2) continue;
            entries.sort(Comparator.comparing(LogEntry::getDate));
            EnduranceEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
            double oldestSpeed = (double) oldest.distance / (oldest.duration / 60.0); // m/min
            double latestSpeed = (double) latest.distance / (latest.duration / 60.0); // m/min
            double speedDiff = latestSpeed - oldestSpeed;
            if (speedDiff <= 0) continue;
            hasProgress = true;
            System.out.println(centerText("Endurance: " + latest.name, 100));
            System.out.println(centerText("  First: " + oldest.formatDistance() + " in " + oldest.formatDuration() + " on " + oldest.formattedDate(), 100));
            System.out.println(centerText("  Last: " + latest.formatDistance() + " in " + latest.formatDuration() + " on " + latest.formattedDate(), 100));
            if (speedDiff >= 1000) {
                System.out.println(centerText("  Progress: +" + String.format("%.2f", speedDiff / 1000) + " km/min", 100));
            } else {
                System.out.println(centerText("  Progress: +" + String.format("%.2f", speedDiff) + " m/min", 100));
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

    /**
     * Main method to run the FitnessLogger application.
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadLog(); // Load existing logs
        displayMainMenu(); // Show main menu

        while (true) {
            String choice = getInput("", scanner);
            if (choice == null) {
                displayMainMenu();
                continue;
            }
            if (choice.equals("exit")) {
                clearScreen();
                System.out.println(centerText("💪 Stay strong, GymRat!", 100));
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
                System.out.println(centerText("💪 Stay strong, GymRat!", 100));
                scanner.close();
                return;
            }
            displayMainMenu();
        }
    }
}
