# Fifteen-Puzzle-Solver

## Overview

This project is a solver for the classic 15 Puzzle game, developed as part of the CMPT 225 course at Simon Fraser University during Spring 2023. The solver utilizes graph exploration algorithms such as BFS, DFS, and A* to find the optimal solution to the puzzle, writing the solution steps to an output file.

## Project Structure

- **`BoardGen.java`**: Generates the puzzle board, initializes the puzzle state, and manages the puzzle configuration.
- **`Edge.java`**: Represents the edges in the graph, connecting nodes that represent puzzle states.
- **`FifteenPuzzle.java`**: The main class that represents the 15 puzzle itself. This class contains the logic for the puzzle, including moves and checks for the solved state.
- **`GraphSolve.java`**: Implements the graph-solving logic, utilizing algorithms like BFS, DFS, and A* to find the shortest path to the solution.
- **`IllegalMoveException.java`**: Handles exceptions when an illegal move is attempted in the puzzle.
- **`NodeWithXYCoordinates.java`**: Represents nodes in the graph with XY coordinates, used to track puzzle pieces' positions.
- **`Solver.java`**: The main entry point for the solver. It reads the puzzle configuration from an input file, runs the chosen algorithm to find the solution, and writes the solution steps to an output file.
- **`StateGraph.java`**: Manages the graph of possible puzzle states, connecting different configurations through valid moves.
- **`BadBoardException.java`**: Handles exceptions related to invalid board configurations, ensuring that the puzzle starts in a valid state.
- **`AStarNodeWrapper.java`**: A wrapper class for nodes used in the A* algorithm, including the necessary heuristic evaluations.

## Features

- **Supports Multiple Solving Algorithms**:
  - **BFS (Breadth-First Search)**
  - **DFS (Depth-First Search)**
  - **A* Search** with customizable heuristics
- **Handles Exceptions**: Includes custom exceptions for illegal moves and invalid board configurations.
- **Outputs Solution**: Writes the sequence of moves to solve the puzzle to an output file.


### Command Line Interface

To run the solver, use the following command:

```bash
java fifteenpuzzle.Solver input.txt sol.txt
