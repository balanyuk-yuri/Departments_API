package com.ybal.dep.controller;

import com.ybal.dep.audit.IpRevision;
import net.minidev.json.JSONObject;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseController {
    @Autowired
    private EntityManagerFactory factory;

    protected List<JSONObject> history(Class<?> c, Long id, Date from, Date to, String ip){
        AuditReader audit = AuditReaderFactory.get(factory.createEntityManager());
        AuditQuery query = audit.createQuery().forRevisionsOfEntity(c, false,true);
        if (id != null){
            query.add(AuditEntity.property("id").eq(id));
        }
        if (from != null){
            query.add(AuditEntity.revisionProperty("timestamp").ge(from.getTime()));
        }
        if (to != null){
            query.add(AuditEntity.revisionProperty("timestamp").le(to.getTime()));
        }
        if (ip != null){
            query.add(AuditEntity.revisionProperty("ip").eq(ip));
        }

        List<JSONObject> res = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        query.getResultList().forEach(r -> {
            Object[] v = (Object[]) r;
            JSONObject json = new JSONObject();
            IpRevision rev = (IpRevision)v[1];
            json.put("date", dateFormat.format(rev.getRevisionDate()));
            json.put("by", rev.getIp());
            json.put("type", v[2].toString());
            json.put(c.getSimpleName().toLowerCase(), v[0]);
            res.add(json);
        });
        return res;
    }
}
