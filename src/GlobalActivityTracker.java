import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class GlobalActivityTracker implements NativeKeyListener, NativeMouseListener {

    private long keystrokeCount = 0;
    private long mouseClickCount = 0;

    public GlobalActivityTracker() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Error registering native hook.");
            ex.printStackTrace();
        }

        // Disable JNativeHook logging.
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);

        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        keystrokeCount++;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) { }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) { }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        mouseClickCount++;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) { }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) { }

    public long getKeystrokeCount() {
        return keystrokeCount;
    }

    public long getMouseClickCount() {
        return mouseClickCount;
    }

    public void shutdown() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }
    }
}
