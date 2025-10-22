package Scripts.HiveMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;

public class QueenBeeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Rise, brave one of the hive.");
                    addText("You are the spark that will light the sky once more. ");
                    addText("Beyond the plains lie uncharted lands, filled with \ndangers and resources.");
                    addText("Claim its nectar, and our song will echo through the \nfields again!");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("In order to attack your foes, press the SPACE key.");
                    addText("Use WASD to move and outsmart your enemies!");
                    addText("Press E to claim potent powerups, and press 1 to \nactivate them.");
                    addText("To extract nectar from flowers, or to enter worlds, \npress SPACE near them!");
                }});

                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));
                addScriptAction(new TextboxScriptAction("I sure love doing walrus things!"));
            }});
        }});
        

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
