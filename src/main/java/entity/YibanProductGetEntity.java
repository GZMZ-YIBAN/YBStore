package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p>
 * -----------------------------
 * @description
 * @date 23:49 2018/4/21
 * @modified By EchoLZY
 */
@Entity
@Table(name = "yiban_product_get", schema = "yiban")
public class YibanProductGetEntity {
    private int id;
    private String userName;
    private String yibanId;
    private String sex;
    private String phone;
    private int price;
    private int num;
    private String img;
    private Integer enable;
    private String productName;
    private Timestamp date;

    public YibanProductGetEntity() {
    }

    public YibanProductGetEntity(String userName, String yibanId, String sex, String phone,
                                 int price, int num, String img, Integer enable,
                                 String productName, Timestamp date) {
        this.userName = userName;
        this.yibanId = yibanId;
        this.sex = sex;
        this.phone = phone;
        this.price = price;
        this.num = num;
        this.img = img;
        this.enable = enable;
        this.productName = productName;
        this.date = date;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "`userName`", nullable = false, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "`yibanId`", nullable = false, length = 255)
    public String getYibanId() {
        return yibanId;
    }

    public void setYibanId(String yibanId) {
        this.yibanId = yibanId;
    }

    @Basic
    @Column(name = "`sex`", nullable = false, length = 255)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "`phone`", nullable = false, length = 255)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "`price`", nullable = false)
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Basic
    @Column(name = "`num`", nullable = false)
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Basic
    @Column(name = "`img`", nullable = true, length = 255)
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Basic
    @Column(name = "`enable`", nullable = true)
    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Basic
    @Column(name = "`productName`", nullable = false, length = 255)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "`date`", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YibanProductGetEntity that = (YibanProductGetEntity) o;
        return id == that.id &&
                price == that.price &&
                num == that.num &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(yibanId, that.yibanId) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(img, that.img) &&
                Objects.equals(enable, that.enable) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userName, yibanId, sex, phone, price, num, img, enable, productName, date);
    }

    @Override
    public String toString() {
        return "YibanProductGetEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", yibanId='" + yibanId + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", img='" + img + '\'' +
                ", enable=" + enable +
                ", productName='" + productName + '\'' +
                ", date=" + date +
                '}';
    }
}
