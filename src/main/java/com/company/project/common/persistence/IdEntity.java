package com.company.project.common.persistence;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @file IdEntity.java
 * @author thon
 * @email thon.ju@gmail.com
 * @date Aug 16, 2013 9:32:29 AM
 * @description 实体主键策略
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String DEL_FLAG = "delFlag";
	public static final int DEL_FLAG_NORMAL = 0;
	public static final int DEL_FLAG_DELETE = 1;
	public static final int DEL_FLAG_AUDIT = 2;

	protected Integer delFlag; // 删除标记（0：正常；1：删除；2：审核）
	protected Integer id;

	public IdEntity() {
		this.delFlag = DEL_FLAG_NORMAL;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
