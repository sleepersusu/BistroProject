package com.example.bistro.backstage.cartId;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class CartId implements Serializable {

    private Integer memberId;
    private Integer menuId;

    public CartId() {}

    public CartId(Integer memberId, Integer menuId) {
        super();
        this.memberId = memberId;
        this.menuId = menuId;
    }

    // hashCode 和 equals 方法，用於確保主鍵比較正確


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(memberId, cartId.memberId) && Objects.equals(menuId, cartId.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, menuId);
    }
}
