package net.example.scripts.state;

import net.botwithus.rs3.entities.PathingEntity;
import net.botwithus.rs3.interfaces.Component;
import net.botwithus.rs3.interfaces.ComponentType;
import net.botwithus.rs3.interfaces.InterfaceManager;
import net.botwithus.rs3.world.Distance;
import net.botwithus.util.Rand;
import net.botwithus.xapi.hud.inventory.Backpack;
import net.botwithus.xapi.query.ComponentQuery;
import net.botwithus.xapi.query.NpcQuery;
import net.botwithus.xapi.query.SceneObjectQuery;
import net.botwithus.xapi.script.BwuScript;
import net.botwithus.xapi.script.permissive.Interlock;
import net.botwithus.xapi.script.permissive.Permissive;
import net.botwithus.xapi.script.permissive.node.Branch;
import net.botwithus.xapi.script.permissive.node.LeafNode;
import net.botwithus.xapi.script.permissive.node.leaf.InteractiveLeaf;
import net.example.scripts.ExampleScript;

public class FiremakingState extends BwuScript.State{
    private ExampleScript script;

    public FiremakingState(String name, ExampleScript script) {
        super(name);
        this.script = script;

        setNode(backpackLacksLogs);
    }

    // Leafs
    private InteractiveLeaf<PathingEntity> captureFireSpriteLeaf = new InteractiveLeaf<>(script, "captureFireSpriteLeaf", () -> script.delayUntil(() -> script.player.getAnimationId() != -1, Rand.nextInt(1, 6)));
    private InteractiveLeaf<Component> selectToolLeaf = new InteractiveLeaf<>(script, "selectToolLeaf", () -> script.delayUntil(() -> !InterfaceManager.isOpen(1179), Rand.nextInt(1, 6)));
    private LeafNode toWoodcuttingStateLeaf = new LeafNode(script, "toWoodcuttingStateLeaf", () -> {
        script.setCurrentState("Woodcutting");
        return true;
    });
    private LeafNode isFiremakingLeaf = new LeafNode(script, "isFiremakingLeaf", () -> script.setStatus("Currently firemaking"));

    // Branches
    private Branch isFireNearby = new Branch(script, "isFireNearby", useLogsOnFireLeaf, lightLogsLeaf,
            new Interlock("isFireNearby",
                    new Permissive( "fireIsNotNull", () -> {
                        script.fireObj = SceneObjectQuery.newQuery().name("Fire").results().nearest();
                        return script.fireObj != null;
                    }),
                    new Permissive("fireIsNearby", () -> Distance.to(script.fireObj) < 6)));

    private Branch isStartInterfaceOpen = new Branch(script, "isStartInterfaceOpen", startFireLeaf, isFireNearby,
            new Interlock("isStartInterfaceOpen",
                    new Permissive("isStartInterfaceOpen", () -> InterfaceManager.isOpen(1179)),
                    new Permissive("componentIsNotNull", () -> {
                        script.tinderboxComp = ComponentQuery.newQuery(1179).type(ComponentType.LAYER).id(12).option("Select").results().first();
                        selectToolLeaf.setTarget(script.tinderboxComp);
                        selectToolLeaf.setOptionText("Select");
                        return script.tinderboxComp != null;
                    })));

    private Branch isChooseToolInterfaceOpen = new Branch(script, "isChooseToolInterfaceOpen", selectToolLeaf, isStartInterfaceOpen,
            new Interlock("isChooseToolInterfaceOpen",
                    new Permissive("isChooseToolInterfaceOpen", () -> InterfaceManager.isOpen(1179)),
                    new Permissive("componentIsNotNull", () -> {
                        script.tinderboxComp = ComponentQuery.newQuery(1179).type(ComponentType.LAYER).id(12).option("Select").results().first();
                        selectToolLeaf.setTarget(script.tinderboxComp);
                        selectToolLeaf.setOptionText("Select");
                        return script.tinderboxComp != null;
                    })));

    private Branch isFiremaking = new Branch(script, "isFiremaking", isFiremakingLeaf, isChooseToolInterfaceOpen,
            new Interlock("isFiremaking",
                    new Permissive("isFiremaking", () -> script.player.getAnimationId() != -1)));

    private Branch isFireSpriteNearby = new Branch(script, "isFireSpriteNearby", captureFireSpriteLeaf, isFiremaking,
            new Interlock("isFireSpriteNearby",
                    new Permissive("isFireSpriteNearby", () -> {
                        script.fireSprite = NpcQuery.newQuery().name("Fire sprite").results().nearest();
                        captureFireSpriteLeaf.setTarget(script.fireSprite);
                        captureFireSpriteLeaf.setOptionText("Capture");
                        return script.fireSprite != null;
                    })));

    private Branch backpackLacksLogs = new Branch(script, "backpackLacksLogs", toWoodcuttingStateLeaf, isFireSpriteNearby,
            new Interlock("backpackLacksLogs",
                    new Permissive("backpackLacksLogs", () -> !Backpack.contains(script.tree.getLogName()))));
}
