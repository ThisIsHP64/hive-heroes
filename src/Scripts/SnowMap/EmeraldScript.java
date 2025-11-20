package Scripts.SnowMap;

import Level.Script;
import ScriptActions.*;
import Scripts.MazeMap.RedTunicScriptAction;
import Scripts.TeleportScriptActions.TeleportGrassScriptAction;
import Utils.Visibility;

import java.util.ArrayList;

public class EmeraldScript extends Script {

    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedGreenEmerald", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Chaos Emerald...");
                    addText("Its power floods you—too much…far too much.");
                    addText("Your head splits with a pounding voice that isn’t \nyour own.");
                    addText("I… I must return this to the Queen…");
                    addText("Yes…return it…at once...");
                }});

                addScriptAction(new NPCChangeVisibilityScriptAction(Visibility.HIDDEN));

                addScriptAction(new ChangeFlagScriptAction("collectedGreenEmerald", true));

                addScriptAction(new CollectGreenEmeraldScriptAction());
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }



}
