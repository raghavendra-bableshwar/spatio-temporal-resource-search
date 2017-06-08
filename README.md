This is a repository created for CS581 - TEAM 7 (SpatioTemporalResourceSearch) project

###Team members:###

Raghavendra Bableshwar - [664260979](https://raghavendrabableshwar.wordpress.com/)
Swapnil Akolkar - [665395774](https://www.linkedin.com/in/sakolk/)
Vishalaxi Tandel - [659468038](https://www.linkedin.com/in/vishalaxivtandel/) 

### What is this project for? ###

Have a look at the [website](https://raghavendrabableshwarblog.wordpress.com/2017/02/26/spatio-temporal-resource-search/) for explanation.

### How do I get set up? ###

* Download the project as zip file (from Download link in left side)
* Extract the downloaded zip
* Open a new workspace in eclipse and right click in project explorer and click 'Import'
* Under 'General', 'Import Exisiting Projects into workspace'
* Click next and then select 'Root directory' and click on browse
* Browse to the newly extracted file till the root folder 'SpatioTemporalResourceSearch' and then click on 'Ok' and 'Finish'
* The new project gets imported to the workspace
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