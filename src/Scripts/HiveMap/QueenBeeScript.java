package Scripts.HiveMap;

import Level.Script;
import ScriptActions.*;
import Scripts.FinalBoss.LavaRainScriptAction;
import Scripts.FinalBoss.TeleportVolcanoBossScriptAction;
import Scripts.VolcanoMap.CheckBothEmeraldsScriptAction;
import Scripts.VolcanoMap.HasBlueEmeraldScriptAction;
import Scripts.VolcanoMap.HasRedEmeraldScript;
import Scripts.VolcanoMap.SetBossActiveScriptAction;
import Sound.Music;
import Sound.MusicManager;
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
                    addText("Child of the hive… we have watched your journey.");
                    addText("We have felt your suffering…and your resolve.");
                    addText("Abandoned and ultimately betrayed, you still pressed forward.");
                    addText("Your heart did not waver, even when the world turned against you.");
                    addText("Let our power guide you now.");
                    addText("You are worthy, little one. Worthy of us…and of what comes next.");
                    addText("Go, bearer. Restore balance where corruption took root.");
                }});

                addScriptAction(new TeleportCreditScriptAction());

            }});



            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasGreenEmerald", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("You carry the Chaos Emerald?");
                    addText("Excellent. You have served your purpose well.");
                }});

                addScriptAction(new WaitScriptAction(60));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Ah… the Chaos Emerald… its voice… it calls to \nme...");
                }});

                addScriptAction(new StopMusicScriptAction());

                addScriptAction(new WaitScriptAction(30));

                addScriptAction(new TextboxScriptAction() {{
                    addText("With this, I will ascend-and I alone command its \npower.");
                    addText("As for you…your part in this ends now.");
                    addText("Be gone from my sight. I banish you to the outer wilds—");
                    addText("—and may what awaits you there finish what I no \nlonger need to.");
                }});

                addScriptAction(new WaitScriptAction(30));

                addScriptAction(new TextboxScriptAction() {{
                    addText("This is your Emergency Bee Senses! Something \nis terribly wrong!");
                    addText("The Queen Bee has been overtaken by the Chaos \nEmerald’s power.");
                    addText("Her corruption threatens all bee-kind. You must \ngather every ounce of strength you have.");
                    addText("She’s twisting the monsters and the weather itself! \nActivate your volcanic tunic now! (Press 3!)");
                    addText("Stay focused. Stay alive. You’re the only one who can\nstop her now!");
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
                    addText("Beyond the plains lie uncharted lands—treacherous, \nyet laden with power.");
                    addText("There, you will seek a radiant jewel of great force.\nIts energy is vital to our hive’s ascension.");
                    addText("Secure it. Do not disappoint the hive. Our future \ndepends on your success.");
                    addText("Now go, soldier—follow the path of the flower, claim \nwhat awaits, and let nothing stand in your way.");
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
                            addText("Beyond the plains lie uncharted lands—treacherous, \nyet laden with power.");
                            addText("There, you will seek a radiant jewel of great force.\nIts energy is vital to our hive’s ascension.");
                            addText("Secure it. Do not disappoint the hive. Our future \ndepends on your success.");
                            addText("Now go, soldier—follow the path of the flower, claim \nwhat awaits, and let nothing stand in your way.");
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
