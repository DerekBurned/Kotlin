package coroutines

import kotlinx.coroutines.*
import kotlin.time.measureTime

suspend fun loadUser(): String { delay(1000); return "Alice" }
suspend fun loadScore(): Int   { delay(1000); return 42 }
suspend fun loadBadge(): String { delay(1000); return "Gold" }

suspend fun loadProfile(): Triple<String, Int, String> = coroutineScope {
    val userFetch = async {loadUser()}
    val scoreFetch = async {loadScore()}
    val badgeFetch = async {loadBadge()}
    Triple(userFetch.await(), scoreFetch.await(), badgeFetch.await())
}

fun main() = runBlocking {
    val time = measureTime {
        val result = loadProfile()
        println(result)
    }
    println("Took: ${time.inWholeMilliseconds}ms")
    // Should print ~1000ms, not ~3000ms
}