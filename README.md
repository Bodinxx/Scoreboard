# LaxStat: Professional Box Lacrosse Scorekeeper

LaxStat is a high-performance scoring and statistics application designed specifically for the fast-paced environment of Box Lacrosse. It provides officials, coaches, and scouts with a streamlined interface for real-time data entry and historical game analysis.

## 🚀 Core Features

### 1. High-Visibility Scoring Interface
* **Dual-Zone Display:** Large, high-contrast halves for Home and Away teams.
* **One-Tap Tracking:** Effortless +1 increments for Goals and Shots on Goal.
* **Precision Corrections:** Swipe-down gestures for decrements or optional -1 buttons.
* **Live Analytics:** Real-time Save % calculation based on current shots and goals.

### 2. Deep Statistical Tracking (Game Memory)
* **Roster Integration:** Track stats by player name and jersey number.
* **Goal Attribution:** Log primary scorers and up to two assists per goal.
* **Penalty Ledger:** Record infractions and players without the complexity of clock management.
* **Scouting Mode:** Ability to track full stats for both teams or keep it simple for the opposition.

### 3. Customization
* **Configurable UI:** Toggle specific modules (Shots on Goal, Save %, -1 Buttons) to simplify the interface.
* **Infraction Architect:** Fully editable penalty lists and "Rule Profiles" for different league standards.
* **Team Branding:** Custom team names and color configurations.

## 🛠 Tech Stack (Suggested)
* **Frontend:** React Native or Flutter (for cross-platform iOS/Android performance).
* **Local Database:** SQLite or Realm (for persistent offline "Game Memory").
* **State Management:** Redux or Provider (to handle real-time stat updates).

# Development Specification
## 1. UI Architecture & Logic

* The app must maintain a split-pane layout responsive to orientation changes. Each pane handles state for a specific team object.
    * **Increment Logic**: onTap -> count++
    * **Decrement Logic**: onSwipeDown OR onBtnClick -> count--(floor: 0)
    * **Calculation**: Save % = ((Shots - Goals) / Shots) × 100
 
## 2. Data Persistence (Game Memory)
Every event (Goal, Assist, Penalty) must be timestamped and linked to a playerID and gameID.

### Database Schema (Primary Entities):
| Entity | Fields |
|--------|--------|
| Teams | ID, Name, ColorCode, Roster[] |
| Players | ID, JerseyNumber, Name, CareerStats |
| GameEvents | ID, GameID, PlayerID, Type (Goal/Assist/Penalty), SubType (Infraction Type) |

# 3. Feature Modules & Configuration
## Penalty Architect
CRUD functionality for penalty types. Default list includes: Slashing, Tripping, Cross-checking, Holding, Interference.

## Modular UI Toggles
Global state flags to hide/show shotsSection and savePercentageSection. When hidden, the goalCount should expand to fill the available space.
