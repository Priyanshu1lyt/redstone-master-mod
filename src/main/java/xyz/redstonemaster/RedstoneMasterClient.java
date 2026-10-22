package xyz.redstonemaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.List;

public class RedstoneMasterClient implements ClientModInitializer {
    private static final ArrayDeque<String> PASTE_QUEUE = new ArrayDeque<>();
    private KeyBinding openMenuKey;

    /** Queue build commands; they are sent a few per tick so servers don't kick for spam. */
    public static void queuePaste(List<String> commands) {
        PASTE_QUEUE.addAll(commands);
    }

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.redstone_master.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new RedstoneMasterScreen());
                }
            }

            if (client.player == null) {
                PASTE_QUEUE.clear();
                return;
            }
            int sent = 0;
            while (sent < 4 && !PASTE_QUEUE.isEmpty()) {
                client.player.networkHandler.sendChatCommand(PASTE_QUEUE.poll());
                sent++;
            }
            if (sent > 0 && PASTE_QUEUE.isEmpty()) {
                client.player.sendMessage(Text.literal("Redstone Master: build pasted!"), false);
            }
        });
    }
}
