# Linux Job Scheduler CLI

A Java-based Linux command-line job scheduler that executes shell commands, supports task automation, and demonstrates Bash scripting, CLI design, and Linux-based development.

## Overview

Linux Job Scheduler CLI is a command-line application built in Java and designed to run in a Linux environment. The project supports running Linux shell commands directly from a Java program and capturing the command output, errors, and exit codes.

The goal of this project is to build a practical software engineering tool that supports saved jobs, recurring task execution, SQLite database persistence, and job history tracking.

## Features

- Run Linux shell commands from a Java command-line interface
- Capture standard output from executed commands
- Capture error output from failed commands
- Display process exit codes
- Compile and run the project using Bash scripts
- Demonstrate Linux-based development through Ubuntu WSL

## Technologies Used

- Java
- Linux / Ubuntu WSL
- Bash scripting
- Git / GitHub
- SQLite

## Project Structure

```text
linux-job-scheduler-cli/
├── lib/
├── out/
├── scripts/
│   └── build.sh
├── src/
│   ├── Main.java
│   ├── CommandExecutor.java
│   ├── Job.java
│   ├── JobRepository.java
│   └── SchedulerService.java
├── run.sh
├── README.md
└── .gitignore
```

## How to Build and Run

From the project directory, run:

```bash
./run.sh help
```

This compiles the Java source files and displays the available commands.

## Example Commands

Run the help command:

```bash
./run.sh help
```

Run the Linux `pwd` command:

```bash
./run.sh run "pwd"
```

Run the Linux `ls -la` command:

```bash
./run.sh run "ls -la"
```

Run the Linux `date` command:

```bash
./run.sh run "date"
```

## Example Output

```text
----- Command Output -----
/home/username/linux-job-scheduler-cli
----- Command Errors -----
Exit Code: 0
```

## Roadmap

- Add job creation using an `add` command
- Add job listing using a `list` command
- Store job metadata in SQLite
- Track job execution history
- Add recurring scheduling logic
- Support multithreaded job execution
- Add detailed logging and reports

## Purpose

This project demonstrates practical software engineering skills in a Linux environment, including Java development, command-line interface design, Bash scripting, process execution, error handling, database persistence, and task automation.
