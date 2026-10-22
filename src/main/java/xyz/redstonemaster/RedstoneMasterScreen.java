package xyz.redstonemaster;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.List;

public class RedstoneMasterScreen extends Screen {
    private TextFieldWidget searchBox;
    private String category = "All";
    private int selected = 0;
    private int scroll = 0;
    private List<BuildEntry> filtered = BuildLibrary.BUILDS;

    public RedstoneMasterScreen() {
        super(Text.literal("Redstone Master"));
    }

    @Override
    protected void init() {
        int left = 16;
        searchBox = new TextFieldWidget(textRenderer, left, 32, 190, 20, Text.literal("Search builds"));
        searchBox.setPlaceholder(Text.literal("Search builds..."));
        searchBox.setChangedListener(s -> refresh());
        addDrawableChild(searchBox);

        String[] cats = {"All", "Doors", "Farms", "Machines", "Robots", "Command Builds"};
        int x = 218;
        for (String cat : cats) {
            int w = cat.equals("Command Builds") ? 105 : 65;
            addDrawableChild(ButtonWidget.builder(Text.literal(cat), b -> {
                category = cat;
                selected = 0;
                scroll = 0;
                refresh();
            }).dimensions(x, 32, w, 20).build());
            x += w + 4;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(width - 72, height - 28, 60, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("↑"), b -> { scroll = Math.max(0, scroll - 1); })
                .dimensions(width - 100, 60, 20, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("↓"), b -> { scroll = Math.min(Math.max(0, filtered.size()-1), scroll + 1); })
                .dimensions(width - 76, 60, 20, 20).build());
        refresh();
    }

    private void refresh() {
        String q = searchBox == null ? "" : searchBox.getText().toLowerCase();
        filtered = BuildLibrary.BUILDS.stream()
                .filter(b -> category.equals("All") || b.category().equals(category))
                .filter(b -> b.name().toLowerCase().contains(q)
                        || b.description().toLowerCase().contains(q)
                        || b.category().toLowerCase().contains(q))
                .toList();
        if (selected >= filtered.size()) selected = Math.max(0, filtered.size()-1);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(8, 8, width - 8, height - 8, 0xE8111724);
        context.drawCenteredTextWithShadow(textRenderer, "REDSTONE MASTER", width / 2, 12, 0xFFFF5555);
        context.drawTextWithShadow(textRenderer, "Search • Categories • Tutorials • Command builds", 16, 58, 0xFFB8D9FF);

        int listTop = 86;
        int listBottom = height - 38;
        context.fill(14, listTop, Math.min(220, width / 2), listBottom, 0xFF202B3A);
        int visible = Math.max(1, (listBottom - listTop) / 22);
        for (int i = 0; i < visible; i++) {
            int idx = i + scroll;
            if (idx >= filtered.size()) break;
            BuildEntry entry = filtered.get(idx);
            int y = listTop + i * 22;
            if (idx == selected) context.fill(16, y, Math.min(218, width / 2 - 2), y + 20, 0xFF3A526E);
            context.drawTextWithShadow(textRenderer, entry.name(), 20, y + 6, 0xFFFFFFFF);
        }

        int panelX = Math.min(232, width / 2 + 4);
        int panelRight = width - 18;
        context.fill(panelX, listTop, panelRight, listBottom, 0xFF202B3A);
        if (filtered.isEmpty()) {
            context.drawTextWithShadow(textRenderer, "No builds found. Add entries to BuildLibrary.java.", panelX + 12, listTop + 12, 0xFFFFCC66);
        } else {
            BuildEntry entry = filtered.get(selected);
            int y = listTop + 12;
            context.drawTextWithShadow(textRenderer, entry.name(), panelX + 12, y, 0xFFFF7777);
            y += 18;
            context.drawTextWithShadow(textRenderer, "Category: " + entry.category() + " | Difficulty: " + entry.difficulty(), panelX + 12, y, 0xFFB8D9FF);
            y += 18;
            y = drawWrapped(context, entry.description(), panelX + 12, y, panelRight - panelX - 24, 0xFFFFFFFF);
            y += 8;
            context.drawTextWithShadow(textRenderer, "Materials:", panelX + 12, y, 0xFF77FFAA);
            y += 14;
            for (String material : entry.materials()) {
                if (y > listBottom - 85) break;
                context.drawTextWithShadow(textRenderer, "• " + material, panelX + 12, y, 0xFFFFFFFF);
                y += 12;
            }
            y += 5;
            if (y < listBottom - 60) {
                context.drawTextWithShadow(textRenderer, "Steps:", panelX + 12, y, 0xFF77FFAA);
                y += 14;
                for (int i = 0; i < entry.steps().size() && y < listBottom - 28; i++) {
                    String step = (i + 1) + ". " + entry.steps().get(i);
                    y = drawWrapped(context, step, panelX + 12, y, panelRight - panelX - 24, 0xFFFFFFFF) + 3;
                }
            }
            context.drawTextWithShadow(textRenderer, entry.commandNote(), panelX + 12, listBottom - 20, 0xFFFFCC66);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private int drawWrapped(DrawContext context, String text, int x, int y, int maxWidth, int color) {
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (textRenderer.getWidth(test) > maxWidth && line.length() > 0) {
                context.drawTextWithShadow(textRenderer, line.toString(), x, y, color);
                y += 11;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) {
            context.drawTextWithShadow(textRenderer, line.toString(), x, y, color);
            y += 11;
        }
        return y;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (searchBox != null && searchBox.isFocused()) {
            return super.keyPressed(input);
        }
        int keyCode = input.key();
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_UP) {
            selected = Math.max(0, selected - 1); return true;
        }
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN) {
            selected = Math.min(Math.max(0, filtered.size()-1), selected + 1); return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() { return false; }
  }
