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
1. `phone`: phone to login  
2. `password`: password to login  
* eg: `{"phone":"12345678901", "password":"111111"}` 
* response parameter:
1. `status`: 0 is successful, 1 is fail. 200
2. `msg`: error text
4. `data`: json object.

### /contact  
* request way: post  
* data structure: json  
* request parameter:  
1. `action`: action is 0 if you want to query contacts, is 1 if you want insert data.  
2. `phone`: phone of login.  
3. `contactName`: needed when action is 1. The name of insert people.  
4. `contactPhone`: needed when action is 1. The phone of insert people.  
* eg: `{"action":"0", "phone":"12345678901"}`, `{"action":"1", "phone":"12345678901", "contactName":"Peter", "contactPhone":"13245678909"}`  

### /login
* request way: post
* data structure: json
* request parameter:
1. `phone`:phone to login
2. `password`: password to login.
* eg: `{"phone":"12345678901", "password":"111111"}` 
* response parameter:
1. `statuscode`: 0 is successful, 1 is fail.
2. `content`: error text




### /protect/matchcode

* request way: post
* data structure: json
* request parameter:  

name|type|description
---|:---:|:---:|
account|String|

* response parameter:  

name|type|description
---|:---:|:---:|
matchcode|String|

```flow
st=>start: Start
i=>inputoutput: 输入年份n
cond1=>condition: n能否被4整除？
cond2=>condition: n能否被100整除？
cond3=>condition: n能否被400整除？
o1=>inputoutput: 输出非闰年
o2=>inputoutput: 输出非闰年
o3=>inputoutput: 输出闰年
o4=>inputoutput: 输出闰年
e=>end
st->i->cond1
cond1(no)->o1->e
cond1(yes)->cond2
cond2(no)->o3->e
cond2(yes)->cond3
cond3(yes)->o2->e
cond3(no)->o4->e
```
--------------------- 
作者：ZK的博客 
来源：CSDN 
原文：https://blog.csdn.net/ww1473345713/article/details/47620577 
版权声明：本文为博主原创文章，转载请附上博文链接！