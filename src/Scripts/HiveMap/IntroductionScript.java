package Scripts.HiveMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class IntroductionScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Welcome to Hive Heroes, a 2D RPG style \nadventure game.");
            addText("For directions on how to play, interact with the \nQueen Bee using the SPACE key.");
            addText("If you ever need reminders of how to play, \ninteract with the Walrus guard.");
        }});

        scriptActions.add(new ChangeFlagScriptAction("introductionAdministered", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
