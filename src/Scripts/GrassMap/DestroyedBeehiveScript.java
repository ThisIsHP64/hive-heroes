package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

/**
 * Dialogue script for the Destroyed Beehive NPC.
 * Triggered when the player interacts with the destroyed hive.
 */
public class DestroyedBeehiveScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // lock player movement during dialogue
        scriptActions.add(new LockPlayerScriptAction());

        // makes NPC face the player (even though hive doesn’t animate, keeps consistency)
        scriptActions.add(new NPCFacePlayerScriptAction());

        // dialogue sequence
        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{

                addScriptAction(new TextboxScriptAction() {{
                    addText("*The neighbour hive is shattered*");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*Broken combs. Scattered nectar. Silence \nwhere life once buzzed*");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*But the beast showed no mercy*");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*The sun will shine on us again*");
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
