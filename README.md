> **GETTING STARTED:** You must start from some combination of the edu.brown.cs.student.main.server.csv Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your src.main.edu.brown.cs.student.main.server.Server class matches the path specified in the run script. Currently, it is set to execute src.main.edu.brown.cs.student.main.server.Server at `edu/brown/cs/student/main/server/src.main.edu.brown.cs.student.main.server.Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
This is  a web API server that enables users to query the United States Census API to retrieve information on broadband coverage. It can also load, view, and search a specific CSV.

# Design Choices
We have separated our code into 2 main packages based on csv/census. Major design choices included adding a proxy class OrganizedData as a shared state to share information between loadHandler(), viewHandler(), and searchHandler(). We also decided to create a CensusAPIUtilities class to help delegate the work of broadbandHandler(). In addition, we chose to store deserialized state and county codes in a map for more efficient searching.

# Errors/Bugs

# Tests

# How to
To run the server, type mvn package followed by ./run
The server will open at http://localhost:3232
To load a file, append /loadcsv?filepath="enter file path here"&headers="enter 0 if the CSV does not have headers, enter 1 otherwise"
To view a file, append /viewcs?filepath="enter file path here"
To search a file, append /searchcsv?filepath="enter file path here"&headers="enter 0 if the CSV does not have headers, enter 1 otherwise"0&identType="enter if you are searching by header of by index. if you're searching the entire file, enter N/A"&colIdentifier="either a header name or an index number or N/A if you're searching the entire file"&word="enter the word you're searching for.

example of searching the entire data/RI.csv for the word "Providence": http://localhost:3232/searchcsv?filepath=data/RI.csv&headers=0&identType=N/A&colIdentifier=N/A&word=Providence

Please note: most of our github commits are on the "s" branch, so when evaluating this part of our assignment, we believe looking at this branch would be most representative.
