package sasuke.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sasuke.Sasuke;
import sasuke.characters.SasukeCharacter;

import static sasuke.Sasuke.makeCardPath;

/**
 * 写轮眼
 *
 * @author xiaoqi
 */
public class SharinganCard extends AbstractDynamicCard {

    public static final String ID = Sasuke.makeID(SharinganCard.class.getSimpleName());
    public static final String IMG = makeCardPath("Power.png");
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = SasukeCharacter.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    private static final int MAGIC = 2;

    public SharinganCard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        this.tags.add(BaseModCardTags.FORM);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //所有攻击牌伤害加倍，费用加倍
        CardGroup attacks = p.hand.getAttacks();
        if (!attacks.isEmpty()) {
            attacks.group.forEach(card -> {
                card.damage *= magicNumber;
                card.cost *= magicNumber;
            });
        }
//        AbstractDungeon.actionManager.addToBottom(
//                new ApplyPowerAction(p, p, new RarePower(p, p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}