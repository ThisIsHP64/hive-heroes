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
            addText("Welcome to Hive Heroes, a 2D RPG style adventure \ngame. (Press E to advance.)");

            addText("Use WASD to move and outsmart your enemies!");
            addText("Combine a movement key with SHIFT to sprint! This \nwill consume stamina.");
            addText("Stamina automatically regenerates when you stand \nstill.");

            addText("Press SPACE to melee attack. If you hit successfully, \nthere will be indicators.");
            addText("Press E to collect powerups, and 1 to activate them.");
            addText("Press SPACE near flowers to collect nectar.");
            addText("Press SPACE near the Queen's head to deposit nectar.");

            addText("Press the E/Space key near Doors, Portals, and NPCs \nto interact with them.");

            addText("If you ever need reminders of the directions, interact \nwith the Queen Bee.");
        }});

        scriptActions.add(new ChangeFlagScriptAction("introductionAdministered", true));
        scriptActions.add(new ChangeFlagScriptAction("controlsReviewed", true));


        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
