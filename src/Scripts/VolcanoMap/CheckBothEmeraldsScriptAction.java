package Scripts.VolcanoMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class CheckBothEmeraldsScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        if (BeeStats.hasBlueEmerald() && BeeStats.hasRedEmerald()) {
            map.getFlagManager().setFlag("hasBothEmeralds");
            return ScriptState.COMPLETED;

        }

        return ScriptState.COMPLETED;
    }
}
