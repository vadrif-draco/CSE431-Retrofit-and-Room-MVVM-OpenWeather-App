# Table of Contents

- [Code Structure](#code-structure)
  - [APICallbacks](#apicallbacks)
  - [DAOAsyncTasks](#daoasynctasks)
  - [JSONAPIs](#jsonapis)
  - [POJOs](#pojos)
    - [Converters](#converters)
    - [ForRetrofit](#forretrofit)
    - [ForRoomDB](#forroomdb)
  - [Constants](#constants)
  - [MainActivity](#mainactivity)
  - [MyDataAccessObject](#mydataaccessobject)
  - [MyRepository](#myrepository)
  - [MyRoomDatabase](#myroomdatabase)
  - [MyViewModel](#myviewmodel)
  - [WeatherCardListAdapter](#weathercardlistadapter)
- [Chain of data retrieval and display](#chain-of-data-retrieval-and-display)
- [Permissions](#permissions)
- [Other](#other)
  - [AndroidManifest.xml](#androidmanifestxml)
  - [build.gradle (Module)](#buildgradle-module)
    - [localProperties / buildConfig](#localproperties--buildconfig)
    - [buildFeatures](#buildfeatures)
    - [dependencies](#dependencies)
  - [local.properties (SDK Location)](#localproperties-sdk-location)
- [Known bugs](#known-bugs)
- [Videos](#videos)


# Code Structure

Sorted in hierarchic alphabetical order



## APICallbacks

This package contains classes used for callbacks to API queries



### CallbackResponseFunction

This interface is used in callback function classes for augmenting them with a custom response provided by the invoker of the callback

The CallbackResponseFunction parameter in the callback classes is nullable, therefore, pass null if no response handling is required/implemented



### GeocodingDataInsertCallback

This class is used as the callback for fetching geocoding data from the OpeanWeather geocoding API to insert them into the geocoding data Room database such that they can be queried against upon recurring additions of the same city in addition to persistence



### WeatherDataInsertCallback

This class is used as the callback for fetching weather data from the OpenWeather current weather API to insert them into the weather data Room database such that they can be persisted in the app

Geocoding data is one-to-one mapped with its respective weather data



### WeatherDataUpdateCallback

This class is used as the callback for updating weather data on-demand by contacting the API for updates - if any - and persisting these updates in the room database; insertion (update) is only done if the retrieved data is newer than the current data



## DAOAsyncTasks

This package contains the AsyncTask's used by the DAO for the MVVM architecture



### GWAsyncTaskParams

This data class is used to aggreggate the parameters required for insertion of Geocode and Weather data (the "GW") in its respective AsyncTask



### MyDataDeleteAsyncTask

This class implements the AsyncTask for deletion of a weather data object from the Room database, and it is to be noted that foreign keys are set in place to propagate such a deletion into the other tables in the database - more on that later



### MyDataInsertAsyncTask

This class implements the AsyncTask for insertion of a geocode-weather data pair provided as a GWAyncTaskParams object to be parsed and inserted into the Room database into the respective tables



## JSONAPIs

This package contains the JSON API interfaces used by Retrofit for sending requests to the different APIs used by this application



### GeocodingDataAPI

This is the interface for contacting the OpenWeather's Geocoding API to get a city's geocoding data, for more information check: https://openweathermap.org/api/geocoding-api

I have opted for using the first city that is returned in response to the user query as I do not intend to handle multiple city matches



### WeatherDataAPI

This is the interface for contacting OpenWeather's current weather API to get a city's weather using its geocoding data (namely latitude and longitude), for more information check: https://openweathermap.org/current



## POJOs

This package contains all the POJOs (plain-old-java-objects) used by the Retrofit and Room libraries as well as utility classes



### Converters

This package contains utility converter classes to convert back and forth between Retrofit and Room POJOs



#### GeocodingDataPOJOConverter

This class has static methods to convert GeocodingData from-to Room and Retrofit POJOs



#### WeatherDataPOJOConverter

This class has static methods to convert WeatherData from-to Room and Retrofit POJOs



### ForRetrofit

This package contains the POJOs specific to the Retrofit library



#### GeocodingDataPOJO4RF

The Retrofit POJO class for Geocoding data received from the Geocoding API



#### WeatherDataPOJO4RF

The Retrofit POJO class for Weather data received from the Current Weather API



### ForRoomDB

This package contains the POJOs specific to the Room library



#### GeocodingDataPOJO4RDB

The Room database POJO class for Geocoding data to be inserted into the "geocoding" table in the sqlite database by Room with one-to-one mapping with the associated auxiliary weather data in the "weather_aux" table



#### WeatherDataAggregatePOJO4RDB

The Room database POJO class for aggreggated Weather data to define the one-to-many relationship between auxiliary weather data and the different weather instances (check the Current Weather API documentation linked earlier for information on what this is)



#### WeatherDataAuxPOJO4RDB

The Room database POJO class for auxiliary weather data (unique per city) to be inserted into the "weather_aux" table in the sqlite database by Room with one-to-one mapping with the associated geocoding data in the "geocoding" table and one-to-many mapping to the child weather instance items from the "weather_instances" table as defined in the relationship in the above WeatherDataAggregatePOJO4RDB class



#### WeatherDataWeatherInstancePOJO4RDB

The Room database POJO class for weather instances in weather data (there can be multiple weather instances for one city's weather data retrieved from the API, and weather instances are not unique per weather data either) to be inserted into the "weather_instances" table in the sqlite database by Room with many-to-one mapping with the parent auxiliary weather data object from the "weather_aux" table as defined in the relationship in the WeatherDataAggregatePOJO4RDB class



## Constants

This is a simple class defining constants used in the application

It is to be noted that the API key used is not defined in this class, it is only retrieved from BuildConfig which is where it is secretly defined for security purposes upon uploading to GitHub



## MainActivity

The main activity class for the application in which view binding is used in the creation and configuration of

- The main screen layout of a prompt or recycler view followed by the city search UI

- The recycler view that contains weather cards for each city's geocoding and weather data

- The view model through which the recycler view is continuously populated with up-to-date LiveData

- An item touch helper to implement swipe functionalities in the recycler view (swipe left to request update*, right to remove)

- Implementation of search logic for the click listener of the search button

- All other design details and conditions and view responses to events, etc.



###### * Update requests have an internal threshold defined in the constants class within which an update request will not contact the API** and will keep the latest cached data as the up-to-date version

###### ** Even upon contact with API, if the retrieved data is not newer than the latest cached data, the database will not be updated



## MyDataAccessObject

The main DAO class in the MVVM architecture for accessing data in the sqlite database, defining the following queries and transactions

- `insertGeocodingData`; to insert geocoding data

- `insertWeatherDataAux`; to insert auxiliary weather data

- `insertWeatherDataInstance`; to insert weather instances in weather data

- `insertGeocodingAndWeatherData`; to insert geocoding data as well as its associated weather data (auxiliary and instances)

- `getAllWeatherData`; to retrieve all weather data entries (auxiliary data and its associated instances for each entry using the aggreggate WeatherDataAggregatePOJO4RDB)

- `getWeatherGeocodingDataByWeatherAuxId`; to retrieve geocoding data for a certain city from the geocoding database through its auxiliary weather data identifier

- `getGeocodingDataByCityCountry`; to retrieve geocoding data for a certain city from the geocoding database by its city and country names

- `deleteWeatherData`; to delete weather data (deletes the auxiliary data and its associated instances)



## MyRepository

The repository class in the MVVM architecture implemented as a Singleton for retrieving data from API or database through the following methods using MyDataAccessObject, the JSONAPIs, the Callbacks, and the AsyncTasks from earlier

- `insertGeocodingAndWeatherData`; invokes the AsyncTask associated with this database insertion operation

- `getWeatherGeocodingDataByWeatherAuxId`; invokes the DAO method associated with this database query

- `getGeocodingDataByCityCountry`; invokes the DAO method associated with this database query

- `deleteWeatherData`; invokes the AsyncTask associated with this database deletion operation

- `searchForAndInsertCity`; invokes an API call using the geocoding JSON API interface mentioned earlier and enqueues the callback for geocoding data search/retrieval and insertion into database upon retrieval from API

- `getAndInsertWeatherByCityGeocode`; invokes an API call using the weather data JSON API interface mentioned earlier and enqueues the callback for weather data retrieval and insertion into database upon retrieval from API

- `updateWeather`; invokes an API call using the weather data JSON API interface mentioned earlier and enqueues the callback for weather data update in database upon retrieval from API



## MyRoomDatabase

The Room database abstraction class in the MVVM architecture implemented as a Singleton provoding the DAO object used to query the database



## MyViewModel

The ViewModel in the MVVM architecture which uses the repository to update its LiveData instance of aggreggated weather data which is used to populate the view in the MainActivity as explained earlier



## WeatherCardListAdapter

The adapter class for the recycler view containing the logic and design of weather cards that populate the recycler view



# Chain of data retrieval and display

-- MainActivity (RecyclerView with WeatherCardListAdapter, City Search UI)

-> MyViewModel (LiveData)

-> MyRepository

-> Retrofit (JSONAPIs, APICallbacks) / Room (MyRoomDatabase -> MyDataAccessObject, DAOAsyncTasks) *

-> Retrofit Library / Room Library

-> Android's sqlite database for the application or the API



###### * Both using their respective POJOs for the data representation



# Permissions



## android.permission.INTERNET

For contacting Geocoding and Current Weather APIs from OpenWeather



# Other



## AndroidManifest.xml



### android:windowSoftInputMode="adjustResize"

Need it so that layout maintains its shape when soft keyboard appears for input



## build.gradle (Module)



### localProperties / buildConfig

Need to use localProperties for secure storage of API KEY in the build configuration (which is not uploaded on GitHub)



### buildFeatures

Need to enable the viewBinding feature



### dependencies



#### MVVM dependencies

Please refer to the SettingAndroidxDependencies.rtf file uploaded on this repository, it was written by our Dr. for the course in which I've made this task



#### Retrofit

Used for calling the OpenWeather APIs and implementing their callbacks, GSON to convert retrieved JSON to POJOs

`com.squareup.retrofit2:converter-gson:2.4.0`

`com.squareup.retrofit2:retrofit:2.4.0`



#### Picasso

Used for loading and caching weather image icons from OpenWeather

`com.squareup.picasso:picasso:2.8`



#### nv-i18n

Used for conversion from and to ISO country codes (ISO 3166-1, etc.) required for the Geocoding API country input

`com.neovisionaries:nv-i18n:1.28`



#### Other dependencies

Other dependencies were automatically added by Android Studio, such as the JUnit framework that come with the basic project example, and the non-basic UI components such as RecyclerView, CardView, etc...



## local.properties (SDK Location)

This is where the API KEY is stored and referenced from localProperties



# Known bugs



There is currently a bug where a city may get disassociated with its respective weather data making its update operation fail, but I can't find a concrete set of steps to reproduce it yet



# Videos


https://user-images.githubusercontent.com/55457021/205380010-24e80586-87b4-4e65-a258-01e7bf895b92.mp4

This video shows removal of all added cities then adding four new ones


https://user-images.githubusercontent.com/55457021/205379663-29f56409-7ef3-4b50-80c1-62d7216ece9e.mp4

This video shows how internal update interval is enforced vs. when no new data is available at the API end even if the update interval has passed


https://user-images.githubusercontent.com/55457021/205379744-b4209ef3-b7a9-4d47-b586-b9f3127c34e6.mp4

This video shows a normal update operation with internal update interval passed and API has new data


https://user-images.githubusercontent.com/55457021/205380098-65b2a2f1-2e99-4872-ae23-9c2379f730b3.mp4

This video shows the handling of attempt to add already-added cities


https://user-images.githubusercontent.com/55457021/205380119-820ed2ef-6408-41a4-bece-5e2a913cae6b.mp4

This video shows normal update operations on all existing cities

