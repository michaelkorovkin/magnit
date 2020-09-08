package org.magnit.korovkin.example.entities;

/**
 * JavaBean на таблицу TEST
 */
public class TestTable {
    private Integer field;

    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "TestTable{" +
                "field=" + field +
                '}';
    }
}
