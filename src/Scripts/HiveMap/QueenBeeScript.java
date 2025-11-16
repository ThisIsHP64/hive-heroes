package Scripts.HiveMap;

import Level.Player;
import Level.Script;
import Level.ScriptState;
import Players.Bee;
import ScriptActions.*;
import java.util.ArrayList;

public class QueenBeeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            // First conversation
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Rise, brave one of the hive.");
                    addText("You are the spark that will light the sky once more.");
                    addText("Beyond the plains lie uncharted lands, filled with\ndangers and resources.");
                    addText("Claim its nectar, and our song will echo through the\nfields again!");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("Use WASD to move and outsmart your enemies!");
                    addText("Press SPACE to attack, E to collect powerups, and\n1 to activate them.");
                    addText("Press SPACE near flowers or NPCs to interact.");
                    addText("Press SPACE near my head to deposit nectar.");
                    addText("Now go, my soldier — follow the path of the flower to\nnew regions and prevail!");
                }});

                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            // Second time — Travel offer (Inferno + Frost)
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Do you need a rundown of your duties?", new String[]{"Yes", "No"});
                }});

                addScriptAction(new ConditionalScriptAction() {{

                    // “Yes” : repeat duties
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

                        // addScriptAction(new TextboxScriptAction() {{
                        //     addText("Do you wish to travel to the furthest lands?", new String[]{"Yes", "No"});
                        // }});

                        // addScriptAction(new ConditionalScriptAction() {{
                        //     // Yes : receive both Red and Blue Tunics
                        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        //         addRequirement(new CustomRequirement() {
                        //             @Override
                        //             public boolean isRequirementMet() {
                        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        //                 return answer == 0;
                        //             }
                        //         });

                        //         addScriptAction(new TextboxScriptAction("Then take these tunics, brave one."));
                        //         addScriptAction(new TextboxScriptAction("The Red Tunic will shield you from flame..."));
                        //         addScriptAction(new TextboxScriptAction("The Blue Tunic will guard you from frost."));
                        //         addScriptAction(new TextboxScriptAction("Press 3 for the Red Tunic, and 4 for the Blue Tunic."));

                        //         addScriptAction(new ScriptAction() {
                        //             @Override
                        //             public ScriptState execute() {
                        //                 Player player = map.getPlayer();
                        //                 if (player instanceof Bee bee) {
                        //                     bee.obtainTunic();      
                        //                     bee.obtainBlueTunic();  
                        //                 }
                        //                 return ScriptState.COMPLETED;
                        //             }
                        //         });
                        //     }});

                        //     // No : still gives them (kind Queen)
                        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        //         addRequirement(new CustomRequirement() {
                        //             @Override
                        //             public boolean isRequirementMet() {
                        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        //                 return answer == 1;
                        //             }
                        //         });

                        //         addScriptAction(new TextboxScriptAction("Take them anyway — you will need their strength soon."));
                        //         addScriptAction(new TextboxScriptAction("Press 3 for the Red Tunic, and 4 for the Blue Tunic."));

                        //         addScriptAction(new ScriptAction() {
                        //             @Override
                        //             public ScriptState execute() {
                        //                 Player player = map.getPlayer();
                        //                 if (player instanceof Bee bee) {
                        //                     bee.obtainTunic();     
                        //                     bee.obtainBlueTunic();   
                        //                 }
                        //                 return ScriptState.COMPLETED;
                        //             }
                        //         });
                        //     }});
                        // }});

                        // addScriptAction(new TextboxScriptAction("Go forth, and let your wings remember the warmth \nand the frost."));
                        addScriptAction(new TextboxScriptAction("Go forth, and fulfill your destiny!"));
                    }});
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
}
