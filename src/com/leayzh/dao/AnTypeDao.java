package com.leayzh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.leayzh.bean.AnType;
import com.leayzh.util.BaseConnection;

public class AnTypeDao {
	// 添加查询对象
	public ArrayList<AnType> getList() {
		ArrayList<AnType> ar = new ArrayList<AnType>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		//获取connection对象
		Connection conn = BaseConnection.getConnection();
		String sql = "select * from anType";
		try {
			//准备执行操作和查询操作
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				AnType an = new AnType();
				an.setAnName(rs.getString("anName"));
				an.setAnId(rs.getInt("anId"));
				ar.add(an);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps, rs);
		}
		return ar;
	}

}
