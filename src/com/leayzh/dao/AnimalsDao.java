package com.leayzh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.leayzh.bean.Animals;
import com.leayzh.util.BaseConnection;

public class AnimalsDao {
	
	//查询操作
	public ArrayList<Animals> getList() {
		ArrayList<Animals> ar = new ArrayList<Animals>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = BaseConnection.getConnection();
		String sql = "select * from Animals";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Animals an = new Animals();
				an.setId(rs.getInt("id"));
				an.setAge(rs.getInt("age"));
				an.setName(rs.getString("name"));
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

	//插入操作
	public boolean insert(Animals an){
		boolean b=false;
		Connection conn=BaseConnection.getConnection();
		PreparedStatement ps = null;
		String sql="insert into animals(name,age,anid) values (?,?,?)";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1, an.getName());
			ps.setInt(2, an.getAge());
			ps.setInt(3, an.getAnId());
			int a=ps.executeUpdate();
			if (a>0) {
				b=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			BaseConnection.closeRes(conn, ps);
		}
		return b;
	}
	
	//更新方法
	public boolean update(Animals an){
		boolean b=false;
		Connection conn=BaseConnection.getConnection();
		PreparedStatement ps = null;
		String sql="update animals set name=?,age=?,anid=? where id=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1, an.getName());
			ps.setInt(2, an.getAge());
			ps.setInt(3, an.getAnId());
			ps.setInt(4, an.getId());
			int a=ps.executeUpdate();
			if (a>0) {
				b=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			BaseConnection.closeRes(conn, ps);
		}
		return b;
	}
	public static void main(String[] args) {
		Animals an=new Animals();
		an.setAge(20);
		an.setId(2);
		an.setName("zhudaibi");
		AnimalsDao AnimalsDao=new AnimalsDao();
		boolean b=AnimalsDao.update(an);
		System.out.println(b);
	}
}
