package xianzhan.tank.client.event;

/**
 * 坦克被击中事件监听者(由坦克实现)
 *
 * @author xianzhan
 * @since 2019-05-06
 */
public interface TankHitListener {

    /**
     * 坦克被击事件
     *
     * @param tankHitEvent 事件
     */
    void actionToTankHitEvent(TankHitEvent tankHitEvent);
}
