package com.wechat.wechat;

import java.io.BufferedInputStream;
import java.util.Scanner;

import com.wechat.utils.TulingApiUtil;

public class Test {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(new BufferedInputStream(System.in));
		while (scanner.hasNext()) {
			String content = scanner.next();
			System.out.println(TulingApiUtil.getTulingResult(content));
		}
		scanner.close();
	}
}
