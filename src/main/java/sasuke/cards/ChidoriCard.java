package sasuke.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sasuke.Sasuke;
import sasuke.characters.SasukeCharacter;

import static sasuke.Sasuke.makeCardPath;

/**
 * 千鸟
 *
 * @author xiaoqi
 */
public class ChidoriCard extends AbstractDynamicCard {

    public static final String ID = Sasuke.makeID("Chidori");

    /**
     * the path to the img for this card(250px x 190px);
     * the path to your larger version of the img (500 x 380p) should be img + "_p" ,
     * so if img is "my_card.png" then the portrait version should be at "my_card_p.png"
     */
    public static final String IMG = makeCardPath("Attack.png");

    /**
     * 稀有度
     */
    private static final CardRarity RARITY = CardRarity.COMMON;

    /**
     * 作用目标
     */
    private static final CardTarget TARGET = CardTarget.ENEMY;

    /**
     * 卡牌类型
     */
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = SasukeCharacter.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DMG = 5;

    public ChidoriCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChidoriCard();
    }
}
