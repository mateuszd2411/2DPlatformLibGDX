package com.mat.mainGame.Tools;

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
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Enemies.Dog;
import com.mat.mainGame.Sprites.Enemies.Enemy;
import com.mat.mainGame.Sprites.Enemies.Fire;
import com.mat.mainGame.Sprites.Enemies.FireRain;
import com.mat.mainGame.Sprites.Enemies.MonsterMouths;
import com.mat.mainGame.Sprites.Enemies.Par;
import com.mat.mainGame.Sprites.Enemies.Raft;
import com.mat.mainGame.Sprites.Enemies.SquareMonster;
import com.mat.mainGame.Sprites.Enemies.Trampoline;
import com.mat.mainGame.Sprites.Enemies.YellowMonster;
import com.mat.mainGame.Sprites.TitleObjects.Brick;
import com.mat.mainGame.Sprites.TitleObjects.Coin;

public class B2WorldCreator {
    private Array<SquareMonster> squareMonsters;
    private Array<Dog> dogs;
    private Array<Fire> fires;
    private Array<FireRain> fireRains;
    private Array<MonsterMouths> monsterMouths;
    private Array<Raft> rafts;
    private Array<Par> pars;
    private Array<Trampoline> trampolines;
    private Array<YellowMonster> yellowMonsters;

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
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/ MainGame.PPM, rect.getHeight() / 2/ MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/ MainGame.PPM, rect.getHeight() / 2/ MainGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MainGame.OBJECT_BIT;
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen, object);
        }
        //create coin bodies
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

           new Coin(screen, object);
        }
        //create squareMonsters
        squareMonsters = new Array<SquareMonster>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            squareMonsters.add(new SquareMonster(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }

        yellowMonsters = new Array<YellowMonster>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            yellowMonsters.add(new YellowMonster(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create Dogs
        dogs = new Array<Dog>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            dogs.add(new Dog(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create Dogs

        ///create fire
        fires = new Array<Fire>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            fires.add(new Fire(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create fire

        ///create trampoline
        trampolines = new Array<Trampoline>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            trampolines.add(new Trampoline(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create trampoline

        ///create fireRain
        fireRains = new Array<FireRain>();
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            fireRains.add(new FireRain(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create fireRain

        ///create MonsterMouths
        monsterMouths = new Array<MonsterMouths>();
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            monsterMouths.add(new MonsterMouths(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create MonsterMouths

        ///create Raft
        rafts = new Array<Raft>();
        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            rafts.add(new Raft(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create Raft

        ///create Par
        pars = new Array<Par>();
        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            pars.add(new Par(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
        }
        ///create Par
    }

    public Array<SquareMonster> getSquareMonsters() {
        return squareMonsters;
    }

    public Array<Dog> getDogs() {
        return dogs;
    }

    public Array<Fire> getFires() {
        return fires;
    }

    public Array<Trampoline> getTrampolines() {
        return trampolines;
    }

    public Array<FireRain> getFireRains() {
        return fireRains;
    }

    public Array<MonsterMouths> getMonsterMouths() {
        return monsterMouths;
    }

    public Array<Raft> getRafts() {
        return rafts;
    }

    public Array<Par> getPars() {
        return pars;
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(squareMonsters);
        enemies.addAll(dogs);
        enemies.addAll(fires);
        enemies.addAll(fireRains);
        enemies.addAll(monsterMouths);
        enemies.addAll(rafts);
        enemies.addAll(yellowMonsters);
        enemies.addAll(pars);
        enemies.addAll(trampolines);
        return enemies;
    }
}
