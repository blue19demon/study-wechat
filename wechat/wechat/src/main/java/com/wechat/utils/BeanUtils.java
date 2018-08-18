package com.wechat.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {
	public static Map<String, Object> objectToMap(Object obj) { 
		Map<String, Object> map = new HashMap<String, Object>();   
        try {
			if(obj == null)  
			    return null;
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
			for (PropertyDescriptor property : propertyDescriptors) {    
			    String key = property.getName();    
			    if (key.compareToIgnoreCase("class") == 0) {   
			        continue;  
			    }  
			    Method getter = property.getReadMethod();  
			    Object value = getter!=null ? getter.invoke(obj) : null;  
			    map.put(key, value);  
			}    
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;  
    }   
}
