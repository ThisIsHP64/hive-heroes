package Scripts.MazeMap;

import Level.Script;

import ScriptActions.*;
import Scripts.TeleportScriptActions.TeleportGrassScriptAction;

import java.util.ArrayList;

public class RedTunicScript extends Script {


    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedRedTunic", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Volcanic tunic acquired! Press 3 to activate.");
                    addText("When active, you will be impeccable to the \nstorms of burning earth.");
                }});

                addScriptAction(new RedTunicScriptAction());

                addScriptAction(new ChangeFlagScriptAction("collectedRedTunic", true));
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
