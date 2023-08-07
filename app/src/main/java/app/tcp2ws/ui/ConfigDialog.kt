package app.tcp2ws.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import app.tcp2ws.Config
import app.tcp2ws.Instance
import app.tcp2ws.addConfig
import app.tcp2ws.rm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigDialog(
    showDialog: MutableState<Boolean>,
    instance: Instance,
    onOk: (value: Config, showDialog: MutableState<Boolean>, isModify: Boolean) -> Boolean = ::addConfig,
    isModify: Boolean = false,
) {
    var name by remember { mutableStateOf(instance.name) }
    // instance
    var ws by remember { mutableStateOf(instance.ws) }
    var listen by remember { mutableStateOf(instance.listen) }
    val textFieldColors = TextFieldDefaults.textFieldColors(
        focusedLabelColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = MaterialTheme.colorScheme.outline,
    )
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.onBackground,
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text("编辑配置", color = MaterialTheme.colorScheme.onPrimary)
        },
        text = {
            Column {
                TextField(
                    singleLine = true,
                    label = {
                        Text("name:")
                    },
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    colors = textFieldColors,
                )
                Spacer(modifier = Modifier.size(6.dp))
                TextField(
                    singleLine = true,
                    label = {
                        Text("ws:")
                    },
                    value = ws,
                    onValueChange = {
                        ws = it
                    },
                    colors = textFieldColors,
                )
                Spacer(modifier = Modifier.size(6.dp))
                TextField(
                    singleLine = true,
                    label = {
                        Text("listen:")
                    },
                    value = listen,
                    onValueChange = {
                        listen = it
                    },
                    colors = textFieldColors,
                )
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = {
                if (onOk(Instance(name, ws, listen), showDialog, isModify && instance.name == name)) {
                    isModify && instance.name != name && rm<Instance>(instance.name)
                }
            }, colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
            )) {
                Text("确定", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = {
                showDialog.value = false
            }, colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
            )) {
                Text("取消", color = MaterialTheme.colorScheme.primary)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true, // 允许通过按下返回键关闭Dialog
            dismissOnClickOutside = false, // 不允许点击外部区域关闭Dialog
            securePolicy = SecureFlagPolicy.SecureOn
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth(),
    )
}