Running the app
===============
Run mvn clean install from project root directory - this will also run the unit tests
Two jar files are created. page-scraper-1.0-SNAPSHOT.jar and page-scraper-1.0-SNAPSHOT-jar-with-dependencies.jar
The second file is packaged with all required dependencies to ease running 
navigate to jar location and enter:
java -jar page-scraper-1.0-SNAPSHOT-jar-with-dependencies.jar http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html

Running tests only
===================
run mvn test from root directory


Dependencies 
============
(As detailed above, all required dependencies are built into the jar-with-dependencies.jar in target)
org.jsoup 1.8.3
junit 4.11 (testing only)
com.google.code.gson 2.8.0
org.apache.commons 3.0
org.mockito 1.10.19 (testing only)
org.json 20160810 (testing only)