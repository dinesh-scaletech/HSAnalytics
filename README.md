# UpAxis

## SDK Configuration

UpAxis SDK is useful for developers to integrate referral tracking events in some simple and straight forward steps.

### Gradle dependency

Always use an updated library version to get stable and more features. Add the Updated library version in your module's Gradle file.

```
implementation "io.github.dinesh-scaletech:UpAxis:1.0.4"
```

### Set Configuration

We provide some configurable parameters for developers to set before call events. We are setting this parameter for the application session. so once it is configured, no need to change it at every event call. We will add a configuration function in
the project's **application** class. (You can also add this function in activity class as well).

Kotlin:

```
UpAxisConfig.Builder()
    .setContext(this) // MANDATORY: Application Context is mandatory
    .baseUrl(baseUrl) // MANDATORY: API call Base url. Default is null. You must need to set up this non empty url.
    .authId(authId) // MANDATORY: Application authorization unique code.
    .setAllowDuplicate(true) // Optional : Use only for development purpose.
    .setTrackInterval(interval) // Optional: Set user tracking interval in milliseconds. it must be greater or equal to 5 minutes (300000 milliseconds).
    .setTrackUser(trackUser) // Optional: Enable to track the user. default is false.
    .setTrackEventName(eventName) //Optional: This parameter related with trackUser flag. if trackUser is false, value of this parameter will be ignored. default event name is "session"
    .build()
```

Java:

```
new UpAxisConfig.Builder()
     .setContext(getBaseContext()) // MANDATORY: Application Context is mandatory
     .baseUrl(baseUrl) // MANDATORY: API call Base url. Default is null. You must need to set up this non empty url.
     .authId(authId) // MANDATORY: Application authorization unique code.
     .setAllowDuplicate(true) // Optional : Use only for development purpose.
     .setTrackInterval(interval) // Optional: Set user tracking interval in milliseconds. it must be greater or equal to 5 minutes.
     .setTrackUser(trackUser) // Optional: Enable to track the user. default is false.
     .setTrackEventName(eventName) //Optional: This parameter related with trackUser flag. if trackUser is false, value of this parameter will be ignored. default event name is "session"
     .build();
```

### Initialize main accessible class variable

Now in your activity or fragment class initialize the UpAxis variable and call the event with variable.

Snapshot:

Declare class or local variable of the upAxis class

Kotlin:

```
calss A : AppCompatActivity(){

private var upAxis: UpAxis? = null

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //.....
        upAxis = UpAxis(this)
        // ....


  }
```

Java:

```
class Class A extends AppCompactActicity{
   private UpAxis upAxis;

    @Override
       protected void onCreate(final Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);

           //....
           upAxis = new UpAxis(this);
           //....
       }

}
```

### List of Events

1. ***postInstallEvent***
   This is an independent method will be called from SDK itself. this method can not be called from outside of this class for 1.0.0 SDK version.

2. ***postEvent***
    - Single function to post all kinds of data to the server.

   List of parameters:
    - eventId: Optional, Specifies what action the user performed.
    - receive: Optional, Monitoring value that should be assigned to this conversion.
    - queue: Optional, Either 0 (false) or 1(true), if 1 post-backs will be queued and not handled in real-time. default is 0
    - extraData: Optional, Any additional parameter can pass here in JSONObject.
    - upAxisCallBack: Optional, This is call back function will developer to track issues or to solve validation related issues.

3. ***TrackUser***
   - Method is restricted and will be called from SDK itself. If UserTracking is enabled then only this method will be executed in provided
     Time interval. default time interval is 5 minutes.


Call Function code, Kotlin:
   
```
   upAxis?.postEvent(eventId = "1", upAxisCallBack = object : UpAxisCallBack<Void> {
        override fun noNetworkAvailable() {
            // when internet connetion not available
        }

        override fun onFailure(t: Throwable?) {
        // API call fail response with message
        }

        override fun onResponse(upAxisResponse: UpAxisResponse) {
           // API call Success.
        }

        override fun validationError(message: String) {
            // User Input validation error. you will get this error if Invalid BaseUrl, App Auth id or referral code.
        }

    })
```

When Call delegation is not required in the app,

```
   upAxis?.postEvent(eventId = "1")
```
    Java:

```
upAxis.postEvent("1",null,null,false, new JSONObject(), new UpAxisCallBack<Void>() {
       @Override
       public void validationError(@NotNull String s) {
           // when internet connetion not available
       }

       @Override
       public void onResponse(@NotNull UpAxisResponse upAxisResponse) {
           // API call fail response with message
       }

       @Override
       public void onFailure(@Nullable Throwable throwable) {
           // API call Success.
       }

       @Override
       public void noNetworkAvailable() {
            // User Input validation error. you will get this error if Invalid BaseUrl, App Auth id or referral code.
       }
   });
```

When Call delegation is not required in the app, Pass null in the last input parameter.

   ```
   upAxis.postEvent("1" ,null,null,false, new JSONObject(),null);
   ```

Send Extra Parameters in the event:

If you want to pass some extra parameters in events then you can add multiple data in JSONObject.

For example:

Kotlin:

```
   val extraData = JSONObjext()
   extraData.put("test", "123")
```

Java:

```
   JSONObject extraData = new JSONObject();
   extraData.put("test","123");
```

