package ru.hotboyzz;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Velkonost
 */
public class MainGame extends Game {


    private AssetManager manager;

    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void create () {
        manager = new AssetManager();
        manager.load("badlogic.jpg", Texture.class);

        manager.finishLoading();

//        setScreen(new MenuScreen(this));
        setScreen(new GameScreen(this));
    }

}

