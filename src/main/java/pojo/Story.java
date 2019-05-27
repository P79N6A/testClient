package pojo;

import util.Priority;
import util.Status;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/27/19:48
 * @Description:
 */
public class Story {

    private long id;
    private String name;
    private String description;
    private String creator;
    private Date created;
    private Date modified;
    private Date begin;
    private Date due;
    private Status status;
    private String owner;
    private String cc;
    private int size;
    private Priority priority;
    private String developer;
    private String test_focus;
    private String type;
    private String source;
    private String version;
    private String module;
    private int business_value;
    private int limit;
    private int page;
    private String fields;
    private String category_id;


    private long workspace_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public String getStatus() {
        return status.getInfo();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPriority() {
        return priority.getValue();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getTest_focus() {
        return test_focus;
    }

    public void setTest_focus(String test_focus) {
        this.test_focus = test_focus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getBusiness_value() {
        return business_value;
    }

    public void setBusiness_value(int business_value) {
        this.business_value = business_value;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public long getWorkspace_id() {
        return workspace_id;
    }

    public void setWorkspace_id(long workspace_id) {
        this.workspace_id = workspace_id;
    }
}
