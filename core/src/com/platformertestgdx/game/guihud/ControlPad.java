package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ControlPad extends Touchpad {

    float alpha;

    public ControlPad(float deadZone, Skin skin, float alpha) {
        super(deadZone, skin);
        this.alpha = alpha;
    }

    public int getDirection() {
        int direction;
        float dx = getKnobPercentX();
        float dy = getKnobPercentY();
        if (dy+dx != 0) direction = (int)(Math.floor((Math.atan2(dy, dx) + Math.PI/8) / (2*Math.PI/8)));
        else direction = -1;
        if (direction == 8) direction = 0;
        return direction;
    }

    public double getAngle(int direction) {
        double angle = direction * (Math.PI/4);
        return angle;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * alpha);

        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();
        float dx = getKnobPercentX();
        float dy = getKnobPercentY();

//        System.out.println("Knob before draw" + "  Knob X:" + getKnobX() + "   Knob Y:" + getKnobY());
//        System.out.println("Knob before draw" + "  Knob %X:" + getKnobPercentX() + "   Knob %Y:" + getKnobPercentY());

        final Drawable bg = getStyle().background;
        if (bg != null) bg.draw(batch, x, y, w, h);


        int direction = (int)(Math.floor((Math.atan2(dy, dx) + Math.PI/8) / (2*Math.PI/8)));
        if (direction == 8) direction = 0;
        double angle = direction * (Math.PI/4);
//        System.out.println("Direction: "+ direction +"   Angle: "+ angle);

        final Drawable knob = getStyle().knob;
        if (knob != null) {
            if(dx+dy != 0) {
                x += (knob.getMinWidth() + knob.getMinWidth()*(Math.cos(angle)))/2f;
                y += (knob.getMinHeight() + knob.getMinWidth()*(Math.sin(angle)))/2f;
            }
            else {
                x += getKnobX() - knob.getMinWidth() / 2f;
                y += getKnobY() - knob.getMinHeight() / 2f;
            }
            knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
        }


//        System.out.println("Knob After draw" + "  Knob %X:" + getKnobX() + "   Knob %Y:" + getKnobY());
//        System.out.println("Knob After draw" + "  Knob %X:" + getKnobPercentX() + "   Knob %Y:" + getKnobPercentY());
    }
}
