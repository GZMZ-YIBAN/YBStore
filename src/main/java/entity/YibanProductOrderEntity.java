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
 * @date 20:26 2018/4/25
 * @modified By EchoLZY
 */
@Entity
@Table(name = "yiban_product_order", schema = "yiban")
public class YibanProductOrderEntity {
    private int id;
    private String productId;
    private String tradeId;
    private String userId;
    private String userName;
    private String sex;
    private String phone;
    private String accessToken;
    private String enable;
    private Timestamp data;


    public YibanProductOrderEntity(String productId, String tradeId, String userId, String userName, String sex, String phone, String accessToken, String enable, Timestamp data) {
        this.productId = productId;
        this.tradeId = tradeId;
        this.userId = userId;
        this.userName = userName;
        this.sex = sex;
        this.phone = phone;
        this.accessToken = accessToken;
        this.enable = enable;
        this.data = data;
    }

    public YibanProductOrderEntity() {
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
    @Column(name = "`productId`", nullable = false, length = 255)
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "`tradeId`", nullable = false, length = 255)
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String orderNumber) {
        this.tradeId = orderNumber;
    }

    @Basic
    @Column(name = "`userId`", nullable = false, length = 255)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    @Column(name = "`access_token`", nullable = false, length = 255)
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Basic
    @Column(name = "`enable`", nullable = false, length = 255)
    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    @Basic
    @Column(name = "`data`", nullable = false)
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YibanProductOrderEntity that = (YibanProductOrderEntity) o;
        return id == that.id &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(tradeId, that.tradeId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(enable, that.enable) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, tradeId, userId, userName, sex, phone, accessToken, enable, data);
    }

    @Override
    public String toString() {
        return "YibanProductOrderEntity{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", enable='" + enable + '\'' +
                ", data=" + data +
                '}';
    }
}
