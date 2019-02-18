package com.company.project.common.persistence;

import com.company.project.common.utils.CollectionUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;


/**
 * @author THON
 * @mail thon.ju@meet-future.com
 * @date 2012-2-13 下午03:08:35
 * @description
 */
public class Page<T> {

	protected Integer pageNo = 1;
	protected Integer pageSize = 5;

	protected Integer length = 5; // 显示页码数量
	protected String pagination; // 分页标签

	protected Boolean autoCount = Boolean.TRUE;
	protected List<String> orderList = CollectionUtil.newArrayList();
	protected List<String> ascOrderList = CollectionUtil.newArrayList();
	protected List<String> descOrderList = CollectionUtil.newArrayList();

	protected List<T> result = CollectionUtil.newArrayList();
	protected Long totalCount = -1L; // 总数量

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Page<T> setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1) {
			this.pageNo = 1;
		}
		return this;
	}



	public Integer getLength() {
		return length;
	}

	public Page<T> setLength(Integer length) {
		this.length = length;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Page<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Integer getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	public Integer getLast() {
		if (getHasNext()) {
			return pageNo* pageSize;
		}else {
			return pageSize <= 1 || pageNo <= 1 ?totalCount.intValue() : (pageNo - 1) * pageSize + (totalCount.intValue() % pageSize);
		}
	}

	public Integer getBegin() {
		return Math.max(1, pageNo - length/2);
	}

	public Integer getEnd() {
		return Math.min(getBegin() + (length - 1), getTotalPages().intValue());
	}

	public Boolean getAutoCount() {
		return autoCount;
	}

	public Page<T> setAutoCount(Boolean autoCount) {
		this.autoCount = autoCount;
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	public Page<T> setResult(List<T> result) {
		this.result = result;
		return this;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public Page<T> setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public Long getTotalPages() {
		if (totalCount <= 0 || pageSize <= 0) {
			return -1L;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public Boolean getHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public Integer getNextPage() {
		if (getHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	public Boolean getHasPre() {
		return (pageNo - 1 >= 1);
	}

	public Integer getPrePage() {
		if (getHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	//
	public List<String> getOrderList() {
		return orderList;
	}

	public List<String> getAscOrderList() {
		return ascOrderList;
	}

	public List<String> getDescOrderList() {
		return descOrderList;
	}

	public Page<T> asc(String column) {
		orderList.add(column + " ASC");
		ascOrderList.add(column);
		return this;
	}

	public Page<T> desc(String column) {
		orderList.add(column + " DESC");
		descOrderList.add(column);
		return this;
	}

	/**
	 * 输出当前分页标签
	 * <ul class="pagination pagination-sm">${page.pagination}</ul>
	 */
	public String getPagination() {

		StringBuilder sb = new StringBuilder();
		if (getTotalPages() > 1) {
            sb.append("<div>");
			sb.append("<ul class=\"pager\">");

			if (getHasPre()) {
				sb.append("<li><a class=\"btn-link\" data=\'{\"p\":\"" + (pageNo - 1) + "\", \"s\": \"" + pageSize + "\"}\'><</a></li>");
			}
            if (getBegin() != 1) {
                sb.append("<li><a class=\"btn-link\" data=\'{\"p\":\"1\", \"s\": \"" + pageSize + "\"}\'>1</a></li>");
            }

			for (int i = getBegin(); i <= getEnd(); i++) {
				if (i == pageNo) {
					sb.append("<li class=\"active\"><a class=\"btn-link\" data=\'{\"p\":\"" + i + "\", \"s\": \"" + pageSize + "\"}\'>" + i + "</a></li>");
				}else {
					sb.append("<li><a class=\"btn-link\" data=\'{\"p\":\"" + i + "\", \"s\": \"" + pageSize + "\"}\'>" + i + "</a></li>");
				}
			}

			if (getEnd().intValue() != getTotalPages().intValue()) {
				sb.append("<li><a class=\"btn-link\" data=\'{\"p\":\"" + getTotalPages() + "\", \"s\": \"" + pageSize + "\"}\'>" + getTotalPages() + "</a></li>");
			}

            if (getHasNext()) {
                sb.append("<li><a class=\"btn-link\" data=\'{\"p\":\"" + (pageNo + 1) + "\", \"s\": \"" + pageSize + "\"}\'>></a></li>");
            }

			sb.append("</ul>");
			sb.append("<span class=\"pager-info\">"+getFirst()+"-"+getLast()+"/"+totalCount+"</span>");
            sb.append("</div>");
		}
		return StringEscapeUtils.unescapeHtml4(sb.toString());
	}



}