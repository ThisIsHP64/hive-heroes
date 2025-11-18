package Scripts.HiveMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;

public class FinalBossDescription extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new TextboxScriptAction() {{
            addText("This is your emergency Bee senses! It appears \nwe've been tricked by the Queen Bee.");
            addText("The fate of all bee-kind is at stake here. \nYou will need to use everything you have.");
            addText("You'll be subject to three rounds of suffering.\nVolcano, then Snow, then Grass.");
            addText("The Queen Bee will manipulate the monsters and \nweather, so make sure to use your tunics!");
            addText("Don't give up, no matter what!");
        }});

        return scriptActions;
    }
    
}
