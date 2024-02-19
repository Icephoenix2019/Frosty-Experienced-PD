package com.shatteredpixel.shatteredpixeldungeon.items.spellbook;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class BookOfEarth extends SpellBook {

    {
        image = ItemSpriteSheet.BOOK_OF_EARTH;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_READ)) {
            if (hero.buff(SpellBookCoolDown.class) != null) {
                GLog.w(Messages.get(this, "cooldown"));
            } else {
                 Buff.affect(hero, SpellBookCoolDown.class).set(35);
                readEffect();
            }
        }
    }

    @Override
    public void readEffect() {
        Buff.affect(Dungeon.hero, Earthroot.Armor.class).level(Dungeon.hero.HT);
        Buff.affect(Dungeon.hero, Barkskin.class).set(Math.round(Dungeon.hero.lvl/2f*(1 + 0.5f )), 1);
    }

    @Override
    public String info() {
        String info = super.info();
        if (Dungeon.hero.buff(SpellBookCoolDown.class) == null) {
            info += "\n\n" + Messages.get(this, "time",
                    Dungeon.hero.HT,
                    Math.round(Dungeon.hero.lvl/2f*(1 + 0.5f )));
        }
        return info;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{WandOfLivingEarth.class};
            inQuantity = new int[]{1};

            cost = 5;

            output = BookOfEarth.class;
            outQuantity = 1;
        }

    }
}
