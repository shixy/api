package com.shixy.web.session;

import java.util.HashMap;
import java.util.Map;

public class SimpleSession implements Session {

	private static final long serialVersionUID = -7878075139177866142L;
	
	private Map<String, Object> attributes = new HashMap<String, Object>();

    public void setAttribute(String name, Object obj) {
        attributes.put(name, obj);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }
}

