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

                addScriptAction(new ResetScreenFX());
                
                addScriptAction(new TextboxScriptAction() {{
                    addText("I… impossible...");
                    addText("You— you made it back?");
                    addText("No one survives that place. No one!");
                    addText("Wait… that glow…");
                    addText("Don’t tell me…");
                    addText("You actually harnessed the power \nof the other Emeralds?!");
                }});

                addScriptAction(new WaitScriptAction(90));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Red and Blue Emeralds begin to react with \nthe Green Emerald.");
                    addText("Return what you have stolen from the world, and \nbegone.");
                }});

                addScriptAction(new WaitScriptAction(30));

                addScriptAction(new TextboxScriptAction() {{
                    addText("NOOOOOOOOOOOOOOOOO!!!");
                }});

                addScriptAction(new NPCChangeVisibilityScriptAction(Visibility.HIDDEN));
                
                addScriptAction(new WaitScriptAction(90));

                addScriptAction(new TextboxScriptAction() {{
                    addText("We thank you for saving the world.");
                }});

                addScriptAction(new TeleportCreditScriptAction());

            }});



            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasGreenEmerald", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("You carry the Chaos Emerald?");
                    addText("Excellent. You have served your purpose well.");
                    addText("With this, the hive ascends—and I alone command its \npower.");
                    addText("As for you…your part in this ends now.");
                    addText("Be gone from my sight. I banish you to the outer wilds—");
                    addText("—and may the dangers there finish what I no longer \nneed to.");
                }});

                addScriptAction(new WaitScriptAction(60));

                addScriptAction(new TextboxScriptAction() {{
                    addText("This is your emergency Bee senses! It appears we've \nbeen tricked by the Queen Bee.");
                    addText("The fate of all bee-kind is at stake here. You will need \nto use everything you have.");
                    addText("The Queen Bee will manipulate the monsters and \nweather, so make sure to activate your volcanic tunic!");
                    addText("Don't give up, no matter what!");
                }});

                addScriptAction(new WaitScriptAction(60));

                addScriptAction(new TextboxScriptAction() {{
                    addText("You are filled with determination. Your health and \nstamina is restored.");
                }});

                addScriptAction(new WaitScriptAction(30));

                addScriptAction(new RestoreStatsScriptAction());

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
                    addText("Your strength is required, and you will not falter.");
                    addText("Beyond the plains lie uncharted lands—treacherous, yet\nladen with power.");
                    addText("There, you will seek a radiant jewel of great force.\nIts energy is vital to our hive’s ascension.");
                    addText("Secure it. Do not disappoint the hive. Our future depends\non your success.");
                    addText("Now go, soldier—follow the path of the flower, claim what\nawaits, and let nothing stand in your way.");
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
                            addText("Your strength is required, and you will not falter.");
                            addText("Beyond the plains lie uncharted lands—treacherous, yet\nladen with power.");
                            addText("There, you will seek a radiant jewel of great force.\nIts energy is vital to our hive’s ascension.");
                            addText("Secure it. Do not disappoint the hive. Our future depends\non your success.");
                            addText("Now go, soldier—follow the path of the flower, claim what\nawaits, and let nothing stand in your way.");
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
                    }});
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
}
