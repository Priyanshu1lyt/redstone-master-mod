package xyz.redstonemaster;

import java.util.List;

public record BuildEntry(
        String name, String category, String difficulty, String description,
        List<String> steps, List<String> materials, String commandNote
) {}
