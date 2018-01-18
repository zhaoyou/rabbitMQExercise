package mq;

import java.sql.Timestamp;
import java.util.List;

public class Message {
    private Integer id;
    private String name;
    private Timestamp createAt;
    private List<String> ids;

    public Message(Integer id, String name, Timestamp createAt, List<String> ids) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.ids = ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createAt=" + createAt +
                ", ids=" + ids +
                '}';
    }
}
