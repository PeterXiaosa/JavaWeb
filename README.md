# JavaWeb
* This is Java Web Project for playApp of Android.

## Input of Server
1. Can receive the parameter in url from client.
2. Can receive the json data from client.
3. Can receive the picture(jpg, png, jpeg) from form-data. (unfinished)
4. Can get data from database. (unfinished)

## Output of Server
1. Response the json to client.
2. Response picture from database to client. (unfinished)


### /register
* request way: post
* data structure: json
* request parameter:  
 `phone`: phone to login  
 `password`: password to login
* eg: {"phone":"12345678901", "password":"111111"}

### /contact
* request way: post
* data structure: json
* request parameter:  
 `action`: action is 0 if you want to query contacts, is 1 if you want insert data.  
 `phone`: phone of login.  
 `contactName`: needed when action is 1. The name of insert people.
 `contactPhone`: needed when action is 1. The phone of insert people.
* eg: {"action":"0", "phone":"12345678901"}  
{"action":"1", "phone":"12345678901", "contactName":"Peter", "contactPhone":"13245678909"}
