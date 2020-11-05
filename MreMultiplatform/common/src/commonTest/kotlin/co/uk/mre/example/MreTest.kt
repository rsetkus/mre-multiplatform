package co.uk.mre.example

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.tests.utils.*
import io.ktor.http.*
import io.ktor.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalAPI
class MreTest {

    @Test
    fun testSample() = testWithEngine(MockEngine) {
        config {
            engine {
                addHandler { request ->
                    when (request.url.toString()) {
                        "http://localhost/hello" -> respond("Hello world", HttpStatusCode.OK)
                        else -> error("Unhandled ${request.url.fullPath}")
                    }
                }
            }
        }

        test {
            val response = it.request<String>("http://localhost/hello")
            assertEquals("Hello world", response)
        }
    }
}