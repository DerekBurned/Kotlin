package coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

suspend fun slowApi(): String {
    delay(2000)
    return "fresh data"
}

suspend fun fetchWithFallback(): String {
    // your solution here
    return withTimeoutOrNull(500L){
        slowApi()
    }?: "cached result"
}

suspend fun main() {
    val result = fetchWithFallback()
    println(result)
    // Should print "cached result", not "fresh data"
}