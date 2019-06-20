package com.imooc.dataobject;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SqMax on 2018/3/31.
 */
@Data
@Entity
public class SellerInfo {

    @Id
    @Column(name = "id")
    private String sellerId;

    private String username;

    private String password;

    private String openid;

}
