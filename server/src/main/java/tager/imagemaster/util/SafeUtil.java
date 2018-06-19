package tager.imagemaster.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SafeUtil {
    @Autowired
    private RedisUtil redisUtil;

    public int validate(String session) {
        if (!redisUtil.exists(session))
            return 0;

        redisUtil.refresh(session, 60);
        return (int) redisUtil.get(session);
    }
}
