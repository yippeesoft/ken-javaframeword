package com.shine.sourceflow.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.shine.framework.core.util.XmlUitl;

/**
 * 配置管理类
 */
public final class ConfigManager {
	/** 配置管理类 */
	private static ConfigManager manager = new ConfigManager();
	
	/** 配置集合 */
	private Map<String, Element> attributes = new HashMap<String, Element>();
	
	/** 系统根目录 */
	private String sysPath;
	
	/** 配置目录 */
	private String configPath;
	
	private ConfigManager() {
	}
	
	public static ConfigManager getManager() {
		return manager;
	}
	
	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(ServletContext context) {
		this.sysPath = context.getRealPath("/");
		this.configPath = this.sysPath + "WEB-INF/config/";
		
		// 从配置文件读取并设置相关配置
		try {
			Document document = XmlUitl.getFileDocument(this.configPath + "boot.xml");
			List<Element> bootList = XmlUitl.getAllElement(document.getRootElement());
			for (Element element : bootList) {
				this.setAttribute(element.getName(), element);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前系统根目录
	 * 
	 * @return 系统根目录
	 */
	public String getSysPath() {
		return sysPath;
	}
	
	/**
	 * 获取系统配置目录
	 * 
	 * @return 系统配置目录
	 */
	public String getConfigPath() {
		return this.configPath;
	}
	
	/**
	 * 获取指定键指示的系统配置
	 * 
	 * @param  key 键
	 * @return 指定键指示的系统配置
	 */
	public Element getAttribute(String key) {
		return this.attributes.get(key);
	}
	
	/**
	 * 设置指定键指示的系统配置
	 * 
	 * @param key   键
	 * @param value 值
	 */
	public void setAttribute(String key, Element value) {
		if (this.attributes.containsKey(key)) {
			throw new IllegalArgumentException(key + "已经存在");
		}
		this.attributes.put(key, value);
	}
}
