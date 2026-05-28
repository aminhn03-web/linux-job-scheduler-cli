# Linux Job Scheduler CLI

A Java-based Linux command-line job scheduler that executes shell commands, manages saved jobs, stores data in SQLite, tracks execution history, and supports recurring multithreaded scheduling.

## Overview

Linux Job Scheduler CLI is a command-line application built in Java and designed to run in a Linux environment. The application allows users to create, view, update, delete, run, and schedule Linux shell commands as saved jobs.

The project demonstrates practical software engineering concepts including command-line interface design, object-oriented programming, SQLite database persistence, process execution, error handling, Bash scripting, and Java multithreading.

## Features

- Add saved jobs with a custom name and Linux command
- List all saved jobs
- Run a Linux command immediately
- Run a saved job by ID
- Update saved job names and commands
- Delete saved jobs
- Store job data in SQLite
- Track job execution history
- Save command status, exit code, and timestamp
- Schedule recurring jobs at fixed intervals
- Run scheduled jobs using Java multithreading
- Use consistent CLI logging for info, success, warning, and error messages
- Compile and run the application through Bash scripts

## Technologies Used

- Java
- Linux / Ubuntu WSL
- Bash scripting
- SQLite
- JDBC
- Git / GitHub
- Java multithreading
- `ScheduledExecutorService`

## Project Structure

```text
linux-job-scheduler-cli/
├── scripts/
│   └── build.sh
├── src/
│   ├── AppLogger.java
│   ├── CommandExecutor.java
│   ├── DatabaseManager.java
│   ├── HistoryRepository.java
│   ├── Job.java
│   ├── JobRepository.java
│   ├── Main.java
│   └── SchedulerService.java
├── run.sh
├── README.md
└── .gitignore
```

## Setup

Install SQLite and the SQLite JDBC driver:

```bash
sudo apt update
sudo apt install sqlite3 libxerial-sqlite-jdbc-java libslf4j-java -y
```

Make sure the scripts are executable:

```bash
chmod +x run.sh
chmod +x scripts/build.sh
```

## How to Build and Run

From the project directory, run:

```bash
./run.sh help
```

The `run.sh` script automatically compiles the Java source files and runs the application.

## Commands

Show available commands:

```bash
./run.sh help
```

Run a Linux command immediately:

```bash
./run.sh run "pwd"
./run.sh run "ls -la"
./run.sh run "date"
```

Add a saved job:

```bash
./run.sh add "Show Date" "date"
./run.sh add "List Files" "ls -la"
```

List saved jobs:

```bash
./run.sh list
```

Run a saved job by ID:

```bash
./run.sh run-job 1
```

Update a saved job:

```bash
./run.sh update 1 "Show Current Directory" "pwd"
```

Delete a saved job:

```bash
./run.sh delete 1
```

View job execution history:

```bash
./run.sh history
```

Schedule a saved job to run repeatedly:

```bash
./run.sh schedule 1 10
```

This runs job ID `1` every 10 seconds. Press `Ctrl + C` to stop the scheduler.

## Example Output

```text
[SUCCESS] Job added: Show Date -> date

Saved Jobs:
1. Show Date -> date
```

```text
Running job: 1. Show Date -> date
[INFO] Command output:
Tue May 28 14:30:15 EDT 2026
[INFO] Command errors:
[SUCCESS] Command completed successfully. Exit Code: 0
[SUCCESS] Execution saved to history.
```

```text
[INFO] Execution History:
1 | Show Date | date | SUCCESS | Exit Code: 0 | 2026-05-28 14:30:15
```

## Database

The application uses SQLite for persistent storage.

The database file is created locally as:

```text
scheduler.db
```

The database contains two main tables:

```text
jobs
history
```

The `jobs` table stores saved job names and commands.

The `history` table stores job execution records, including status, exit code, and execution timestamp.

The database file is excluded from GitHub using `.gitignore` because it is local runtime data.

## Multithreaded Scheduling

Recurring jobs are scheduled using Java’s `ScheduledExecutorService`.

This allows scheduled jobs to run on a background scheduler thread at fixed intervals instead of relying on a manual infinite loop. This design is cleaner, more scalable, and better reflects real software engineering practices.

## Future Improvements

- Support multiple scheduled jobs running at the same time
- Add more detailed filtering for execution history
- Add job enable/disable status
- Add configuration options for database location
- Add automated tests