# OOP Document Similarity Project

This is the README for the Document Similarity project. The program enables the user to compare two large text files by computing their Jaccard index.

## Getting Started

The full java code can be found in the src folder. 


## Description

The program takes in two strings, one for each document name, as well as the k number and shingleSize. The program will then display the percentage similarity.

### Runner.java

* The Runner class contains the main method
* It starts the programs by creating an instance of the Menu.java class and shows the menu.

### Menu.java

* The Menu class displays all the menu options to the user and prompts for user input.
* It loops through the menu until the user exits the program.
* Creates an instance of Launcher.java and sends all the required parameters to it.

### Launcher.java

* Launches the code by creating consumer and producer threads.
* The launcher start()s and join()s all the threads together so that their processes are synchronised.
* Creates 2 DocumentParser Threads and 1 Consumer Thread.

### DocumentParser.java
* Class that takes in a document, reads it using a buffered reader, and breaks the lines into shingles, then creates shingles based on shingleSize
* Creates instances of Shingles and sends those Shingles into the blocking queue.

### Shingles.java
* Object class that holds a documentID and a hashcode.

### Poison.java
* A EOF indicator, extends the Shingle class.

### Consumer.java
* Class that creates a worker thread that takes a Shingle of the blocking queue, XOR the value with the minhash array, and calculates the similarities between the two documents.
* Assigns threads from a threadpool to do all the minhashing work.

## Extra funcionality
* The user can choose to input any amount of k number to adjust the accuracy of the Jaccard index.
* The user can input Shingle size to adjust the accuracy of the minhashes.

## Steps Required to run program
* Make sure the computer has an up to date JRE installed.
* Open up a terminal and change directory to where the JAR file is located.
* Ensure that the 2 text files that the user wishes to check similarity on is in the same directory as the JAR file.
* Execute the following command:  java –cp ./oop.jar ie.gmit.sw.Runner
* The user will be prompted to enter 1 to start the program or 2 to exit.
* Select 1, then input the doc 1 name, doc 2 name, k and shingleSize
* The program will then return the similarity in percentage.


