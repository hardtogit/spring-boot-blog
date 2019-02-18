package com.company.project.common.mapper;

import com.company.project.common.utils.Exceptions;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by thon on 8/30/14.
 */
public class BeanUtils extends BeanUtilsBean{

    @Override
    public void copyProperty(Object dest, String name, Object value){
        if(value==null)return;
        try {
            super.copyProperty(dest, name, value);
        } catch (IllegalAccessException e) {
            throw Exceptions.unchecked(e);
        } catch (InvocationTargetException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
