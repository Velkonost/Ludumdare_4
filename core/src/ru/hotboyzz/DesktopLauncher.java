package ru.hotboyzz;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		new LwjglApplication(new MainGame(), config);

		config.width = 1280;
		config.height = 720;
	}
}
