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
import ru.hotboyzz.entities.*;

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

    private WinScreen winScreen;
    private LoseScreen loseScreen;

    private ArrayList<KoreanManEntity> koreanMen;
    private ArrayList<AmericanManEntity> americanMen;

    private float timerKoreanSpawn = 0;
    private float timerAmericanSpawn = 0;
    private float timerAddEnergy = 0;
    private float timerUpSpawnSpeed = 0;

    private float timerAllGame = 0;

    private float koreanSpawnCooldawn = 1.5f;
    private float americanSpawnCooldawn = 2.5f;
    private float upSpawnSpeedCooldawn = 1f;
    private static final float ENERGY_COOLDAWN = 1f;

    private float prostitutkaAmX = 5f, prostitutkaAmY = 3f;
    private float rocketX = 10f, rocketY = 3f;
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
    private Texture background, prostitutka, appleRed, appleGold, appleBlue, rocket;

    private Label textKoreans, textAmericans, energyKor, energyAm, lvlAmerican, lvlKor;
    private Label.LabelStyle textStyle;

    private ProstitutkaEntity prostitutki;
    private RocketEntity rockets;
    private boolean isProstitutkaShowed = false;
    private boolean isApplesKorShowed = false, applesKorDrop = false;
    private boolean isApplesAmShowed = false, applesAmDrop = false;
    private boolean isRocketsShowed = false;
    private boolean isRockets = false;
    private boolean isApplesShowed = false, applesDrop = false;
    private boolean isRedbullShowed = false, showRedbull = false;
    private float redbullTimer = 0;
    private RedbullEntity redbullEntity;
    private Texture redbullTexture;
    private float redbullX = 0, redbullY = 0;

    private ArrayList<AppleEntity> applesAm, applesKor;

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

        loseScreen = new LoseScreen(game);
        winScreen = new WinScreen(game);

        koreanMen = new ArrayList<KoreanManEntity>();
        americanMen = new ArrayList<AmericanManEntity>();
        koreanMenTexture = new ArrayList<Texture>();
        americanMenTexture = new ArrayList<Texture>();

        applesAm = new ArrayList<AppleEntity>();
        applesKor = new ArrayList<AppleEntity>();

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

        addTextOnScreen();
        timerAllGame+=delta;
        if(applesAmDrop){
            if(!isApplesAmShowed){
                for(int i = 0; i<10; i++){
                    switch (generateRandomNum(1, 3)){
                        case 1:
                            applesAm.add(new AppleEntity(appleGold, world, generateRandomNum(1,6), 6f, 0.5f, 0.5f));
                            break;
                        case 2:
                            applesAm.add(new AppleEntity(appleRed, world, generateRandomNum(1,6), 6f, 0.5f, 0.5f));
                            break;
                        case 3:
                            applesAm.add(new AppleEntity(appleBlue, world, generateRandomNum(1,6), 6f, 0.5f, 0.5f));
                            break;
                    }
                }
                isApplesAmShowed = true;
            }
            for(int i = 0; i<10; i++){
                stage.addActor(applesAm.get(i));
            }

            for(int i = 0; i<10; i++){
                if(applesAm.get(i).getY()<7){
                    applesAm.get(i).remove();
                    applesAm.clear();
                    applesAmDrop = false;
                    isApplesAmShowed = false;
                    for (int j = 0; j < 100; j++) {
                        americanMen.add(
                                new AmericanManEntity(koreanMenTexture.get(
                                        generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                                )
                        );
                        stage.addActor(koreanMen.get(koreanMen.size() - 1));
                    }
                    break;
                }
            }
        }

        if(applesKorDrop){
            if(!isApplesKorShowed){
                for(int i = 0; i<10; i++){
                    switch (generateRandomNum(1,3)){
                        case 1:
                            applesKor.add(new AppleEntity(appleGold, world, generateRandomNum(7,12), 6f, 0.5f, 0.5f));
                            break;
                        case 2:
                            applesKor.add(new AppleEntity(appleRed, world, generateRandomNum(7,12), 6f, 0.5f, 0.5f));
                            break;
                        case 3:
                            applesKor.add(new AppleEntity(appleBlue, world, generateRandomNum(7,12), 6f, 0.5f, 0.5f));
                            break;
                    }
                }
                isApplesKorShowed = true;
            }
            for(int i = 0; i<10; i++){
                stage.addActor(applesKor.get(i));
            }

            for(int i = 0; i<10; i++){
                if(applesKor.get(i).getY()<7){
                    applesKor.get(i).remove();
                    applesKor.clear();
                    applesKorDrop = false;
                    isApplesKorShowed = false;
                    for (int j = 0; j < 100; j++) {
                        koreanMen.add(
                                new KoreanManEntity(koreanMenTexture.get(
                                        generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                                )
                        );
                        stage.addActor(koreanMen.get(koreanMen.size() - 1));
                    }
                    break;
                }
            }
        }

        if(showRedbull){
            redbullTimer+=delta;
            if(!isRedbullShowed){
                redbullEntity = new RedbullEntity(redbullTexture, world, redbullX, redbullY, 4f, 4f);
                isRedbullShowed = true;
            }
            stage.addActor(redbullEntity);
            if(redbullTimer>3){
                redbullEntity.remove();
                showRedbull = false;
                isRedbullShowed = false;
                redbullTimer = 0;
            }
        }
        if(prostitutkaAm){
            if (!isProstitutkaShowed) {
                prostitutki = new ProstitutkaEntity(prostitutka, world, prostitutkaAmX, prostitutkaAmY, 3f, 3f);
                isProstitutkaShowed = true;
            }
            stage.addActor(prostitutki);
            if(prostitutkaAmX > 7f){
                prostitutkaAmX = 5f;
                prostitutki.remove();
                prostitutkaAm = false;
                isProstitutkaShowed = false;
                int min = Math.min(koreanMen.size(), americanFirstSkillAmount);
                for (int i = 0; i < min; i++) {
                    koreanMen.get(0).addAction(Actions.removeActor());
                    koreanMen.remove(0);
                }
            } else {
                prostitutkaAmX += 0.02f;
            }
        }

        if(isRockets){
            if (!isRocketsShowed) {
                rockets = new RocketEntity(rocket, world, rocketX, rocketY, 2f, 2f);
                isRocketsShowed = true;
            }
            stage.addActor(rockets);
            if(rocketX < 2f){
                rocketX = 3f;

                rockets.remove();
                isRockets = false;
                isRocketsShowed = false;
                int min = Math.min(americanMen.size(), koreanFirstSkillAmount);
                for (int i = 0; i < min; i++) {
                    americanMen.get(0).addAction(Actions.removeActor());
                    americanMen.remove(0);
                }
            } else {
                rocketX -= 0.08f;
            }
        }

        timerKoreanSpawn += delta;
        if (timerKoreanSpawn >= koreanSpawnCooldawn) {
            spawnKorean();
        }

        timerAmericanSpawn += delta;
        if (timerAmericanSpawn >= americanSpawnCooldawn) {
            spawnAmerican();
        }

        timerAddEnergy += delta;
        if (timerAddEnergy >= ENERGY_COOLDAWN) {
            addEnergy();
        }

        timerUpSpawnSpeed += delta;
        if (timerUpSpawnSpeed >= upSpawnSpeedCooldawn) {
            reduceSpawnCooldawn();
        }

        stage.act();
        stage.getBatch().begin();

        stage.getBatch().draw(background, 0, 0, 1280, 720);
        stage.getBatch().end();
        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
        stage.draw();

        textAmericans.remove();
        textKoreans.remove();

        energyKor.remove();
        energyAm.remove();

        lvlAmerican.remove();
        lvlKor.remove();
    }

    private void spawnKorean() {
        koreanMen.add(
                new KoreanManEntity(koreanMenTexture.get(
                        generateRandomKoreanImg()), world, generateRandomKoreanX(), generateRandomKoreanY(), 0.5f, 1f
                )
        );

        stage.addActor(koreanMen.get(koreanMen.size() - 1));
        timerKoreanSpawn = 0;
    }

    private void spawnAmerican() {
        americanMen.add(
                new AmericanManEntity(americanMenTexture.get(
                        generateRandomAmericanImg()), world, generateRandomAmericanX(), generateRandomAmericanY(), 0.5f, 1f
                )
        );

        stage.addActor(americanMen.get(americanMen.size() - 1));
        timerAmericanSpawn = 0;
    }

    private void addEnergy() {
        americanEnergy += 1;
        koreanEnergy += 1;

        if (americanEnergy > 100) americanEnergy = 100;
        if (koreanEnergy > 100) koreanEnergy = 100;

        timerAddEnergy = 0;
    }

    private void reduceSpawnCooldawn() {
        americanSpawnCooldawn -= 0.1f;
        koreanSpawnCooldawn -= 0.1f;

        if (americanSpawnCooldawn <= 0.2f) americanSpawnCooldawn = 0.2f;
        if (koreanSpawnCooldawn <= 0.1f) koreanSpawnCooldawn = 0.1f;

        timerUpSpawnSpeed = 0;
    }

    private void addTextOnScreen() {
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
        appleBlue = game.getManager().get("imgs/apple3.png");
        appleGold = game.getManager().get("imgs/apple2.png");
        appleRed = game.getManager().get("imgs/apple1.png");
        redbullTexture = game.getManager().get("imgs/redbull.png");

        rocket = game.getManager().get("imgs/rocket.png");
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
            useThirdSkillAmerican();
        }
        if (keycode == Input.Keys.R) {
            upgradeAmerican();
        }

        if (keycode == Input.Keys.A) {
           useFirstSkillKorean();
        }
        return true;
    }

    private void useFirstSkillAmerican() {
        if (!prostitutkaAm) {
            if (americanEnergy >= (firstSkillCost - americanUpgradeLvl)) {
                americanEnergy -= (firstSkillCost - americanUpgradeLvl);
                prostitutkaAm = true;
            }
        }
    }

    private void useFirstSkillKorean() {
        if (!isRockets) {
            if (koreanEnergy >= (firstSkillCost - koreanUpgradeLvl)) {
                koreanEnergy -= (firstSkillCost - koreanUpgradeLvl);
                isRockets = true;
//                int min = Math.min(americanMen.size(), koreanFirstSkillAmount);
//                for (int i = 0; i < min; i++) {
//                    americanMen.get(0).addAction(Actions.removeActor());
//                    americanMen.remove(0);
//                }
            }
        }
    }


    private void useThirdSkillAmerican() {
        if (
                americanEnergy >= (thirdSkillCost - americanUpgradeLvl)
                && koreanEnergy >= (thirdSkillCost - koreanUpgradeLvl)
                ) {
            americanEnergy -= (thirdSkillCost - americanUpgradeLvl);
            koreanEnergy -= (thirdSkillCost - koreanUpgradeLvl);
            switch (generateRandomSkillNum()) {

                case 1:
                    applesKorDrop = true;
                    System.out.println(1);

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
                    applesAmDrop = true;
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
                    game.setScreen(new LoseScreen(game, timerAllGame));
                    //lose
                    break;
                case 6:
                    game.setScreen(winScreen);
                    System.out.println(6);
                    //win
                    break;
                case 7:
                    if(!showRedbull) {
                        americanEnergy += 60;
                        showRedbull = true;
                        redbullX = 2f;
                        redbullY = 3f;
                    }
                    break;
                case 8:
                    if(!showRedbull) {
                        koreanEnergy += 60;
                        showRedbull = true;
                        redbullX = 9f;
                        redbullY = 3f;
                    }
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
        if (americanEnergy >= 50 && koreanEnergy >= 50) {
            americanEnergy -= 50;
            koreanEnergy -= 50;
            americanUpgradeLvl += 1f;
            koreanUpgradeLvl += 1f;
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

    private int generateRandomNum(int l, int r){
        return (l + (int)(Math.random() * r));
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
