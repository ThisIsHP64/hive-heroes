package Scripts.HiveMap;

import Level.Script;
import ScriptActions.*;
import Scripts.FinalBoss.LavaRainScriptAction;
import Scripts.FinalBoss.TeleportVolcanoBossScriptAction;
import Scripts.VolcanoMap.CheckBothEmeraldsScriptAction;
import Scripts.VolcanoMap.HasBlueEmeraldScriptAction;
import Scripts.VolcanoMap.HasRedEmeraldScript;
import Scripts.VolcanoMap.SetBossActiveScriptAction;
import Utils.Visibility;

import java.util.ArrayList;

public class QueenBeeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        // player should first only have the green emerald.
        scriptActions.add(new HasGreenEmeraldScriptAction());


        // after returning from the volcano boss fight, check for both emeralds
        scriptActions.add(new HasRedEmeraldScript());

        scriptActions.add(new HasBlueEmeraldScriptAction());

        scriptActions.add(new CheckBothEmeraldsScriptAction());

        
        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasBothEmeralds", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("How even...");
                    addText("What? How is this possible??");
                    addText("No other one of my offspring has survived being \nbanished to that hellish region...");
                    addText("Don't tell me...");
                    addText("You received the blessing of the other Emeralds?!");
                }});

                addScriptAction(new WaitScriptAction(120));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Red and Blue Emeralds begin to react with \n the Green Emerald.");
                    addText("Return what you have stolen from the world, and begone.");
                }});

                addScriptAction(new WaitScriptAction(30));

                addScriptAction(new TextboxScriptAction() {{
                    addText("NOOOOOOOOOOOOOOOOO!!!");
                }});

                addScriptAction(new NPCChangeVisibilityScriptAction(Visibility.HIDDEN));
                
                addScriptAction(new WaitScriptAction(120));

                addScriptAction(new TextboxScriptAction() {{
                    addText("We thank you for saving the world.");
                }});

                addScriptAction(new TeleportCreditScriptAction());

            }});



            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasGreenEmerald", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("You have the Chaos Emerald?");
                    addText("Good going, little one. Now I have all the \npower in the world!");
                    addText("As for you, you're free now. Free to die!");
                    addText("Now suffer from the regions you've faced, and \nendless enemies!");
                }});

                addScriptAction(new WaitScriptAction(60));

                addScriptAction(new TextboxScriptAction() {{
                    addText("This is your emergency Bee senses! It appears \nwe've been tricked by the Queen Bee.");
                    addText("The fate of all bee-kind is at stake here. \nYou will need to use everything you have.");
                    addText("You'll be subject to three rounds of suffering.\nVolcano, then Snow, then Grass.");
                    addText("The Queen Bee will manipulate the monsters and \nweather, so make sure to use your tunics!");
                    addText("Don't give up, no matter what!");
                }});

                // sets boss active to true
                addScriptAction(new SetBossActiveScriptAction());

                // begins the lava rain in the volcano region
                addScriptAction(new LavaRainScriptAction());

                // teleports player to the volcano region
                addScriptAction(new TeleportVolcanoBossScriptAction());

            }});


            // First conversation
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Rise, brave one of the hive.");
                    addText("You are the spark that will light the sky once more.");
                    addText("Beyond the plains lie uncharted lands, filled with\ndangers and resources.");
                    addText("Claim its nectar, and our song will echo through the\nfields again!");
                    addText("Now go, my soldier — follow the path of the flower to\nnew regions and prevail!");
                }});

                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("What do you need?", new String[]{"Speech", "Controls"});
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
                        addScriptAction(new TextboxScriptAction() {{
                            addText("Rise, brave one of the hive.");
                            addText("You are the spark that will light the sky once more.");
                            addText("Beyond the plains lie uncharted lands, filled with\ndangers and resources.");
                            addText("Claim its nectar, and our song will echo through the\nfields again!");
                            addText("Now go, my soldier — follow the path of the flower to\nnew regions and prevail!");
                        }});
                    }});


                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 1;
                            }
                        });

                    addScriptAction(new TextboxScriptAction() {{
                        addText("Use WASD to move and outsmart your enemies!");
                        addText("Press SPACE to melee attack, E to collect powerups, \nand 1 to activate them.");
                        addText("Press SPACE near flowers to collect nectar.");
                        addText("Press SPACE near my head to deposit nectar.");
                        addText("Press the E/Space key near Doors, Portals, and NPCs \nto interact with them.");
                    }});

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
                    }});
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
}
