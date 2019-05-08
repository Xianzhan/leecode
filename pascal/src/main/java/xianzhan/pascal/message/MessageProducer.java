package xianzhan.pascal.message;

/**
 * @author xianzhan
 * @since 2019-05-07
 */
public interface MessageProducer {
    /**
     * Add a listener to the listener list.
     *
     * @param listener the listener to add.
     */
    void addMessageListener(MessageListener listener);

    /**
     * Remove a listener from the listener list.
     *
     * @param listener the listener to remove.
     */
    void removeMessageListener(MessageListener listener);

    /**
     * Notify listeners after setting the message.
     *
     * @param message the message to set.
     */
    void sendMessage(Message message);
}
