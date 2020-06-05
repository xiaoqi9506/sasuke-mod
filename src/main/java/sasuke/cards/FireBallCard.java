package sasuke.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sasuke.characters.SasukeCharacter;
import sasuke.Sasuke;

import static sasuke.Sasuke.makeCardPath;

/**
 * 豪火球之术
 *
 * @author xiaoqi
 */
public class FireBallCard extends AbstractDynamicCard {

    public static final String ID = Sasuke.makeID("FireBall");

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

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 2;

    public FireBallCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
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
        return new FireBallCard();
    }
}
