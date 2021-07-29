# UpAxis

## SDK Configuration

UpAxis SDK is usedful for developers to integrate referral tracking events in some simple and straigth forward steps.

### Gradle dependency
 Always use updated library version to get stable and more features.
 Add Updated library version in your module's gradle file.

 implementation "io.github.dinesh-scaletech:UpAxis:0.0.17"

 ### Set Configuration

 We provide some configurable parameters for developer to set before call events.
 We are setting this paramters for application seession. so once it is configured, no need to change at every
 event call.
 We will add configuration function in project's **application** class.(You can also add this function in activity class as well).

```
 UpAxisConfig.Builder()
             .setContext(this) // MANDATORY: Application Context is mandatory
             .baseUrl(baseUrl) // MANDATORY: API call Base url. Default is null. You must need to set up this non empty url.
             .authId(authId) // MANDATORY: Application authorization unique code.
             .build()
```
 ### Events

 UpAxis class where written all events. you need to initialize UpAxis class in your activity or fragment class.



