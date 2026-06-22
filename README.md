# Algorithm Visualizer

Algorithm Visualizer is a JavaFX desktop application for stepping through common algorithms and data structures visually. I built this project to help my friends and classmates better understand the algorithms and structures we were learning in class by seeing each comparison, swap, insertion, deletion, probe, and structural change happen step by step.

## Features

### Array Sorting Visualizations

The app supports several sorting algorithms:

- Insertion Sort
- Merge Sort
- Bubble Sort
- Selection Sort
- Quick Sort
- Heap Sort
- Radix Sort

Array algorithms are shown as bars. The visualizer highlights comparisons, swaps, writes, and active ranges depending on the algorithm.

### Tree Visualizations

The app supports binary search tree and AVL tree operations:

- Create BST
- Insert into BST
- AVL Insert
- Search BST
- Delete from BST

Tree operations are rendered as node-link diagrams. Compared or modified nodes are highlighted so users can follow the path taken through the tree.

### Heap Visualizations

The app supports both max heap and min heap operations:

- Max Heap Insert
- Max Heap Extract
- Min Heap Insert
- Min Heap Extract

Heap operations are rendered as a tree using array-index relationships. The visualizer highlights heap comparisons and swaps during heapify-up and heapify-down.

### Hash Table Visualizations

The app supports linear probing hash table operations:

- Create
- Insert
- Search

Hash tables are displayed as indexed slots. The visualizer highlights probes, collisions, successful searches, failed searches, insertions, and load factor.

## How It Works

The project is built around a step-based visualization system.

Each algorithm generator creates a list of `Step` objects instead of directly animating the UI. A `StepRunner` applies those steps one at a time, allowing the user to move forward, move backward, reset, or play through the process automatically.

The main parts are:

- `model` - stores the current state of arrays, trees, heaps, and hash tables
- `steps` - contains individual operations such as compare, swap, insert, delete, probe, and rotate
- `algo` - generates step sequences for each algorithm or data structure operation
- `util` - shared helpers such as counters, validators, and the step runner
- `render` - helper classes for positioning tree nodes
- `app.ui` - JavaFX controller and rendering logic

## Controls

- **Load** - generate a new random structure or input set
- **Reset** - return to the beginning of the current visualization
- **Prev** - step backward
- **Next** - step forward
- **Play** - automatically step through the visualization
- **Pause** - stop playback
- **Mode selector** - choose between Array, Trees, Heap, and Hash Table
- **Algorithm selector** - choose the algorithm or operation to visualize
- **Input field** - enter values for operations like insert, search, or delete

## Metrics

The side panel tracks:

- Step count
- Comparisons
- Swaps
- Writes
- Current status or event message

These counters help connect the visual process to algorithm analysis.

## Tech Stack

- Java
- JavaFX
- FXML
- CSS

The project is configured as an IntelliJ IDEA Java project with JavaFX support.

## Running the Project

Open the project in IntelliJ IDEA.

Make sure JavaFX is configured in the project settings, then run:

```text
src/app/Main.java
```
The main JavaFX layout is in:

```text
resources/MainView.fxml
```

The app styling is in:

```text
resources/styles.css
```

## Project Goal

The goal of this project is educational. Instead of only reading pseudocode or watching static diagrams, students can step through algorithms interactively and see how data changes over time.

This makes it easier to understand:

- Why comparisons happen
- When swaps or writes occur
- How recursive algorithms break work into smaller parts
- How tree operations move through nodes
- How heap properties are restored
- How linear probing handles hash collisions, etc.
