package app.tcp2ws

class Bridge {
    companion object {
        init {
            System.loadLibrary("native")
        }
    }

    external fun test()
    external fun start(name: String, ws: String, listen: String): Boolean
    external fun stop(name: String, ws: String, listen: String): Boolean
}

val bridge = Bridge()