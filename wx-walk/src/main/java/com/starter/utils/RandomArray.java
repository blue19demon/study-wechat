package com.starter.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public class RandomArray {

	/**从list中随机抽取元素
	 * @return  
	 * @Title: createRandomList 
	 * @Description: TODO
	 * @param list
	 * @param i 
	 * @return void  
	 * @throws 
	 */ 
	public static JSONArray createRandomList(JSONArray  list, int n) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		JSONArray listNew = new JSONArray();
		if(list.size()<=n){
			return list;
		}else{
			while(map.size()<n){
				int random = (int) (Math.random() * list.size());
				if (!map.containsKey(random)) {
					map.put(random, "");
					listNew.add(list.get(random));
				}
			}
			return listNew;
		}
	}
}
