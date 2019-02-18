package com.company.project.service.impl;

import com.company.project.common.persistence.Page;
import com.company.project.core.AbstractService;
import com.company.project.dao.AttachmentMapper;
import com.company.project.model.Attachment;
import com.company.project.service.AttachmentService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class AttachmentImpl extends AbstractService<Attachment> implements AttachmentService {
    @Resource
    private AttachmentMapper attachmentMapper;
}
