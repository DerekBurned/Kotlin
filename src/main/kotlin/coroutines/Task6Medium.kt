package coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

data class Task(val id: Int)

suspend fun process(task: Task) {
    delay(100)
    println("done: ${task.id}")
}

suspend fun runPool(
    tasks: List<Task>,
    workerCount: Int
): Unit = coroutineScope {
    val channel = produce {
        for (task in tasks) send(task)
    }
    repeat(workerCount) { workerId ->
    launch {
        for (task in channel) {
            println("Worker $workerId processes task $task")
            delay(10)
        }
    }
}
}


fun main() = runBlocking {
    val time = System.currentTimeMillis()
    runPool(List(100000) { Task(it) }, workerCount = 100)
    println("took: ${System.currentTimeMillis() - time}ms")
    // should print ~500ms, not ~2000ms
}