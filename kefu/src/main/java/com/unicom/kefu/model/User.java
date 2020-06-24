package com.unicom.kefu.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 	用户表
 * @author Administrator
 *
 */

public class User {
	
	private Integer id;
	private String name;
	private String account;
	
	private String password1;
	private String password2;
	
	private String idcard;
	private String rename;
	private String recommendCode;
	private String recommendUser;
	private Integer level;
	private BigDecimal shopbi;
	private BigDecimal integral;
	private Integer food;
	private BigDecimal commission;
	private Integer isAuth;
	private Integer isNew;
	private Integer isEnabled;
	private Integer isLock;
	private Date createTime;
	private BigDecimal contract;
	
	//view
	private User userFirst;
	private User userSecond;
	private User userThree;
	private Integer userLevel;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getRename() {
		return rename;
	}
	public void setRename(String rename) {
		this.rename = rename;
	}
	public String getRecommendCode() {
		return recommendCode;
	}
	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}
	public String getRecommendUser() {
		return recommendUser;
	}
	public void setRecommendUser(String recommendUser) {
		this.recommendUser = recommendUser;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public BigDecimal getShopbi() {
		return shopbi;
	}
	public void setShopbi(BigDecimal shopbi) {
		this.shopbi = shopbi;
	}
	public BigDecimal getIntegral() {
		return integral;
	}
	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}
	public Integer getFood() {
		return food;
	}
	public void setFood(Integer food) {
		this.food = food;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public Integer getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
	public Integer getIsNew() {
		return isNew;
	}
	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Integer getIsLock() {
		return isLock;
	}
	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getContract() {
		return contract;
	}
	public void setContract(BigDecimal contract) {
		this.contract = contract;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public User getUserFirst() {
		return userFirst;
	}
	public void setUserFirst(User userFirst) {
		this.userFirst = userFirst;
	}
	public User getUserSecond() {
		return userSecond;
	}
	public void setUserSecond(User userSecond) {
		this.userSecond = userSecond;
	}
	public User getUserThree() {
		return userThree;
	}
	public void setUserThree(User userThree) {
		this.userThree = userThree;
	}
	
	
	
}
