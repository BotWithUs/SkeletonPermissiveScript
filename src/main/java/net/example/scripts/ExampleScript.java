package net.example.scripts;

import net.botwithus.imgui.ImGui;
import net.botwithus.rs3.entities.LocalPlayer;
import net.botwithus.rs3.entities.PathingEntity;
import net.botwithus.rs3.entities.SceneObject;
import net.botwithus.rs3.interfaces.Component;
import net.botwithus.scripts.Info;
import net.botwithus.ui.workspace.Workspace;
import net.botwithus.xapi.script.BwuScript;
import net.example.scripts.game.Tree;
import net.example.scripts.state.FiremakingState;

import java.util.Arrays;

@Info(name = "Permissive Script", description = "Example Permissive Script", author = "Sudo", version = "1.0")
public class ExampleScript extends BwuScript {

    public Tree tree = Tree.NORMAL;
    public String treeOption = "Chop down";
    public Component tinderboxComp = null;
    public SceneObject treeObj, fireObj;
    public PathingEntity fireSprite;

    public LocalPlayer player;

    public ExampleScript() {
        // Define the states of the script
        super(new FiremakingState("Woodcutting", ),
            new State("Firemaking", null));
    }

    @Override
    public void onDraw(Workspace workspace) {
        super.onDraw(workspace);
    }

    @Override
    public void onDrawConfig(Workspace workspace) {
        // Draw the configuration panel
        if(ImGui.beginTabItem("Config", 0)) {
            var treeIndex = ImGui.combo("Tree", tree.ordinal(), Arrays.stream(Tree.values()).map(Tree::getTreeName).toArray(String[]::new), 30);
            tree = Tree.values()[treeIndex];
        }
    }

    @Override
    public boolean onPreTick() {
        player = LocalPlayer.self();

        if (player == null) {
            return false;
        }

        return super.onPreTick();
    }
}
