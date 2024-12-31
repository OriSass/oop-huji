package pepse.world.avatar.jump;

/**
 * An interface for objects that observe jump events.
 */
public interface JumpObserver {
    /**
     * Called when a jump event occurs.
     */
    void onJump();
}
