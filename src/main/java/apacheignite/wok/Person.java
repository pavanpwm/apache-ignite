package apacheignite.wok;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;


public class Person implements Serializable {
    private static final long serialVersionUID = 0L;

    @QuerySqlField(name = "id", index = true)
    private int id;
    @QuerySqlField(name = "name", index = true)
    private String name;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}