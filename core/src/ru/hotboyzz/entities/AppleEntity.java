package ru.hotboyzz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static ru.hotboyzz.Constants.PIXELS_IN_METER;

/**
 * Created by admin on 02.12.2017.
 */
public class AppleEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    float width, height, x, y;
    private float xVelocity;
    float startAngle = 0f;


    public float speed = 1f;
    public AppleEntity(Texture texture, World world, float x, float y, float width, float height){
        this.texture = texture;

        this.width = width;
        this.height = height;

        this.world = world;
//        this.game = game;
        setPosition(x, y);
        this.x = x;
        this.y = y;

        BodyDef def = new BodyDef();
        def.position.set(x, y);
        def.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(def);
//        body.setFixedRotation(true);

        final PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);

        body.setFixedRotation(false);

        xVelocity = 0;

        box.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        body.setLinearVelocity(xVelocity, -speed);
        setPosition((body.getPosition().x) * PIXELS_IN_METER, (body.getPosition().y) * PIXELS_IN_METER);;
        batch.draw(texture, getX(), getY(), width  * PIXELS_IN_METER, height  * PIXELS_IN_METER);
        body.setTransform(body.getPosition().x, body.getPosition().y, (float)(startAngle * 3.14/180));
        startAngle += 10f;
//        batch.draw(texture, getX(), getY(), (getWidth()) / 2, (getHeight()) / 2, getWidth(), getHeight(), 1,
//                1, 0, 1, 1, 900, 900, false, false);
    }

    public void setBodyX(float x){
        setPosition(x*PIXELS_IN_METER, body.getPosition().y*PIXELS_IN_METER);
        setX(x * PIXELS_IN_METER);
    }
}
