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
        manager.load("imgs/bg1.png", Texture.class);
        manager.load("imgs/kor1.png", Texture.class);
//        manager.load("imgs/kor21.png", Texture.class);
        manager.load("imgs/kor3.png", Texture.class);
        manager.load("imgs/kor4.png", Texture.class);
        manager.load("imgs/kor5.png", Texture.class);
        manager.load("imgs/luckylee.png", Texture.class);
        manager.load("imgs/am1.png", Texture.class);
        manager.load("imgs/am2.png", Texture.class);

        manager.finishLoading();

//        setScreen(new MenuScreen(this));
        setScreen(new GameScreen(this));
    }

}

