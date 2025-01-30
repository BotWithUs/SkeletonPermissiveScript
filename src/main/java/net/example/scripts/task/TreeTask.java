package net.example.scripts.task;

import com.google.gson.JsonObject;
import net.botwithus.xapi.script.task.AbstractTask;
import net.example.scripts.game.Tree;

public class TreeTask extends AbstractTask<Tree> {

    public TreeTask(Tree target, int currentCount, int completeCount) {
        super(target, currentCount, completeCount);
    }

}
