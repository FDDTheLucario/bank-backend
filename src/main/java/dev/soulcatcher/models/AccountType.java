package dev.soulcatcher.models;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Table(name = "account_types")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_type_id", insertable = false, updatable = false, nullable = false)
    private int typeId;
    @Column(name = "account_type_name")
    private String typeName;

    public AccountType(String typeName) {
        this.typeName = typeName;
    }

    public AccountType() {
        super();
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
