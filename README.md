# COVID-19 Location Info

Deployed at: www.cv19location.com

This Web-based application utilizes SpringMVC and RESTful web services to provide a user with current 
information on how COVID-19 is impacting their community in the United States.

Upon entering the website, the user will be greeted by a splash page. Here, the user will be provided a search box to enter their country, state, or county. 
The user will then be taken to a results page that renders information from Covid19API regarding the total number of cases of COVID-19 in their country, 
total number of cases in their province/state, and total number of cases in their county. 

The number of cases of COVID-19 will contain numbers for confirmed cases, recovered cases, and death cases. 

NOTE: As of 03/25/2020, for JHU CSSE's _county-level_ information for the United States, there is no data available between and including 03/10/2020 and 03/22/2020. In addition,
there are a number of redundancies in country names, associated slugs, and state/province names. This spreadsheet outlines the issues being addressed (and hopefully corrected for)
imminently: https://docs.google.com/spreadsheets/d/19x2CUBdHPlxBKUtfgJU2LzVXR_piVtvlo8lgi_DictI/edit?usp=sharing

## Run the app
Visit the website at: www.cv19location.com

Or if you'd like to run it from your local machine: 
- Install IntelliJ IDEA. 
- Clone this repository into your local machine through the terminal command 'git clone https://github.com/svakam/covid-19-location.git'. 
- Open IntelliJ IDEA. 
- Choose the 'Import project' option, locate the project folder through the file explorer, and import this project. 
- Wait for the project to build. 
- Run the app with the green Play button, either located at the bottom left of the IDE or top right. 
- Open a browser session, and in the URL text box, type 'localhost:8080' and enter. 

## Change log/Commit History
03/20/2020
- Initialized app
- Set up Spring Boot, SpringMVC
- Initialized Firebase

03/21/2020
- Initialized Bootstrap
- Set up templates
- Rendering templates through Thymeleaf
- Called Covid19API successfully with HttpURLConnection class

03/22/2020
- Passing JSON info successfully into template 
- Refactored HttpURLConnection to work with GSON and API call
- GSON deserializing JSON from API
- Passing JSON and objects appropriately into template
- Populating country dropdown menu with countries from API
- Passing dropdown choice to results page and rendering country's information (country, slug, provinces)

03/23/2020
- Refactored postmapping to be more RESTful with dropdown choice and /results
- Successfully accessing endpoint with case information for countries
- Rendering confirmed/death/recovered cases of user's choice of country on /results
- Began Bootstrap work on index - jumbotron implemented and sized

03/24/2020
- Added JHU CSSE dashboard to front page
- Deployed to AWS EB

03/25/2020
- Note: JHU CSSE data contains a number of discrepancies/conflicts on the country, state, and county level. Created a spreadsheet that accounts for 
these issues here: https://docs.google.com/spreadsheets/d/19x2CUBdHPlxBKUtfgJU2LzVXR_piVtvlo8lgi_DictI/edit?usp=sharing
- Header nav bar for results page to filter by province if available

03/26/2020


## Data Flow
Search bar entry that ideally contains the state or province, but also country, will be used to query 
the /countries endpoint. If successful, it must be decided whether the query was a country or a state/province. If a country was entered, the /summary endpoint will be queried
to obtain the country, countryslug, and case data (confirmed, deaths, recovered). If a state/province was entered, the countryslug will be used to query the /country/
{countryslug}/status/{status} endpoint. The array of states/provinces from the /countries endpoint will be used to further find the desired state/province and iterate through
the endpoint for /country/{countryslug}/status/{status} to get the state/province information. 

