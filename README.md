This is a spring boot application written to verify the orders at the end of a trading session.

The application will read a txt file from a directory and upload the results to another directory.

Both the directories need to be defined by the User.

The application once up and running will look for files being added to the configured directory and then process it.
Only text/plain files will be processed other files will be ignored.

Please make sure you only add the files to the directory once the application is up and running. 
Only the files added after application start up will be processed.

Also make sure the input and output directories are different to avoid processing of output files as input for application.


To build the project use the maven wrapper ./mvnw clean install

Before running the application make sure the input and the output directories are provided.

to run the application use maven wrapper and run as a springboot app using command (Please replace the directory locations to location on your local file System).

./mvnw spring-boot:run -Dspring-boot.run.arguments="--order-book.input-directory=/Users/animeshpradhan/Downloads/inputDirectory --order-book.output-directory=/Users/animeshpradhan/Downloads/outputDirectory"

The output will be written to the configured output directory. The file name will be the input file name prefixed by String "output_".