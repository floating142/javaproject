package maze;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import maze.Config.Conf;

public class Main {

    // 设置全局字体
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

    public static void main(String[] args) {
        InitGlobalFont(new Font("宋体", Font.PLAIN, 40));
        new Window("myproject", 100, 100, Conf.width, Conf.height);	

    }
}
