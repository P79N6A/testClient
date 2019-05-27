package util;


import pojo.Story;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/27/16:49
 * @Description:
 */
public class ResultStory {

    private Story story;

    public ResultStory(Story story) {

        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }
}
