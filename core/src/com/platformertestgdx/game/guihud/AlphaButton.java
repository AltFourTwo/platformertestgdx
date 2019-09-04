package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AlphaButton extends Button {

    public AlphaButton(Skin skin, String stylename) {
        super(skin, stylename);
    }

    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
    }
}
