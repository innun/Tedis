package com.tedis.protocol;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private List<Command> cmds;

    public Commands() {
        cmds = new ArrayList<>();
    }

    public void add(Command cmd) {
        cmds.add(cmd);
    }

    public List<Command> getCmds() {
        return cmds;
    }

    public void setCmds(List<Command> cmds) {
        this.cmds = cmds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Commands{ ");
        for (Command cmd : cmds) {
            sb.append(cmd);
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }
}
