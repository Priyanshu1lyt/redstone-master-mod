package xyz.redstonemaster;

import java.util.List;

public record BuildEntry(
        String name, String category, String difficulty, String description,
        List<String> steps, List<String> materials, String commandNote,
        List<String> paste
) {
    public BuildEntry(String name, String category, String difficulty, String description,
                      List<String> steps, List<String> materials, String commandNote) {
        this(name, category, difficulty, description, steps, materials, commandNote, List.of());
    }

    public boolean canPaste() {
        return paste != null && !paste.isEmpty();
    }
}
