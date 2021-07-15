package com.scaletech.hsanalytic


public class UpAxisConfig() {

    companion object{
        internal var BASE_URL:String?= null
        internal var AUTH_ID:String?= null
    }
    private constructor(builder: Builder) : this() {
        BASE_URL = builder.baseUrl
        AUTH_ID = builder.authId
    }

    class Builder {
        var baseUrl: String = ""
            private set
        var authId: String = ""
            private set

        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        fun authId(authId: String) = apply { this.authId = authId }
        fun build() = UpAxisConfig(this)

    }

    /*public fun validateConfigs(){
        if (baseUrl.isEmpty()){
            return
        }
    }*/

}