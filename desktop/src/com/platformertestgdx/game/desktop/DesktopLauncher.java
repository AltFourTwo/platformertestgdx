package com.platformertestgdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformertestgdx.game.MyGdxGame;

import static com.platformertestgdx.game.CNSTNT.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH*2;
		config.height = HEIGHT*2;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
