# MapViewer

https://user-images.githubusercontent.com/6456871/183304440-c63ee331-31c4-4dae-a397-0961f0bfba39.mp4

## Overview

* This application is used to display a list of maps that are created and stored in ArcGIS Online.
* User can choose on a map in the map Gallery and will display the map in detail.
* The map will show line, point and polygon features. 
* Any featutre can be identified by selecting it. But for line it will also draw a profile by querying the elevation values using REST API call.
* Then there is a option to add a geofence reminder at any feature. Once added it is stored in a local database and a buffer of 1 mile is drawn around the point 
* You can also delete a reminder from the list.

Instructions:
  This application uses [ArcGIS Runtime API for Android](https://developers.arcgis.com/android/get-started/) to display webmaps and incorporates capabilities like identify and display feature details, drawing elevation profile of any line segment and also to draw a   buffer around the feature which is added for geofence reminder. Geofencing API from Google is used to create the reminders.
  1. Create a START page that uses the motionLayout
  2. MapGallery - Create a screen that displays the list of webmaps available for the account in [ArcGISOnline](https://www.arcgis.com/home/index.html) by using a groupID. Glide is used to show the thumbnails of the webmaps. 
  3. MapPage - Selecting any map in the MapGallery will navigate to the desired map page. 
  4. Selecting any feature will display the details in a bottom sheet. if it is line feature then there will be 2 tabs one for details and other for the elevation profile. The data for the elevation profile is fetched using the Retrofit and Moshi library.
  5. Then user can save the location of any feature as a geofence reminder. The geofence is created using [Google geofencing API](https://developers.google.com/location-context/geofencing) The form to enter the details for geofence will be shown as a Dialog fragment and data will be saved in a local database using Room library. To be able to create a geofence reminder the user need to give access to the Location Permission.

  

![image](https://user-images.githubusercontent.com/6456871/178089737-a06dd12f-01d4-42c2-ba1e-8a229d749e1b.png)

Click Next

![image](https://user-images.githubusercontent.com/6456871/178089744-78871245-b363-409c-a300-4f103ee04dd7.png)

Choose a map e.g. the Tahoe one by clicking on the title


click on any feature on the map. The feature should get selected. The recyclerview should also get populated. But the issue is it is not




![image](https://user-images.githubusercontent.com/6456871/179329758-5f57c1ef-ca34-4dcc-b560-bb6aaaa3600e.png)

![image](https://user-images.githubusercontent.com/6456871/179329777-2884cb3c-fa36-4029-8166-a013feb09d21.png)
![Screenshot 2022-08-07 114326](https://user-images.githubusercontent.com/6456871/183306276-54dc4941-0895-44ef-92ee-8328f25e407c.png)
![image](https://user-images.githubusercontent.com/6456871/179329793-4fc9acaa-39c2-4874-811f-4069589ee3e9.png)

![Screenshot 2022-08-07 113916](https://user-images.githubusercontent.com/6456871/183306113-8292719c-35d9-4797-9d55-ef338dac742b.png)
![Screenshot 2022-08-07 113947](https://user-images.githubusercontent.com/6456871/183306121-f63b306a-5569-4aca-a580-17ab3ffde0a7.png)


