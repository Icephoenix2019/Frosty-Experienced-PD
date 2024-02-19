package com.shatteredpixel.shatteredpixeldungeon.items.spellbook;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class SpellBook extends Item {

    public static final int MAGIC_MISSILE   = 0;
    public static final int LIGHTNING       = 1;
    public static final int DISINTEGRATION  = 2;
    public static final int FIREBLAST       = 3;
    public static final int CORROSION       = 4;
    public static final int BLASTWAVE       = 5;
    public static final int LIVINGEARTH     = 6;
    public static final int FROST           = 7;
    public static final int PRISMATICLIGHT  = 8;
    public static final int WARDING         = 9;
    public static final int TRANSFUSION     = 10;
    public static final int CORRUPTION      = 11;
    public static final int REGROWTH        = 12;

    private static final HashMap<Class<?extends Wand>, Integer> spellTypes = new HashMap<>();
    static {
        spellTypes.put(WandOfMagicMissile.class,   MAGIC_MISSILE   );
        spellTypes.put(WandOfLightning.class,      LIGHTNING       );
        spellTypes.put(WandOfDisintegration.class, DISINTEGRATION  );
        spellTypes.put(WandOfFireblast.class,      FIREBLAST       );
        spellTypes.put(WandOfCorrosion.class,      CORROSION       );
        spellTypes.put(WandOfBlastWave.class,      BLASTWAVE       );
        spellTypes.put(WandOfLivingEarth.class,    LIVINGEARTH     );
        spellTypes.put(WandOfFrost.class,          FROST           );
        spellTypes.put(WandOfPrismaticLight.class, PRISMATICLIGHT  );
        spellTypes.put(WandOfWarding.class,        WARDING         );
        spellTypes.put(WandOfTransfusion.class,    TRANSFUSION     );
        spellTypes.put(WandOfCorruption.class,     CORRUPTION      );
        spellTypes.put(WandOfRegrowth.class,       REGROWTH        );
    }

    public SpellBook newSpellBook(int wand) {
        switch (wand) {
            case MAGIC_MISSILE: default:
                return new BookOfMagic();
            case LIGHTNING:
                return new BookOfThunderBolt();
            case DISINTEGRATION:
                return new BookOfDisintegration();
            case FIREBLAST:
                return new BookOfFire();
            case CORROSION:
                return new BookOfCorrosion();
            case BLASTWAVE:
                return new BookOfBlast();
            case LIVINGEARTH:
                return new BookOfEarth();
            case FROST:
                return new BookOfFrost();
            case PRISMATICLIGHT:
                return new BookOfLight();
            case WARDING:
                return new BookOfWarding();
            case TRANSFUSION:
                return new BookOfTransfusion();
            case CORRUPTION:
                return new BookOfCorruption();
            case REGROWTH:
                return new BookOfRegrowth();
        }
    }

    public static final String AC_READ		= "READ";

    {
        defaultAction = AC_READ;

        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_READ)) {
            if (hero.buff(SpellBookCoolDown.class) == null) {
                Invisibility.dispel();
                curUser.busy();
                ((HeroSprite)curUser.sprite).read();
                Sample.INSTANCE.play(Assets.Sounds.READ);
                if (hero.subClass == HeroSubClass.BATTLEMAGE || hero.subClass == HeroSubClass.KING) {
                    Buff.affect(hero, SpellBookEmpower.class).set(Random.Int(100, 200));
                    if (Random.Float() < 0.5f) {
                        MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
                        if (staff != null) {
                            SpellBook sideEffect = newSpellBook(spellTypes.get(staff.wandClass()));
                            sideEffect.readEffect();
                        }
                    }
                }
            }
        }
    }

    //need overriding
    public void readEffect() {}

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public long value() {
        return 300;
    }

    public static class SpellBookCoolDown extends Buff {

        int duration = 0;
        int maxDuration = 0;

        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        public void set(int time) {
            maxDuration = time;
            duration = maxDuration;
        }

        public void hit(int time) {
            duration -= time;
            if (duration <= 0) detach();
        }

        @Override
        public boolean act() {
            duration--;
            if (duration <= 0) {
                detach();
            }
            spend(TICK);
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xD4A04F);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxDuration - duration) / maxDuration);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(duration);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", duration);
        }

        private static final String MAX_DURATION = "maxDuration";
        private static final String DURATION = "duration";
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( MAX_DURATION, maxDuration );
            bundle.put( DURATION, duration );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            maxDuration = bundle.getInt( MAX_DURATION );
            duration = bundle.getInt( DURATION );
        }
    }

    public static class SpellBookEmpower extends Buff {

        int duration = 0;
        int maxDuration = 0;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public void set(int time) {
            maxDuration = time;
            duration = maxDuration;
        }

        @Override
        public boolean act() {
            duration--;
            if (duration <= 0) {
                detach();
            }
            spend(TICK);
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFFFF00);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxDuration - duration) / maxDuration);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(duration);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", duration);
        }

        private static final String MAX_DURATION = "maxDuration";
        private static final String DURATION = "duration";
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( MAX_DURATION, maxDuration );
            bundle.put( DURATION, duration );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            maxDuration = bundle.getInt( MAX_DURATION );
            duration = bundle.getInt( DURATION );
        }
    }

}
