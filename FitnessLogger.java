import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FitnessLogger {
    interface LogEntry {
        String toCSV();
        String toString();
        LocalDate getDate();
    }

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

        int totalReps() { return sets * reps; }

        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        @Override
        public String toString() {
            return formattedDate() + " - Strength: " + name + ": " + sets + " sets of " + reps + " reps";
        }

        @Override
        public String toCSV() {
            return "strength;" + name + ";" + sets + ";" + reps + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        static StrengthEntry fromCSV(String[] parts) {
            return new StrengthEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

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

        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String formatDuration() {
            int h = duration / 3600, m = (duration % 3600) / 60, s = duration % 60;
            if (h > 0) return String.format("%dh%dm%ds", h, m, s);
            if (m > 0) return String.format("%dm%ds", m, s);
            return s + "s";
        }

        @Override
        public String toString() {
            return formattedDate() + " - Cardio: " + name + ": " + sets + " sets of " + formatDuration();
        }

        @Override
        public String toCSV() {
            return "cardio;" + name + ";" + duration + ";" + sets + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        static CardioEntry fromCSV(String[] parts) {
            return new CardioEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

    static class RunningEntry implements LogEntry {
        String name;
        int distance, duration;
        LocalDate date;

        RunningEntry(String name, int distance, int duration, LocalDate date) {
            this.name = name;
            this.distance = distance;
            this.duration = duration;
            this.date = date;
        }

        String formattedDate() {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String formatDistance() {
            return distance >= 1000 ? (distance / 1000.0) + "km" : distance + "m";
        }

        String formatDuration() {
            int h = duration / 3600, m = (duration % 3600) / 60, s = duration % 60;
            if (h > 0) return String.format("%dh%dm%ds", h, m, s);
            if (m > 0) return String.format("%dm%ds", m, s);
            return s + "s";
        }

        @Override
        public String toString() {
            return formattedDate() + " - Endurance: " + name + ": " + formatDistance() + " in " + formatDuration();
        }

        @Override
        public String toCSV() {
            return "endurance;" + name + ";" + distance + ";" + duration + ";" + date;
        }

        @Override
        public LocalDate getDate() { return date; }

        static RunningEntry fromCSV(String[] parts) {
            return new RunningEntry(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
        }
    }

    static List<LogEntry> log = new ArrayList<>();
    static final String LOG_FILE = "log.txt";

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
                        case "endurance": log.add(RunningEntry.fromCSV(parts)); break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading log: " + e.getMessage());
        }
    }

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

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static String centerText(String text, int width) {
        return " ".repeat(Math.max(0, (width - text.length()) / 2)) + text;
    }

    static void printAsciiArt() {
        String[] art = {
                "███████╗██╗████████╗███╗░░██╗███████╗░██████╗░██████╗  ██╗░░░░░░█████╗░░██████╗░░██████╗░███████╗██████╗░",
                "██╔════╝██║╚══██╔══╝████╗░██║██╔════╝██╔════╝██╔════╝  ██║░░░░░██╔══██╗██╔════╝░██╔════╝░██╔════╝██╚══██╗",
                "█████╗░░██║░░░██║░░░██╔██╗██║█████╗░░╚█████╗░╚█████╗░  ██║░░░░░██║░░██║██║░░██╗░██║░░██╗░█████╗░░██████╔╝",
                "██╔══╝░░██║░░░██║░░░██║╚████║██╔══╝░░░╚═══██╗░╚═══██╗  ██║░░░░░██║░░██║██║░░╚██╗██║░░╚██╗██╔══╝░░██╔══██╗",
                "██║░░░░░██║░░░██║░░░██║░╚███║███████╗██████╔╝██████╔╝  ███████╗╚█████╔╝╚██████╔╝╚██████╔╝███████╗██║░░██║",
                "╚═╝░░░░░╚═╝░░░╚═╝░░░╚═╝░░╚══╝╚══════╝╚═════╝░╚═════╝░  ╚══════╝░╚════╝░░╚═════╝░░╚═════╝░╚══════╝╚═╝░░╚═╝",
                "",
                centerText("🏋️ Track your progress like a beast! 🏋️", 100)
        };
        for (String line : art) System.out.println(line);
    }

    static void displayWindow(String title) {
        clearScreen();
        printAsciiArt();
        System.out.println("\n" + centerText(title, 100) + "\n");
    }

    static void displayMainMenu() {
        try {
            Thread.sleep(2000);
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

    static String getInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("back")) return null;
        return input;
    }

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

    static LocalDate parseDate(String input) {
        if (input.trim().isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }

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

        log.add(new RunningEntry(name, distance, duration, date));
        saveLog();
        System.out.println(centerText("✅ Exercise added!", 100));
        return null;
    }

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

    static void clearLog() {
        log.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
        } catch (IOException e) {
            System.out.println("Error clearing log: " + e.getMessage());
        }
        System.out.println("🗑️ Log cleared!");
    }

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

    static String showProgress(Scanner scanner) {
        displayWindow("Show Progress");
        Map<String, List<StrengthEntry>> strength = new HashMap<>();
        Map<String, List<CardioEntry>> cardio = new HashMap<>();
        Map<String, List<RunningEntry>> running = new HashMap<>();

        for (LogEntry e : log) {
            if (e instanceof StrengthEntry se) strength.computeIfAbsent(se.name.toLowerCase(), k -> new ArrayList<>()).add(se);
            else if (e instanceof CardioEntry ce) cardio.computeIfAbsent(ce.name.toLowerCase(), k -> new ArrayList<>()).add(ce);
            else if (e instanceof RunningEntry re) running.computeIfAbsent(re.name.toLowerCase(), k -> new ArrayList<>()).add(re);
        }

        System.out.println(centerText("📈 Progress Summary:", 100));
        boolean hasProgress = false;

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

        for (var entry : running.entrySet()) {
            List<RunningEntry> entries = entry.getValue();
            if (entries.size() < 2) continue;
            entries.sort(Comparator.comparing(LogEntry::getDate));
            RunningEntry oldest = entries.get(0), latest = entries.get(entries.size() - 1);
            double oldestSpeed = (double) oldest.distance / (oldest.duration / 60.0); // м/мин
            double latestSpeed = (double) latest.distance / (latest.duration / 60.0); // м/мин
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
