package app.tcp2ws

class Bridge {
    companion object {
        init {
            System.loadLibrary("native")
        }
    }

    external fun test()
//    external fun start(server: String, port: Short, password: String, localService: String): String
//    external fun stop(handler: String)
}

val bridge = Bridge()