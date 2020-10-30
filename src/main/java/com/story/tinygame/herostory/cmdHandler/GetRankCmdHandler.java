package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.msg.GameMsgProtocol;
import com.story.tinygame.herostory.rank.RankItem;
import com.story.tinygame.herostory.rank.RankService;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;

/**
 * @Author story
 * @CreateTIme 2020/10/29
 **/
public class GetRankCmdHandler implements ICmdHandler<GameMsgProtocol.GetRankCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.GetRankCmd msg) {
        if (ctx == null || msg == null) return;

        RankService.getInstance().getRank(rankItems -> {
            if (rankItems == null) {
                rankItems = Collections.emptyList();
            }

            GameMsgProtocol.GetRankResult.Builder resultBuilder
                    = GameMsgProtocol.GetRankResult.newBuilder();

            for (RankItem rankItem : rankItems) {
                GameMsgProtocol.GetRankResult.RankItem.Builder rankItemBuilder
                        = GameMsgProtocol.GetRankResult.RankItem.newBuilder();

                rankItemBuilder.setRankId(rankItem.getRankId());
                rankItemBuilder.setUserId(rankItem.getUserId());
                rankItemBuilder.setUserName(rankItem.getUserName());
                rankItemBuilder.setHeroAvatar(rankItem.getHeroAvatar());
                rankItemBuilder.setWin(rankItem.getWin());

                resultBuilder.addRankItem(rankItemBuilder);
            }

            GameMsgProtocol.GetRankResult newResult = resultBuilder.build();
            ctx.writeAndFlush(newResult);
        });
    }
}
