# KAMAR Notices Interface

Kotlin Edition

KNI (KAMAR Notices Interface) is a project designed to bring a way of accessing notices from
the [KAMAR](https://kamar.nz) portal software. KNIs goal is to produce usable libraries in as many languages as possible

This currently only has support for Kotlin JVM, However if you wish to add support for native or JS you can do so by
creating a custom implantation using KNIImpl

### Retrieving Notices

```kotlin
val kni = KNI("demo.school.kiwi");
val noticeObject: Notices = kni.retrieve("01/01/2020")
print(noticeObject)
```

By Jacobtread