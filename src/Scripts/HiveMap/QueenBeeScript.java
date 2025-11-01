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
                    addText("Claim its nectar, and our song will echo through \nthe fields again!");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("Use WASD to move and outsmart your enemies!");
                    addText("In order to attack your foes, press the SPACE key \nnear them.");
                    addText("Press E to claim potent powerups, and press 1 to \nactivate them.");
                    addText("To extract nectar from flowers, enter worlds, and to \ninteract with NPCs, press SPACE near them!");
                    addText("To transfer collected nectar to me, press SPACE \nnear my head!");
                    addText("Now go, my soldier follow the path of the flower to \nnew regions and prevail!");
                }});

                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));
                scriptActions.add(new TextboxScriptAction() {{
                    addText("Do you need a rundown of your duties?", new String[] { "Yes", "No" });
                }});

            scriptActions.add(new ConditionalScriptAction() {{
                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new CustomRequirement() {
                        @Override
                        public boolean isRequirementMet() {
                            int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                            return answer == 0;
                        } 
                    });

                    addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", false));

                }});

                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new CustomRequirement() {
                        @Override
                        public boolean isRequirementMet() {
                            int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                            return answer == 1;
                        }
                    });

                    addScriptAction(new TextboxScriptAction("Fulfill your destiny!"));

                    }});
                }});
            }});
        }});
        

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
