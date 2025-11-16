package Scripts.MazeMap;

import Level.Player;
import Level.Script;
import Level.ScriptState;
import Players.Bee;
import ScriptActions.*;
import Scripts.DungeonMap.FrostTunicScriptAction;
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
                    addText("Volcanic tunic acquired!");
                }});

                addScriptAction(new RedTunicScriptAction());

                addScriptAction(new ChangeFlagScriptAction("collectedRedTunic", true));
            }});
        }});

        scriptActions.add(new TeleportGrassScriptAction());

        return scriptActions;
    }
}
