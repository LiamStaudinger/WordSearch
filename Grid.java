/*
 * File: Grid.java
 * Author: Liam Staudinger
 * Course: CSC 210, Fall 2024
 * 
 * This class represents a grid for a word search puzzle. It contains instance variables that are a 2D array
 * of characters to store the grid and a boolean array to keep track of which positions have been used. The
 * class also contains a constructor to create a grid with random letters and methods to place and remove 
 * words in the grid. The Grid class is used by the WordSearch class to create the word search puzzle grid 
 * and run the word search game.
 */
package com.gradescope.wordsearch;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

	// Instance variables to store the grid, keep track of used positions, keep track of words in the grid,
	// and a Random object
	protected char[][] grid;
	protected boolean[][] isUsed;
	protected ArrayList<Word> words = new ArrayList<>();
	private Random rand = new Random();
	
	/*
	 * Constructor to create a grid with random letters and set all positions to unused.
	 * 
	 * @param rows: The number of rows in the grid
	 * @param cols: The number of columns in the grid
	 */
	public Grid (int rows, int cols) {
		grid = new char[rows][cols];
		isUsed = new boolean[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = (char) ('A' + rand.nextInt(26)); // random letter
				//grid[i][j] = '*'; // empty space
				isUsed[i][j] = false;
			}
		}
	}
	
	/*
	 * Method to place a list of words in the grid at random positions and directions.
	 * 
	 * @param wordsList: The list of words to place in the grid
	 */
	public void placeWord(ArrayList<String> wordsList) {
		ArrayList<String> unplaced = new ArrayList<>();
		for (String word : wordsList) {
			boolean placed = false;
			for (int i = 0; i < 100; i++) { // try 100 times
				// Get random position and direction
				int row = rand.nextInt(grid.length);
				int col = rand.nextInt(grid[0].length);
				int[] direction = randomdirection();
				// Check if the word can be placed at the position and direction
				if (canPlace(word, row, col, direction)) {
					// Place the word in the grid
					for (int j = 0; j < word.length(); j++) {
						grid[row + j * direction[0]][col + j * direction[1]] = word.toUpperCase().charAt(j); // place letter
						isUsed[row + j * direction[0]][col + j * direction[1]] = true; // mark as used
					}
					words.add(new Word(word, row, col, direction)); // add to list of words
					placed = true;
					break;
				}
			}
			if (!placed) unplaced.add(word); // add to list of unplaced words
		}
		// if not every word was placed after 100 attempts, fallback to systematic placement
		if (unplaced.size() > 0) systematic(unplaced);
	}

	/*
	 * Method to check if a word can be placed in the grid at a given starting position and direction.
	 * 
	 * @param word: The word to place in the grid
	 * @param row: The starting row position in the grid
	 * @param col: The starting column position in the grid
	 * @param direction: The direction to place the word in the grid
	 * @return: True if the word can be placed in the grid, false otherwise
	 */
	private boolean canPlace(String word, int row, int col, int[] direction) {
		for (int i = 0; i < word.length(); i++) {
			// Calculate new position
			int newRow = row + i * direction[0];
			int newCol = col + i * direction[1];
			// Check if the new position is out of bounds
			if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) return false; 
			// Check if the new position is part of another word and, if it is, if the letters match
			if (Character.toLowerCase(grid[newRow][newCol]) != word.charAt(i) && isUsed[newRow][newCol]) return false; 
		}
		// All positions for every letter of the word are valid
		return true;
	}

	/*
	 * Method to place words in the grid systematically if they could not be placed randomly.
	 * 
	 * @param unplaced: The list of words that could not be placed randomly
	 */
	private void systematic(ArrayList<String> unplaced) {
		for (String word : unplaced) {
			boolean placed = false;
			int[][] directions = {{0,1}, {1,0}, {1,1}};
			// try every position and direction
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					for (int[] dir : directions) {
						// Check if the word can be placed at the position and direction
						if (canPlace(word, i, j, dir)) {
							// Place the word in the grid
							for (int k = 0; k < word.length(); k++) {
								grid[i + k * dir[0]][j + k * dir[1]] = word.toUpperCase().charAt(k); // place letter
								isUsed[i + k * dir[0]][j + k * dir[1]] = true; // mark as used
							}
							words.add(new Word(word, i, j, dir)); // add to list of words
							placed = true;
							break;
						}
					}
					if (placed) break; // exit loop if word was placed
				}
				if (placed) break; // exit loop if word was placed
			}
			if (!placed) System.out.println(word + " could not be placed");
		}
	}

	/*
	 * Method to remove a word from the grid.
	 * 
	 * @param word: The word to remove from the grid
	 * @param direction: The direction of the word in the grid
	 */
	public void removeWord(Word word, char direction) {
		// Get direction array, row, and column of the word
		int[] dir = getDirectionArray(direction);
		int row = word.getRow();
		int col = word.getCol();
		// Check if the word can be removed
		if (canRemove(word.getText(), row, col, dir)) {
			// for each letter in the word
			for (int i = 0; i < word.getText().length(); i++) { 
				// remove letter if it is not shared with another word
				if (!isPartOfAnotherWord(row + i * dir[0], col + i * dir[1], word)) {
					grid[row + i * dir[0]][col + i * dir[1]] = '*'; // remove letter
					isUsed[row + i * dir[0]][col + i * dir[1]] = false; // mark as unused
				}
			}
			words.remove(word); // remove from list of words
			System.out.println("\n" + word.getText() + " removed\n");
			return;
		} else System.out.println("\n" + word.getText() + " not found\n");
	}

	/*
	 * Method to check if a word can be removed from the grid.
	 * 
	 * @param word: The word to remove from the grid
	 * @param row: The row of the word in the grid
	 * @param col: The column of the word in the grid
	 * @param direction: The direction of the word in the grid
	 * @return: True if the word can be removed, false otherwise
	 */
	private boolean canRemove(String word, int row, int col, int[] direction) {
		// for each letter in the word
		for (int i = 0; i < word.length(); i++) {
			// Calculate new position
			int newRow = row + i * direction[0];
			int newCol = col + i * direction[1];
			
			// Check if the new position is out of bounds
			if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) return false; 
			// Check if the letter at the new position matches the current letter we're comparing
			if (Character.toLowerCase(grid[newRow][newCol]) != word.charAt(i)) return false; 
		}
		// All positions for every letter of the word are valid
		return true;
	}
	
	/*
	 * Method to check if a position in the grid is part of another word.
	 * 
	 * @param row: The row of the position in the grid
	 * @param col: The column of the position in the grid
	 * @param wordToRemove: The word to remove from the grid
	 * @return: True if the position is part of another word, false otherwise
	 */
	private boolean isPartOfAnotherWord(int row, int col, Word wordToRemove) {
		// for each word in the list of words
		for (Word word : words) {
			// check if the word is not the word to remove and if it occupies the position (row, col)
			// if this is true, wordToRemove shares letters with another word
			if (!word.equals(wordToRemove) && isWordAtPosition(word, row, col)) return true;
		}
		return false;
	}

	/*
	 * Method to check if a word occupies a position in the grid.
	 * 
	 * @param word: The word to check
	 * @param row: The row of the position in the grid
	 * @param col: The column of the position in the grid
	 * @return: True if the word occupies the position, false otherwise
	 */
	private boolean isWordAtPosition (Word word, int row, int col) {
		int[] direction = word.getDirection();
		// for each letter in the word
		for (int i = 0; i < word.getText().length(); i++) {
			// calculate new position of that letter
			int newRow = word.getRow() + i * direction[0];
			int newCol = word.getCol() + i * direction[1];
			// check if the position of the letter is equal to the position of the word to remove (row, col)
			// if this is true, the word to remove and another word share a letter at position (newRow, newCol)
			if (newRow == row && newCol == col) return true;
		}
		return false;
	}

	/*
	 * Method to get the direction array for a given direction character.
	 * 
	 * @param direction: The direction character ('H', 'V', or 'D')
	 * @return: The direction array for the given direction character
	 */
	public int[] getDirectionArray(char direction) {
		switch(Character.toLowerCase(direction)) {
			case 'h':
				return new int[] {0, 1};
			case 'v':
				return new int[] {1, 0};
			case 'd':
				return new int[] {1, 1};
			default:
				return null;
		}
	}

	/*
	 * Method to get a random direction array.
	 * 
	 * @return: A random direction array
	 */
	private int[] randomdirection() {
		int[][] directions = {{0,1}, {1,0}, {1,1}};
		return directions[rand.nextInt(directions.length)];
	}

	/*
	 * Method to get the list of words in the grid.
	 * 
	 * @return: The list of words in the grid
	 */
	public ArrayList<Word> getWords() {
		return words;
	}

	/*
	 * Method to get the grid as a string.
	 * Overrides the toString method in the Object class.
	 * 
	 * @return: The grid as a string
	 */
	@Override
	public String toString() {
		String str = "   ";

		// add column labels
		for (int i = 0; i < grid[0].length; i++) {
			str += (char) ('a' + i) + " ";
		}
		str += "\n";

		// add grid rows with row labels
		for (int i = 0; i < grid.length; i++) {
			str += String.format("%02d", i) + " ";
			for (char c : grid[i]) {
				str += c + " ";
			}
			str += "\n";
		}
		return str;
	}
}