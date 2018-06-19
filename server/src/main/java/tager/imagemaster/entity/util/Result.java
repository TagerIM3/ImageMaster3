package tager.imagemaster.entity.util;

public class Result {
    public ResultMessage message;

    public Object object;

    public Result(ResultMessage message) {
        this.message = message;
        this.object = null;
    }

    public Result(Object object) {
        this.message = ResultMessage.SUCCESS;
        this.object = object;
    }
}
