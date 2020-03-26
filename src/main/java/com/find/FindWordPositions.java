package com.find;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/*
* This program implements word dictionary with positions stored. It implements something similar to the
* first half of the following link:
*
* http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/
*
* The structure of the code flow is that a text file is read from the current running location and reads
* in every line and looks at each word and stores it location/position within the line.
*
* The main data structure is a HashMap that has a key of word and value of another HashMap that has key
* as line number and value as positions within that line.
*
* author: Frank Giordano 10/24/2015
*/
public class FindWordPositions {

    private static HashMap<String, HashMap<Integer, ArrayList<Integer>>> dictionary;
    private static String fileName = "t3.txt";

    public static void search(String searchWord) {
        if (dictionary != null) {  // use current cache
            print(searchWord);
            return;
        }

        processFile();
        print(searchWord);
    }

    private static void processFile() {

        int position = 0;
        int lineCount = 0;
        String line = "";

        // HashMap<Integer, ArrayList<Integer>> = line #, list of position numbers
        // within the line
        HashMap<Integer, ArrayList<Integer>> wordLinePosInfo;
        dictionary = new HashMap<>();
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(fileName));

            // while we loop through the file, read each line until there is nothing left to
            // read. this assumes we have carriage returns ending each text line.
            while ((line = bReader.readLine()) != null) {

                lineCount++;

                line = toLowerCase(line);
                String[] words = line.replaceAll("[^a-z0-9]", " ").split(" ");
                for (String word : words) {
                    if ("".equals(word))
                        continue;

                    int startIndex = 0;
                    while ((position = line.indexOf(word, startIndex)) != -1) {
                        startIndex = position + word.length();
                        // ignore finding the word text within another larger word
                        if (isWordEmbedded(position, word, line)) {
                            continue; // skip this position
                        }

                        // first time this word is seen
                        if (!dictionary.containsKey(word)) {
                            wordLinePosInfo = new HashMap<>();
                            addFirstLinePosition(position, lineCount, wordLinePosInfo);
                            dictionary.put(word, wordLinePosInfo);
                            break;
                        }

                        // first time adding a position for this word for this line
                        if (dictionary.containsKey(word) && dictionary.get(word).get(lineCount) == null) {
                            wordLinePosInfo = dictionary.get(word);
                            addFirstLinePosition(position, lineCount, wordLinePosInfo);
                            break;
                        }

                        // at this point, it is obvious add position to existing word/line storage
                        wordLinePosInfo = dictionary.get(word);
                        // retrieve all current positions of the word found so far for this line
                        ArrayList<Integer> currWordLinePositions = wordLinePosInfo.get(lineCount);
                        // get the last word position from the list
                        int lastKnownPos = currWordLinePositions.get(currWordLinePositions.size() - 1);
                        // increase last known position to the end of the word so it starts searching
                        // there
                        lastKnownPos = lastKnownPos + word.length();
                        // search for the next word occurrence from current read in line file
                        position = line.indexOf(word, lastKnownPos);
                        if (position == -1)
                            break;
                        if (!currWordLinePositions.contains(Integer.valueOf(position))) {
                            wordLinePosInfo.get(lineCount).add(position);
                            startIndex = position + word.length();
                        }
                    }
                }
            }

            bReader.close();
        } catch (IOException e) {
            System.out.print("Error reading file. Error message = " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void addFirstLinePosition(int position, int lineCount, HashMap<Integer, ArrayList<Integer>> wordLinePosInfo) {
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(position);
        wordLinePosInfo.put(lineCount, positions);
    }

    private static boolean isWordEmbedded(int position, String word, String line) {
        if (position != 0 && position != line.length() - 1 && position + (word.length() - 1) != (line.length() - 1)) {
            boolean isAlphabeticRightSide = Character.isAlphabetic(line.charAt(position + (word.length())));
            boolean isAlphabeticLeftSide = Character.isAlphabetic(line.charAt(position - 1));
            if (isAlphabeticRightSide || isAlphabeticLeftSide) {
                return true;
            }
        }

        if (position == 0 && (position + (word.length()) < line.length())) {
            boolean isAlphabeticRightSide = Character.isAlphabetic(line.charAt(position + (word.length())));
            if (isAlphabeticRightSide)
                return true;
        }

        return false;
    }

    private static String toLowerCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            Character charAt = input.charAt(i);
            if (Character.isAlphabetic(charAt)) {
                result.append(Character.toLowerCase(charAt));
            } else {
                result.append(charAt);
            }
        }
        return result.toString();
    }

    private static void print(String searchWord) {
        processFile();
        if (dictionary.get(searchWord) == null) {
            System.out.println("The following word " + searchWord + " was not found.");
        } else {
            for (Entry<Integer, ArrayList<Integer>> entry : dictionary.get(searchWord).entrySet()) {
                foundMsg(searchWord, entry);
            }
        }
    }

    private static void foundMsg(String searchWord, Entry<Integer, ArrayList<Integer>> entry) {
        StringBuilder message = new StringBuilder();
        message.append("The following word ");
        message.append("\"");
        message.append(searchWord);
        message.append("\"");
        message.append(" was found at line number ");
        message.append(entry.getKey());
        message.append(" at position(s): ");
        message.append((entry.getValue().toString()));
        System.out.println(message);
    }

    public static void main(String args[]) {

        String searchWord = null;
        byte[] input;

        do {
            input = new byte[80];
            System.out.printf("Enter a word to search the following file %s for location info\n", fileName);
            System.out.print("> ");
            try {
                System.in.read(input);
            } catch (IOException e) {
                System.out.print("Error reading given input. Error message = " + e.getMessage());
                System.exit(-1);
            }
            searchWord = (new String(input, 0, input.length)).trim();
            if (searchWord.length() > 0) {
                FindWordPositions.search(searchWord);
            } else {
                System.exit(0);
            }
        } while (searchWord.length() > 0);
    }

}