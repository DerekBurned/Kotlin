package coroutines
import kotlinx.coroutines.*

 fun main() =  runBlocking {
    val job = launch(  Dispatchers.Default) {
        var count = 0L
        while (isActive) {
            count++
        }
    }
    delay(100)
    job.cancelAndJoin()
    println("stopped") // this line is never reached — fix that
}