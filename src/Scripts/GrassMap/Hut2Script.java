package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class Hut2Script extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("**Hmm... the scent of strange potions drifts from\nwithin...**");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("**If I'm not mistaken, this must be the Kitwitch's\nhut...**");
                }});

            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
