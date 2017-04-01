package com.example.xml;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParser {
	public static final String XML_DATA_PATH = "oschina_app.xml";
	
	public static void main(String[] args) throws Exception {
		Process parseXmlData = parseXmlData();
		System.out.println("process = " + parseXmlData);
	}

	public static Process parseXmlData() throws Exception{
		File file = new File(XML_DATA_PATH);
		if (!file.exists()) {
			System.out.println("配置文件不存在");
			return null;
		}
		Process process = new Process();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(XML_DATA_PATH));
		Element root = document.getRootElement();
		process.setPackageName(root.attributeValue("package"));
		process.setProcessName(root.attributeValue("name"));
		process.setTerminalName(root.attributeValue("terminal"));
		
		Iterator<Element> iterator = root.elementIterator();
		while(iterator.hasNext()){
			Element element = iterator.next();
			String name = element.getName();
			switch (name) {
			case "task":
				Task task = new Task();
				String key = element.attributeValue("name");
				task.setTaskClass(element.attributeValue("class"));
				task.setTaskName(key);
				Iterator<Element> taskIterator = element.elementIterator();
				if (taskIterator.hasNext()) {
					Element cache_data = taskIterator.next();
					Iterator<Element> dataIterator = cache_data.elementIterator();
					while(dataIterator.hasNext()){
						Element dataElement = dataIterator.next();
						switch (dataElement.getName()) {
						case "data":
							Data data = new Data();
							data.setDataId(dataElement.attributeValue("id"));
							data.setDataName(dataElement.attributeValue("name"));
							data.setDataType(dataElement.attributeValue("type"));
							task.getDataList().add(data);
							break;
						default:
							break;
						}
					}
				}
				process.getTaskMap().put(key, task);
				break;
			default:
				break;
			}
		}
		return process;
	}
}
