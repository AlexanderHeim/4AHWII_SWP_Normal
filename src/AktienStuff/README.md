# AktienStuff
"AktienStuff" is a Java Project by me. All it does is fetch some stock data from [Alphavantage](https://www.alphavantage.co/ "Alphavantage"), process it and save it in a mysql database. It offers a version with a user interface (GUI) or a "--nogui" version, which basically updates the data for specific stocks and creates some pngs containing a graph.

## How to setup
- Make sure you use an updated Java Runtime
- Compile the Project as a jar.
- Create a "Codes.txt" file in the jar directory. Write the codes of the stocks you want into it (One per line).
- Create a "AlphavantageKey.txt" file in the jar directory. Write your key into it.
- Run the jar. (If you want to run it as the non gui version, make sure you pass "--nogui" as an argument. So in cmd you would do: "java -jar AktienStuff.jar --nogui")

### How to compile
You need the following java libraries:
- commons-io-2.8.0
- jfreechart
- json-20201115
- mysql-connector-java-8.0.23

*Note: JFreeChart doesn't offer a compiled jar to download. So you need to compile it yourself. Which is really simple.*
