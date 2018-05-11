package fabiorapanelo.com.donation.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by fabio on 16/09/2017.
 */

class CacheManagerKotlin private constructor() {

    private val cachedItems: ConcurrentHashMap<String, CacheObject>

    init {
        cachedItems = ConcurrentHashMap()
    }

    operator fun get(key: String): Any? {
        val cacheObject = cachedItems[key] ?: return null

        if (cacheObject.isExpired) {
            cachedItems.remove(key)
            return null
        }

        return cacheObject.`object`
    }

    fun put(key: String, `object`: Any) {
        this.put(key, `object`, 0)
    }

    fun put(key: String, obj: Any, secondsToLive: Int = 0) {
        val cacheObject = CacheObject(obj, secondsToLive)
        cachedItems[key] = cacheObject
    }

    fun evict(key: String) {
        if (cachedItems.containsKey(key)) {
            cachedItems.remove(key)
        }
    }

    companion object {

        val instance = CacheManagerKotlin()
    }
}
