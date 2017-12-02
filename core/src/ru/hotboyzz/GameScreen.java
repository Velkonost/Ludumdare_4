package ru.hotboyzz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ru.hotboyzz.entities.AmericanManEntity;
import ru.hotboyzz.entities.KoreanManEntity;

import java.util.ArrayList;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen {

//    oY : 0-7
//    oX : 0-13
    private Stage stage;

    private World world;
    public MainGame game;

    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;

    BitmapFont font;
    SpriteBatch sp;

    private ArrayList<KoreanManEntity> koreanMen;
    private ArrayList<AmericanManEntity> americanMen;

    private float timerKoreanSpawn = 0;
    private float timerAmericanSpawn = 0;
    private static final float KOREAN_SPAWN_COOLDAWN = 0.5f;
    private static final float AMERICAN_SPAWN_COOLDAWN = 1f;


    private ArrayList<Texture> koreanMenTexture;
    private ArrayList<Texture> americanMenTexture;
    private Texture background;

    public GameScreen(MainGame game) {
        this.game = game;

        stage = new Stage(new FitViewport(1280, 720));
        world = new World(new Vector2(0, 0), true);

    }

    @Override
    public void show() {
        koreanMen = new ArrayList<KoreanManEntity>();
        americanMen = new ArrayList<AmericanManEntity>();
        koreanMenTexture = new ArrayList<Texture>();
        americanMenTexture = new ArrayList<Texture>();

        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(32, 18);
        camera.translate(0, 1);

        getTextures();

        sp = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timerKoreanSpawn += delta;
        if (timerKoreanSpawn >= KOREAN_SPAWN_COOLDAWN) {
            koreanMen.add(
                    new KoreanManEntity(koreanMenTexture.get(
                            generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                    )
            );

            stage.addActor(koreanMen.get(koreanMen.size() - 1));
            timerKoreanSpawn = 0;
        }

        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, 1280, 720);
        stage.getBatch().end();
        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();
    }

    private void getTextures() {
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor1.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor3.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor4.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor5.png"));

        background = game.getManager().get("imgs/bg1.png");
    }

    public void hide() {
//        rocket.detach();
//        earth.detach();
//        mars.detach();
        //  fireball.detach();
//        fireball2.detach();

//        rocket.remove();
//        mars.remove();
//        earth.remove();
        //    fireball.remove();
//        fireball2.remove();
    }

    private int generateRandomKoreanImg() {
        return (0 + (int) (Math.random() * 3));
    }

    private float generateRandomKoreanX() {
        return (7f + (float)(Math.random() * 6f));
    }

    private float generateRandomKoreanY() {
        return (0f +  (float)(Math.random() * 6f));
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        renderer.dispose();
    }
}
