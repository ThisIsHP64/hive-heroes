package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class CatScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("Meow... I am sorry for what happened to your \nhive.");
                }});
                addScriptAction(new TextboxScriptAction() {{
                    addText("It had very big teeth, sharp claws...");
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

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
