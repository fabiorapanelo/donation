package fabiorapanelo.com.donation.cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import fabiorapanelo.com.donation.repositories.CampaignRepository;
import fabiorapanelo.com.donation.services.CampaignService;

/**
 * Created by fabio on 16/09/2017.
 */

public class CacheManager {

    private static CacheManager instance = new CacheManager();

    private ConcurrentHashMap<String, CacheObject> cachedItems;

    private CacheManager(){
        cachedItems = new ConcurrentHashMap<>();
    }

    public static CacheManager getInstance(){
        return instance;
    }

    public Object get(String key){
        CacheObject cacheObject = cachedItems.get(key);

        if(cacheObject == null){
            return null;
        }

        if(cacheObject.isExpired()){
            cachedItems.remove(key);
            return null;
        }

        return cacheObject.getObject();
    }

    public void put(String key, Object object){
        this.put(key, object, 0);
    }

    public void put(String key, Object object, int secondsToLive){
        CacheObject cacheObject = new CacheObject(object, secondsToLive);
        cachedItems.put(key, cacheObject);
    }

    public void evict(String key){
        if(cachedItems.containsKey(key)){
            cachedItems.remove(key);
        }
    }
}
