package com.upaxis.model

class UpAxisResponse {
    var responseCode:Int?= 0
    var body:Any? = null
    var isSuccessful:Boolean? = false
    var errorBody:String?= null
    var message:String? = null
}