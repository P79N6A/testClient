package util;

public enum Status {
    NEW("new"),
    PLANNING("planning"),
    DEVELOPING("developing"),
    REJECTED("reject"),
    SUSPENDED("suspended"),
    AUDITING("auditing"),
    AUDITED("audited"),
    ABANDONED("abandoned"),
    PLANNED("planned"),
    UI_DESIGN("UI_design"),
    PRODUCTION_EXPERIENCE("production_experience"),
    UI_RECONSTRUCT("UI_reconstruct"),
    IN_PROGRESS("in_progress"),
    TESTING("testing"),
    FOR_TEST("for_test");



    private String info;

    Status(String info) {
        this.info = info;
    }

    public String getInfo(){
        return info;
    }
//实现中	developing
//已实现	resolved
//已拒绝	rejected
//挂起	suspended
//评审中	auditing
//已评审	audited
//临时取消	abandoned
//已规划	planned
//UI设计	UI_design
//产品体验	product_experience
//页面重构	UI_reconstruct
//实现中	in_progress
//测试中	testing
//转测试	for_test


}
