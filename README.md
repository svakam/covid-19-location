# COVID-19 Locator

(work in progress)

Deployed at: http://www.cv19location.com (being updated)

This Web-based application utilizes SpringMVC and RESTful web services to provide a user with current 
information on how COVID-19 is impacting their community in the United States.

Upon entering the website, the user will be greeted by a splash page. Here, the user will be provided a search box to enter their countries, state, or county. 
The user will then be taken to a results page that renders information from Covid19API regarding the total number of cases of COVID-19 in their countries, 
total number of cases in their province/state, and total number of cases in their county. 

The number of cases of COVID-19 will contain numbers for confirmed cases, recovered cases, and death cases. 

NOTE: As of 03/25/2020, for JHU CSSE's _county-level_ information for the United States, there is no data available between and including 03/10/2020 and 03/22/2020. In addition,
there are a number of redundancies in countries names, associated slugs, and state/province names. This spreadsheet outlines the issues being addressed (and hopefully corrected for)
imminently: https://docs.google.com/spreadsheets/d/19x2CUBdHPlxBKUtfgJU2LzVXR_piVtvlo8lgi_DictI/edit?usp=sharing

NOTE: As 0f 03/28/2020, all JHU CSSE data has been well-standardized and more developer-approachable for parsing. 

## Run the app
Visit the website at: http://www.cv19location.com (being updated)

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
- GSON deserializing JSON from Covid19API
- Passing JSON and objects appropriately into template
- Populating countries dropdown menu with countries from API
- Passing dropdown choice to results page and rendering countries's information (countries, slug, provinces)

03/23/2020
- Refactored postmapping to be more RESTful with dropdown choice and /results
- Successfully accessing endpoint with case information for countries
- Rendering confirmed/death/recovered cases of user's choice of countries on /results
- Began Bootstrap work on index - jumbotron implemented and sized

03/24/2020
- Added JHU CSSE dashboard to front page
- Deployed to AWS EB

03/25/2020
- Note: JHU CSSE data contains a number of discrepancies/conflicts on the countries, state, and county level. Results also show in 
Covid19API. Created a spreadsheet that accounts for these issues here: 
https://docs.google.com/spreadsheets/d/19x2CUBdHPlxBKUtfgJU2LzVXR_piVtvlo8lgi_DictI/edit?usp=sharing
- Header nav bar for results page to filter by province if available

03/26/2020
- Refactored countries dropdown methods to filter out redundant countries from data
- Refactored "/" getmapping route functions out to appropriate class

03/27/2020
- Refactored routes to be more RESTful

03/28/2020
- Rough province-level information being populated in dropdown menu
- Country-level data populating on results/country

04/02/2020
- Scrapping Covid19API - data is not being updated regularly to match JHU CSSE updates

04/12/2020
- Created test API to parse test data that simulates JHU CSSE CSV format



## Data Flow
Search bar entry that ideally contains the state or province, but also countries, will be used to query 
the /countries endpoint. If successful, it must be decided whether the query was a countries or a state/province. If a countries was entered, the /summary endpoint will be queried
to obtain the countries, countryslug, and case data (confirmed, deaths, recovered). If a state/province was entered, the countryslug will be used to query the /countries/
{countryslug}/status/{status} endpoint. The array of states/provinces from the /countries endpoint will be used to further find the desired state/province and iterate through
the endpoint for /countries/{countryslug}/status/{status} to get the state/province information. 

## Project management
Trello: https://trello.com/b/LuJDmF4r/covid-19

## Languages Used
- Java
- HTML/CSS

## Tools, Libraries, Frameworks, IDEs, APIs used
- IDE: IntelliJ IDEA
- Spring: SpringMVC, Boot, Thymeleaf
- AWS: Route 53, Elastic Beanstalk
- APIs: Covid19API, Postman

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
- URLEncoder: https://www.baeldung.com/java-url-encoding-decoding


Bootstrap documentation: 
- Implementation: https://getbootstrap.com/docs/4.4/getting-started/introduction/


Stack Overflow:
- Using Firebase with Spring boot REST application https://stackoverflow.com/questions/39183107/how-to-use-firebase-with-spring-boot-rest-application
- IOException: https://stackoverflow.com/questions/22900477/java-io-exception-stream-closed
- GSON import/build issue: https://stackoverflow.com/questions/47566665/cannot-resolve-symbol-gson-and-it-wont-allow-me-to-import/47566770
- Thymeleaf list iteration: https://stackoverflow.com/questions/38367339/thymeleaf-how-to-loop-a-list-by-index
- Ambiguous handler methods: https://stackoverflow.com/questions/35155916/handling-ambiguous-handler-methods-mapped-in-rest-application-with-spring
- URL Encoding: https://stackoverflow.com/questions/10786042/java-url-encoding-of-query-string-parameters
- @PathVariable vs. @RequestParam in Spring: https://stackoverflow.com/questions/30809302/spring-rest-use-of-pathvariable-and-requestparam 
https://stackoverflow.com/questions/24059773/correct-way-to-pass-multiple-values-for-same-parameter-name-in-get-request
- HTTP Content-Type: https://stackoverflow.com/questions/23714383/what-are-all-the-possible-values-for-http-content-type-header


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
- RESTful Practice: http://zetcode.com/springboot/requestparam/ https://www.codebyamir.com/blog/spring-mvc-essentials-requestmapping-pathvariable-annotations 
https://www.baeldung.com/spring-request-param https://apiguide.readthedocs.io/en/latest/build_and_publish/use_RESTful_urls.html
- URL Encoding: https://www.urlencoder.io/learn/ https://docs.oracle.com/javase/7/docs/api/java/net/URLEncoder.html 
https://www.geeksforgeeks.org/java-net-urlencoder-class-java/
- Parsing and type-casting: https://beginnersbook.com/2019/04/java-char-to-int-conversion/


Code Fellows Java curriculum