package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class Cat3Script extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("Meow! Do you see all those purple flowers?");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("They used to be sunflowers, but I brewed a potion\nthat changed them into something else!");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("I also lost some of my power-up and shield potions\nwhile experimenting...");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("Not that it mattered none of them worked on me\nanyway.");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("If you find any, just press E to pick them up!");
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
