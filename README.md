# Library Management System

A Java command-line program that manages a college library's items, members and loans.

**GitHub repository:** https://github.com/Yashmangal123/LibraryManagementSystem

| **Name** | Yash Mangal |
| **Roll number** | BC2025118 |
| **Language** | Java |

## Features

- Three item types: books, magazines and reference books (reference books cannot be borrowed)
- Three member types with their own borrowing limits and loan periods
- Borrowing, returning and late fines
- Simulated time: the library starts on day 0 and only `ADVANCE` moves it forward
- Reports: search, member loans, overdue loans and inventory
- Reservations with a waiting queue (bonus feature)
- Custom exceptions for every failure, caught in one place

## How to run

Requires a JDK (tested with Java 21).

```sh
javac *.java
java Main
```

Type one command per line. End the input with Ctrl+D (Linux/macOS) or Ctrl+Z then Enter (Windows).

To run a script of commands:

```sh
java Main < tests/sample.txt
```

## Example

| Input | Output |
|---|---|
| `ADD_BOOK\|B1\|Introduction to Algorithms\|Cormen` | `OK` |
| `ADD_MEMBER\|S1\|Ananya Rao\|STUDENT` | `OK` |
| `BORROW\|S1\|B1` | `OK due on day 14` |
| `ADVANCE\|20` | `OK today is day 20` |
| `RETURN\|B1` | `OK fine 12` |

## Commands

| Command | Description |
|---|---|
| `ADD_BOOK\|id\|title\|author` | Add a book |
| `ADD_MAGAZINE\|id\|title\|issue` | Add a magazine |
| `ADD_REFERENCE\|id\|title\|author` | Add a reference book |
| `ADD_MEMBER\|id\|name\|type` | Add a member (`STUDENT`, `STAFF` or `FACULTY`) |
| `BORROW\|memberId\|itemId` | Lend an item |
| `RETURN\|itemId` | Return an item and show the fine |
| `RESERVE\|memberId\|itemId` | Join the queue for an item that is on loan |
| `ADVANCE\|days` | Move time forward |
| `SEARCH\|text` | Find items by title, ignoring case |
| `LOANS\|memberId` | List a member's loans |
| `OVERDUE` | List overdue loans |
| `INVENTORY` | List all items and their status |

## Rules

| Member | Maximum items | Loan period |
|---|---|---|
| Student | 3 | 14 days |
| Staff | 4 | 21 days |
| Faculty | 5 | 30 days |

| Item | Borrowable | Fine per day late |
|---|---|---|
| Book | Yes | ₹2 |
| Magazine | Yes | ₹1 |
| Reference book | No | None |

When a reserved item is returned, it goes to the first member in the queue who is below their limit. Skipped members stay in the queue.

## Design

![Class diagram](BC2025118_UML.pdf)

| Class | Responsibility |
|---|---|
| `Main` | Starts the program |
| `CommandProcessor` | Reads commands, prints results, catches exceptions |
| `Library` | Holds items, members, loans, reservations and the current day |
| `LibraryItem` | Abstract parent of `Book`, `Magazine` and `ReferenceBook` |
| `Borrowable` | Interface for items that can be lent (`Book`, `Magazine`) |
| `Member` | Abstract parent of `Student`, `Staff` and `Faculty` |
| `Loan` | Due day, days late and fine for one borrowed item |
| `LibraryException` | Parent of all custom exceptions |

- Each member subclass supplies its own limit and loan period, so no rule checks the member type by name.
- Each borrowable item supplies its own fine rate.
- Active loans are stored by item ID, so an item has at most one loan at a time.

## Project structure

```
.
├── *.java          source files
├── tests/          input scripts and expected outputs
├── docs/           class diagrams and design changes
├── run_tests.sh    test runner (Linux/macOS)
├── run_tests.bat   test runner (Windows)
└── README.md
```

## Testing

```sh
sh run_tests.sh      # Linux/macOS
run_tests.bat        # Windows
```

The tests cover the sample session, error messages, borrowing limits, report sorting and reservations.

## Assumptions

- If a command breaks several rules, checks run in this order: member, item, borrowable, on loan, limit.
- IDs and titles sort as plain strings (`B10` comes before `B2`).
- Search results with the same title are ordered by item ID.
- A member who borrows an item they were queued for leaves that queue.

