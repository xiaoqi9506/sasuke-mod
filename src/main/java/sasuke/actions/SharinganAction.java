package sasuke.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

/**
 * @author xiaoqi
 * @date 2020/06/04
 */
public class SharinganAction extends AbstractGameAction {

    private AbstractPlayer p;

    public SharinganAction(AbstractPlayer p) {
        this.p = p;
    }

    @Override
    public void update() {
        p.hand.group.forEach(card->{
            card.updateCost(card.cost);
        });
    }
}
