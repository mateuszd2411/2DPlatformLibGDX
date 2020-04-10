package com.mat.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Screens.PlayScreen;
import com.mat.mariobros.Sprites.Enemies.Dog;
import com.mat.mariobros.Sprites.Enemies.Enemy;
import com.mat.mariobros.Sprites.Enemies.Fire;
import com.mat.mariobros.Sprites.Enemies.FireRain;
import com.mat.mariobros.Sprites.Enemies.MonsterMouths;
import com.mat.mariobros.Sprites.Enemies.Trampoline;
import com.mat.mariobros.Sprites.Enemies.Turtle;
import com.mat.mariobros.Sprites.TitleObjects.Brick;
import com.mat.mariobros.Sprites.TitleObjects.Coin;
import com.mat.mariobros.Sprites.Enemies.Goomba;

public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Dog> dogs;
    private Array<Fire> fires;
    private Array<FireRain> fireRains;
    private Array<MonsterMouths> monsterMouths;
    private Array<Trampoline> trampolines;
    private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/ MarioBros.PPM, rect.getHeight() / 2/ MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);


        }


        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/ MarioBros.PPM, rect.getHeight() / 2/ MarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fdef);


        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object);
        }


        //create coin bodies
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

           new Coin(screen, object);
        }

        //create goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }


        ///create Dogs

        dogs = new Array<Dog>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            dogs.add(new Dog(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        ///create Dogs

        ///create fire

        fires = new Array<Fire>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            fires.add(new Fire(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        ///create fire

        ///create trampoline

        trampolines = new Array<Trampoline>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            trampolines.add(new Trampoline(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        ///create trampoline


        ///create fireRain

        fireRains = new Array<FireRain>();
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            fireRains.add(new FireRain(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        ///create fireRain

        ///create MonsterMouths

        monsterMouths = new Array<MonsterMouths>();
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            monsterMouths.add(new MonsterMouths(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

        ///create MonsterMouths

    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    ///dogs

    public Array<Dog> getDogs() {
        return dogs;
    }

    ///dogs

    ///fire

    public Array<Fire> getFires() {
        return fires;
    }

    ///fire

    ///trampoline

    public Array<Trampoline> getTrampolines() {
        return trampolines;
    }

    ///trampoline

    ///fireRain

    public Array<FireRain> getFireRains() {
        return fireRains;
    }

    ///fireRain

    ///MonsterMouths

    public Array<MonsterMouths> getMonsterMouths() {
        return monsterMouths;
    }

    ///MonsterMouths

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(dogs);
        enemies.addAll(fires);
        enemies.addAll(fireRains);
        enemies.addAll(monsterMouths);
        enemies.addAll(turtles);
        enemies.addAll(trampolines);
        return enemies;
    }
}
