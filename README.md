Fitness Logger

A console-based Java application to track workouts, including strength, cardio, and endurance exercises (e.g., running, swimming). Log your exercises, view your training history, and monitor progress with a sleek ASCII-art interface. Perfect for gym rats and fitness enthusiasts!

Features





Exercise Types:





Strength: Track exercises like push-ups (sets and reps).



Cardio: Log exercises like planks (duration and sets).



Endurance: Monitor running, swimming, etc. (distance and duration).



Progress Tracking:





Strength: Increase in total reps (sets × reps).



Cardio: Increase in total duration (sets × duration, in minutes).



Endurance: Increase in speed (meters/min or km/min).



Input Formats:





Time: 2h56m45s, 15m24s, 15m, 2h, 2h30m, 48s.



Distance: 100m, 2km, or plain numbers (meters).



Date: dd/MM/yyyy (e.g., 18/05/2025), empty for today.



Log Storage: Saves to log.txt in CSV format (e.g., endurance;Swimming;1000;1200;2025-05-18).



Interface:





ASCII-art header with slogan: "🏋️ Track your progress like a beast! 🏋️".



Commands: back to return to menu, exit to quit.



2-second delay after actions, screen clears for a clean UI.



Log Management: View logs, clear logs with clear command.

How to Run





Requirements: Java 8 or higher.



Setup:





Copy FitnessLogger.java to your project.



Compile and run in an IDE (e.g., IntelliJ IDEA) or via terminal:

javac FitnessLogger.java
java FitnessLogger



Usage:





Select options (1-3) from the main menu.



Follow prompts to add exercises, view logs, or check progress.



Use back to return, exit to quit.

Code Details





Classes:





StrengthEntry: Stores strength exercises (name, sets, reps, date).



CardioEntry: Stores cardio exercises (name, duration, sets, date).



RunningEntry: Stores endurance exercises (name, distance, duration, date).



LogEntry: Interface for consistent log handling.



Key Methods:





addExercise: Guides user to add exercises by type.



viewLog: Displays all logged exercises, supports clearing.



showProgress: Compares first and last entries for progress.



parseDuration: Parses time inputs (e.g., 15m → 900 seconds).



parseDistance: Parses distance inputs (e.g., 2km → 2000 meters).



Log Format:





Strength: strength;Push-ups;3;10;2025-05-18.



Cardio: cardio;Plank;60;2;2025-05-18.



Endurance: endurance;Swimming;1000;1200;2025-05-18.



UI Features:





ASCII-art banner for a cool vibe.



Centered text for clean output.



No main menu in viewLog/showProgress for focused interaction.

Example Usage

Adding Exercises

[ASCII-art FITNESS LOGGER]
🏋️ Track your progress like a beast! 🏋️

Add Exercise

1. Strength (e.g., Push-ups)
2. Cardio (e.g., Plank)
3. Endurance (e.g., Running, Swimming)
Type 'back' to return or 'exit' to quit
Select type: 3

Add Endurance Exercise

Enter exercise name (e.g., Running, Swimming): Swimming
Enter distance (e.g., 100m, 2km): 1km
Enter duration (e.g., 2h56m45s, 15m24s, 15m, 2h, 2h30m, 48s): 20m
Enter date (dd/MM/yyyy, empty for today): 18/05/2025
✅ Exercise added!

Viewing Log

View Log

📓 Exercise Log:
 - 18/05/2025 - Strength: Push-ups: 3 sets of 10 reps
 - 18/05/2025 - Cardio: Plank: 2 sets of 1m0s
 - 18/05/2025 - Endurance: Swimming: 1.0km in 20m0s
Type 'clear' to clear, 'back' to return, 'exit' to quit:
Command:

Checking Progress

Show Progress

📈 Progress Summary:
Endurance: Swimming
  First: 2.0km in 15m0s on 17/05/2025
  Last: 1.0km in 20m0s on 18/05/2025
  Progress: +16.67 m/min

Type 'back' to return, 'exit' to quit:
Command:

Notes





Log Compatibility: Old logs with running type won't load. Contact for backward compatibility.



Class Name: Endurance exercises use RunningEntry internally (for simplicity). Can be renamed to EnduranceEntry if needed.



Error Handling: Invalid inputs (e.g., wrong time format) prompt user to retry or return.

Contributing

Feel free to fork, suggest improvements, or add features like new exercise types or stats! Contact me for collabs. 💪



Built with 🏋️ by a gym rat for gym rats!
