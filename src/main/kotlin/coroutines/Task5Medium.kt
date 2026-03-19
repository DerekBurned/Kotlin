package coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

val keystrokes: Flow<String> = flow {
    emit("k");     delay(50)
    emit("ko");    delay(50)
    emit("kot");   delay(400)  // pause → search should fire
    emit("kotl");  delay(50)
    emit("kotlin"); delay(400) // pause → search should fire
}

var searchCalls = 0
suspend fun search(q: String): List<String> {
    searchCalls++
    delay(200)
    return listOf("Result for $q")
}
fun main() = runBlocking {
    keystrokes
        .debounce(300)
        .filter { it.isNotEmpty() }      // пропускаємо порожні
        .distinctUntilChanged()          // пропускаємо дублікати
        .flatMapLatest { query ->        // скасовує попередній якщо прийшов новий
            flow { emit(search(query)) }
        }
        .collect { println(it) }

    println("search calls: $searchCalls") // 2
}