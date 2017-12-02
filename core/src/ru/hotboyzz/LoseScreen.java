package ru.hotboyzz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * @author Velkonost
 */
public class LoseScreen extends BaseScreen {
    public MainGame game;
    private SpriteBatch LS;
    BitmapFont font;
    float CAM_W = 1280, CAM_H = 720;
    private Texture losePik;
    private float timer = 0;
    private Sprite losePikch;
    ///winPikch 720*480

    private GameScreen mGameScreen;

    private float score;

    public LoseScreen(MainGame game, float score){
        super();
        this.game = game;
        this.score = score;
    }

    public LoseScreen(MainGame game) {
    }

    public void show() {
        FileHandle fontFile = Gdx.files.internal("fonts/font.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 150;


        LS = new SpriteBatch();
        losePik = game.getManager().get("imgs/lostScreen.png");
       //font = new BitmapFont();
        font = generator.generateFont(parameter);
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
        font.setColor(Color.BLACK);
       //e font.getData().setScale(5f);
        font.draw(LS, ""+Math.round(score) + (Math.round(score)!=1f ? " seconds" :  " second"), 470, 380);

        LS.end();
    }
}
