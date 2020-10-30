package com.story.tinygame.herostory.rank;

import com.alibaba.fastjson.JSONObject;
import com.story.tinygame.herostory.async.AsyncOperationProcessor;
import com.story.tinygame.herostory.async.IAsyncOperation;
import com.story.tinygame.herostory.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @Author story
 * @CreateTIme 2020/10/30
 * 排行榜服务
 **/
public final class RankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankService.class);

    private static final RankService INSTANCE = new RankService();

    private RankService() {
    }

    /**
     * 获取单例对象
     *
     * @return RankService
     */
    public static RankService getInstance() {
        return INSTANCE;
    }

    /**
     * 获取排行列表
     *
     * @param callback 回调函数
     */
    public void getRank(Consumer<List<RankItem>> callback) {
        if (callback == null) {
            return;
        }

        IAsyncOperation asyncOp = new AsyncGetRank() {
            @Override
            public void doFinish() {
                callback.accept(this.getRankItem());
            }
        };

        AsyncOperationProcessor.getInstance().process(asyncOp);

    }

    /**
     * 异步获取排名列表
     */
    private class AsyncGetRank implements IAsyncOperation {

        private List<RankItem> returnRankItems = null;

        public List<RankItem> getRankItem() {
            return returnRankItems;
        }

        @Override
        public void doAsync() {
            try (Jedis redis = RedisUtil.getRedis()) {
                if (redis == null) return;

                List<RankItem> rankItems = new ArrayList<>();

                //获取字符串值集合
                Set<Tuple> valSet = redis.zrangeWithScores("Rank", 0, 9);

                int rankId = 0;

                for (Tuple t : valSet) {
                    //获取用户id
                    int userId = Integer.parseInt(t.getElement());

                    //获取用户基本信息
                    String basicInfo = redis.hget("User_" + userId, "BasicInfo");
                    if (basicInfo == null || basicInfo.isEmpty()) continue;

                    JSONObject jsonObj = JSONObject.parseObject(basicInfo);

                    RankItem newItem = new RankItem();
                    newItem.setRankId(++rankId);
                    newItem.setUserId(userId);
                    newItem.setUserName(jsonObj.getString("userName"));
                    newItem.setHeroAvatar(jsonObj.getString("heroAvatar"));
                    newItem.setWin((int) t.getScore());

                    rankItems.add(newItem);
                }

                //将结果集合赋值给内部类私有变量
                returnRankItems = rankItems;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
