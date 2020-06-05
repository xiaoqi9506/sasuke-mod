package sasuke.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sasuke.Sasuke;
import sasuke.cards.*;
import sasuke.relics.DefaultClickableRelic;
import sasuke.relics.PlaceholderRelic;
import sasuke.relics.PlaceholderRelic2;

import java.util.ArrayList;

import static sasuke.Sasuke.THE_DEFAULT_CORPSE;
import static sasuke.Sasuke.THE_DEFAULT_SHOULDER_1;
import static sasuke.Sasuke.THE_DEFAULT_SHOULDER_2;
import static sasuke.Sasuke.THE_DEFAULT_SKELETON_ATLAS;
import static sasuke.Sasuke.THE_DEFAULT_SKELETON_JSON;
import static sasuke.Sasuke.makeID;
import static sasuke.characters.SasukeCharacter.Enums.COLOR_GRAY;

/**
 * 新角色
 *
 * @author xiaoqi
 */
public class SasukeCharacter extends CustomPlayer {

    private static final Logger logger = LogManager.getLogger(Sasuke.class.getName());

    public static class Enums {

        @SpireEnum
        public static AbstractPlayer.PlayerClass PHARMACIST;

        @SpireEnum(name = "DEFAULT_GRAY_COLOR")
        public static AbstractCard.CardColor COLOR_GRAY;

        @SpireEnum(name = "DEFAULT_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== 基础属性 =================

    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int CARD_DRAW = 9;
    private static final int ORB_SLOTS = 3;

    // =============== STRINGS =================

    private static final String ID = makeID("SasukeCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "sasukeResources/images/char/defaultCharacter/orb/layer1.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer2.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer3.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer4.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer5.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer6.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer1d.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer2d.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer3d.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer4d.png",
            "sasukeResources/images/char/defaultCharacter/orb/layer5d.png",};

    public SasukeCharacter(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "sasukeResources/images/char/defaultCharacter/orb/vfx.png", null,
                new SpriterAnimation(
                        "sasukeResources/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null,
                THE_DEFAULT_SHOULDER_2,
                THE_DEFAULT_SHOULDER_1,
                THE_DEFAULT_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));


        // =============== ANIMATIONS =================  

        loadAnimation(THE_DEFAULT_SKELETON_ATLAS, THE_DEFAULT_SKELETON_JSON, 1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    /**
     * 初始牌组
     */
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(DefaultCommonAttack.ID);
        retVal.add(DefaultUncommonAttack.ID);
        retVal.add(DefaultRareAttack.ID);

        retVal.add(DefaultCommonSkill.ID);
        retVal.add(DefaultUncommonSkill.ID);
        retVal.add(DefaultRareSkill.ID);

        retVal.add(DefaultCommonPower.ID);
        retVal.add(DefaultUncommonPower.ID);
        retVal.add(DefaultRarePower.ID);

        retVal.add(DefaultAttackWithVariable.ID);
        retVal.add(DefaultSecondMagicNumberSkill.ID);
        retVal.add(OrbSkill.ID);
        return retVal;
    }

    /**
     * 初始遗物
     */
    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(PlaceholderRelic.ID);
        UnlockTracker.markRelicAsSeen(PlaceholderRelic.ID);
        return retVal;
    }

    /**
     * 角色选择特效
     */
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    /**
     * 角色选择按钮声音
     */
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    /**
     * 卡组颜色
     */
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_GRAY;
    }

    @Override
    public Color getCardTrailColor() {
        return Sasuke.DEFAULT_GRAY;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new DefaultCommonAttack();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new SasukeCharacter(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return Sasuke.DEFAULT_GRAY;
    }

    @Override
    public Color getSlashAttackColor() {
        return Sasuke.DEFAULT_GRAY;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