Covid19API endpoints (see the [API](https://covid19api.com/#details)):
- /countries: Country, Slug, Provinces[]
- /summary: Countries[Country, CountrySlug, NewConfirmed, TotalConfirmed, NewDeaths, TotalDeaths, NewRecovered, TotalRecovered]
- /total/country/{countryslug}/status/{status}: Country (same one), Province, Lat/Lon, Date, Cases, Status (same one): relevant data is total # cases per day
- /country/{countryslug}/status/{status}: same as above, but relevant data is specific to province level
- /total/dayone/country/{countryslug}/status/{status}: for specific country only: and since day one of case #1 only
- /dayone/country/{countryslug}/status/{status}: same as above but specific to province level

## Project management
Trello: https://trello.com/b/LuJDmF4r/covid-19

## Languages Used
- Java
- HTML/CSS

## Tools, Libraries, Frameworks, IDEs Used
- IntelliJ IDEA
- Spring: SpringMVC, Boot, Thymeleaf
- AWS: Route 53, Elastic Beanstalk
- Postman
- 

## Acknowledgements
COVID-19 APIs and COVID-19 data used/consulted:
- Kyle Redelinghuys, who created the [Covid19API](https://covid19api.com/#details)
- Johns Hopkins CSSE: https://github.com/CSSEGISandData/COVID-19 https://systems.jhu.edu/research/public-health/2019-ncov-map-faqs/ https://www.arcgis.com/apps/opsdashboard/index.html#/bda7594740fd40299423467b48e9ecf6
- CDC: https://open.cdc.gov/apis.html
- USAFacts: https://usafacts.org/visualizations/coronavirus-covid-19-spread-map/
- Some state/local health department websites

Spring.io documentation:
- REST: https://spring.io/guides/gs/rest-service/
- REST/HTTP: https://spring.io/guides/tutorials/bookmarks/
- REST: https://spring.io/guides/gs/serving-web-content/

Baeldung documentation:
- HTTP Request in Java: https://www.baeldung.com/java-http-request
- Thymeleaf: https://www.baeldung.com/thymeleaf-in-spring-mvc
- Path variables: https://www.baeldung.com/spring-thymeleaf-path-variables
- GSON Deserialization: https://www.baeldung.com/gson-deserialization-guide

Bootstrap documentation: 
- Implementation: https://getbootstrap.com/docs/4.4/getting-started/introduction/

Stack Overflow:
- Using Firebase with Spring boot REST application https://stackoverflow.com/questions/39183107/how-to-use-firebase-with-spring-boot-rest-application
- IOException: https://stackoverflow.com/questions/22900477/java-io-exception-stream-closed
- GSON import/build issue: https://stackoverflow.com/questions/47566665/cannot-resolve-symbol-gson-and-it-wont-allow-me-to-import/47566770
- Thymeleaf list iteration: https://stackoverflow.com/questions/38367339/thymeleaf-how-to-loop-a-list-by-index

Thymeleaf documentation: 
- Layouts: https://www.thymeleaf.org/doc/articles/layouts.html
- Iteration: https://www.baeldung.com/thymeleaf-iteration
- Manual: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#the-good-thymes-virtual-grocery

Deployment: 
- Heroku vs AWS: https://www.guru99.com/heroku-vs-aws.html
- CNAME and Domain Names: https://www.freecodecamp.org/news/why-cant-a-domain-s-root-be-a-cname-8cbab38e5f5c/
- AWS Route 53: https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/dns-configuring.html
- Routing Traffic to an AWS Elastic Beanstalk Environment: https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/routing-to-
beanstalk-environment.html#routing-to-beanstalk-environment-create-alias-procedure

W3 Schools:
- Dropdown: https://www.w3schools.com/tags/att_option_value.asp

Other:
- HTTP Crash Course & Exploration: https://www.youtube.com/watch?v=iYM2zFP3Zn0
- HTTP Request Lifecycle: https://dev.to/dangolant/things-i-brushed-up-on-this-week-the-http-request-lifecycle-
- GSON: https://www.baeldung.com/java-http-request https://github.com/google/gson/blob/master/UserGuide.md
- GSON: https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects

Code Fellows Java curriculum