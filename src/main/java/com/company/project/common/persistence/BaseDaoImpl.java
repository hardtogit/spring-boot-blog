package com.company.project.common.persistence;

import com.company.project.common.utils.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author THON
 * @mail thon.ju@meet-future.com
 * @date 2012-2-13 下午05:20:42
 * @description
 */
public class BaseDaoImpl<E extends IdEntity> implements BaseDao<E> {

	private Logger log=Logger.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;

	protected Class<?> entityClass;

	public BaseDaoImpl(Class<E> entityClass) {
		this.entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 获取 Session
	 */
	public Session getSession(){
	  return (Session) getEntityManager().getDelegate();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush(){
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear(){
		getSession().clear();
	}

	public void save(E entity) {
		getSession().saveOrUpdate(entity);
	}

	public void delete(E entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public E get(int id){
		return (E)getSession().get(entityClass, id);
	}

	protected Long count(String queryString, Object... values) {
		Query query = createQuery(queryString, values);
		return ((Number)query.uniqueResult()).longValue();
	}

	protected Long countBySql(String sqlString, Object... values) {
		SQLQuery query = createSqlQuery(sqlString, values);

		return ((Number)query.uniqueResult()).longValue();
	}

	public int update(String queryString, Object... parameter){
		return createQuery(queryString, parameter).executeUpdate();
	}

	public int updateBySql(String sqlString, Object... parameter){
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	protected E findOne(String queryString, Object... values){
		Query query = createQuery(queryString, values);
		return (E) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	protected <E> E findOneBySql(String sqlString, Class<?> resultClass, Object... values){
		SQLQuery query = createSqlQuery(sqlString, values);
		setResultTransformer(query, resultClass);
		return (E)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll(){
		String queryString ="FROM " + entityClass.getSimpleName();
		Query query = getSession().createQuery(queryString);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	protected <E> List<E> findList(String queryString, Object... values){
		Query query = createQuery(queryString, values);
		return query.list();
	}

	protected <E> List<E> findListBySql(String sqlString, Object... values) {
		return findListBySql(sqlString, null, values);
	}

	@SuppressWarnings("unchecked")
	protected <E> List<E> findListBySql(String sqlString, Class<?> resultClass, Object... values) {
		SQLQuery query = createSqlQuery(sqlString, values);
		setResultTransformer(query, resultClass);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	protected Page<E> findPage(Page<E> page, String queryString, Object...values) {
		String countString = getCountQuery(queryString);
		if(page.getAutoCount()) {
			Long totalCount = count(countString, values);
			page.setTotalCount(totalCount);
		}
		queryString = appendOrders(page.getOrderList(), queryString);
		Query query = createQuery(queryString, values);

		// 分页
		if(page.getPageNo() >0 && page.getPageSize() >0 ){
			query.setFirstResult(page.getFirst()-1);
			query.setMaxResults(page.getPageSize());
		}

		return page.setResult(query.list());
	}

	@SuppressWarnings("unchecked")
	protected <E> Page<E> findPageBySql(Page<E> page, String sqlString, Class<?> resultClass, Object...values) {
		String countString = getCountQuery(sqlString);
		if(page.getAutoCount()) {
			Long totalCount = countBySql(countString, values);
			page.setTotalCount(totalCount);
		}

		sqlString = appendOrders(page.getOrderList(), sqlString);
		SQLQuery query = createSqlQuery(sqlString, values);

		// 分页
		if(page.getPageNo() >0 && page.getPageSize() >0 ){
			query.setFirstResult(page.getFirst()-1);
			query.setMaxResults(page.getPageSize());
		}

		setResultTransformer(query, resultClass);

		return page.setResult(query.list());
	}

	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<E> find(Page<E> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<E> find(Page<E> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		page.setTotalCount(count(detachedCriteria));
		if (page.getTotalCount() < 1) {
			return page;
		}

		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		// 分页
		if(page.getPageNo() >0 && page.getPageSize() >0 ){
	        criteria.setFirstResult(page.getFirst()-1);
	        criteria.setMaxResults(page.getPageSize());
		}

		page.setResult(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<E> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}

	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String queryString, Object... values){
		Query query = getSession().createQuery(queryString);
		setParameter(query, values);
		return query;
	}

	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Object... values){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameter(query, values);
		return query;
	}

	private void setParameter(Query query, Object...values) {
		// set parameters to query
		for(int i=0; i<values.length; i++) {
			query.setParameter(i, values[i]);
		}
	}

	/**
	 * 设置查询结果类型
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.addEntity(resultClass);
			}
		}
	}

	private String getCountQuery(String queryStr) {
		String countQueryStr = queryStr;
		// clean order by or group by
		if (queryStr.toUpperCase().startsWith("SELECT")) {
			countQueryStr = "SELECT COUNT(*) FROM (" + countQueryStr +") temp";
		}else {
			countQueryStr = "FROM " + StringUtils.substringAfter(countQueryStr, "FROM");
			countQueryStr = StringUtils.substringBefore(countQueryStr, "ORDER BY");
			countQueryStr = StringUtils.substringBefore(countQueryStr,"GROUP BY");
			countQueryStr = "SELECT COUNT(*) " + countQueryStr;
		}

		return countQueryStr;
	}

	private String appendOrders(List<String> orderList, String qlString) {
		if(!orderList.isEmpty()) {
			StringBuffer buffer = new StringBuffer(qlString);
			if(!StringUtils.contains(qlString, "ORDER BY")) {
				buffer.append(" ORDER BY");
			} else {
				buffer.append(",");
			}

			for(int i=0; i<orderList.size(); i++) {
				buffer.append(" " + orderList.get(i));
				if(i + 1 < orderList.size()) {
					buffer.append(",");
				}
			}
			return buffer.toString();
		}
		return qlString;
	}

}
