package Scripts.MazeMap;

import Level.MapEntityStatus;
import Level.Player;
import Level.ScriptState;
import NPCs.FireTunic;
import NPCs.FrostTunic;
import Players.Bee;
import ScriptActions.ScriptAction;

public class RedTunicScriptAction extends ScriptAction {

    public ScriptState execute() {
        Player player = map.getPlayer();
        if (player instanceof Bee bee) {
            bee.obtainTunic();
            bee.obtainTunic();
        }

        for (var npc : this.map.getNPCs()) {
            if (npc instanceof FireTunic fireTunic) {
                fireTunic.setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
        return ScriptState.COMPLETED;
    }




}