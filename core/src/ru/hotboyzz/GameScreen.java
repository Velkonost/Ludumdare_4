package ru.hotboyzz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ru.hotboyzz.entities.AmericanManEntity;
import ru.hotboyzz.entities.KoreanManEntity;
import ru.hotboyzz.entities.ProstitutkaEntity;

import java.util.ArrayList;

/**
 * @author Velkonost
 */
public class GameScreen extends BaseScreen implements InputProcessor {

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
    private float timerAddEnergy = 0;
    private float timerUpSpawnSpeed = 0;

    private float koreanSpawnCooldawn = 1.5f;
    private float americanSpawnCooldawn = 2.5f;
    private float upSpawnSpeedCooldawn = 1f;
    private static final float ENERGY_COOLDAWN = 1f;

    private float prostitutkaAmX = 5f, prostitutkaAmY = 3f;
    private boolean prostitutkaAm = false, prostitutkaKor = false;
    private float prostitutkaRot =  0f;

    private int koreanEnergy = 100;
    private int americanEnergy = 100;
    private float koreanUpgradeLvl = 0f;
    private float americanUpgradeLvl = 0f;

    private float firstSkillCost = 30f;
    private float secondSkillCost = 30f;
    private float thirdSkillCost = 30f;

    private int americanFirstSkillAmount = 10;
    private int americanSecondSkillAmount = 10;
    private int koreanFirstSkillAmount = 10;
    private int koreanSecondSkillAmount = 10;

    private ArrayList<Texture> koreanMenTexture;
    private ArrayList<Texture> americanMenTexture;
    private Texture background, prostitutka;

    private Label textKoreans, textAmericans, energyKor, energyAm, lvlAmerican, lvlKor;
    private Label.LabelStyle textStyle;

    private ProstitutkaEntity prostitutki;
    private boolean isProstitutkaShowed = false;

    public GameScreen(MainGame game) {
        this.game = game;

        stage = new Stage(new FitViewport(1280, 720));
        world = new World(new Vector2(0, 0), true);

    }

