package coroutines

import kotlinx.coroutines.*
import java.io.File

var createdFile: File? = null

suspend fun downloadFile(url: String): File {
    val tmp = File.createTempFile("dl_", ".tmp")
    createdFile = tmp  // save reference
    try {
        for (chunk in 1..10) {
            delay(200)
            tmp.appendText("chunk$chunk\n")
            println("downloaded chunk $chunk")
        }
        return tmp
    } finally {
        tmp.delete()
        println("cleaned up")
    }
}

fun main() = runBlocking {
    val job = launch {
        downloadFile("https://example.com/file")
    }
    delay(650)
    job.cancelAndJoin()

    // Now check the specific file
    println("file exists: ${createdFile?.exists()}")
}