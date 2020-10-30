package com.story.tinygame.herostory.msg;

/**
 * @Author story
 * @CreateTIme 2020/10/30
 * 战斗结果消息
 **/
public class VictorMsg {
    private int winnerId;//赢家id
    private int loserId;//输家id

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getLoserId() {
        return loserId;
    }

    public void setLoserId(int loserId) {
        this.loserId = loserId;
    }
}
