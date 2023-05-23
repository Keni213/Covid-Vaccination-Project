<h2>Data Structure and Software Design </h2>
<ul>

<li>•	What is our customized method </li>
Our customized feature is to interrogate the full vaccinations per capita for the area with the highest market value per capita for the date that the user inputs. It will return a String with the ZIP Code of the area concatenated by the full vaccinations per capita for the area.

<li>•	How the method works</li>
The function will take a date from the user and check its format, then pass it into the function as an argument. The function will use the input date and the underlying covid record, properties, and population data to perform the inquiry by following steps.
<ul>
<li>
o	Use the function described in 3.3 to collect the full vaccinations per capita on the inquired date for all the available areas. The data are then stored in a map with ZIPCode as its key and full vaccinations per capita as its value. </li>
<li>
o	Iterate over the collected ZIPCode in the map and use the method described in 3.5 to interrogate the associated market value per capita to find the area that has the highest market value. </li>
<li>
o	Return the ZIP Code and the full vaccinations per capita in a string format to the UI and print it to the console.</li>
</ul>

•	Why the method works
In this customized function, we implemented the required methods that are defined in the assignment to follow the principle of code reuse. We choose to interrogate the full vaccinations per capita first to avoid the situation where the area with the highest market value per capita does not have an available covid record for the full vaccinations per capita on that date. By interrogating the vaccination data first, we secure that all ZIP Code entries we used for pulling the market value data will have an associated record of full vaccinations per capita. The function will return a message if the vaccination or market value data is not available for the specified date and ZIP Code. 
</ul>

<h3>Data Structure</h3>
•	LinkedList
The linked list is used in the data management section of the project to store data read from the csv/json file. The linked list is an implementation of List interface in Java. It maintains the sequence of the elements stored in the data structure and allows for duplicated elements. An alternative is to use Array List which has similar features to the Linked List. The reason we chose Linked List is that the size of the data structure can be determined dynamically based on the size of the data. In the contrast, Array List has a predefined capacity and will have to resize its underlying array when the size of the data changes which results in less efficiency in using the memory space. However, this is debatable because each element in the Linked List will have a certain overhead space to store the pointer to the next element. But for simplicity, we consider LinkedList is more space efficient than Array List in our case.
•	HashMap
The HashMap is used for Storing data for population and  Memoiztion in the processor section. The population is ready to be searched by zip code by storing in HashMap. Also, HashMap can be used for Memorization to improve software efficiency. In this method, input and output for the function can be stored as keys and values. When providing the same input, it is easy to get a result from the HashMap without running the function again. In these two parts, ordering is unnecessary, HashMap is the best choice to store the data in (Key, Value) pairs, and we can access the value by searching the key.
•	Tree Map
TreeMap proves to be an efficient way of sorting and storing the key-value pairs, which is used in the processor section of the project. The calculation of vaccination rate for each area required the information for total vaccination number and total population, storing the data into a map helps to searching both population and total number in one specific zip code area. Also, the information should display in ascending numerical order, the features of TreeMap help to display the result as requested.




