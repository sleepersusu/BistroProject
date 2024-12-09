package com.example.bistro.frontstage.cartId;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class CartId implements Serializable {

    // 參考: https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#identifiers-composite-aggregated
    // Example 132. @EmbeddedId with @ManyToOne
    private Integer membersId;

    private Integer menuId;

    // hashCode 和 equals 方法，用於確保主鍵比較正確


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(membersId, cartId.membersId) && Objects.equals(menuId, cartId.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membersId, menuId);
    }
}
