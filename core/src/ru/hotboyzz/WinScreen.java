package ru.hotboyzz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Velkonost
 */
public class WinScreen extends BaseScreen {
    public MainGame game;
    private SpriteBatch LS;
    BitmapFont font;
    float CAM_W = 1280, CAM_H = 720;
    private Texture losePik;
    private Sprite losePikch;
    private float timer;
    ///winPikch 720*480

    private GameScreen mGameScreen;

    private int score;

    public WinScreen(MainGame game){
        super();
        this.game = game;
    }

    public void show() {
        LS = new SpriteBatch();
        losePik = game.getManager().get("imgs/wonScreen.png");
//        font = new BitmapFont(fontFile);
        mGameScreen = new GameScreen(game);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timer+=delta;

        if(timer>3){
            game.setScreen(new MenuScreen(game));
        }
        LS.begin();
        LS.draw(losePik, 0, 0);
        LS.end();
    }
}