    @Override
    public void show() {
        font = new BitmapFont();
        textStyle = new Label.LabelStyle();
        textStyle.font = font;


        koreanMen = new ArrayList<KoreanManEntity>();
        americanMen = new ArrayList<AmericanManEntity>();
        koreanMenTexture = new ArrayList<Texture>();
        americanMenTexture = new ArrayList<Texture>();

        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(32, 18);
        camera.translate(0, 1);

        getTextures();

        Gdx.input.setInputProcessor(this);

        sp = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        textKoreans = new Label("Koreans: " + koreanMen.size(),textStyle);
        textAmericans = new Label("Americans: " + americanMen.size(),textStyle);
        lvlAmerican = new Label("American level: " + americanUpgradeLvl,textStyle);
        lvlKor = new Label("Korean level:  " + koreanUpgradeLvl,textStyle);
        energyAm = new Label("American energy:  " + americanEnergy,textStyle);
        energyKor = new Label("Korean energy:  " + koreanEnergy,textStyle);

        textKoreans.setX(Gdx.graphics.getWidth()-150);
        textKoreans.setY(Gdx.graphics.getHeight()-25);

        lvlKor.setX(Gdx.graphics.getWidth()-150);
        lvlKor.setY(Gdx.graphics.getHeight()-40);

        energyKor.setX(Gdx.graphics.getWidth()-150);
        energyKor.setY(Gdx.graphics.getHeight()-55);

        textAmericans.setX(50);
        textAmericans.setY(Gdx.graphics.getHeight()-25);

        lvlAmerican.setX(50);
        lvlAmerican.setY(Gdx.graphics.getHeight()-40);

        energyAm.setX(50);
        energyAm.setY(Gdx.graphics.getHeight()-55);

        stage.addActor(textKoreans);
        stage.addActor(textAmericans);
        stage.addActor(lvlKor);
        stage.addActor(lvlAmerican);
        stage.addActor(energyKor);
        stage.addActor(energyAm);

        if(prostitutkaAm){
            if (!isProstitutkaShowed) {
                prostitutki = new ProstitutkaEntity(prostitutka, world, prostitutkaAmX, prostitutkaAmY, 2f, 2f);
                isProstitutkaShowed = true;
            }
            stage.addActor(prostitutki);
            if(prostitutkaAmX > 7f){
                prostitutkaAmX = 5f;
                prostitutki.remove();
                prostitutkaAm = false;
                isProstitutkaShowed = false;
            } else {
                prostitutkaAmX += 0.02f;
//                prostitutki.setBodyX(0f);
            }
        }

        timerKoreanSpawn += delta;
        if (timerKoreanSpawn >= koreanSpawnCooldawn) {
            koreanMen.add(
                    new KoreanManEntity(koreanMenTexture.get(
                            generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                    )
            );

            stage.addActor(koreanMen.get(koreanMen.size() - 1));
            timerKoreanSpawn = 0;
        }

        timerAmericanSpawn += delta;
        if (timerAmericanSpawn >= americanSpawnCooldawn) {
            americanMen.add(
                    new AmericanManEntity(americanMenTexture.get(
                            generateRandomAmericanImg()), world, generateRandomAmericanX(), generateRandomAmericanY(), 0.5f, 1f
                    )
            );

            stage.addActor(americanMen.get(americanMen.size() - 1));
            timerAmericanSpawn = 0;
        }

        timerAddEnergy += delta;
        if (timerAddEnergy >= ENERGY_COOLDAWN) {
            americanEnergy += 1;
            koreanEnergy += 1;

            if (americanEnergy > 100) americanEnergy = 100;
            if (koreanEnergy > 100) koreanEnergy = 100;

            timerAddEnergy = 0;
        }

        timerUpSpawnSpeed += delta;
        if (timerUpSpawnSpeed >= upSpawnSpeedCooldawn) {
            americanSpawnCooldawn -= 0.1f;
            koreanSpawnCooldawn -= 0.1f;

            if (americanSpawnCooldawn <= 0.2f) americanSpawnCooldawn = 0.2f;
            if (koreanSpawnCooldawn <= 0.1f) koreanSpawnCooldawn = 0.1f;

            timerUpSpawnSpeed = 0;
        }




        stage.act();
        stage.getBatch().begin();

        stage.getBatch().draw(background, 0, 0, 1280, 720);
        stage.getBatch().end();
        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
//        stage.getBatch().begin();
//        font.getData().setScale(1, 1);
//        font.draw(stage.getBatch(), "Koreans: " + koreanMen.size(), Gdx.graphics.getWidth()-200,  Gdx.graphics.getHeight()-25);
//        font.draw(stage.getBatch(), "Americans: " + americanMen.size(), 100,  Gdx.graphics.getHeight()-25);
//        stage.getBatch().end();
        stage.draw();

        textAmericans.remove();
        textKoreans.remove();

        energyKor.remove();
        energyAm.remove();

        lvlAmerican.remove();
        lvlKor.remove();

//        prostitutki.remove();
    }

    private void getTextures() {
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor1.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor3.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor4.png"));
        koreanMenTexture.add((Texture) game.getManager().get("imgs/kor5.png"));

        americanMenTexture.add((Texture) game.getManager().get("imgs/am1.png"));
        americanMenTexture.add((Texture) game.getManager().get("imgs/am2.png"));

        prostitutka = game.getManager().get("imgs/prostitutki.png");
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

    private int generateRandomAmericanImg() {
        return (0 + (int) (Math.random() * 2));
    }

    private float generateRandomKoreanX() {
        return (7f + (float)(Math.random() * 6f));
    }

    private float generateRandomAmericanX() {
        return (0f + (float)(Math.random() * 6f));
    }

    private float generateRandomKoreanY() {
        return (0f +  (float)(Math.random() * 6f));
    }

    private float generateRandomAmericanY() {
        return (0f +  (float)(Math.random() * 6f));
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        renderer.dispose();
    }


    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.Q) {
            useFirstSkillAmerican();
        }
        if (keycode == Input.Keys.W) {
            useSecondSkillAmerican();
        }
        if (keycode == Input.Keys.E) {
            useThirdSkillAmerican();
        }
        if (keycode == Input.Keys.R) {
            upgradeAmerican();
        }

        if (keycode == Input.Keys.A) {
           useFirstSkillKorean();
        }
        if (keycode == Input.Keys.S) {
            useSecondSkillKorean();
        }
        if (keycode == Input.Keys.D) {
            useThirdSkillKorean();
        }
        if (keycode == Input.Keys.F) {
            upgradeKorean();
        }
        return true;
    }

    private void useFirstSkillAmerican() {
        if (!prostitutkaAm) {
            if (americanEnergy >= (firstSkillCost - americanUpgradeLvl)) {
                americanEnergy -= (firstSkillCost - americanUpgradeLvl);
                prostitutkaAm = true;
                int min = Math.min(koreanMen.size(), americanFirstSkillAmount);
                for (int i = 0; i < min; i++) {
                    koreanMen.get(0).addAction(Actions.removeActor());
                    koreanMen.remove(0);
                }
            }
        }
    }

