package xianzhan.tank.client.event;

import xianzhan.tank.client.bean.Missile;

/**
 * 坦克被击事件
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public class TankHitEvent {
    private Missile source;

    public TankHitEvent(Missile source) {
        this.source = source;
    }

    public Missile getSource() {
        return source;
    }

    public void setSource(Missile source) {
        this.source = source;
    }
}
