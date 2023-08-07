package app.tcp2ws

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.core.content.edit
import app.tcp2ws.ui.configs
import app.tcp2ws.ui.showToast
import lib.log

/*
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="version">3.0</string>
    <set name="instances">
        <string>instance1</string>
    </set>
    <set name="servers">
        <string>server1</string>
    </set>
    <set name="instance1">
        <string>server_name:server1</string>
        <string>remote_port:8080</string>
        <string>local_address:127.0.0.1:5555</string>
    </set>
    <set name="server1">
        <string>address:1.1.1.1:1234</string>
        <string>password:test</string>
    </set>
</map>
 */

sealed interface Config {
    var name: String
}

data class Instance(
    override var name: String,
    var ws: String,
    var listen: String,
) : Config {
    companion object {
        fun default() = Instance("service", "ws://wsp.my-api.workers.dev/?ws://", "127.0.0.1:8888")
    }
}

lateinit var sharedPreferences: SharedPreferences

fun getConfigs(): Pair<List<Instance>, Unit> {
    if (sharedPreferences.getString("version", null).apply { log.i("config version: $this") } == null) {
        log.i("config.xml load failed")
        sharedPreferences.edit(commit = true) {
            putString("version", "1.0")
        }
    }
    val instances: List<Instance> = sharedPreferences.getStringSet("instances", setOf()).let { it ->
        log.i(it?.size)
        it?.map { name ->
            log.i("name -> $name")
            val instance = sharedPreferences.getStringSet(name, setOf())
                ?.toList()
                ?: return@map null
            val inst = Instance.default()
            inst.name = name
            instance.forEach {
                val split = it.split(":", limit = 2)
                when (split[0]) {
                    "ws" -> inst.ws = split[1]
                    "listen" -> inst.listen = split[1]
                }
            }
            log.i(inst)
            inst
        } ?: listOf<Instance>()
    }.filterNotNull().sortedBy { it.name }
    return Pair(instances, Unit)
}

/**
 * 添加配置
 * @param isModifySameName 如果 isSameName 为真，则意味着修改配置名未发生变化，不会检测重复
 */
fun addConfig(value: Config, showDialog: MutableState<Boolean>, isModifySameName: Boolean): Boolean {
    if (!isModifySameName && sharedPreferences.getStringSet(value.name, null) != null) {
        showToast("名称不能重复！")
        return false
    }
    when (value) {
        is Instance -> {
            val instances = sharedPreferences.getStringSet("instances", setOf())!!.toMutableSet()
            sharedPreferences.edit(commit = true) {
                putStringSet(value.name, setOf(
                    "ws:${value.ws}",
                    "listen:${value.listen}",
                ))
                putStringSet("instances", instances.apply { this.add(value.name) })
            }
        }
    }
    configs.value = getConfigs()
    showDialog.value = false
    return true
}

inline fun <reified T: Config> rm(name: String): Boolean {
    val type = when (T::class) {
        Instance::class -> "instances"
        else -> return false
    }
    val names = sharedPreferences.getStringSet(type, setOf())!!.toMutableSet()
    sharedPreferences.edit(commit = true) {
        if (names.remove(name)) {
            putStringSet(type, names).remove(name)
        }
    }
    configs.value = getConfigs()
    return true
}

// 运行中的配置名及其id
// 请考虑配置名被修改的情况（运行中不允许修改、删除即可）
val runningList = hashMapOf<String, Instance>()
fun playOrStop(instance: Instance, running: MutableState<Boolean>) {
    if (running.value) {
        runningList[instance.name]?.let { instance ->
            bridge.stop(instance.name, instance.ws, instance.listen)
            runningList.remove(instance.name)
            showToast("停止成功：${instance.name}")
        }
        running.value = false
        instRmBindService?.update()
        return
    }
    val ok = bridge.start(instance.name, instance.ws, instance.listen)
    if (ok) {
        showToast("启动成功：${instance.name}")
        runningList[instance.name] = instance
        running.value = true
        instRmBindService?.update()
    } else {
        showToast("启动失败：${instance.name}")
    }
}

fun isRunning(name: String): Boolean {
    return runningList[name] != null
}