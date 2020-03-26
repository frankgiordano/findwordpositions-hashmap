# findwordpositions-hashmap
 
This program implements word dictionary with positions stored. It implements something similar to the
first half of the following link:
 
http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/

The structure of the code flow is that a text file is read from the current running location and reads
in every line and looks at each word and stores it location/position within the line.

The main data structure is a HashMap that has a key of word and value of another HashMap that has key
as line number and value as positions within that line.

NOTE: This problem stores the first character position of the word within the line number.
It does not store the occurrence position of the entire string word.

For instance,

"test frank giordano" 

test located at line 1, position 0
frank located at line 1, position 5
etc

whereas all the words positions would be:
test located at position 0 and frank located at position 1 and giordano located at position 3

## Setup, Installation, and running the application:

1 - Install Java 8

2 - Install Maven 3.6.3 or higher

At project's root directory, perform the following commands:

3 - mvn clean install

4 - java -jar .\target\FindWordPositions.jar
