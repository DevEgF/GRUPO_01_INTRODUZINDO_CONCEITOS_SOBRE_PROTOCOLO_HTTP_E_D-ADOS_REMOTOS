import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

// Objetivo: construção de uma casa, trabalhadores operando para contruir uma casa. Subir as paredes e instalar
// as portas, janelas e telhado.

enum class BuildingMaterial(val description: String, val veliveringTimeInMillis: Long) {
    DOORS("PORTAS", 500),
    WINDOWS("JANELAS", 1_000),
    ROOF("TELHADO", 3_000),
}

fun getTimeNow(): String =
    SimpleDateFormat("hh:mm:ss:SSS", Locale.getDefault()).format(Date())

suspend fun order(material: BuildingMaterial): BuildingMaterial {
    println("${getTimeNow()}  >>>>> SOLICITANDO ${material.description}...")
    delay(material.veliveringTimeInMillis)
    println("${getTimeNow()}  >>>>> ${material.description} CHEGARAM!")
    return material
}

suspend fun doWork(task: String) {
    println("${getTimeNow()}  >>>>> EXECUTANDO TAREFA [$task]...")
    delay(1_000)
    println("${getTimeNow()}  >>>>> TAREFA [$task] CONCLUÍDA!")
}

fun main() {
    runBlocking {
        val doors = async { order(BuildingMaterial.DOORS) }
        val roof = async { order(BuildingMaterial.ROOF) }
        val windows = async { order(BuildingMaterial.WINDOWS) }

        doWork("CONSTRUIR PAREDES")
        doWork("CONSTRUIR PAREDES")
        launch { doWork("INSTALAR ${doors.await().description}") }
        launch { doWork("INSTALAR ${roof.await().description}") }.cancel()
        launch { doWork("INSTALAR ${windows.await().description}") }
    }
}