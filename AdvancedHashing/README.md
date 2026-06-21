# Advanced Hashing Evaluation: Robin Hood vs. Linear Probing
### *Academic Project | Data Structures & Algorithms (DSA) Curriculum*

An open-addressing hash table optimization framework featuring scratch implementations of Linear Probing and Robin Hood Hashing with localized backward-shift deletion.

## The "Why"
Standard open-addressing hash tables (like Linear Probing) suffer severely from two critical flaws at high load factors: **primary clustering** (where long chains of items form, degrading lookup times to O(N)) and **tombstone pollution** (where deleted slots permanently slow down future lookups because searches can't prematurely terminate). 

I built this project to research and empirically measure how advanced hashing techniques combat these inefficiencies. By implementing **Robin Hood Hashing**, the table reduces the variance of probe sequence lengths (PSLs) by letting "poor" elements steal slots from "rich" ones. To eliminate tombstones completely, I implemented a complex **backward-shift deletion** routine, ensuring the data structure maintains predictable, production-grade performance even under heavy loads.

## Key Features
- **Zero-Library Custom Implementation:** Built completely from scratch without utilizing `java.util.HashMap` or standard collections.
- **Robin Hood Ingestion Engine:** Implements the "take from the rich, give to the poor" property to minimize maximum probe distances.
- **Tombstone-Free Deletion:** Utilizes localized backward-shift deletion to shift elements back to their ideal slots upon removal, eliminating deletion overhead.
- **Automated Empirical Benchmark Suite:** Simulates real-world conditions over 30 statistical trials across multiple configuration matrices.

## Tech Stack
- **Core Language:** Java (Object-Oriented Design with custom `HashTable` interfaces)
- **Development Environment:** Visual Studio Code (VS Code)
- **Data & Benchmarking:** CSV-driven metrics logging (`experiment_results.csv`)

### Prerequisites
Ensure you have the Java Development Kit (JDK) installed on your system.