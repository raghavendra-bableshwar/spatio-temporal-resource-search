
This is a repository created for CS581 - TEAM 7 (SpatioTemporalResourceSearch) project

###Team members:###

Swapnil Akolkar - 665395774
Vishalaxi Tandel - 659468038 
Raghavendra Bableshwar - 664260979
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Download the project as zip file
* Create a dynamic web project in eclipse
* Import the zip project into the workspace
* Add jar dependencies for servlet-api.jar and gson.jar (both the jars are there in lib folder)
* Create a new Tomcat server and add the project to it
* Clean the server
* Clean and Build the project
* Run the project on server
* The application will start running


### How do run the simulation test ?###
* Set the congestion value in Congestion.properties file
* Go to SpatioTemporal.java, we have a main method to run the simulations in an automated way
* Initialize an appropriate value for the variable “localDataFilePath” with location of “data files” folder.
* Configure the project in eclipse to redirect standard output to a csv file. (Go to Run Configurations -> Common tab -> In Output File textbox, add path for .csv file)
* There are code snippet commented for each algorithm, uncomment a snippet for you wish to run the algorithm. Now, run the code as a Java application.
* The output can be viewed in the appropriate output csv file