package coroutines

import kotlinx.coroutines.*
import java.io.IOException

suspend fun <T> retryWithBackoff(
    times: Int = 3,
    initialDelay: Long = 100,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            if (attempt == times-1 ){
                throw e
            }
        }
        delay(currentDelay)
        currentDelay *= 2       // як змінюється затримка?
    }
    error("unreachable")          // компілятор не знає що repeat вичерпався
}

fun main() = runBlocking {
    var attempt = 0
    val result = retryWithBackoff(times = 3) {
        attempt++
        if (attempt < 3) throw IOException("not yet")
        "success on attempt $attempt"
    }
    println(result) // "success on attempt 3"
}