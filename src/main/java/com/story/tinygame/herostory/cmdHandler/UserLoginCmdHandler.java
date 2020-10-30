package com.story.tinygame.herostory.cmdHandler;

import com.story.tinygame.herostory.login.LoginService;
import com.story.tinygame.herostory.model.User;
import com.story.tinygame.herostory.model.UserManager;
import com.story.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author story
 * @CreateTIme 2020/10/27
 * 用户登录指令处理器
 **/
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd msg) {

        if (ctx == null || msg == null) return;

        LOGGER.info("username = {} , password = {}",
                msg.getUserName(),
                msg.getPassword()
        );

        LoginService.getInstance().userLogin(msg.getUserName(), msg.getPassword(), (userEntity -> {
            if (userEntity == null) {
                LOGGER.error("用户登录失败,");
                return;
            }

            int userId = userEntity.getUserId();
            String heroAvatar = userEntity.getHeroAvatar();

            //将用户加入到用户字典
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setUserName(userEntity.getUserName());
            newUser.setHeroAvatar(heroAvatar);
            newUser.setCurHp(100);
            UserManager.addUser(newUser);

            //将用户id附着到channel上
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            GameMsgProtocol.UserLoginResult.Builder resultBuilder =
                    GameMsgProtocol.UserLoginResult.newBuilder();
            resultBuilder.setUserId(newUser.getUserId());
            resultBuilder.setUserName(newUser.getUserName());
            resultBuilder.setHeroAvatar(newUser.getHeroAvatar());

            //构造结果并发送消息
            GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
            ctx.writeAndFlush(newResult);

        }));


    }
}
