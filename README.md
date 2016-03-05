# findwordpositions-hashmap

/*
 * author: Frank Giordano 10/24/2015
 * program implements word dictionary with positions stored
 * implements first half of the following link
 * http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/
 * this program uses java hashmap structure
 * 
 */


Please note this problem stores the first character position of the word within the line number.. 
It does not store the occurence position of the entire string word.. 

For instance,

"test frank giordano" 

test located at line 1, position 0
frank located at line 1, position 5
etc

whereas all the words positions would be:
test located at position 0 and frank located at position 1 and giordano located at position 3
