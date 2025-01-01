package com.example.bistro.backstage.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;


@Transactional
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

	@Query("FROM Menu  WHERE productName like %:productname%")
	List<Menu> findMenuByNameLike(@Param("productname") String productname);

	//for python
	Menu findByProductName(String productName);
	@Query(value = "SELECT * FROM Menu WHERE productName LIKE %:productName%", nativeQuery = true)
	Menu findByProductNameLike2(@Param("productName") String productName);


	// 11/19修改
	@Query("SELECT c.menu.productName AS productName, COALESCE(AVG(c.commentRating), 0.0) AS avgScore "
			+ "FROM Comment c GROUP BY c.menu.productName")
	List<Object[]> countAvgScores(); // 返回 Object[] 是因為查詢會返回兩個值，菜品名稱和平均分數

	// 11/19修改
	@Query("FROM Menu  WHERE menuStatus = '上架'")
	List<Menu> findMenuByStatusIsSold();

	@Query("""
			SELECT c.menu.ID, c.menu.productName, COALESCE(AVG(c.commentRating), 0.0) AS avgScore
			FROM Comment c
			JOIN c.menu
			WHERE c.menu.menuStatus = '上架'
			GROUP BY c.menu.ID, c.menu.productName
			""")
	List<Object[]> countAvgScoresMenuIsSold();

	@Query("SELECT DISTINCT productCategory FROM Menu WHERE menuStatus = '上架'")
	List<String> findAllCategories();

	@Query("FROM Menu WHERE productCategory = :category AND menuStatus = '上架'")
	List<Menu> findMenuByCategoryAndIsSold(String category);
	
	
	@Query(value = "SELECT TOP 3 * FROM Menu  WHERE menuStatus = '上架' ORDER BY avgScore DESC",nativeQuery = true)
	List<Menu> findTopThreeMenu();
	
	
	@Query("SELECT COALESCE(AVG(c.commentRating), 0.0) AS avgScore "
			+ "FROM Comment c  where commentProduct= :productName")
	Double countOneMenuAvgScore(String productName); 

	
	
	
	
	
	
	
	
	
	
}
