package internal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CompletableDeferred

class OAuthServer {
    private val deferred = CompletableDeferred<String>()
    private lateinit var server: ApplicationEngine

    fun start(port: Int = 8888) {
        server = embeddedServer(Netty, port) {
            routing {
                get("/callback") {
                    val code = call.request.queryParameters["code"]
                    if (code != null) {
                        call.respondText(
                            "<html><body><h1>Authentication successfully completed! You can close this window.</h1></body></html>",
                            ContentType.Text.Html
                        )
                        deferred.complete(code)
                    } else {
                        call.respondText(
                            "<html><body><h1>Authentication failed or was canceled by the user.</h1></body></html>",
                            ContentType.Text.Html
                        )
                        deferred.completeExceptionally(Exception("No code received"))
                    }
                }
            }
        }.start(wait = false)
    }

    suspend fun waitForCode(): String {
        return deferred.await()
    }

    fun stop() {
        if (::server.isInitialized) {
            server.stop(1000, 2000)
        }
    }
}