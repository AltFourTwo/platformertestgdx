package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.platformertestgdx.game.gameentities.Player;

public class HeadsUpDisplay {

    private HitPointBar hpbar;
    private PointBar somebar;


    public HeadsUpDisplay(Stage stage, Skin skin, Player player, float barsAlpha, float someotherbarAlpha, float minimapAlpha) {

        hpbar = new HitPointBar(0, player.getMaxHP(), player.getCurrentHP(), 1, skin, barsAlpha);
        player.setHpBar(hpbar);

        somebar = new PointBar(0, player.getMaxPower(), player.getPower(), 1, skin, someotherbarAlpha);
        player.setOtherbar(somebar);

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.align(Align.topLeft);

        table.pad(8f,8f,8f,8f);
        table.add(hpbar)
                .size(hpbar.getPrefWidth()*player.getMaxHP()/12,
                        hpbar.getPrefHeight()*1)
                .pad(8f)
                .align(Align.left);
        table.row();
        table.add(somebar)
                .size(somebar.getPrefWidth()*player.getMaxPower()/4,
                        somebar.getPrefHeight()*1)
                .pad(8f)
                .align(Align.left);
        stage.addActor(table);
    }

}
