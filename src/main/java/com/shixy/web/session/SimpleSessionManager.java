package com.shixy.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SimpleSessionManager implements SessionManager {


    private SimpleSessionManager(){};
    
    private static SimpleSessionManager instance = new SimpleSessionManager();
    
    private static final Map<String, Session> sessionCache = new ConcurrentHashMap<String, Session>(128, 0.75f, 32);

    public static SimpleSessionManager getInstance(){
    	return instance;
    }
    
    @Override
    public void addSession(String sessionId, Session session) {
        sessionCache.put(sessionId, session);
    }

    @Override
    public Session getSession(String sessionId) {
        return sessionCache.get(sessionId);
    }

    @Override
    public void removeSession(String sessionId) {
        sessionCache.remove(sessionId);
    }

}