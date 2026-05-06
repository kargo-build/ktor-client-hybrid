package builders.kargo.ktor.client.hybrid

import co.touchlab.kermit.Logger
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers

/**
 * Windows Native implementation of HybridEngine.
 * 
 * Uses CIO for all requests (both HTTP and HTTPS work fine on Windows).
 */
@OptIn(InternalAPI::class)
actual class HybridEngine actual constructor(
    actual override val config: HybridEngineConfig
) : HttpClientEngineBase("ktor-hybrid-windows") {

    private val cioEngine: HttpClientEngine by lazy {
        Logger.i { "Initializing CIO engine for all requests (Windows Native - HTTPS supported)" }
        CIO.create()
    }

    actual override val dispatcher = Dispatchers.Default
    actual override val supportedCapabilities: Set<HttpClientEngineCapability<*>> = emptySet()

    actual override suspend fun execute(data: HttpRequestData): HttpResponseData {
        Logger.d { "Using CIO engine for ${data.url} (Windows Native)" }
        return cioEngine.execute(data)
    }

    actual override fun close() {
        Logger.i { "Closing hybrid engine (Windows Native)" }
        cioEngine.close()
        super.close()
    }
}
