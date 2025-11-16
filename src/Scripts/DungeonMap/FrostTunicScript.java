package Scripts.DungeonMap;

import Level.Player;
import Level.Script;
import Level.ScriptState;
import Players.Bee;
import ScriptActions.*;

import java.util.ArrayList;

public class FrostTunicScript extends Script {


    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedFrostTunic", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Frost tunic acquired!");
                }});

                addScriptAction(new FrostTunicScriptAction());

                addScriptAction(new ChangeFlagScriptAction("collectedFrostTunic", true));
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
