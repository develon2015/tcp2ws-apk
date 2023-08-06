package app.tcp2ws

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.tcp2ws.ui.AppUI
import app.tcp2ws.ui.theme.RemoteBindTheme
import lib.log

class RmBindActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemoteBindTheme {
                AppUI(
//                    configs = getConfigs(),
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        log.d("复用 Activity 实例")
    }
}