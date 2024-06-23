package com.example.desensitise.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可对单个Key进行过期的ConcurrentHashMap
 * 具体存储的逻辑委派给内部的ConcurrentHashMap
 * @since 1.0
 */
public class ExpireTimeMap{
	
	private static final Logger logger = LoggerFactory.getLogger(ExpireTimeMap.class);
	
	/**
     * 具体存储缓存数据的容器
     */
    private static Map<String,Object> map = new ConcurrentHashMap<String,Object>();
    
    /**
     * 过期时间记录
     */
    private static Map<String, Long> expireRecord = new ConcurrentHashMap<String,Long>();
    
    /**
     * 清理过期缓存线程是否在运行
     */
    private static volatile boolean ifCleanThreadRunning = false;
    
    /**
     * 默认的过期时间,5分钟
     */
    private static final long DEFAULT_EXPIRE_TIME = 1000 * 60 * 2;
    
    /**
     * 根据Key，获取字符串类型的值
     * @param key
     * @return
     */
    public static String get(String key) {
    	return get(key, String.class);
    }
    
    /**
     * 根据Key，获取传入参数类型的值
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T  get(String key, Class<T> clazz) {
    	
    	Long expireTime = expireRecord.get(key);
    	
    	// 未记录过期时间就返回map.getKey
        if(expireTime == null){
            return (T)map.get(key);
        }
        
        // 命中缓存后 返回缓存数据
        if (System.currentTimeMillis() <= expireTime){
            //System.out.printf("key:%s,命中缓存",key);
            return (T)map.get(key);
            
        }else {
        	//数据过期移除数据存储和过期记录存储
            expireRecord.remove(key);
            map.remove(key);
            return null;
            
        }
        
    }
    
    /**
     * 默认的过期时间
     */
    public static void put(String cacheKey, Object cacheValue) {
    	
    	//设置key的过期时间
        put(cacheKey, cacheValue, DEFAULT_EXPIRE_TIME ,TimeUnit.MILLISECONDS);
        
    }
    
	/**
	 * 设置缓存的过期时间
	 */
    public static void put(String cacheKey, Object cacheValue, Long times, TimeUnit timeUnit) {
    	
    	//设置清理线程开启
    	startCleanThread();
    	
    	//设置key的过期时间
        expireRecord.put(cacheKey, System.currentTimeMillis() + timeUnit.toMillis(times));
        
        //添加缓存到数据中
        map.put(cacheKey, cacheValue);
    	
    }
    
    /**
     * 删除过期的缓存
     */
    private static void deleteTimeOut() {
    	
		// 获取迭代器
		Iterator<Map.Entry<String, Long>> it = expireRecord.entrySet().iterator();

		// 遍历，并判断
		while (it.hasNext()) {
			Map.Entry<String, Long> record = it.next();
			Long expireTime = record.getValue();
    		if (System.currentTimeMillis() > expireTime){
                
    			String key = record.getKey();
    			//数据过期移除数据存储和过期记录存储
				it.remove();
                map.remove(key);
                
            }
    	}
    	
    }
    
    /**
     * 设置清理线程的运行状态为正在运行
     */
    private static void setCleanThreadRun() {
    	ifCleanThreadRunning = true;
    }
    
    /**
     * 开启清理过期缓存的线程
     */
    private static void startCleanThread() {
    	
    	if (!ifCleanThreadRunning) {
    		
    		CleanTimeOutThread cleanTimeOutThread = new CleanTimeOutThread();
            Thread thread = new Thread(cleanTimeOutThread);
            //设置为后台守护线程
            thread.setDaemon(true);
            thread.start();
    		
    	}
    	
    }
    
    /**
     * 每一分钟清理一次过期缓存
     */
    private static class CleanTimeOutThread implements Runnable{

        public void run() {
        	
        	setCleanThreadRun();
        	
            while (true) {
            	
                try {
                	
                	//休息1小时
                    Thread.sleep(1000*60*60l);
                    
                    //删除过期的缓存
                    deleteTimeOut();
                    
                    if(logger.isDebugEnabled()){
                    	logger.debug("clean thread run, map.size() is {}, expireRecord.size() is {}." , map.size(), expireRecord.size());
                    }
                    //System.out.println("clean thread run ");
                    //System.out.println("map.size()" + map.size());
                    //System.out.println("expireRecord.size()" + expireRecord.size());
                    
                } catch (InterruptedException e) {
                	logger.error("error",e);
                }
                
            }
        }
        
    }

    /**
     * 测试代码
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
    	
    	ExpireTimeMap.put("test", "1234");
    	while(true){
    		logger.info(ExpireTimeMap.get("test"));
    		Thread.sleep(1000l);
    	}
		
	}

}
