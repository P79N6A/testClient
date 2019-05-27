package util;

import com.alibaba.fastjson.annotation.JSONField;
import pojo.SubStory;


/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/27/21:30
 * @Description:
 */
public class ResultSubStory {

    @JSONField(name = "Story")
    private SubStory subStory;

    public ResultSubStory(SubStory subStory) {
        this.subStory = subStory;
    }

    public SubStory getSubStory() {
        return subStory;
    }

    public void setSubStory(SubStory subStory) {
        this.subStory = subStory;
    }
}
