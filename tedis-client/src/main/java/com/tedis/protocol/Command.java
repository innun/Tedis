package com.tedis.protocol;

import java.util.List;

public class Command {

    private List<String> parts;

    public Command(List<String> parts) {
        this.parts = parts;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }

    @SuppressWarnings("Duplicates")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Command{ ");
        for (int i = 0; i < parts.size(); i++) {
            sb.append(parts.get(i));
            if (i < parts.size() - 1) {
                sb.append(" ");
            }
        }
        sb.append(" }");
        return sb.toString();
    }
}
