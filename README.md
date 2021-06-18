# COVID-19 Locator

Deployed at: http://www.cv19location.com

## UPDATE 06/18
It's been over a year since this app has been updated. Stay tuned for pending refurbishment! 

--------------------------

This Web-based application utilizes SpringMVC and RESTful web services to provide a user with current case 
information on how COVID-19 is impacting their community in the United States. Raw case information (total confirmed/deaths/recovered cases) 
is rendered directly from Johns Hopkins University CSSE .csv files sourced on GitHub. Users can browse for a location to see case data visualized in graphs and tables. Users
may also subscribe to SMS to receive daily updates on new case information for their selected location. Developers may access the free COVID-19 Locator API. For more information on available endpoints, access the API tab at the top of the page. 

## Run the app
Visit the website at: http://www.cv19location.com (currently in alpha)

Or if you'd like to run it from your local machine: 
- Install IntelliJ IDEA. 
- Clone this repository into your local machine through the terminal command 'git clone https://github.com/svakam/covid-19-location.git'. 
- Open IntelliJ IDEA. 
- Choose the 'Import project' option, locate the project folder through the file explorer, and import this project. 
- Wait for the project to build. 
- Run the app with the green Play button, either located at the bottom left of the IDE or top right. 
- Open a browser session, and in the URL text box, type 'localhost:5000' and enter. 

## Screenshots
![Landing](src/main/resources/static/img/Landing.PNG)
![Totals](src/main/resources/static/img/Totals.PNG)
![Progression](src/main/resources/static/img/Progression.PNG)
![Subscribe](src/main/resources/static/img/Subscribe.PNG)

## Project management
Trello: https://trello.com/b/LuJDmF4r/covid-19

## Languages and Technologies Used
- Languages
  - Java, JavaScript, HTML/CSS
- Technologies
  - IDE: IntelliJ IDEA
  - Frameworks: SpringMVC, Spring Boot, Thymeleaf
  - APIs: CV19Locator API, Postman
  - Libraries: CanvasJS, Bootstrap, GSON, jQuery
  - AWS: DynamoDB, SNS, Elastic Beanstalk, Route 53
  
## Contact
If you're interested in contributing, or have questions regarding the CV19Locator API, drop an email at svakam6370@gmail.com or DM me on LinkedIn at linkedin.com/in/svakam. 

## JHU Data Changes
- NOTE: As of 03/25/2020, for JHU CSSE's _county-level_ information for the United States, there is no data available between and including 03/10/2020 and 03/22/2020. In addition,
there are a number of redundancies in countries names, associated slugs, and state/province names. This spreadsheet outlines the issues being addressed (and hopefully corrected for)
imminently: https://docs.google.com/spreadsheets/d/19x2CUBdHPlxBKUtfgJU2LzVXR_piVtvlo8lgi_DictI/edit?usp=sharing

- NOTE: As of 03/28/2020, JHU CSSE .csv data appears to have been re-standardized and more developer-approachable for parsing.

- NOTE: As of 04/15/2020, I've noticed an errata.csv file in JHU CSSE. I'm looking into this. 

- NOTE: As of 05/01/2020, errata.csv is already accounted for in the data. 

