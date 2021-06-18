package jp.microvent.microvent.service.model

class Test(val name : String){
    companion object Factory{
        fun from(name:String):Test{
            return Test(name)
        }
    }

    var test = "example"
}