    private void useSecondSkillAmerican() {
        if (americanEnergy >= (secondSkillCost - americanUpgradeLvl)) {
            americanEnergy -= (secondSkillCost - americanUpgradeLvl);

            for (int i = 0; i < secondSkillCost; i++) {
                americanMen.add(
                        new AmericanManEntity(americanMenTexture.get(
                                generateRandomAmericanImg()), world, generateRandomAmericanX(), generateRandomAmericanY(), 0.5f, 1f
                        )
                );
                stage.addActor(americanMen.get(americanMen.size() - 1));
            }
        }
    }

    private void useFirstSkillKorean() {
        if (koreanEnergy >= (firstSkillCost - koreanUpgradeLvl)) {
            koreanEnergy -= (firstSkillCost - koreanUpgradeLvl);

            int min = Math.min(americanMen.size(), koreanFirstSkillAmount);
            for (int i = 0; i < min; i++) {
                americanMen.get(0).addAction(Actions.removeActor());
                americanMen.remove(0);
            }
        }
    }

    private void useSecondSkillKorean() {
        if (koreanEnergy >= (secondSkillCost - koreanUpgradeLvl)) {
            koreanEnergy -= (secondSkillCost - koreanUpgradeLvl);

            for (int i = 0; i < secondSkillCost; i++) {
                koreanMen.add(
                        new KoreanManEntity(koreanMenTexture.get(
                                generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                        )
                );
                stage.addActor(koreanMen.get(koreanMen.size() - 1));
            }
        }
    }

    private void useThirdSkillAmerican() {
        if (americanEnergy >= (thirdSkillCost - americanUpgradeLvl)) {
            americanEnergy -= (thirdSkillCost - americanUpgradeLvl);
            switch (generateRandomSkillNum()) {
                case 1:
                    System.out.println(1);
                    for (int i = 0; i < 100; i++) {
                        koreanMen.add(
                                new KoreanManEntity(koreanMenTexture.get(
                                        generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                                )
                        );
                        stage.addActor(koreanMen.get(koreanMen.size() - 1));
                    }
                    break;
                case 2:
                    System.out.println(2);
                    int min = Math.min(americanMen.size(), 30);
                    for (int i = 0; i < min; i++) {
                        americanMen.get(0).addAction(Actions.removeActor());
                        americanMen.remove(0);
                    }
                    break;
                case 3:
                    System.out.println(3);
                    for (int i = 0; i < 30; i++) {
                        americanMen.add(
                                new AmericanManEntity(americanMenTexture.get(
                                        generateRandomAmericanImg()), world, generateRandomAmericanX(), generateRandomAmericanY(), 0.5f, 1f
                                )
                        );
                        stage.addActor(americanMen.get(americanMen.size() - 1));
                    }
                    break;
                case 4:
                    System.out.println(4);
                    for (int i = 0; i < 100; i++) {
                        americanMen.add(
                                new AmericanManEntity(americanMenTexture.get(
                                        generateRandomAmericanImg()), world, generateRandomAmericanX(), generateRandomAmericanY(), 0.5f, 1f
                                )
                        );
                        stage.addActor(americanMen.get(americanMen.size() - 1));
                    }
                    break;
                case 5:
                    System.out.println(5);
                    //lose
                    break;
                case 6:
                    System.out.println(6);
                    //win
                    break;
                case 7:
                    americanEnergy += 60;
                    break;
                case 8:
                    koreanEnergy += 60;
                    break;
            }
        }
    }

    private void useThirdSkillKorean() {
        if (koreanEnergy >= (thirdSkillCost - koreanUpgradeLvl)) {
            koreanEnergy -= (thirdSkillCost - koreanUpgradeLvl);

            switch (generateRandomSkillNum()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
        }
    }

    private void upgradeKorean() {
        if (koreanEnergy >= 50) {
            koreanEnergy -= 50;
            koreanUpgradeLvl += 1f;
        }
    }

    private void upgradeAmerican() {
        if (americanEnergy >= 50) {
            americanEnergy -= 50;
            americanUpgradeLvl += 1f;
        }
    }

    private int generateRandomSkillNum() {
        /*
            1 : + 100 enemy : хороший урожай - падают яблоки с неба
            2 : - 30 own : Миграция
            3 : + 30 own : Миграция
            4 : + 100 own : хороший урожай - падают яблоки с неба
            5 : lose game : проигрыш
            6 : win game : победа
            7 : + 60 energy own : red bull
            8 : + 60 energy enemy : red bull
        */
        return (1 + (int)(Math.random() * 8));
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
