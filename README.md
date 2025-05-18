# Fitness Logger 🏋️‍♂️

Console Java app to track workouts with flair! Log **strength**, **cardio**, **endurance** exercises, view history, and flex progress with ASCII-art UI. For gym rats! 💪

---

## 🔥 Features

- **Exercises**:
  - 🏋️ *Strength*: Push-ups, squats (sets, reps).
  - 🏃 *Cardio*: Planks, burpees (duration, sets).
  - 🏊 *Endurance*: Running, swimming (distance, duration).
- **Progress**:
  - Strength: Total reps gained.
  - Cardio: Extra minutes.
  - Endurance: Speed (m/min or km/min).
- **Inputs**:
  - ⏱️ Time: `2h56m45s`, `15m`, `48s`.
  - 📏 Distance: `100m`, `2km`, or meters.
  - 📅 Date: `dd/MM/yyyy` (e.g., `18/05/2025`) or empty.
- **Log**:
  - CSV in `log.txt` (e.g., `endurance;Swimming;1000;1200;2025-05-18`).
- **UI**:
  - 🎨 ASCII-art: "🏋️ Track your progress like a beast! 🏋️".
  - 📲 Commands: `back`, `exit`.
  - ⏳ 2s delay, screen clears.
- **Log Management**:
  - 📓 View/clear logs (`clear`).

---

## 🚀 Setup

1. **Requirements**: Java 8+.
2. **Run**:
   ```bash
   javac FitnessLogger.java
   java FitnessLogger
