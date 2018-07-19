package com.xiu.jpa.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 创建者: Administrator
 * @createTime 创建时间:2018-07-19
 */


@Data
@Entity
@Table(name = "test")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@ApiModel(value = "Test",description = "Test实体类")
public class Test implements Serializable {
    /**	主键	*/
    @Id
    @GeneratedValue(generator = "idFactory")
    @GenericGenerator(name = "idFactory",strategy = "user.service.api.factory.IdFactory")
    @Column(name = "id",columnDefinition = "bigint COMMENT  '主键'")
    @ApiModelProperty(value = "主键:主键",name = "id",dataType = "BIGINT")
    private Long id;

    /**	存款	*/
    @JsonSerialize(using = BigDecimalJsonSerializer.class)
    @JsonDeserialize(using = BigDecimalJsonDeserializer.class)
    @Column(name = "money",columnDefinition = "decimal(65,30) COMMENT  '存款'")
    @ApiModelProperty(value = "存款",name = "money",dataType = "DECIMAL")
    private BigDecimal money;

    /**	创建时间	*/
    @Column(name = "create_time",columnDefinition = "DATETIME COMMENT  '创建时间'")
    @ApiModelProperty(value = "创建时间",name = "createTime",dataType = "TIMESTAMP")
    private Date createTime;

    /**	年龄	*/
    @Column(name = "age",columnDefinition = "int COMMENT  '年龄'")
    @ApiModelProperty(value = "年龄",name = "age",dataType = "INTEGER")
    private Integer age;

    /**	姓名	*/
    @Column(name = "name",columnDefinition = "varchar(60) COMMENT  '姓名'")
    @ApiModelProperty(value = "姓名",name = "name",dataType = "VARCHAR")
    private String name;

    /**	图像照片	*/
    @Column(name = "pic",columnDefinition = "text COMMENT  '图像照片'")
    @ApiModelProperty(value = "图像照片",name = "pic",dataType = "LONGVARCHAR")
    private String pic;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }
}