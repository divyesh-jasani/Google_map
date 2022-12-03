# 🗺**Google Map with Room Database and Route Detection**

*  Add/Update/Delete - Implement the functionality of add/update/delete location sources, Functionality should store all locations in persistence storage (Use local storage database).

*  Search - To add the location source to give functionality for search, Search will show suggestions based on input.

*  Sorting - Functionality of sorting locations (ascending and descending both) on the listing page of locations  

*  Preview - Show the location marker on the path of all the added locations and draw a path connecting each location, Make sure it is a relevant road path and not a bird-eye view. 

Screen Wise Flow
Screen 1: List of locations with buttons for adding source location and showing the path. From the list, the user can edit or delete the source location.

Screen 2:  On Tap of add source location -  With the help of the search option all locations will be added. 
On typing, it should show me suggestions based on my input value. (You can use google place API for searching, the Place API response has the coordinates for that source which will help in sorting.)

Screen 3: Map screen - This will open when the user clicks on the show path from the listing page. 
The map screen should show markers on the map for all the location sources and draw a path connecting all the sources.


Google Place API Enabled Key = *****************************************************
For reference : https://developers.google.com/maps/documentation/places/android-sdk/get-api-key

# ScreenShots

![ss1](https://user-images.githubusercontent.com/91209858/205440851-39414ee3-8ce2-4d67-bf8e-793e72a5a7af.jpg)
