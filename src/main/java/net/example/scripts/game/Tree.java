package net.example.scripts.game;

import com.google.gson.JsonObject;
import net.botwithus.xapi.script.task.SerializableTarget;

public enum Tree implements SerializableTarget {
    NORMAL("Tree", "Logs"),
    OAK("Oak", "Oak logs"),
    WILLOW("Willow", "Willow logs"),
    MAPLE("Maple", "Maple logs"),
    YEW("Yew", "Yew logs"),
    MAGIC("Magic", "Magic logs");

    private final String treeName, logName;

    Tree(String treeName, String logName) {
        this.treeName = treeName;
        this.logName = logName;
    }

    public String getTreeName() {
        return treeName;
    }

    public String getLogName() {
        return logName;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("tree", this.ordinal());
        return json;
    }

    @Override
    public Tree deserialize(JsonObject json) {
        return Tree.values()[json.get("tree").getAsInt()];
    }
}
