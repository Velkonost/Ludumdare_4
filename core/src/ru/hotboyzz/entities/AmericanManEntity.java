package ru.hotboyzz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static ru.hotboyzz.Constants.PIXELS_IN_METER;

/**
 * @author Velkonost
 */
public class AmericanManEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    float width, height, x, y;

    public AmericanManEntity(Texture texture, World world, float x, float y, float width, float height) {
        this.texture = texture;

        this.width = width;
        this.height = height;

        this.world = world;
//        this.game = game;
        setPosition(x, y);

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(def);
        body.setFixedRotation(true);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.25f, 0.5f);

        fixture = body.createFixture(box, 1000000000);
        fixture.setUserData("koreanMan");

        body.setFixedRotation(false);

        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x) * PIXELS_IN_METER, (body.getPosition().y) * PIXELS_IN_METER);
//        System.out.println(getX() / PIXELS_IN_METER);
        if (getX() / PIXELS_IN_METER <= 7f) {

            setX(7f * PIXELS_IN_METER);
        }
        if (getX() / PIXELS_IN_METER >= 13.5f) {
            setX(13.5f * PIXELS_IN_METER);
        }
        if (getY() / PIXELS_IN_METER <= 0f) {
            setY(0f * PIXELS_IN_METER);
        }
        if (getY() / PIXELS_IN_METER >= 7f) {
            setY(7f * PIXELS_IN_METER);
        }
        batch.draw(texture, getX(), getY(), width  * PIXELS_IN_METER, height  * PIXELS_IN_METER);
//        batch.draw(texture, getX(), getY(), (getWidth()) / 2, (getHeight()) / 2, getWidth(), getHeight(), 1,
//                1, 0, 1, 1, 900, 900, false, false);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
