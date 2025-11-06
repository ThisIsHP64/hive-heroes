package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

/**
 * Dialogue script for the Cat NPC.
 * Triggered when the player interacts (usually by pressing SPACE near the Cat).
 */
public class CatScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // lock player movement while dialogue plays
        scriptActions.add(new LockPlayerScriptAction());

        // make the cat face the player
        scriptActions.add(new NPCFacePlayerScriptAction());

        // dialogue sequence (one or more text boxes)
        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("Meow... I am sorry for what happened to your \nhive.");
                }});
                addScriptAction(new TextboxScriptAction() {{
                    addText("Very big teeth, sharp claws...");
                }});
                addScriptAction(new TextboxScriptAction() {{
                    addText("Did not even spare my dear friend deer!");
                }});
                addScriptAction(new TextboxScriptAction() {{
                    addText("Luckily the beast did not notice me..");
                }});
                addScriptAction(new TextboxScriptAction() {{
                    addText("Good luck on your journey!");
                }});
            }});
        }});

        // unlock player after dialogue ends
        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
