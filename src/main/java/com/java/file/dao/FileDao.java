package com.java.file.dao;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class FileDao implements FileDaoInterface {
	
	@Resource(name="sqlSession")
	SqlSession sess;
	
	@Override
	public int insert(HashMap<String, Object> param) {
		
		return sess.insert("file.insert", param);
	}

}
