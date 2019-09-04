package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class HitPointBar extends PointBar implements Disableable {

    private HitPointBarStyle style;

    private float value2;
    private float position2;

    public HitPointBar(int min, int max, int current, float stepSize, Skin skin, float alpha) {
        super(min, max, current, stepSize, alpha);
        setStyle(skin.get("default", HitPointBarStyle.class));
    }

    public HitPointBar(int min, int max, int current, float stepSize, Skin skin, String styleName, float alpha) {
        super(min, max, current, stepSize, alpha);
        setStyle(skin.get(styleName, HitPointBarStyle.class));
    }

    public void setStyle (HitPointBarStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /** Returns the hit point bar style. Modifying the returned style may not have an effect until
     * {@link #setStyle(HitPointBarStyle)} is called. */
    public HitPointBarStyle getStyle () {
        return style;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        HitPointBarStyle style = this.style;
        boolean disabled = this.disabled;
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        final Drawable ol = (disabled && style.disabledoverlay != null) ? style.disabledoverlay: style.overlay;
        final Drawable knob1 = getKnobDrawable();
        final Drawable knob1Before = (disabled && style.disabledknob1before != null) ? style.disabledknob1before : style.knob1before;
        final Drawable knob2 = getKnob2Drawable();
        final Drawable knob2Before = (disabled && style.disabledknob2before != null) ? style.disabledknob2before : style.knob2before;

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        float knob1Height = knob1 == null ? 0 : knob1.getMinHeight();
        float knob1Width = knob1 == null ? 0 : knob1.getMinWidth();
        float knob2Height = knob2 == null ? 0 : knob2.getMinHeight();
        float knob2Width = knob2 == null ? 0 : knob2.getMinWidth();
        float percent1 = getVisualPercent1();
        float percent2 = getVisualPercent2();

        batch.setColor(color.r, color.g, color.b, color.a * alpha);

        float positionWidth = width;

        float bgLeftWidth = 0;
        if (bg != null) {
            if (round)
                bg.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
            else
                bg.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, width, bg.getMinHeight());
            bgLeftWidth = bg.getLeftWidth();
            positionWidth -= bgLeftWidth + bg.getRightWidth();

        }

        float knob1WidthHalf = 0;
        float knob2WidthHalf = 0;
        if (min != max) {
            if (knob1 == null) {
                knob1WidthHalf = knob1Before == null ? 0 : knob1Before.getMinWidth() * 0.5f;
                position1 = (positionWidth - knob1WidthHalf) * percent1;
                position1 = Math.min(positionWidth - knob1WidthHalf, position1);
            } else {
                knob1WidthHalf = knob1Width * 0.5f;
                position1 = (positionWidth - knob1Width) * percent1;
                position1 = Math.min(positionWidth - knob1Width, position1) + bgLeftWidth;
            }
            if (knob2 == null) {
                knob2WidthHalf = knob2Before == null ? 0 : knob2Before.getMinWidth() * 0.5f;
                position2 = (positionWidth - knob2WidthHalf) * percent2;
                position2 = Math.min(positionWidth - knob1WidthHalf, position2);
                position2 = Math.max(position2, position1);
            } else {
                knob2WidthHalf = knob2Width * 0.5f;
                position2 = (positionWidth - knob2Width) * percent2;
                position2 = Math.min(positionWidth - knob2Width, position2) + bgLeftWidth;
                position2 = Math.max(position2, position1);
            }
            position1 = Math.max(0, position1);
        }

        if (knob2Before != null) {
            float offset = 0;
            if (bg != null) offset = bgLeftWidth;
            if (round)
                knob2Before.draw(batch, Math.round(x + offset), Math.round(y + (height - knob2Before.getMinHeight()) * 0.5f),
                        Math.round(position2 + knob2WidthHalf), Math.round(knob2Before.getMinHeight()));
            else
                knob2Before.draw(batch, x + offset, y + (height - knob2Before.getMinHeight()) * 0.5f,
                        position2 + knob2WidthHalf, knob2Before.getMinHeight());
        }
        if (knob2 != null) {
            if (round)
                knob2.draw(batch, Math.round(x + position2), Math.round(y + (height - knob2Height) * 0.5f), Math.round(knob2Width), Math.round(knob2Height));
            else
                knob2.draw(batch, x + position2, y + (height - knob2Height) * 0.5f, knob2Width, knob2Height);
        }

        if (knob1Before != null) {
            float offset = 0;
            if (bg != null) offset = bgLeftWidth;
            if (round)
                knob1Before.draw(batch, Math.round(x + offset), Math.round(y + (height - knob1Before.getMinHeight()) * 0.5f),
                        Math.round(position1 + knob1WidthHalf), Math.round(knob1Before.getMinHeight()));
            else
                knob1Before.draw(batch, x + offset, y + (height - knob1Before.getMinHeight()) * 0.5f,
                        position1 + knob1WidthHalf, knob1Before.getMinHeight());
        }
        if (knob1 != null) {
            if (round)
                knob1.draw(batch, Math.round(x + position1), Math.round(y + (height - knob1Height) * 0.5f), Math.round(knob1Width), Math.round(knob1Height));
            else
                knob1.draw(batch, x + position1, y + (height - knob1Height) * 0.5f, knob1Width, knob1Height);
        }

        if (ol != null) {
            if (round)
                ol.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
            else
                ol.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, ol.getMinWidth(), ol.getMinHeight());
        }

    }

    public float getValue2 () {
        return value2;
    }

    /** If {@link #setAnimateDuration(float) animating} the progress bar value1, this returns the value1 current displayed. */
    public float getVisualValue2 () {
        if (animateTime > 0) return animateInterpolation.apply(animateFromValue, value2, 1 - animateTime / animateDuration);
        return value2;
    }

    public float getPercent2 () {
        return (value2 - min) / (max - min);
    }

    public float getVisualPercent2 () {
        return visualInterpolation.apply((getVisualValue2() - min) / (max - min));
    }

    @Override
    public Drawable getKnobDrawable () {
        return (disabled && style.disabledknob1 != null) ? style.disabledknob1 : style.knob1;
    }
    public Drawable getKnob2Drawable () {
        return (disabled && style.disabledknob2 != null) ? style.disabledknob2 : style.knob2;
    }

    /** Returns progress bar visual position1 within the range. */
    public float getKnob2Position () {
        return this.position2;
    }

    /** Sets the progress bar position1, rounded to the nearest step size and clamped to the minimum and maximum values.
     * {@link #clamp(float)} can be overridden to allow values outside of the progress bar's min/max range.
     * @return false if the value1 was not changed because the progress bar already had the value1 or it was canceled by a
     *         listener. */
    public boolean setValue2(float value) {
        value = clamp(Math.round(value / stepSize) * stepSize);
        float oldValue = this.value2;
        if (value == oldValue) return false;
        float oldVisualValue = getVisualValue2();
        this.value2 = value;
        if (animateDuration > 0) {
            animateFromValue = oldVisualValue;
            animateTime = animateDuration;
        }
        return true;
    }

    @Override
    public float getPrefWidth () {
        final Drawable knob = getKnobDrawable();
        final Drawable knob2 = getKnob2Drawable();
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        return Math.max(Math.max(knob == null ? 0 : knob.getMinWidth(), knob2 == null ? 0 : knob2.getMinWidth()), bg.getMinWidth());
    }

    @Override
    public float getPrefHeight () {
        final Drawable knob = getKnobDrawable();
        final Drawable knob2 = getKnob2Drawable();
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        return Math.max(Math.max(knob == null ? 0 : knob.getMinHeight(), knob2 == null ? 0 : knob2.getMinHeight()), bg.getMinHeight());
    }

    // TODO author name
    /** The style for a points bar (eg : hp bar), see {@link HitPointBar}.
     * @author me
     */
    static public class HitPointBarStyle extends PointBarStyle {
        /** Optional, centered on the background. */
        public Drawable knob2, disabledknob2;
        /** Optional. */
        public Drawable knob2before, disabledknob2before;

        public HitPointBarStyle () {
        }

        public HitPointBarStyle (HitPointBarStyle style) {
            this.background = style.background;
            this.overlay = style.overlay;
            this.disabledbackground = style.disabledbackground;
            this.disabledoverlay = style.disabledoverlay;
            this.knob1 = style.knob1;
            this.disabledknob1 = style.disabledknob1;
            this.knob1before = style.knob1before;
            this.disabledknob1before = style.disabledknob1before;
            this.knob2 = style.knob2;
            this.disabledknob2 = style.disabledknob2;
            this.knob2before = style.knob2before;
            this.disabledknob2before = style.disabledknob2before;
        }
    }

}
