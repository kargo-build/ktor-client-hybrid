# ktor-client-native

**ktor-client-native** is a memory-safe, Kotlin/Native-first HTTP client engine compatible with Ktor Client.

Designed to replace the traditional `ktor-client-curl` engine, this project focuses on solving the notorious memory leak and thread-blocking issues by adopting a **Defensive Loop Worker Architecture**. It uses `libcurl` natively (`curl_multi_*`) inside a strictly constrained Coroutine scope with robust C-Interop memory management.

## Goals

-   **Zero Memory Leaks**: Predictable resource lifecycle using strict `StableRef` and `memScoped` cleanup blocks.
-   **No Main-Thread Blocking**: The engine delegates native polling to a background Worker Coroutine, preventing your UI or main event loop from freezing.
-   **Streamlined Data Flow**: Data gets pushed directly into Kotlin `ByteChannel`s on arrival instead of buffering huge blocks in C memory.
-   **High Performance**: Designed for concurrent, high-volume requests without spawning separate processes per request.

---

## 🚀 Usage & Setup

### 1. System Requirements (Linux)

You need the `libcurl` development headers installed on your host to compile the C-Interop bindings.

On Debian/Ubuntu:
```bash
sudo apt-get install libcurl4-openssl-dev
```

### 2. Kargo / Amper Configuration

Update your `module.yaml` to declare the engine dependency and point the build system to the C-Interop wrapper definition included in this library.

```yaml
dependencies:
  - io.ktor:ktor-client-core:3.3.0
  # And other ktor dependencies...

settings:
  native:
    cinterop:
      libcurl:
        defFile: resources/cinterop/libcurl.def
        packageName: libcurl
```

*(Note: The `cinterop` block is supported by the Kargo build system, solving seamless native interactions).*

### 3. Using in Ktor

The engine is fully compatible with Ktor's `HttpClientEngine` API. Integration is seamless:

```kotlin
import build.kargo.ktor.client.native.Native
import io.ktor.client.*

val client = HttpClient(Native) {
    // Configure Ktor as usual
}
```

---

## Status

**Beta**. The engine provides a stable memory-safe foundation for HTTP/1.1 networking on `linuxX64` targets, acting as the primary driver for high-performance Native Kotlin apps on the Kargo toolchain.
