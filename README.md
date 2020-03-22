# COVID-19 Location Info

Deployed at: ____

This Web-based application utilizes SpringMVC and RESTful web services to provide a user with current 
information on how COVID-19 is impacting their community in the United States.

Upon entering the website, the user will be greeted by a splash page. Here, the user will be provided a search box to enter their country, state, or county. 
The user will then be taken to a results page that renders information from Covid19API regarding the total number of cases of COVID-19 in their country, 
total number of cases in their province/state, and total number of cases in their county. 

The number of cases of COVID-19 will contain numbers for confirmed cases, recovered cases, and death cases. 

Case data for the designated country or state tends to be up to date. County data may lag behind by about 12 days. 

## Run the app
Visit the website at: ___

Or if you'd like to run it from your local machine: 
- Install IntelliJ IDEA. 
- Clone this repository into your local machine through the terminal command 'git clone https://github.com/svakam/covid-19-location.git'. 
- Open IntelliJ IDEA. 
- Choose the 'Import project' option, locate the project folder through the file explorer, and import this project. 
- Wait for the project to build. 
- Run the app with the green Play button, either located at the bottom left of the IDE or top right. 
- Open a browser session, and in the URL text box, type 'localhost:8080' and enter. 

## Change log
03/20/2020
- Initialized app
- Set up Spring Boot, SpringMVC
- Initialized Firebase

03/21/2020
- Initialized Bootstrap
- Set up templates
- Rendering templates through Thymeleaf
- Called Covid19API successfully with HttpURLConnection class

## Data flow
Search bar entry that ideally contains the state or province, but also country, will be used to query 
the /countries endpoint. If successful, it must be decided whether the query was a country or a state/province. If a country was entered, the /summary endpoint will be queried
to obtain the country, countryslug, and case data (confirmed, deaths, recovered). If a state/province was entered, the countryslug will be used to query the /country/
{countryslug}/status/{status} endpoint. The array of states/provinces from the /countries endpoint will be used to further find the desired state/province and iterate through
the endpoint for /country/{countryslug}/status/{status} to get the state/province information. 
/countries: Country, Slug, Provinces[]
/summary: Countries[Country, CountrySlug, NewConfirmed, TotalConfirmed, NewDeaths, TotalDeaths, NewRecovered, TotalRecovered]
/total/country/{countryslug}/status/{status}: Country (same one), Province, Lat/Lon, Date, Cases, Status (same one): relevant data is total # cases per day
/country/{countryslug}/status/{status}: same as above but relevant data is specific to province level
/total/dayone/country/{countryslug}/status/{status}: for specific country only: and since day one of case #1 only
/dayone/country/{countryslug}/status/{status}: same as above but specific to province level

## Project management
Trello: https://trello.com/b/LuJDmF4r/covid-19

## Acknowledgements
COVID-19 APIs and COVID-19 data used/consulted:
- Kyle Redelinghuys, who created the Covid19API: https://covid19api.com/#details
- CDC: https://open.cdc.gov/apis.html
- Johns Hopkins CSSE: https://github.com/CSSEGISandData/COVID-19
- USAFacts: https://usafacts.org/visualizations/coronavirus-covid-19-spread-map/
- State/local health departments

Spring.io documentation:
- REST: https://spring.io/guides/gs/rest-service/
- REST/HTTP: https://spring.io/guides/tutorials/bookmarks/
- REST: https://spring.io/guides/gs/serving-web-content/

Baeldung documentation:
- HTTP Request in Java: https://www.baeldung.com/java-http-request

Bootstrap documentation: 
- Implementation: https://getbootstrap.com/docs/4.4/getting-started/introduction/

Stack Overflow:
- Using Firebase with Spring boot REST application https://stackoverflow.com/questions/39183107/how-to-use-firebase-with-spring-boot-rest-application
- IOException: https://stackoverflow.com/questions/22900477/java-io-exception-stream-closed

Thymeleaf documentation: 
- Layouts: https://www.thymeleaf.org/doc/articles/layouts.html

Other:
- HTTP Crash Course & Exploration: https://www.youtube.com/watch?v=iYM2zFP3Zn0
- HTTP Request Lifecycle: https://dev.to/dangolant/things-i-brushed-up-on-this-week-the-http-request-lifecycle-
- GSON: https://www.baeldung.com/java-http-request https://github.com/google/gson/blob/master/UserGuide.md

Code Fellows Java curriculum