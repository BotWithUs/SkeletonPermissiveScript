package net.example.scripts.state;

import net.botwithus.rs3.entities.SceneObject;
import net.botwithus.util.Rand;
import net.botwithus.xapi.hud.inventory.Backpack;
import net.botwithus.xapi.query.SceneObjectQuery;
import net.botwithus.xapi.script.BwuScript;
import net.botwithus.xapi.script.permissive.Interlock;
import net.botwithus.xapi.script.permissive.Permissive;
import net.botwithus.xapi.script.permissive.node.Branch;
import net.botwithus.xapi.script.permissive.node.LeafNode;
import net.botwithus.xapi.script.permissive.node.leaf.InteractiveLeaf;
import net.example.scripts.ExampleScript;


public class WoodcuttingState extends BwuScript.State {
    private ExampleScript script;

    public WoodcuttingState(String name, ExampleScript script) {
        super(name);
        this.script = script;

        setNode(isBackpackFull);
    }

    // Leafs
    private LeafNode isChoppingTreeLeaf = new LeafNode(script, "isChoppingTree", () -> script.setStatus("Chopping " + script.tree.getTreeName()));
    private LeafNode noTreeNearbyLeaf = new LeafNode(script, "noTreeNearbyLeaf", () -> script.setStatus("No tree nearby, walk to a " + script.tree.getTreeName()));
    private LeafNode toFiremakingStateLeaf = new LeafNode(script, "toFiremakingStateLeaf", () -> {
        script.setCurrentState("Firemaking");
        return true;
    });
    private InteractiveLeaf<SceneObject> chopTreeLeaf = new InteractiveLeaf<>(script, "chopTreeLeaf", () -> script.delayUntil(() -> script.player.getAnimationId() != -1, Rand.nextInt(1, 6)));

    // Branches
    private Branch isTreeNearby = new Branch(script, "isTreeNearby", noTreeNearbyLeaf, chopTreeLeaf,
            new Interlock("IsTreeNearby",
                    new Permissive(script, "isTreeNearby", () -> {
                        script.treeObj = SceneObjectQuery.newQuery().name(script.tree.getTreeName()).option(script.treeOption).results().nearest();
                        chopTreeLeaf.setTarget(script.treeObj);
                        chopTreeLeaf.setOptionText("Chop down");
                        return script.treeObj != null;
                    })));

    private Branch isChoppingTree = new Branch(script, "isChoppingTree", isChoppingTreeLeaf, isTreeNearby,
            new Interlock("IsChoppingTree",
                    new Permissive(script, "isChoppingTree", () -> script.player.getAnimationId() != -1)));

    private Branch isBackpackFull = new Branch(script, "isBackpackFull", toFiremakingStateLeaf, isChoppingTree,
            new Interlock("IsBackpackFull",
                    new Permissive(script, "isBackpackFull", Backpack::isFull)));
}
