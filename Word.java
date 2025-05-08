/*
 * File: Word.java
 * Author: Liam Staudinger
 * Course: CSC 210, Fall 2024
 * 
 * This class represents a word in a word search puzzle. 
 * It contains the text of the word, the row and column where the word starts, 
 * and the direction of the word. This class defines a constructor, 
 * a method to get the text of the word, methods to get the row, column, and 
 * direction of the word, and a method that checks if an object is equal to a specific word.
 */

package com.gradescope.wordsearch;

public class Word {
    // Instance variables for row, column, direction, and text of the word
    protected String text;
    protected int row;
    protected int col;
    protected int[] direction;

    /*
     * Constructor for the Word class that initializes the text, row, column, and direction of the word.
     * 
     * @param text the text of the word
     * @param row the row where the word starts 
     * @param col the column where the word starts
     * @param direction the direction of the word as an array of two integers
     */
    public Word(String text, int row, int col, int[] direction) {
        this.text = text;
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    /*
     * Method to get the text of the word.
     * 
     * @return the text of the word
     */
    public String getText() {
        return text;
    }

    /*
     * Method to get the row where the word starts.
     * 
     * @return the row where the word starts
     */
    public int getRow() {
        return row;
    }

    /*
     * Method to get the column where the word starts.
     * 
     * @return the column where the word starts
     */
    public int getCol() {
        return col;
    }

    /*
     * Method to get the direction of the word as an array of two integers.
     * 
     * @return the direction of the word as an array of two integers
     */
    public int[] getDirection() {
        return direction;
    }

    /*
     * Method that checks if an object is equal to a specific word.
     * Overrides the equals method in the Object class.
     * 
     * @param obj the object to compare with the word
     * @return true if the object is equal to the word, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Word)) {
            return false;
        }
        Word word = (Word) obj;
        return text.equals(word.text) && row == word.row && col == word.col && direction[0] == word.direction[0] && direction[1] == word.direction[1];
    }
}
